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

package com.github.maven_nar.cpptasks.ibm;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.gcc.AbstractLdLinker;

import com.github.maven_nar.cpptasks.gcc.GccLibrarian;

import com.github.maven_nar.cpptasks.types.LibrarySet;

import java.util.ArrayList;

import java.util.List;


/**
 * Adapter for IBM(r) Visual Age(tm) Linker for AIX(tm)
 *
 * @author Curt Arnold
 */
public final class XlcrLinker extends AbstractLdLinker {
	
  private static final String[] discardFiles = new String[] {};
  
  private static final String[] objFiles = new String[] {
		  
      ".o", ".a", ".lib", ".dll", ".so", ".sl"
      
  };
  
  private static final XlcrLinker dllLinker = new XlcrLinker("xlC_r", objFiles, "lib");
  
  private static final XlcrLinker instance = new XlcrLinker("xlC_r", objFiles, "");

  
  public static XlcrLinker getInstance() {
	  
    return instance;
    
  }
  

  private XlcrLinker(final String command, final String[] extensions,
                      final String outputPrefix) {
	  
    //
    // just guessing that -? might display something useful
    //
    super(command, "-?", extensions, outputPrefix, false, null);
    
  }

  
  public void
      addImpliedArgs(final LinkType linkType, final List<String> args) {
    
    if (linkType.isSharedLibrary()) {
    	
      args.add("-qmkshrobj");
      
    }
    
  }

  
  /**
   * Gets identifier for the compiler.
   * 
   * Initial attempt at extracting version information
   * would lock up. Using a stock response.
   */
  @Override
  public String getIdentifier() {
	  
    return "xlC_r linker - unidentified version";
    
  }

  
  @Override
  public Linker getLinker(final LinkType type) {
	  
    if (type.isStaticLibrary()) {
    	
      return GccLibrarian.getInstance();
      
    }
    
    if (type.isSharedLibrary()) {
    	
      return dllLinker;
      
    }
    
    return instance;
    
  }


  @Override
  public String[] addLibrarySets(CCTask task, LibrarySet[] libsets, ArrayList<String> preargs, ArrayList<String> midargs,
		ArrayList<String> endargs) {

	return discardFiles;
	
  }

}
