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

package com.github.maven_nar.cpptasks.compaq;

import java.io.File;

import java.util.ArrayList;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.msvc.MsvcCompatibleLinker;

import com.github.maven_nar.cpptasks.msvc.MsvcProcessor;

import com.github.maven_nar.cpptasks.types.LibrarySet;


/**
 * Adapter for the Compaq(r) Visual Fortran linker.
 *
 * @author Curt Arnold
 */
public final class CompaqVisualFortranLinker extends MsvcCompatibleLinker {
	
  private static final CompaqVisualFortranLinker dllLinker = new CompaqVisualFortranLinker(".dll");
  
  private static final CompaqVisualFortranLinker instance = new CompaqVisualFortranLinker(".exe");

  
  public static CompaqVisualFortranLinker getInstance() {
	  
    return instance;
    
  }
  

  private CompaqVisualFortranLinker(final String outputSuffix) {
	  
    super("DF", "__bogus__.xxx", outputSuffix);
    
  }
  
  
  @Override
  protected void addImpliedArgs(final boolean debug, final LinkType linkType, final ArrayList<String> args) {
	  
    args.add("/NOLOGO");
    
    final boolean staticRuntime = linkType.isStaticRuntime();
    
    if (staticRuntime) {
    	
      args.add("/libs:static");
      
    } else {
    	
      args.add("/libs:dll");
      
    }
    
    if (debug) {
    	
      args.add("/debug");
      
    } 

    if (linkType.isSharedLibrary()) {
    	
      args.add("/dll");
      
    } else {
    	
      args.add("/exe");
      
    }
    
  }
  

  @Override
  public Linker getLinker(final LinkType type) {
	  
    if (type.isStaticLibrary()) {
    	
      return (Linker) CompaqVisualFortranLibrarian.getInstance();
      
    }
    
    if (type.isSharedLibrary()) {
    	
      return dllLinker;
      
    }
    
    return instance;
    
  }
  

  @Override
  public String[] getOutputFileSwitch(final String outputFile) {
	  
    final StringBuilder buf = new StringBuilder("/OUT:");
    
    if (outputFile.indexOf(' ') >= 0) {
    	
      buf.append('"');
      
      buf.append(outputFile);
      
      buf.append('"');
      
    } else {
    	
      buf.append(outputFile);
      
    }
    
    return new String[] {
      buf.toString()
    };
    
  }

  
  @Override
  public String[] addLibrarySets(CCTask task, LibrarySet[] libsets, ArrayList<String> preargs, ArrayList<String> midargs,
		ArrayList<String> endargs) {

	return addLibrarySets();
	
  }
  
  protected void addImpliedArgs(final ArrayList<String> args) {
	  
	    args.add("/nologo");
	    
	  }

	  
	  protected String getNewCommandFileSwitch(final String commandFile) {
		  
	    return MsvcProcessor.getCommandFileSwitch(commandFile);
	    
	  }
	  
	  @Override
	  public File[] getLibraryPath() {
		  
	    return new File[0];
	    
	  }
	  

	  public String[] getLibraryPatterns() {
		  
	    return new String[0];
	    
	  }
	  

	  public Linker getNewLinker(final LinkType type) {
		  
	    return CompaqVisualFortranLinker.getInstance().getLinker(type);
	    
	  }
	  
	  @Override
	  public boolean isCaseSensitive() {
		  
	    return false;
	    
	  }
	  
  
}
