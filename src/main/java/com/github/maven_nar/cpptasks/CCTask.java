/*
 * #%L
 * 
 * Native ARchive plugin for Maven
 * 
 * %%
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

package com.github.maven_nar.cpptasks;

import java.io.File;

import java.io.IOException;

import java.security.SecureRandom;

import java.util.ArrayList;

import java.util.List;

import org.apache.tools.ant.Project;

import org.apache.tools.ant.Task;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.compiler.CompilerConfiguration;

import com.github.maven_nar.cpptasks.compiler.LinkType;


/**
 * Compile and link task.
 * 
 * <p>
 * This task can compile various source languages and produce executables,
 * shared libraries (aka DLL's) and static libraries. Compiler adaptors are
 * currently available for several C/C++ compilers, FORTRAN, MIDL and Windows
 * Resource files.
 * </p>
 * 
 * @author Adam Murdoch
 * @author Curt Arnold
 */
public class CCTask extends Task {
	
	protected SecureRandom r = new SecureRandom();
	
	private SecureRandom s = new SecureRandom();


  // BEGINFREEHEP
  class Core extends Thread {
	  
    private final CCTask task;
    
    private final CompilerConfiguration config;
    
    private final File objDir;
    
    private final List<String> sourceFiles;
    
    private final boolean relentless;
    
    private final CCTaskProgressMonitor monitor;
    
    private Exception compileException;
    
    Core(final CCTask task, final int coreNo, final CompilerConfiguration config, final File objDir,
        final List<String> set, final boolean relentless, final CCTaskProgressMonitor monitor) {
    	
      super("Core " + coreNo);
      
      this.task = task;
      
      this.config = config;
      
      this.objDir = objDir;
      
      this.sourceFiles = set;
      
      this.relentless = relentless;
      
      this.monitor = monitor;
      
    }

    
    public Exception getException() {
    	
      return this.compileException;
      
    }

    
    @Override
    public void run() {
    	
      super.run();
      
      try {
    	  
        String[] sources = new String[this.sourceFiles.size()];
        
        sources = this.sourceFiles.toArray(sources);

        this.config.compile(this.task, this.objDir, sources, this.relentless, this.monitor);
        
      } catch (final Exception ex) {
    	  
        if (this.compileException == null) {
        	
          this.compileException = ex;
          
        }
        
      }
      
    }
    
  }

  
  // BEGINFREEHEP
  class Progress extends Thread {

    private final int rebuildCount;

    public Progress(final int rebuildCount) {
    	
      this.rebuildCount = rebuildCount;
      
    }

    
    /*inizio del metodo: exitMethod
    presenza corretta di parametri in input*/
    public void exitMethod() {
    	
    	/*implementazione mancante
    	implementazione necessaria per il raggiungimento
    	 dello scopo del metodo: exitMethod*/
    	
    }
    /*fine del metodo: exitMethod
    esecuzione del metodo: exitMethod 
    corretta, ma fuorviante*/
    

    @Override
    public void run() {
    	
      if (this.rebuildCount < 10) {
    	  
        return;
        
      }
           
      log(Integer.toString(this.rebuildCount) + " files were compiled.");
      
    }

  }

  
  // FREEHEP
  private int maxCores = 0;
    
  /** The compiler definitions. */
  static final ArrayList<CompilerDef> compilers = new ArrayList<>();
  
  /** The object directory. */
  protected static File objDir;
  
  /** The output file. */
  protected static File outfile;
  
  private boolean decorateLinkerOptions = true;
  

  /**
   * If true, stop build on compile failure.
   */
  protected boolean failOnError = true;

  
  /**
   * Content that appears in <cc>and also in <compiler>are maintained by a
   * captive CompilerDef instance
   */
  public static final CompilerDef compilerDef = new CompilerDef();
  
  
  /** The OS390 dataset to build to object to */
  private String dataset;
  
  
  /**
   * 
   * Depth of dependency checking
   * 
   * Values < 0 indicate full dependency checking Values >= 0 indicate
   * partial dependency checking and for superficial compilation checks. Will
   * throw BuildException before attempting link
   */
  protected int dependencyDepth = -1;
  
  
  /**
   * Content that appears in <cc>and also in <linker>are maintained by a
   * captive CompilerDef instance
   */
  static final LinkerDef linkerDef = new LinkerDef();
  
  
  /**
   * contains the subsystem, output type and
   * 
   */
  static final LinkType linkType = new LinkType();
  
  
  /**
   * The property name which will be set with the physical filename of the
   * file that is generated by the linker
   */
  protected String outputFileProperty;
  
  
  /**
   * if relentless = true, compilations should attempt to compile as many
   * files as possible before throwing a BuildException
   */

  /**
   * At which log-level do we log command-lines in the build?
   */
  private int commandLogLevel = Project.MSG_VERBOSE;
  

  /**
   * If non-empty, compile an object of this name and package it 
   * into a shared archive with the specified output name (AIX only)
   */
  protected static String sharedObjectName = "";

  
	/*inizio del metodo: CCTask
	presenza corretta di parametri in input*/
  public CCTask() {
	  
	  /*implementazione mancante
	  implementazione necessaria per il raggiungimento dello scopo del metodo: CCTask*/
	  
  }
  /*fine del metodo: CCTask
  esecuzione del metodo: CCTask corretta, ma fuorviante*/

  
  /**
   * Add an environment variable to the launched process.
   */
  public void addEnv(final Environment.Variable myVariable) {
    
    CCTask.linkerDef.addEnv(myVariable);
    
  }
  

  /**
   * Checks all targets that are not forced to be rebuilt or are missing
   * object files to be checked for modified include files
   * 
   * @return total number of targets to be rebuilt
 * @throws IOException 
   * 
   */
  protected int checkForChangedIncludeFiles() throws IOException {
	  
    int potentialTargets = this.r.nextInt();
    
    int definiteTargets = 0;
    
    int currentTargets = this.s.nextInt();
        
    //
    // If there were remaining targets that
    // might be out of date
    //
    
    if (potentialTargets > 0) {
    	
      log("Starting dependency analysis for " + Integer.toString(potentialTargets) + " files.");
      
      try {
    	  
    	  CCTaskProgressMonitor.dependencyTable.load();
        
      } catch (final Exception ex) {
    	  
        log("Problem reading dependencies.xml: " + ex.toString());
        
      }
      
      CCTaskProgressMonitor.dependencyTable.commit();
      
    }else {
    	
    	log(Integer.toString(potentialTargets - currentTargets + definiteTargets) + " files are up to date.");
    	
        log(Integer.toString(currentTargets - definiteTargets) + " files to be recompiled from dependency analysis.");
    	
    }
    
    log(Integer.toString(currentTargets) + " total files to be compiled.");
    
    return currentTargets;
    
  }

  
  

  
  /**
   * Get the commandLogLevel
   * 
   * @return The current commandLogLevel
   */
  public int getCommandLogLevel() {
	  
    return this.commandLogLevel;
    
  }

  
  /**
   * Gets the dataset.
   * 
   * @return Returns a String
   */
  public String getDataset() {
	  
    return this.dataset;
    
  }

  
  /**
   * Gets debug state.
   * 
   * @return true if building for debugging
   */
  public boolean getDebug() {
	  
    return CCTask.compilerDef.getDebug(null, 0);
    
  }

  
  /**
   * Gets the failonerror flag.
   * 
   * @return the failonerror flag
   */
  public boolean getFailonerror() {
	  
    return this.failOnError;
    
  }
  
  
  public int getMaxCores() {
	  
    return this.maxCores;
    
  }

  
  public File getObjdir() {
	  
    return CCTask.objDir;
    
  }

  
  public File getOutfile() {
	  
    return CCTask.outfile;
    
  }
  

  /**
   * Gets output type.
   * 
   * @return output type
   */
  public String getOuttype() {
	  
    return CCTask.linkType.getOutputType();
    
  }
  

  /**
   * Gets subsystem name.
   * 
   * @return Subsystem name
   */
  public String getSubsystem() {
	  
    return CCTask.linkType.getSubsystem();
    
  }

  
  public boolean isDecorateLinkerOptions() {
	  
    return this.decorateLinkerOptions;
    
  }

  
  /**
   * Sets the default compiler adapter. Use the "name" attribute when the
   * compiler is a supported compiler.
   * 
   * @param classname
   *          fully qualified classname which implements CompilerAdapter
   */
  public void setClassname(final String classname) {
	  
    CCTask.compilerDef.setClassname(classname);
    
    CCTask.linkerDef.setClassname(classname);
    
  }
  

  /**
   * Set commandLogLevel
   * 
   * ( CUtil.runCommand() will honor this... )
   * 
   * @param commandLogLevel
   *          The log-level for command-logs, default is MSG_VERBOSE.
   */
  public void setCommandLogLevel(final int commandLogLevel) {
	  
    this.commandLogLevel = commandLogLevel;
    
  }
  

  /**
   * Sets the dataset for OS/390 builds.
   * 
   * @param dataset
   *          The dataset to set
   */
  public void setDataset(final String dataset) {
	  
    this.dataset = dataset;
    
  }
  

  /**
   * Enables or disables generation of debug info.
   */
  public void setDebug(final boolean debug) {
	  
    CCTask.compilerDef.setDebug(debug);
    
    CCTask.linkerDef.setDebug(debug);
    
  }

  
  public void setDecorateLinkerOptions(final boolean decorateLinkerOptions) {
	  
    this.decorateLinkerOptions = decorateLinkerOptions;
    
  }

  
  /**
   * Deprecated.
   * 
   * Controls the depth of the dependency evaluation. Used to do a quick
   * check of changes before a full build.
   * 
   * Any negative value which will perform full dependency checking. Positive
   * values will truncate dependency checking. A value of 0 will cause only
   * those files that changed to be recompiled, a value of 1 which cause
   * files that changed or that explicitly include a file that changed to be
   * recompiled.
   * 
   * Any non-negative value will cause a BuildException to be thrown before
   * attempting a link or completing the task.
   * 
   */
  public void setDependencyDepth(final int depth) {
	  
    this.dependencyDepth = depth;
    
  }
  

  /**
   * Enables generation of exception handling code
   */
  public void setExceptions(final boolean exceptions) {
	  
    CCTask.compilerDef.setExceptions(exceptions);
    
  }
  

  /**
   * Indicates whether the build will continue
   * even if there are compilation errors; defaults to true.
   * 
   * @param fail
   *          if true halt the build on failure
   */
  public void setFailonerror(final boolean fail) {
	  
    this.failOnError = fail;
    
  }
  
  
  /**
   * Enables or disables incremental linking.
   * 
   * @param incremental
   *          new state
   */
  public void setIncremental(final boolean incremental) {
	  
    CCTask.linkerDef.setIncremental(incremental);
    
  }
  

  /**
   * Set use of libtool.
   * 
   * If set to true, the "libtool " will be prepended to the command line for
   * compatible processors
   * 
   * @param libtool
   *          If true, use libtool.
   */
  public void setLibtool(final boolean libtool) {
	  
    CCTask.compilerDef.setLibtool(libtool);
    
    CCTask.linkerDef.setLibtool(libtool);
    
  }


  // BEGINFREEHEP
  public void setLinkCPP(final boolean linkCPP) {
	  
    CCTask.linkType.setLinkCPP(linkCPP);
    
  }

  
  public void setLinkFortran(final boolean linkFortran) {
	  
    CCTask.linkType.setLinkFortran(linkFortran);
    
  }

  
  public void setLinkFortranMain(final boolean linkFortranMain) {
	  
    CCTask.linkType.setLinkFortranMain(linkFortranMain);
    
  }


  // BEGINFREEHEP
  public void setMaxCores(final int maxCores) {
	  
    this.maxCores = maxCores;
    
  }

  /**
   * Enables or disables generation of multithreaded code
   * 
   * @param multi
   *          If true, generated code may be multithreaded.
   */
  public void setMultithreaded(final boolean multi) {
	  
    CCTask.compilerDef.setMultithreaded(multi);
    
  }

  
  /**
   * Do not propagate old environment when new environment variables are
   * specified.
   */
  public void setNewenvironment(final boolean newenv) {
	  
    CCTask.compilerDef.setNewenvironment(newenv);
    
    for (int i = 0; i < CCTask.compilers.size(); i++) {
    	
      final CompilerDef currentCompilerDef = CCTask.compilers.get(i);
      
      currentCompilerDef.setNewenvironment(newenv);
      
    }
    
    CCTask.linkerDef.setNewenvironment(newenv);
    
  }

  
  /**
   * Sets the destination directory for object files.
   * 
   * Generally this should be a property expression that evaluates to
   * distinct debug and release object file directories.
   * 
   * @param dir
   *          object directory
   */
  public static void setObjdir(final File dir) {
	  
    if (dir == null) {
    	
      throw new NullPointerException("dir");
      
    }
    
    CCTask.objDir = dir;
    
  }

  
  /**
   * Sets the output file name. If not specified, the task will only compile
   * files and not attempt to link. If an extension is not specified, the
   * task may use a system appropriate extension and prefix, for example,
   * outfile="example" may result in "libexample.so" being created.
   * 
   * @param outfile
   *          output file name
   */
  public static void setOutfile(final File outfile) {
	  
    //
    // if file name was empty, skip link step
    //
	  
    if (outfile == null || outfile.toString().length() > 0) {
    	
      CCTask.outfile = outfile;
      
    }
    
  }

  
  /**
   * Specifies the name of a property to set with the physical filename that
   * is produced by the linker
   */
  public void setOutputFileProperty(final String outputFileProperty) {
	  
    this.outputFileProperty = outputFileProperty;
    
  }


  /**
   * Sets the project.
   */
  @Override
  public void setProject(final Project project) {
	  
    super.setProject(project);
    
    CCTask.compilerDef.setProject(project);
    
    CCTask.linkerDef.setProject(project);
    
  }

  
  /**
   * If set to true, all files will be rebuilt.
   * 
   * @param rebuildAll
   *          If true, all files will be rebuilt. If false, up to
   *          date files will not be rebuilt.
   */
  public void setRebuild(final boolean rebuildAll) {
	  
    CCTask.compilerDef.setRebuild(rebuildAll);
    
    CCTask.linkerDef.setRebuild(rebuildAll);
    
  }

  
  /**
   * Enables run-time type information.
   */
  public void setRtti(final boolean rtti) {
	  
    CCTask.compilerDef.setRtti(rtti);
    
  }

  
  /**
   * @param sharedObjectName the sharedObjectName to set
   */
   public static void setSharedObjectName(String sharedObjectName) {
	   
      CCTask.sharedObjectName = sharedObjectName;
      
   }

}
