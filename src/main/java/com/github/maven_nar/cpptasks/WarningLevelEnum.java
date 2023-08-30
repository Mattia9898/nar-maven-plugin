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

import org.apache.tools.ant.types.EnumeratedAttribute;

import com.github.maven_nar.cpptasks.types.ConditionalFileSet;

import com.github.maven_nar.cpptasks.types.LibrarySet;

import com.github.maven_nar.cpptasks.types.SystemLibrarySet;


/**
 * Enumerated attribute with the values "none", "severe", "default",
 * "production", "diagnostic", and "aserror".
 */
public final class WarningLevelEnum extends EnumeratedAttribute {
	
	
  /**
   * Constructor.
   *
   */
  public WarningLevelEnum() {
	  
    setValue("default");
    
  }
  

  /**
   * Get allowable values.
   * 
   * @return allowable values
   */
  @Override
  public String[] getValues() {
	  
    return new String[] {
        "none", "severe", "default", "production", "diagnostic", "aserror"
    };
    
  }
  
  
  /**
   * Adds a source file set.
   * 
   * Files in these filesets will be auctioned to the available compiler
   * configurations, with the default compiler implied by the cc element
   * bidding last. If no compiler is interested in the file, it will be
   * passed to the linker.
   * 
   * To have a file be processed by a particular compiler configuration, add
   * a fileset to the corresponding compiler element.
   */
  public void addFileset(final ConditionalFileSet srcSet) {
	  
    CCTask.compilerDef.addFileset(srcSet);
    
  }
  
  
  /**
   * Adds a library set.
   * 
   * Library sets will be inherited by all linker elements that do not have
   * inherit="false".
   * 
   * @param libset
   *          library set
   * @throws NullPointerException
   *           if libset is null.
   */
  public void addLibset(final LibrarySet libset) {
	  
    if (libset == null) {
    	
      throw new NullPointerException("libset");
      
    }
    
    CCTask.linkerDef.addLibset(libset);
    
  }
  
  
  /**
   * Specifies the generation of IDE project file. Experimental.
   * 
   * @param projectDef
   *          project file generation specification
   */
  public void addProject(final CompilerDef projectDef) {
	  
    if (projectDef == null) {
    	
      throw new NullPointerException("projectDef");
      
    }
    
    CCTask.compilers.add(projectDef);
    
  }
  
  
  /**
   * Adds a system library set. Timestamps and locations of system library
   * sets are not used in dependency analysis.
   * 
   * Essential libraries (such as C Runtime libraries) should not be
   * specified since the task will attempt to identify the correct libraries
   * based on the multithread, debug and runtime attributes.
   * 
   * System library sets will be inherited by all linker elements that do not
   * have inherit="false".
   * 
   * @param libset
   *          library set
   * @throws NullPointerException
   *           if libset is null.
   */
  public void addSyslibset(final SystemLibrarySet libset) {
	  
    if (libset == null) {
    	
      throw new NullPointerException("libset");
      
    }
    
    CCTask.linkerDef.addSyslibset(libset);
    
  }
  
}
