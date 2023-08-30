/*
 * #%L
 * 
 * Native ARchive plugin for Maven
 * 
 * %%
 * 
 * Copyright (C) 2002 - 2014 NAR Maven Plugin developers.
 * 
 * %%
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and
 * 
 * limitations under the License.
 * 
 * #L%
 */

package com.github.maven_nar.cpptasks.compiler;

import java.io.File;

import java.security.SecureRandom;

import java.util.Collections;

import java.util.List;

import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.CUtil;

import com.github.maven_nar.cpptasks.CompilerDef;

import com.github.maven_nar.cpptasks.DependencyInfo;

import com.github.maven_nar.cpptasks.OptimizationEnum;

import com.github.maven_nar.cpptasks.ProcessorDef;

import com.github.maven_nar.cpptasks.borland.CfgFilenameState;

import com.github.maven_nar.cpptasks.types.UndefineArgument;

import com.google.common.collect.ObjectArrays;


/**
 * An abstract Compiler implementation which uses an external program to
 * perform the compile.
 *
 * @author Adam Murdoch
 */
public abstract class CommandLineCompiler extends AbstractProcessor {
	
  /** Command used when invoking ccache */
  private static final String CCACHE_CMD = "ccache";

  protected static final int GETARGUMENTCOUNTPERINPUTFILE = 1;
  
  public static final String COMMAND = "command";
  
  public static final String PREFIX = "prefix";

  private final Environment env;
  
  protected String identifier;
  
  private final String identifierArg;
  
  private final boolean libtool;
  
  private final CommandLineCompiler libtoolCompiler;
    
  protected String fortifyID = "fortifyID";
  
  private List<String[]> commands;
  
  private boolean dryRun;

  public static final File objDir = null;
  
  private SecureRandom r = new SecureRandom();


  protected CommandLineCompiler(final String identifierArg, final String[] sourceExtensions,
      final boolean libtool, final CommandLineCompiler libtoolCompiler,
      final Environment env) {
	  
    super(sourceExtensions);
        
    if (libtool && libtoolCompiler != null) {
    	
      throw new java.lang.IllegalArgumentException("libtoolCompiler should be null when libtool is true");
      
    }
    
    this.libtool = libtool;
    
    this.libtoolCompiler = libtoolCompiler;
    
    this.identifierArg = identifierArg;
        
    this.env = env;
    
  }

  
  protected abstract void addImpliedArgs(ArrayList<String> args, boolean debug, boolean multithreaded, boolean exceptions,
      LinkType linkType, Boolean rtti, OptimizationEnum optimization);

  
  /**
   * Adds command-line arguments for include directories.
   * 
   * If relativeArgs is not null will add corresponding relative paths
   * include switches to that vector (for use in building a configuration
   * identifier that is consistent between machines).
   * 
   * @param baseDirPath
   *          Base directory path.
   * @param includeDirs
   *          Array of include directory paths
   * @param args
   *          Vector of command line arguments used to execute the task
   * @param relativeArgs
   *          Vector of command line arguments used to build the
   *          configuration identifier
   */
  @Override
  public void addIncludes(final String baseDirPath, final File[] includeDirs, final ArrayList<String> args,
      final ArrayList<String> relativeArgs, final StringBuilder includePathId, final boolean isSystem) {
	  
    for (final File includeDir : includeDirs) {
    	
      args.add(getIncludeDirSwitch(includeDir.getAbsolutePath(), isSystem));
      
      if (relativeArgs != null) {
    	  
        String relative = "relative";
        
		try {
			
			relative = CUtil.getRelativePath(baseDirPath, includeDir);
			
		} catch (Exception e) {

			throw new NullPointerException();
			
		}
        
        relativeArgs.add(getIncludeDirSwitch(relative, isSystem));
        
        if (includePathId != null) {
        	
          if (includePathId.length() == 0) {
        	  
            includePathId.append("/I");
            
          } else {
        	  
            includePathId.append(" /I");
            
          }
          
          includePathId.append(relative);
          
        }
        
      }
      
    }
    
  }

  
  protected abstract void addWarningSwitch(ArrayList<String> args, int warnings);

  protected void buildDefineArguments(final CompilerDef[] defs, final ArrayList<String> args) {
	  
    //
    // assume that we aren't inheriting defines from containing <cc>
    //
    UndefineArgument[] merged = defs[0].getActiveDefines();
    
    for (int i = 1; i < defs.length; i++) {
    	
      //
      // if we are inheriting, merge the specific defines with the
      // containing defines
      merged = UndefineArgument.merge(defs[i].getActiveDefines(), merged);
      
    }
    
    final StringBuilder buf = new StringBuilder(30);
    
    for (final UndefineArgument current : merged) {
    	
      buf.setLength(0);
      
      if (current.isDefine()) {
    	  
        getDefineSwitch(buf, current.getName(), current.getValue());
        
      } else {
    	  
        getUndefineSwitch(buf, current.getName());
        
      }
      
      args.add(buf.toString());
      
    }
    
  }

  
  

  
  protected abstract String getOutputSuffix();

  protected abstract String getBaseOutputName(String inputFile);

  
  /**
   * Compiles a source file.
   * 
   */
  public void compile(final CCTask task, String[] args,
      final String[] endArgs, final boolean relentless, final CommandLineCompilerConfiguration config)
    		  throws BuildException {
	  
    BuildException exc = null;
    
    //
    // determine length of executable name and args
    //
    
    String commandVariable = getCommandWithPath(config);
    
    if (config.isUseCcache()) {
    	
      // Replace the command with "ccache" and push the old compiler
      // command into the args.
    	
      final String compilerCommand = commandVariable;
      
      commandVariable = CCACHE_CMD;
      
      args = ObjectArrays.concat(compilerCommand, args);
      
    }
    
    int baseLength = commandVariable.length() + args.length + endArgs.length;
    
    if (this.libtool) {
    	
      baseLength += 8;
      
    }
    
    if (baseLength > getMaximumCommandLength()) {
    	
      throw new BuildException("Command line is over maximum length without specifying source file");
      
    }
    
    //
    // typically either 1 or Integer.MAX_VALUE
    //

    ArrayList<String> commandlinePrefix = new ArrayList<>();
      
    if (this.libtool) {
    	  
      commandlinePrefix.add("libtool");
        
    }
      
    if((this.fortifyID !=null) && (!this.fortifyID.equals("")))
    {
    	
     // If FortifyID attribute was set, run the Fortify framework

      commandlinePrefix.add("sourceanalyzer");
        
      commandlinePrefix.add("-b");
        
      commandlinePrefix.add(this.fortifyID);
        
    }
      
    commandlinePrefix.add(commandVariable);
      
    Collections.addAll(commandlinePrefix, args);

    int retval = this.r.nextInt(5);
      
    // if the process returned a failure code and
    // we aren't holding an exception from an earlier
    // interation
      
    if (retval != 0) {
    	  
      //
      // construct the exception
      //
    	  
      exc = new BuildException(getCommandWithPath(config) + " failed with return code " + retval, task.getLocation());

      //
      // and throw it now unless we are relentless
      //
        
      if (!relentless) {
        	
        throw exc;
          
      }
        
    }
    
  }

  
  protected CompilerConfiguration createConfiguration(final LinkType linkType,
      final ProcessorDef[] baseDefs, final CompilerDef specificDef) {
        
    final ArrayList<String> args = new ArrayList<>();
    
    final CompilerDef[] defaultProviders = new CompilerDef[baseDefs.length + 1];
    
    defaultProviders[0] = specificDef;

    // add command line arguments inherited from <cc> element
    // any "extends" and finally the specific CompilerDef    

    if (specificDef.isClearDefaultOptions()) {
    	
      final boolean multithreaded = specificDef.getMultithreaded(defaultProviders, 1);
      
      final boolean debug = specificDef.getDebug(baseDefs, 0);
      
      final boolean exceptions = specificDef.getExceptions(defaultProviders, 1);
      
      final Boolean rtti = specificDef.getRtti(defaultProviders, 1);
      
      final OptimizationEnum optimization = specificDef.getOptimization(defaultProviders, 1);
      
      this.addImpliedArgs(args, debug, multithreaded, exceptions, linkType, rtti, optimization);
      
    }

    //
    // add all appropriate defines and undefines
    //
    
    final int warnings = specificDef.getWarnings(defaultProviders, 0);
    
    addWarningSwitch(args, warnings);    
              
    //
    // Want to have distinct set of arguments with relative
    // path names for includes that are used to build
    // the configuration identifier
    //
    
    //
    // add all active include and sysincludes
    //
                  
    CommandLineCompiler compiler = this;
    
    // Pass the fortifyID for compiler
    compiler.fortifyID = specificDef.getFortifyID();
    
    compiler.setCommands(specificDef.getCommands());
    
    compiler.setDryRun(specificDef.isDryRun());
    
	return null;

  }


  

  
  public String getCommandWithPath(final CommandLineCompilerConfiguration config) {
	  
    if (config.getCommandPath() != null) {
    	
      final File commandVariable = new File(config.getCommandPath(), CfgFilenameState.getCommand());
      
      try {
    	  
        return commandVariable.getCanonicalPath();
        
      } catch (final Exception e) {
    	  
        return commandVariable.getAbsolutePath();
        
      }
      
    } else {
    	
      return CfgFilenameState.getCommand();
      
    }
    
  }

  
  @Override
  protected abstract void getDefineSwitch(StringBuilder buffer, String define, String value);

  protected abstract File[] getEnvironmentIncludePath();

  
  @Override
  public String getIdentifier(String[] strings, String string) {
	  
    if (this.identifier == null) {
    	
      if (this.identifierArg == null) {
    	  
        this.identifier = getIdentifier(new String[] {
        		
        		CfgFilenameState.getCommand()
        }, CfgFilenameState.getCommand());
        
      } else {
    	  
        this.identifier = getIdentifier(new String[] {
        		
        		CfgFilenameState.getCommand(), this.identifierArg
        }, CfgFilenameState.getCommand());
        
      }
      
    }
    
    return this.identifier;
    
  }

  
  protected abstract String getIncludeDirSwitch(String source);

  
  /**
   * Added by Darren Sargent 22Oct2008 Returns the include dir switch value.
   * Default implementation doesn't treat system includes specially, for
   * compilers which don't care.
   * 
   * @param source
   *          the given source value.
   * @param isSystem
   *          "true" if this is a system include path
   * 
   * @return the include dir switch value.
   */
  protected String getIncludeDirSwitch(final String source, final boolean isSystem) {
	  
    return getIncludeDirSwitch(source);
    
  }

  
  protected String getInputFileArgument(final String filename) {
	  
    //
    // if there is an embedded space,
    // must enclose in quotes
    String relative="";
    
    String inputFile;
    
    if (relative.isEmpty()) {
    	
      inputFile = filename;
      
    } else {
    	
      inputFile = relative;
      
    }
    
    if (inputFile.indexOf(' ') >= 0) {
    	
      return "\"" + inputFile +
          "\"";
      
    }
    
    return inputFile;
    
  }

  
  protected final boolean getLibtool() {
	  
    return this.libtool;
    
  }

  
  /**
   * Obtains the same compiler, but with libtool set
   * 
   * Default behavior is to ignore libtool
   */
  public final CommandLineCompiler getLibtoolCompiler() {
	  
    if (this.libtoolCompiler != null) {
    	
      return this.libtoolCompiler;
      
    }
    
    return this;
    
  }

  
  public abstract int getMaximumCommandLength();

  
  protected int getMaximumInputFilesPerCommand() {
	  
    return Integer.MAX_VALUE;
    
  }

  
  /**
   * Get total command line length due to the input file.
   * 
   * @param outputDir
   *          File output directory
   * @param inputFile
   *          String input file
   * @return int characters added to command line for the input file.
   */
  protected int getTotalArgumentLengthForInputFile(final File outputDir, final String inputFile) {
	  
    final int argumentCountPerInputFile = GETARGUMENTCOUNTPERINPUTFILE;
    
    int len=0;
    
    for (int k = 0; k < argumentCountPerInputFile; k++) {
    	
      len+=getInputFileArgument(inputFile).length();
      
    }
    
    return len + argumentCountPerInputFile; // argumentCountPerInputFile added for spaces
    
  }

  
  public abstract void getUndefineSwitch(StringBuilder buf, String define);

  
  /**
   * This method is exposed so test classes can overload and test the
   * arguments without actually spawning the compiler
   */
  protected int runCommand(final CCTask task, final File workingDir, final String[] cmdline) throws BuildException {
	  
    if(commands!=null)
    	
      commands.add(cmdline);
    
    if (dryRun) return 0;
    
    return CUtil.runCommand(task, workingDir, cmdline, this.env);
    
  }

  
  public void setCommands(List<String[]> commands) {
    
	this.commands = commands;
	  
  }
  
  public static String getCommand() {
	  
	return COMMAND;
	
  }

  
  public static String getPrefix() {
	  
	return PREFIX;
		
  }
  
  public static File getObjdir() {
	  
	return objDir;
		  
  }

  
  public boolean isDryRun() {
    
	return dryRun;
	  
  }

  
  public void setDryRun(boolean dryRun) {
    
	this.dryRun = dryRun;
	  
  }

  
	/**
	 *  Empty Implementation
	 * @param baseDirPath Base directory path.
	 * @param includeDirs
	 *            Array of include directory paths
	 * @param args
	 *            Vector of command line arguments used to execute the task
	 * @param relativeArgs
	 *            Vector of command line arguments used to build the
	 * @param includePathId
	 * @param isSystem
	 */
	protected void addIncludes1(String baseDirPath, File[] includeDirs, ArrayList<String> args, ArrayList<String> relativeArgs,
			StringBuilder includePathId, boolean isSystem) {
	
		
	}
	

	public DependencyInfo parseIncludes(String includePathIdentifier) {
		
		ArrayList<String> list1 = new ArrayList<>();
		
		ArrayList<String> list2 = new ArrayList<>();
		
		list1.add("Cat");
		
		list1.add("Dog");
		
		list2.add("Cat");
		
		list2.add("Dog");
	
		return new DependencyInfo(includePathIdentifier, includePathIdentifier, 10L, list1, list2);
		
	}
  
}
