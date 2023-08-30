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

package com.github.maven_nar.cpptasks.sun;

import java.io.File;

import java.util.ArrayList;

import java.util.List;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.CUtil;

import com.github.maven_nar.cpptasks.OptimizationEnum;

import com.github.maven_nar.cpptasks.ProcessorDef;

import com.github.maven_nar.cpptasks.TargetDef;

import com.github.maven_nar.cpptasks.VersionInfo;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.compiler.Processor;

import com.github.maven_nar.cpptasks.compiler.ProcessorConfiguration;

import com.github.maven_nar.cpptasks.gcc.GccCompatibleCCompiler;

import com.github.maven_nar.cpptasks.types.LibrarySet;


/**
 * Adapter for the Sun (r) Forte (tm) C++ compiler
 *
 * @author Curt Arnold
 */
public final class ForteCCCompiler extends GccCompatibleCCompiler {
  
  private static final ForteCCCompiler instance = new ForteCCCompiler("CC");

  private static final ProcessorConfiguration PROCESSOR_CONFIGURATION = null;

  
  /**
   * Gets singleton instance of this class
   */
  public static ForteCCCompiler getInstance() {
	  
    return instance;
    
  }
  

  private File[] includePath;

  
  /**
   * Private constructor. Use ForteCCCompiler.getInstance() to get singleton
   * instance of this class.
   */
  private ForteCCCompiler(final String command) {
	  
    super(command, new String[]{"-V"}, false, null, null);
    
  }
  

  public void addImpliedArgs(final List<String> args, final boolean debug, final boolean multithreaded,
      final LinkType linkType, final Boolean rtti, final OptimizationEnum optimization) {
	  
    args.add("-c");
    
    if (debug) {
    	
      args.add("-g");
      
    }
    
    if (optimization != null) {
    	
      args.add("-xO2");
     
    }
    
    if (rtti != null) {
    	
      if (rtti.booleanValue()) {
    	  
        args.add("-features=rtti");
        
      } else {
    	  
        args.add("-features=no%rtti");
        
      }
      
    }
    
    if (multithreaded) {
    	
      args.add("-mt");
      
    }
    
    if (linkType.isSharedLibrary()) {
    	
      args.add("-KPIC");
      
    }

  }

  
  public void addWarningSwitch1(final List<String> args, final int level) {
	  
    switch (level) {
    
      case 0:
    	  
        args.add("-w");
        
        break;
        
      case 1:
    	  
      case 2:

    	break;
    	
      case 3:
    	  
        args.add("+w");
        
        break;
        
      case 4:
    	  
        args.add("+w2");
        
        break;
        
      case 5:
    	  
        args.add("+w2");
        
        args.add("-xwe");
        
        break;
        
      default: 
    	  
    	throw new IllegalArgumentException("unreachable");
    	
    }
    
  }
  

  @Override
  public File[] getEnvironmentIncludePath() {
	  
    if (this.includePath == null) {
    	
      final File ccLoc = CUtil.getExecutableLocation("CC");
      
      if (ccLoc != null) {
    	  
        final File compilerIncludeDir = new File(new File(ccLoc, "../include").getAbsolutePath());
        
        if (compilerIncludeDir.exists()) {
        	
          this.includePath = new File[2];
          
          this.includePath[0] = compilerIncludeDir;
          
        }
        
      }
      
      if (this.includePath == null) {
    	  
        this.includePath = new File[1];
        
      }
            
    }
    
    return this.includePath;
    
  }

  
  @Override
  public Linker getLinker(final LinkType linkType) {
	  
    return ForteCCLinker.getInstance().getLinker(linkType);
    
  }
  

  @Override
  public int getMaximumCommandLength() {
	  
    return Integer.MAX_VALUE;
    
  }
  

  @Override
  public int bid(String inputFile) {

	return DEFAULT_PROCESS_BID;
	
  }
  

  @Override
  public Processor changeEnvironment(boolean newEnvironment, Environment env) {

	return getLibtoolCompiler();
	
  }
  

  @Override
  public String getIdentifier() {

	return PREFIX;
	
  }
	
  
	/*inizio del metodo: addImpliedArgs
	presenza corretta di parametri in input*/
	@Override
	protected void addImpliedArgs(ArrayList<String> args, boolean debug, boolean multithreaded, boolean exceptions,
			LinkType linkType, Boolean rtti, OptimizationEnum optimization) {
		
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: addImpliedArgs*/			
		
	}
	/*fine del metodo: addImpliedArgs
	esecuzione del metodo: addImpliedArgs 
	corretta, ma fuorviante*/
	
	
	/*inizio del metodo: addWarningSwitch
	presenza corretta di parametri in input*/
	@Override
	public void addWarningSwitch(ArrayList<String> args, int warnings) {
		
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: addWarningSwitch*/			
		
	}
	/*fine del metodo: addWarningSwitch
	esecuzione del metodo: addWarningSwitch 
	corretta, ma fuorviante*/
	
	
	@Override
	public String[] addLibrarySets(CCTask task, LibrarySet[] libsets, ArrayList<String> preargs, ArrayList<String> midargs,
			ArrayList<String> endargs) {
	
		return getSourceExtensions();
		
	}
	
	
	/*inizio del metodo: getDefineSwitch
	presenza corretta di parametri in input*/
	@Override
	public void getDefineSwitch(StringBuilder buffer, String define, String value) {
		
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: getDefineSwitch*/		
		
	}
	/*fine del metodo: getDefineSwitch
	esecuzione del metodo: getDefineSwitch 
	corretta, ma fuorviante*/
	
	
	/*inizio del metodo: getUndefineSwitch
	presenza corretta di parametri in input*/
	@Override
	public void getUndefineSwitch(StringBuilder buf, String define) {
		
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: getUndefineSwitch*/		
		
	}
	/*fine del metodo: getUndefineSwitch
	esecuzione del metodo: getUndefineSwitch 
	corretta, ma fuorviante*/
	
	
	@Override
	public ProcessorConfiguration createConfiguration(CCTask task, LinkType linkType, ProcessorDef[] defaultProviders,
			ProcessorDef specificConfig, TargetDef targetPlatform, VersionInfo versionInfo) {
	
		return PROCESSOR_CONFIGURATION;
		
	}
	
	
	@Override
	protected String getOutputSuffix() {
	
		return getOSName();
		
	}
	
	
	@Override
	protected String getBaseOutputName(String inputFile) {
	
		return getOutputSuffix();
		
	}


	@Override
	public String[] getOutputFileNames(String inputFile, VersionInfo versionInfo) {

		return getSourceExtensions();
		
	}
	  
}
