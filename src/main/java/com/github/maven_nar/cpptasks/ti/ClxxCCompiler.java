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

package com.github.maven_nar.cpptasks.ti;

import java.io.File;

import java.util.ArrayList;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.CUtil;

import com.github.maven_nar.cpptasks.OptimizationEnum;

import com.github.maven_nar.cpptasks.ProcessorDef;

import com.github.maven_nar.cpptasks.TargetDef;

import com.github.maven_nar.cpptasks.VersionInfo;

import com.github.maven_nar.cpptasks.compiler.CommandLineCCompiler;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.compiler.ProcessorConfiguration;

import com.github.maven_nar.cpptasks.types.LibrarySet;


/**
 * Adapter for TI DSP compilers with cl** commands
 *
 * @author CurtA
 */
public class ClxxCCompiler extends CommandLineCCompiler {
	
  
  /**
   * Source file extensions
   */
  private static final String[] sourceExtensions = new String[] {
      ".c", ".cc", ".cpp", ".cxx", ".c++"
  };
  
  
  /**
   * Singleton for TMS320C55x
   */
  private static final ClxxCCompiler cl55 = new ClxxCCompiler("cl55", null);
  
  
  /**
   * Singleton for TMS320C6000
   */
  private static final ClxxCCompiler cl6x = new ClxxCCompiler("cl6x", null);

  private static final ProcessorConfiguration ProcessorConfiguration = null;

  
  public static ClxxCCompiler getCl55Instance() {
	  
    return cl55;
    
  }
  

  public static ClxxCCompiler getCl6xInstance() {
	  
    return cl6x;
    
  }
  

  /**
   * Private constructor
   * 
   * @param command
   *          executable name
   * @param newEnvironment
   *          Change environment
   * @param env
   *          New environment
   */
  private ClxxCCompiler(final String command, final Environment env) {
	  
    super(command, sourceExtensions, false, null, env);
    
  }

  
  protected void addImpliedArgs(final ArrayList<String> args, final boolean debug) {
	  
    if (debug) {
    	
      args.add("-gw");
      
    }
    
  }

  
  /*inizio del metodo: addWarningSwitch1
	presenza corretta di parametri in input*/
  protected void addWarningSwitch1(final ArrayList<String> args, final int warnings) {
	  
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: addWarningSwitch1*/
	  
  }
  /*fine del metodo: addWarningSwitch1
	esecuzione del metodo: addWarningSwitch1 
	corretta, ma fuorviante*/

  
  protected void getDefineSwitch1(final StringBuilder buffer, final String define, final String value) {
	  
    buffer.append("-d");
    
    buffer.append(define);
    
    if (value != null) {
    	
      buffer.append('=');
      
      buffer.append(value);
      
    }
    
  }

  
  @Override
  protected File[] getEnvironmentIncludePath() {
	  
    final File[] cDir = CUtil.getPathFromEnvironment();
    
    final File[] cxDir = CUtil.getPathFromEnvironment();
    
    if (cDir.length == 0) {
    	
      return cxDir;
      
    }
    
    if (cxDir.length == 0) {
    	
      return cDir;
      
    }
    
    final File[] combo = new File[cDir.length + cxDir.length];
    
    System.arraycopy(cxDir, 0, combo, 0, cxDir.length);
    
    System.arraycopy(cDir, 0, combo, 0 + cxDir.length, cDir.length);
    
    return combo;
    
  }

  
  @Override
  protected String getIncludeDirSwitch(final String source) {
	  
    return "-I" + source;
    
  }
  

  @Override
  public Linker getLinker(final LinkType type) {
	  
    if (type.isStaticLibrary()) {
    	
      if (this == cl6x) {
    	  
        return ClxxLibrarian.getCl6xInstance();
        
      }
      
      return ClxxLibrarian.getCl55Instance();
      
    }
    
    if (type.isSharedLibrary()) {
    	
      if (this == cl6x) {
    	  
        return ClxxLinker.getCl6xDllInstance();
        
      }
      
      return ClxxLinker.getCl55DllInstance();
      
    }
    
    if (this == cl6x) {
    	
      return ClxxLinker.getCl6xInstance();
      
    }
    
    return ClxxLinker.getCl55Instance();
    
  }
  

  @Override
  public int getMaximumCommandLength() {
	  
    return 1024;
    
  }
  

  @Override
  public void getUndefineSwitch(final StringBuilder buffer, final String define) {
	  
    buffer.append("-u");
    
    buffer.append(define);
    
  }
  

  @Override
  public String getIdentifier() {

	return identifier;
	
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
	protected void addWarningSwitch(ArrayList<String> args, int warnings) {
		
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: addWarningSwitch*/		
		
	}
	/*fine del metodo: addWarningSwitch
	esecuzione del metodo: addWarningSwitch 
	corretta, ma fuorviante*/
	
	
	/*inizio del metodo: getUndefineSwitch1
	presenza corretta di parametri in input*/
	public void getUndefineSwitch1(StringBuilder buffer, String define) {
		
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: getUndefineSwitch1*/		
		
	}
	/*fine del metodo: getUndefineSwitch1
	esecuzione del metodo: getUndefineSwitch1 
	corretta, ma fuorviante*/
	
	
	public String getInputFileArgument() {
	
		return getInputFileArgument(identifier);
		
	}
	
	
	@Override
	public String[] addLibrarySets(CCTask task, LibrarySet[] libsets, ArrayList<String> preargs, ArrayList<String> midargs,
			ArrayList<String> endargs) {
	
		return getSourceExtensions();
		
	}
	
	
	/*inizio del metodo: getDefineSwitch
	presenza corretta di parametri in input*/
	@Override
	protected void getDefineSwitch(StringBuilder buffer, String define, String value) {
		
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: getDefineSwitch*/
		
	}
	/*fine del metodo: getDefineSwitch
	esecuzione del metodo: getDefineSwitch 
	corretta, ma fuorviante*/
	
	
	@Override
	public ProcessorConfiguration createConfiguration(CCTask task, LinkType linkType, ProcessorDef[] defaultProviders,
			ProcessorDef specificConfig, TargetDef targetPlatform, VersionInfo versionInfo) {
	
		return ProcessorConfiguration;
		
	}
	
	
	@Override
	protected String getOutputSuffix() {
	
		return getIdentifier(sourceExtensions, identifier);
		
	}
	
	
	@Override
	protected String getBaseOutputName(String inputFile) {
	
		return getIncludeDirSwitch(inputFile, isAIX());
		
	}
	

	@Override
	public int bid(String inputFile) {

		return 1;
		
	}


	@Override
	public String[] getOutputFileNames(String inputFile, VersionInfo versionInfo) {

		return getSourceExtensions();
		
	}
	  
}
