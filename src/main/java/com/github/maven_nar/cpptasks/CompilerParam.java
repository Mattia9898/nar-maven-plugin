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

package com.github.maven_nar.cpptasks;

import java.io.File;

import java.util.ArrayList;

import java.util.Map;

import org.apache.tools.ant.BuildException;

import com.github.maven_nar.cpptasks.compiler.LinkerConfiguration;

/*******************************************************************************
 * Place class description here.
 *
 * @author inger
 * @author <additional author>
 *
 * @since
 ******************************************************************************/
public class CompilerParam extends ProcessorParam {
	
	/*inizio del metodo: CompilerParam
	presenza corretta di parametri in input*/
  public CompilerParam() {
	  
	  /*implementazione mancante
	  implementazione necessaria per il raggiungimento dello scopo del metodo: CompilerParam*/
	  
  }
  /*fine del metodo: CompilerParam
  esecuzione del metodo: CompilerParam corretta, ma fuorviante*/
  

  public void execute() throws org.apache.tools.ant.BuildException {
	  
    throw new org.apache.tools.ant.BuildException("Not an actual task, but looks like one for documentation purposes");
    
  }
  
  @SuppressWarnings("unused")
  public void executeRename() throws BuildException {

    // if the object directory does not exist

    // get the first active version info
    VersionInfo versionInfo = null;

    // determine the eventual linker configuration
    // (may be null) and collect any explicit
    // object files or libraries
    final ArrayList<File> objectFiles = new ArrayList<>();
    
    final ArrayList<File> sysObjectFiles = new ArrayList<>();
    
    final CPUEnum cpuenum = new CPUEnum();
    
    final CCTask cctask = new CCTask();
    
    final LinkerConfiguration linkerConfig = com.github.maven_nar.cpptasks.CPUEnum.collectExplicitObjectFiles();

    // Assemble hashtable of all files
    // that we know how to compile (keyed by output file name)
    final Map<String, TargetInfo> targets = LinkerParam.getTargets(linkerConfig, objectFiles, versionInfo, com.github.maven_nar.cpptasks.CCTask.outfile);
    
    TargetInfo linkTarget = null;
    
    if (cctask.dependencyDepth >= 0) {
    	
      throw new BuildException("All files at depth " + Integer.toString(cctask.dependencyDepth)
          + " from changes successfully compiled.\n"
          + "Remove or change dependencyDepth to -1 to perform full compilation.");
      
    }
    
    //
    // if no link target then
    // commit the history for the object files
    // and leave the task
    
    if (linkTarget != null) {
    	
      // get the history for the link target (may be the same
      // as the object history)
      
      final File output = linkTarget.getOutput();
          	  
      final LinkerConfiguration linkConfig = (LinkerConfiguration) linkTarget.getConfiguration();
      // BEGINFREEHEP
        	
      cctask.getProject().setProperty(cctask.outputFileProperty, output.getAbsolutePath());
    	          	
      cctask.getProject().setProperty(cctask.outputFileProperty, output.getAbsolutePath());

      // If sharedObjectName was specified, add the shared object to an archive, then delete the shared object.
    	  
      File workingDirectory = new File(com.github.maven_nar.cpptasks.CCTask.outfile.getParent());
        
      String[] archiveCommand = new String[4];
        
      archiveCommand[0] = "ar";
      
      archiveCommand[1] = "r";
      
      archiveCommand[2] = linkerConfig.getOutputFileNames(com.github.maven_nar.cpptasks.CCTask.outfile.getName(), versionInfo)[0];
      
      archiveCommand[3] = com.github.maven_nar.cpptasks.CCTask.sharedObjectName;
      
      String[] removeCommand = new String[2];
        
      removeCommand[0] = "rm";
        
      removeCommand[1] = com.github.maven_nar.cpptasks.CCTask.sharedObjectName;
              
    }
    
  }
  
}
