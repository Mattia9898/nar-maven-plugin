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

import java.io.File;

import java.util.ArrayList;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.CUtil;

import com.github.maven_nar.cpptasks.compiler.CommandLineLinkerConfiguration;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.types.LibrarySet;


/**
 * Adapter for the OpenWatcom linker.
 *
 * @author Curt Arnold
 */
public final class OpenWatcomCLinker extends OpenWatcomLinker {
	
	
  /**
   * Dll linker.
   */
  private static final OpenWatcomCLinker DLL_LINKER = new OpenWatcomCLinker(".dll");
  
  
  /**
   * Exe linker.
   */
  private static final OpenWatcomCLinker INSTANCE = new OpenWatcomCLinker(".exe");
  

  /**
   * Get linker instance.
   * 
   * @return OpenWatcomCLinker linker
   */
  public static OpenWatcomCLinker getInstance() {
	  
    return INSTANCE;
    
  }
  

  /**
   * Constructor.
   * 
   * @param outputSuffix
   *          String output suffix.
   */
  private OpenWatcomCLinker(final String outputSuffix) {
	  
    super("wcl386", outputSuffix);
    
  }
  

  /**
   * Get linker.
   * 
   * @param type
   *          LinkType link type
   * @return Linker linker
   */
  @Override
  public Linker getLinker(final LinkType type) {
	  
    if (type.isSharedLibrary()) {
    	
      return DLL_LINKER;
      
    }
    
    return INSTANCE;
    
  }
  

	@Override
	public String[] addLibrarySets(CCTask task, LibrarySet[] libsets, ArrayList<String> preargs, ArrayList<String> midargs,
			ArrayList<String> endargs) {
	
		return addLibrarySets();
		
	}
	

	  /**
	   * Add base address.
	   * 
	   * @param base
	   *          long base address
	   * @param args
	   *          Vector command line arguments
	   */
	  //inizio del metodo: addBase
	  //presenza corretta di parametri in input
	  protected void addBase() {
		  
		  //implementazione mancante
		  //implementazione necessaria per il raggiungimento dello scopo del metodo: addBase
		  
	  }
	  //fine del metodo: addBase
	  //esecuzione del metodo: addBase corretta, ma fuorviante

	  
	  /**
	   * Add alternative entry point.
	   * 
	   * @param entry
	   *          String entry point
	   * @param args
	   *          Vector command line arguments
	   */
	  /*inizio del metodo: addEntry (overriding)
	  presenza corretta di parametri in input*/
	  protected void addEntry() {
		  
		  /*implementazione mancante
		  implementazione necessaria per il raggiungimento dello scopo del metodo: addEntry (overriding)*/
		  
	  }
	  /*fine del metodo: addEntry (overriding)
	  esecuzione del metodo: addEntry (overriding) corretta, ma fuorviante*/

	  
	  /**
	   * Add fixed parameter.
	   * 
	   * @param fixed
	   *          Boolean true if fixed
	   * @param args
	   *          Vector command line arguments
	   */
	  /*inizio del metodo: AddFixed (overriding)
	  presenza corretta di parametri in input*/
	  protected void addFixed() {
		  
		  /*implementazione mancante
		  implementazione necessaria per il raggiungimento dello scopo del metodo: AddFixed (overriding)*/
		  
	  }
	  /*fine del metodo: AddFixed (overriding)
	  esecuzione del metodo: AddFixed (overriding) corretta, ma fuorviante*/

	  /**
	   * Add implied arguments.
	   * 
	   * @param debug
	   *          boolean true if debugging
	   * @param linkType
	   *          LinkType link type
	   * @param args
	   *          Vector command line arguments
	   */
	  /*inizio del metodo: addImpliedArgs
	  presenza corretta di parametri in input*/
	  protected void addImpliedArgs() {
		  
		  /*implementazione mancante
		  implementazione necessaria per il raggiungimento dello scopo del metodo: addImpliedArgs*/
		  
	  }
	  /*fine del metodo: addImpliedArgs
	  esecuzione del metodo: addImpliedArgs corretta, ma fuorviante*/
	  
	  
	  protected void addIncremental() {
		  
		  /*implementazione mancante
		  implementazione necessaria per il raggiungimento dello scopo del metodo: addIncremental*/
		  
	  }
	  /*fine del metodo: addIncremental
	  esecuzione del metodo: addIncremental corretta, ma fuorviante*/
	  

	  /**
	   * Add map option.
	   * 
	   * @param map
	   *          boolean true to create map file
	   * @param args
	   *          Vector command line argument
	   */
	  /*inizio del metodo: addMap
	  presenza corretta di parametri in input*/
	  protected void addMap() {
		  
		  /*implementazione mancante
		  implementazione necessaria per il raggiungimento dello scopo del metodo: addMap*/
		  
	  }
	  /*fine del metodo: addMap
	  esecuzione del metodo: addMap corretta, ma fuorviante*/

	  
	  /**
	   * Add stack size option.
	   * 
	   * @param stack
	   *          int stack size
	   * @param args
	   *          Vector command line arguments
	   */
	  /*inizio del metodo: addStack
	  presenza corretta di parametri in input*/
	  protected void addStack() {
		  
		  /*implementazione mancante
		  implementazione necessaria per il raggiungimento dello scopo del metodo: addStack*/
		  
	  }
	  /*fine del metodo: addStack
	  esecuzione del metodo: addStack corretta, ma fuorviante*/
	  

	  /**
	   * Get command file switch.
	   * 
	   * @param cmdFile
	   *          String command file
	   * @return String command file switch
	   */
	  protected String getNewCommandFileSwitch(final String cmdFile) {
		  
	    return OpenWatcomProcessor.getCommandFileSwitch(cmdFile);
	    
	  }
	  

	  /**
	   * Get library search path.
	   * 
	   * @return File[] library search path
	   */
	  @Override
	  public File[] getLibraryPath() {
		  
	    return CUtil.getPathFromEnvironment();
	    
	  }
	  

	  /**
	   * Get file selectors for specified library names.
	   * 
	   * @param libnames
	   *          String[] library names
	   * @param libType
	   *          LibraryTypeEnum library type enum
	   * @return String[] file selection patterns
	   */
	  public String[] getLibraryPatterns(final String[] libnames) {
		  
	    return OpenWatcomProcessor.getLibraryPatterns(libnames);
	    
	  }
	  

	  /**
	   * Get linker.
	   * 
	   * @param type
	   *          LinkType link type
	   * @return Linker linker
	   */
	  public Linker getNewLinker(final LinkType type) {
		  
	    return OpenWatcomCLinker.getInstance().getLinker(type);
	    
	  }
	 

	  /**
	   * Create output file switch.
	   * 
	   * @param outFile
	   *          String output file switch
	   * @return String[] output file switch
	   */
	  @Override
	  public String[] getOutputFileSwitch(final String outFile) {
		  
	    return OpenWatcomProcessor.getOutputFileSwitch(outFile);
	    
	  }

	  
	  /**
	   * Gets case-sensisitivity of processor.
	   * 
	   * @return boolean true if case sensitive
	   */
	  @Override
	  public boolean isCaseSensitive() {
		  
	    return OpenWatcomProcessor.TYPE_BOOLEAN;
	    
	  }


	  /**
	   * Prepares argument list for exec command.
	   * 
	   * @param task
	   *          task
	   * @param outputDir
	   *          output directory
	   * @param outputName
	   *          output file name
	   * @param sourceFiles
	   *          object files
	   * @param config
	   *          linker configuration
	   * @return arguments for runTask
	   */
	  protected String[] prepareArguments(final String[] sourceFiles, final CommandLineLinkerConfiguration config) {
		  
	    final String[] preargs = config.getPreArguments();
	    
	    final String[] endargs = config.getEndArguments();
	    
	    final ArrayList<String> execArgs = new ArrayList<>(preargs.length + endargs.length + 10 + sourceFiles.length);

	    for (final String sourceFile : sourceFiles) {
	    	
	      final String last4 = sourceFile.substring(sourceFile.length() - 4).toLowerCase();
	      
	      if (!last4.equalsIgnoreCase("def")) {
	    	  
	        new File(sourceFile).length();
	        
	      }
	      
	    }

	    return new String[execArgs.size()];
	    
	  }

}
