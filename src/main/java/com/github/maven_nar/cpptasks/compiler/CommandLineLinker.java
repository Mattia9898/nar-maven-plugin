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

import java.io.FileWriter;

import java.io.IOException;

import java.util.*;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.CUtil;

import com.github.maven_nar.cpptasks.LinkerDef;

import com.github.maven_nar.cpptasks.ProcessorDef;

import com.github.maven_nar.cpptasks.ProcessorParam;

import com.github.maven_nar.cpptasks.TargetDef;

import com.github.maven_nar.cpptasks.VersionInfo;


/**
 * An abstract Linker implementation that performs the link via an external
 * command.
 *
 * @author Adam Murdoch
 */
public abstract class CommandLineLinker extends AbstractLinker {
	
  private String command;
  
  private String prefix;
  
  private Environment env = null;
  
  private String identifier;
  
  private final String identifierArg;
  
  private final boolean isLibtool;
  
  private final CommandLineLinker libtoolLinker;
  
  private final String outputSuffix;
  
  private List<String[]> commands;
  
  private boolean dryRun;

  // FREEHEP
  private static final int MAXPATHLENGTH = 250;

  /** Creates a comand line linker invocation */
  protected CommandLineLinker(final String command, 
		  final String identifierArg, 
		  final String[] extensions,
		  final String outputSuffix, 
		  final boolean isLibtool,
		  final CommandLineLinker libtoolLinker) {
	  
    super(extensions);
    
    this.command = command;
    
    this.identifierArg = identifierArg;
    
    this.outputSuffix = outputSuffix;
    
    this.isLibtool = isLibtool;
    
    this.libtoolLinker = libtoolLinker;
    
  }

  
  protected void addBase(final CCTask task, final long base, final ArrayList<String> preargs) {
    // NB: Do nothing by default.
  }

  
  protected void addEntry(final CCTask task, final String entry, final ArrayList<String> preargs) {
    // NB: Do nothing by default.
  }

  
  protected void addFixed(final CCTask task, final Boolean fixed, final ArrayList<String> preargs) {
    // NB: Do nothing by default.
  }

  
  public void addImpliedArgs(final CCTask task, final boolean debug, final LinkType linkType,
      final List<String> preargs) {
    // NB: Do nothing by default.
  }

  
  protected void addIncremental(final CCTask task, final boolean incremental, final ArrayList<String> preargs) {
    // NB: Do nothing by default.
  }

  
  protected void addLibraryDirectory(final File libraryDirectory, final LinkedList<String> preargs) {
	  
    try {
    	
      if (libraryDirectory != null && libraryDirectory.exists()) {
    	  
        final File currentDir = new File(".").getParentFile();
        
        String path = libraryDirectory.getCanonicalPath();
        
        if (currentDir != null) {
        	
          final String currentPath = currentDir.getCanonicalPath();
          
          path = CUtil.getRelativePath(currentPath, libraryDirectory);
          
        }
        
        addLibraryPath(preargs, path);
        
      }
      
    } catch (final Exception e) {
    	
      throw new IllegalArgumentException("Unable to add library path: " + libraryDirectory);
      
    }
    
  }

  
  protected void addLibraryPath(final LinkedList<String> preargs, final String path) {
  }

  
  //
  // Windows processors handle these through file list
  //
  protected String[] addLibrarySets() {
	  
    return getOutputFileSwitch1(outputSuffix);
    
  }

  
  protected void addMap(final CCTask task, final boolean map, final ArrayList<String> preargs) {
    // NB: Do nothing by default.
  }

  
  protected void addStack(final CCTask task, final int stack, final ArrayList<String> preargs) {
    // NB: Do nothing by default.
  }

  
  @Override
  protected LinkerConfiguration createConfiguration(final CCTask task, final LinkType linkType,
      final ProcessorDef[] baseDefs, final LinkerDef specificDef, final TargetDef targetPlatform,
      final VersionInfo versionInfo) {

    final ArrayList<String> preargs = new ArrayList<>();
    
    final ArrayList<String> midargs = new ArrayList<>();
    
    final ArrayList<String> endargs = new ArrayList<>();
    
    final ArrayList<String>[] args = new ArrayList[] {
        preargs, midargs, endargs
    };

    this.prefix  = specificDef.getLinkerPrefix();

    final LinkerDef[] defaultProviders = new LinkerDef[baseDefs.length + 1];
    
    defaultProviders[0] = specificDef;

    //
    // add command line arguments inherited from <cc> element
    // any "extends" and finally the specific CompilerDef

    final ArrayList<ProcessorParam> params = new ArrayList<>();
    //
    // add command line arguments inherited from <cc> element
    // any "extends" and finally the specific CompilerDef
    ProcessorParam[] paramArray;

    paramArray = params.toArray(new ProcessorParam[params.size()]);

    final boolean debug = specificDef.getDebug(baseDefs, 0);

    final String startupObject = getStartupObject();

    addImpliedArgs(task, debug, linkType, preargs);
    
    addIncremental(task, specificDef.getIncremental(defaultProviders, 1), preargs);
    
    addFixed(task, specificDef.getFixed(defaultProviders, 1), preargs);
    
    addMap(task, specificDef.getMap(defaultProviders, 1), preargs);
    
    addBase(task, specificDef.getBase(defaultProviders, 1), preargs);
    
    addStack(task, specificDef.getStack(defaultProviders, 1), preargs);
    
    addEntry(task, specificDef.getEntry(defaultProviders, 1), preargs);

    String[] libnames = null;
    // FREEHEP call at all times

    libnames = addLibrarySets();
    
    final String[][] options = new String[][] {
        new String[args[0].size() + args[1].size()], new String[args[2].size()]
    };

    // if this linker doesn't have an env, and there is a more generically
    // definition for environment, use it.
    
    if (null != specificDef.getEnv() && null == this.env) {
    	
      this.env = specificDef.getEnv();
      
    }
    
    final boolean rebuild = specificDef.getRebuild(baseDefs, 0);
    
    setCommands(specificDef.getCommands());
    
    setDryRun(specificDef.isDryRun());


    return new CommandLineLinkerConfiguration(this, options, paramArray, rebuild, libnames,
        startupObject);
    
  }
  

  /**
   * Allows drived linker to decorate linker option.
   * Override by GccLinker to prepend a "-Wl," to
   * pass option to through gcc to linker.
   *
   * @param buf
   *          buffer that may be used and abused in the decoration process,
   *          must not be null.
   * @param arg
   *          linker argument
   */
  @Override
  public String decorateLinkerOption(final StringBuilder buf, final String arg) {
	  
    return arg;
    
  }
  

  protected final String getCommand() {
	  
    if (this.prefix != null && (!this.prefix.isEmpty())) {
    	
      return this.prefix + this.command;
      
    } else {
    	
      return this.command;
      
    }
    
  }
  

  protected abstract String getCommandFileSwitch(String commandFile);
  

  public String getCommandWithPath(final CommandLineLinkerConfiguration config) {
	  
    if (config.getCommandPath() != null) {
    	
      final File commandVariable = new File(config.getCommandPath(), this.getCommand());
      
      try {
    	  
        return commandVariable.getCanonicalPath();
        
      } catch (final IOException e) {

        return commandVariable.getAbsolutePath();
        
      }
      
    } else {
    	
      return this.getCommand();
      
    }
    
  }
  

  @Override
  public String getIdentifier() {
	  
    if (this.identifier == null) {
    	
      if (this.identifierArg == null) {
    	  
        this.identifier = getIdentifier(new String[] {
          this.getCommand()
        }, this.getCommand());
        
      } else {
    	  
        this.identifier = getIdentifier(new String[] {
          this.getCommand(), this.identifierArg
        }, this.getCommand());
        
      }
      
    }
    
    return this.identifier;
    
  }
  

  public final CommandLineLinker getLibtoolLinker() {
	  
    if (this.libtoolLinker != null) {
    	
      return this.libtoolLinker;
      
    }
    
    return this;
    
  }
  

  protected abstract int getMaximumCommandLength();

  
  @Override
  public String[] getOutputFileNames(final String baseName, final VersionInfo versionInfo) {
	  
    return new String[] {
      baseName + this.outputSuffix
    };
    
  }
  

  protected String[] getOutputFileSwitch1(final String outputFile) {
	  
    // FREEHEP BEGIN
    if (isWindows() && outputFile.length() > CommandLineLinker.MAXPATHLENGTH) {
    	
      throw new BuildException("Absolute path too long, " + outputFile.length() + " > " + CommandLineLinker.MAXPATHLENGTH + ": '"
          + outputFile);
      
    }
    
    // FREEHEP END
    return getHeaderExtensions();
    
  }
  

  protected abstract String[] getOutputFileSwitch(String outputFile);
  

  protected String getStartupObject() {
	  
    return null;
    
  }
  

  /**
   * Performs a link using a command line linker
 * @throws IOException 
   *
   */
  public void link(final CCTask task, final File outputFile, final String[] sourceFiles,
      final CommandLineLinkerConfiguration config) throws BuildException, IOException {
	  
    final File parentDir = new File(outputFile.getParent());
    
    String parentPath;
    
    try {
    	
      parentPath = parentDir.getCanonicalPath();
      
    } catch (final IOException ex) {
    	
      parentPath = parentDir.getAbsolutePath();
      
    }
    
    String[] execArgs = prepareArguments(task, parentPath, sourceFiles, config);
    
    int commandLength = 0;
    
    for (final String execArg : execArgs) {
    	
      commandLength += execArg.length() + 1;
      
    }

    //
    // if command length exceeds maximum
    // then create a temporary
    // file containing everything but the command name
    if (commandLength >= this.getMaximumCommandLength()) {
    	
      try {
    	  
        execArgs = prepareResponseFile(outputFile, execArgs);
        
      } catch (final IOException ex) {
    	  
        throw new BuildException(ex);
        
      }
      
    }
    

    final int retval = runCommand(task, parentDir, execArgs);
    //
    // if the process returned a failure code then
    // throw an BuildException
    //
    
    if (retval != 0) {
    	
      //
      // construct the exception
      //
      throw new BuildException(getCommandWithPath(config) + " failed with return code " + retval, task.getLocation());
      
    }

  }
  

  /**
   * Prepares argument list for exec command. Will return null
   * if command line would exceed allowable command line buffer.
   *
   * @param task
   *          compilation task.
   * @param outputFile
   *          linker output file
   * @param sourceFiles
   *          linker input files (.obj, .o, .res)
   * @param config
   *          linker configuration
   * @return arguments for runTask
   */
  protected String[] prepareArguments(final CCTask task, final String outputFile,
      final String[] sourceFiles, final CommandLineLinkerConfiguration config) {

    final String[] preargs = config.getPreArguments();
    
    final String[] endargs = config.getEndArguments();
    
    final String[] outputSwitch = getOutputFileSwitch1(outputFile);
    
    int allArgsCount = preargs.length + 1 + outputSwitch.length + sourceFiles.length + endargs.length;
    
    if (this.isLibtool) {
    	
      allArgsCount++;
      
    }
    
    final String[] allArgs = new String[allArgsCount];
    
    int index = 0;
    
    if (this.isLibtool) {
    	
      allArgs[index++] = "libtool";
      
    }
    
    allArgs[index++] = getCommandWithPath(config);
    
    final StringBuilder buf = new StringBuilder();

    for (final String prearg : preargs) {
    	
      allArgs[index++] = task.isDecorateLinkerOptions() ? decorateLinkerOption(buf, prearg) : prearg;
      
    }

    for (final String element : outputSwitch) {
    	
      allArgs[index++] = element;
      
    }
    
    for (final String endarg : endargs) {
    	
      allArgs[index++] = task.isDecorateLinkerOptions() ? decorateLinkerOption(buf, endarg) : endarg;
      
    }

    return allArgs;
    
  }

  
  /**
   * Processes filename into argument form
   *
   */
  protected String prepareFilename(final StringBuilder buf, final String sourceFile) {
	  
    // FREEHEP BEGIN exit if absolute path is too long. Max length on relative
    // paths in windows is even shorter.
    if (isWindows() && sourceFile.length() > CommandLineLinker.MAXPATHLENGTH) {
    	
      throw new BuildException("Absolute path too long, " + sourceFile.length() + " > " + CommandLineLinker.MAXPATHLENGTH + ": '"
          + sourceFile);
      
    }
    
    // FREEHEP END
    return quoteFilename(buf, sourceFile);
    
  }

  
  /**
   * Prepares argument list to execute the linker using a
   * response file.
   *
   * @param outputFile
   *          linker output file
   * @param args
   *          output of prepareArguments
   * @return arguments for runTask
   */
  protected String[] prepareResponseFile(final File outputFile, final String[] args) throws IOException {
	  
    final String baseName = outputFile.getName();
    
    final File commandFile = new File(outputFile.getParent(), baseName + ".rsp");
    
    try(final FileWriter writer = new FileWriter(commandFile);){
    	
    	int execArgCount = 1;
    	
    	if (this.isLibtool) {
    		
    		execArgCount++;
    		
    	}
    	
    	final String[] execArgs = new String[execArgCount + 1];
    	
    	System.arraycopy(args, 0, execArgs, 0, execArgCount);
    	
    	execArgs[execArgCount] = getCommandFileSwitch(commandFile.toString());
    
    	for (int i = execArgCount; i < args.length; i++) {
    		
    		//
    		// if embedded space and not quoted then
    		// quote argument
    		if (args[i].contains(" ") && args[i].charAt(0) != '\"') {
    	  
    			writer.write('\"');
    			
    			writer.write(args[i]);
    			
    			writer.write("\"\n");
        
    		} else {
    	  
    			writer.write(args[i]);
    			
    			writer.write('\n');
        
    		}
      
    	}
    
    }
    
	return args;
    
  }

  
  @Override
  protected String quoteFilename(final StringBuilder buf, final String filename) {
	  
    if (filename.indexOf(' ') >= 0) {
    	
      buf.setLength(0);
      
      buf.append('\"');
      
      buf.append(filename);
      
      buf.append('\"');
      
      return buf.toString();
      
    }
    
    return filename;
    
  }
  

  /**
   * This method is exposed so test classes can overload
   * and test the arguments without actually spawning the
   * compiler
   */
  protected int runCommand(final CCTask task, final File workingDir, final String[] cmdline) throws BuildException {
	  
    commands.add(cmdline);
    
    if (dryRun)
    	
    	return 0;
    
    return CUtil.runCommand(task, workingDir, cmdline, this.env);
    
  }
  

  protected final void setCommand(final String command) {
	  
    this.command = command;
    
  }
  

  public void setCommands(List<String[]> commands) {
	  
    this.commands = commands;
    
  }
  

  public boolean isDryRun() {
	  
    return dryRun;
    
  }
  

  public void setDryRun(boolean dryRun) {
	  
    this.dryRun = dryRun;
    
  }

}
