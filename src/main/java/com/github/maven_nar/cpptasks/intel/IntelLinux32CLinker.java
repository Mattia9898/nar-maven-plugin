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

// FREEHEP
package com.github.maven_nar.cpptasks.intel;

import java.util.ArrayList;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.gcc.AbstractLdLinker;

import com.github.maven_nar.cpptasks.gcc.GccLibrarian;

import com.github.maven_nar.cpptasks.types.LibrarySet;


public final class IntelLinux32CLinker extends AbstractLdLinker {
	
  private static final String[] objFiles = new String[] {
      ".o", ".a", ".lib", ".dll", ".so", ".sl"
  };
  
  private static final IntelLinux32CLinker dllLinker = new IntelLinux32CLinker("lib", false,
      new IntelLinux32CLinker("lib", true, null));
  
  private static final IntelLinux32CLinker instance = new IntelLinux32CLinker("", false, null);

  
  public static IntelLinux32CLinker getInstance() {
	  
    return instance;
    
  }
  

  private IntelLinux32CLinker(final String outputPrefix, final boolean isLibtool,
      final IntelLinux32CLinker libtoolLinker) {
	  
    super("icc", "-V", objFiles, outputPrefix, isLibtool, libtoolLinker);
    
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
	
		return objFiles;
		
	}

}
