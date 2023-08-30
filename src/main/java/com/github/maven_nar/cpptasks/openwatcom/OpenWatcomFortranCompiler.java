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

package com.github.maven_nar.cpptasks.openwatcom;

import java.util.ArrayList;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.OptimizationEnum;

import com.github.maven_nar.cpptasks.ProcessorDef;

import com.github.maven_nar.cpptasks.TargetDef;

import com.github.maven_nar.cpptasks.VersionInfo;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.compiler.ProcessorConfiguration;

import com.github.maven_nar.cpptasks.parser.FortranParser;

import com.github.maven_nar.cpptasks.parser.Parser;

import com.github.maven_nar.cpptasks.types.LibrarySet;


/**
 * Adapter for the OpenWatcom Fortran compiler.
 *
 * @author Curt Arnold
 */
public final class OpenWatcomFortranCompiler extends OpenWatcomCompiler {
	
	
  /**
   * Singleton.
   */
  private static final OpenWatcomFortranCompiler[] INSTANCE = new OpenWatcomFortranCompiler[] {
    new OpenWatcomFortranCompiler("wfl386", false, null)
  };

  
  /**
   * Get instance.
   * 
   * @return OpenWatcomFortranCompiler compiler instance
   */
  public static OpenWatcomFortranCompiler getInstance() {
	  
    return INSTANCE[0];
    
  }
  

  /**
   * Constructor.
   * 
   * @param command
   *          String command
   * @param newEnvironment
   *          boolean use new environment
   * @param env
   *          Environment environment
   */
  private OpenWatcomFortranCompiler(final String command, final boolean newEnvironment, final Environment env) {
	  
    super(command, "/?", new String[] {
        ".f90", ".for", ".f"
    }, new String[] {
        ".i", ".i90", ".fpp"
    }, newEnvironment, env);
    
  }
  

  /**
   * Create dependency parser.
   * 
   * @param source
   *          File source file
   * @return Parser parser
   */
  public Parser createParser() {
	  
    return new FortranParser();
    
  }
  

  /**
   * Get linker.
   * 
   * @param type
   *          link type
   * @return linker
   */
  @Override
  public Linker getLinker(final LinkType type) {
	  
    return OpenWatcomFortranLinker.getInstance().getLinker(type);
    
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
	
		return getOSArch();
		
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
	
	
	public String[] getOutputFileSwitch() {
	
		return getHeaderExtensions();
		
	}
	
	
	@Override
	public ProcessorConfiguration createConfiguration(CCTask task, LinkType linkType, ProcessorDef[] defaultProviders,
			ProcessorDef specificConfig, TargetDef targetPlatform, VersionInfo versionInfo) {
	
		return null;
		
	}
	
	
	@Override
	protected String getOutputSuffix() {
	
		return getBaseOutputName(identifier);
		
	}
	
	
	@Override
	protected String getBaseOutputName(String inputFile) {
	
		return identifier;
		
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
