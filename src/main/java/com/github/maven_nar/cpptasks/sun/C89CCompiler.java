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

import com.github.maven_nar.cpptasks.compiler.Processor;

import com.github.maven_nar.cpptasks.compiler.ProcessorConfiguration;

import com.github.maven_nar.cpptasks.types.LibrarySet;


/**
 * Adapter for the Sun C89 C++ Compiler
 *
 * @author Hiram Chirino (cojonudo14@hotmail.com)
 */
public class C89CCompiler extends CommandLineCCompiler {
	
  private static final C89CCompiler instance = new C89CCompiler(null);

  private static final ProcessorConfiguration PROCESSOR_CONFIGURATION = null;

  
  public static C89CCompiler getInstance() {
	  
    return instance;
    
  }
  

  private C89CCompiler(final Environment env) {
	  
    super("c89", null, false, null, env);
    
  }

  
  protected void addImpliedArgs(final ArrayList<String> args, final boolean debug) {
	  
    // Specifies that only compilations and assemblies be done.
    args.add("-c");
    
    if (debug) {
    	
      args.add("-g");
      
      args.add("-D_DEBUG");
      
    } else {
    	
      args.add("-DNDEBUG");
      
    }
    
  }

  
  protected void addWarningSwitch1(final ArrayList<String> args, final int level) {
	  
    C89Processor.addWarningSwitch(args, level);
    
  }
  

  @Override
  public Processor changeEnvironment(final boolean newEnvironment, final Environment env) {
	  
    if (newEnvironment || env != null) {
    	
      return new C89CCompiler(env);
      
    }
    
    return this;
    
  }

  
  protected void getDefineSwitch1(final StringBuilder buf, final String define, final String value) {
	  
    C89Processor.getDefineSwitch(buf, define, value);
    
  }
  

  @Override
  protected File[] getEnvironmentIncludePath() {
	  
    return CUtil.getPathFromEnvironment();
    
  }
  

  @Override
  protected String getIncludeDirSwitch(final String includeDir) {
	  
    return C89Processor.getIncludeDirSwitch(includeDir);
    
  }
  

  @Override
  public Linker getLinker(final LinkType type) {
	  
    return C89Linker.getInstance().getLinker(type);
    
  }
  

  @Override
  public int getMaximumCommandLength() {
	  
    return Integer.MAX_VALUE;
    
  }
  

  /* Only compile one file at time for now */
  @Override
  protected int getMaximumInputFilesPerCommand() {
	  
    return 1;
    
  }

  
  public void getUndefineSwitch1(final StringBuilder buf, final String define) {
	  
    C89Processor.getUndefineSwitch(buf, define);
    
  }
  

  @Override
  public int bid(String inputFile) {

	return DEFAULT_DISCARD_BID;
	
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
	
	
	public String getInputFileArgument() {
	
		return getOSName();
		
	}
	
	
	@Override
	public String[] addLibrarySets(CCTask task, LibrarySet[] libsets, ArrayList<String> preargs, ArrayList<String> midargs,
			ArrayList<String> endargs) {
	
		return getHeaderExtensions();
		
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
	
		return identifier;
		
	}
	
	
	@Override
	protected String getBaseOutputName(String inputFile) {
	
		return getOSName();
				
	}


	@Override
	public String[] getOutputFileNames(String inputFile, VersionInfo versionInfo) {

		return getSourceExtensions();
		
	}
	  
}
