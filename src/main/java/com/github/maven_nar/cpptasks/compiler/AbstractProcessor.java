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

import java.util.ArrayList;

import org.apache.tools.ant.types.Environment;


/**
 * An abstract processor (compiler/linker) implementation.
 *
 * @author Curt Arnold
 */
public abstract class AbstractProcessor implements Processor, Cloneable {
	
	
  /**
   * default bid for a file name that the processor recognizes but does not
   * process and does not want to fall through to the linker
   */
  public static final int DEFAULT_DISCARD_BID = 1;
  
  
  /**
   * default bid for a file name that the processor desires to process
   */
  public static final int DEFAULT_PROCESS_BID = 100;
  

  /**
   * Determines the identification of a command line processor by capture the
   * first line of its output for a specific command.
   * 
   * @param command
   *          array of command line arguments starting with executable
   *          name. For example, { "cl" }
   * @param fallback
   *          start of identifier if there is an error in executing the
   *          command
   * @return identifier for the processor
   */
  protected String getIdentifier(final String[] command, final String fallback) {
	  
    String identifier = fallback;
    
    try {
    	
      final String[] cmdout = CaptureStreamHandler.run(command);
      
      if (cmdout.length > 0) {
    	  
        identifier = cmdout[0];
        
      }
      
    } catch (Exception ex) {
    	
      identifier = fallback + ":" + ex.toString();
      
    }
    
    return identifier;
    
  }

  
  private String[] headerExtensions = {"headerExtensions"};
  
  private String[] sourceExtensions = {"sourceExtensions"};

  protected AbstractProcessor(final String[] sourceExtensions) {
	  
    this.sourceExtensions = sourceExtensions.clone();
        
  }

  
  /**
   * Returns the bid of the processor for the file.
   * 
   * @param inputFile
   *          filename of input file
   * @return bid for the file, 0 indicates no interest, 1 indicates that the
   *         processor recognizes the file but doesn't process it (header
   *         files, for example), 100 indicates strong interest
   */
  public int bid() {
	      
    return 0;
    
  }

  
  @Override
  public Processor changeEnvironment(final boolean newEnvironment, final Environment env) {
	  
    return this;
    
  }

  
  AbstractProcessor abstractProcessorMethod() throws CloneNotSupportedException {
	  
	    return (AbstractProcessor) super.clone();
	    
  }
  

  public String[] getHeaderExtensions() {
	  
    return this.headerExtensions.clone();
    
  }

  
  /**
   * Gets the target operating system architecture
   * 
   * @return String target operating system architecture
   */
  protected String getOSArch() {
	  
    return System.getProperty("os.arch");
    
  }

  
  /**
   * Gets the target operating system name
   * 
   * @return String target operating system name
   */
  protected String getOSName() {
	  
    return System.getProperty("os.name");
    
  }

  
  public String[] getSourceExtensions() {
	  
    return this.sourceExtensions.clone();
    
  }

  
  /**
   * Returns true if the target operating system is Mac OS X or Darwin.
   * 
   * @return boolean
   */
  protected boolean isDarwin() {
	  
    final String osName = getOSName();
    
    return "Mac OS X".equals(osName);
    
  }

  
  // BEGINFREEHEP
  protected boolean isWindows() {
	  
    final String osName = getOSName();
    
    return osName != null && osName.startsWith("Windows");
    
  }
 
  
  protected boolean isAIX() {
	  
       final String osName = getOSName();
       
       return (osName != null) && osName.equals("AIX");
        
  }
	
  // ENDFREEHEP

  @Override
  public final String toString() {
	  
    return getIdentifier();
    
  }


  protected void addIncremental(boolean incremental, ArrayList<String> args) {

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
  @Override
public void addIncludes(String baseDirPath, File[] includeDirs, ArrayList<String> args, ArrayList<String> relativeArgs,
		StringBuilder includePathId, boolean isSystem) {

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
protected String decorateLinkerOption(StringBuilder buf, String arg) {

	return null;
	
}


/**
   * Add command line options for preprocessor macro
   * 
   * @see com.github.maven_nar.cpptasks.compiler.CommandLineCompiler#getDefineSwitch(java.lang.StringBuffer,
   *      java.lang.String, java.lang.String)
   */
protected void getDefineSwitch(StringBuilder buffer, String define, String value) {

	
}

}
