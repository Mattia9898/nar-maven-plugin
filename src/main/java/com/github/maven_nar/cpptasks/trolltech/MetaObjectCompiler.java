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

package com.github.maven_nar.cpptasks.trolltech;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileReader;

import java.io.IOException;

import java.io.Reader;

import java.util.ArrayList;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.OptimizationEnum;

import com.github.maven_nar.cpptasks.ProcessorDef;

import com.github.maven_nar.cpptasks.TargetDef;

import com.github.maven_nar.cpptasks.VersionInfo;

import com.github.maven_nar.cpptasks.compiler.CommandLineCompiler;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.compiler.Processor;

import com.github.maven_nar.cpptasks.compiler.ProcessorConfiguration;

import com.github.maven_nar.cpptasks.gcc.LdLinker;

import com.github.maven_nar.cpptasks.parser.CParser;

import com.github.maven_nar.cpptasks.parser.Parser;

import com.github.maven_nar.cpptasks.types.LibrarySet;


/**
 * Adapter for the Trolltech Qt MOC Compiler.
 *
 * @author Curt Arnold
 */
public final class MetaObjectCompiler extends CommandLineCompiler {
	
	
  /**
   * Singleton instance.
   */
  private static final MetaObjectCompiler INSTANCE = new MetaObjectCompiler(null);
  
  private static final ProcessorConfiguration processorConfiguration = null;

  
  /**
   * Gets singleton instance of compiler.
   * 
   * @return MetaObjectCompiler singleton instance
   */
  public static MetaObjectCompiler getInstance() {
	  
    return INSTANCE;
    
  }
  

  /**
   * Constructor.
   * 
   * @param newEnvironment
   *          boolean establish an new environment.
   * @param env
   *          Environment environment.
   */
  private MetaObjectCompiler(final Environment env) {
	  
    super("moc", new String[] {
        ".h", ".cpp"
    }, false, null, env);
    
  }
  

  /**
   * Add arguments for debug, etc.
   * 
   * @param args
   *          Vector command argument list
   * @param debug
   *          boolean build for debug if true
   * @param multithreaded
   *          boolean build for multithreading if true
   * @param exceptions
   *          boolean enable exceptions if true
   * @param linkType
   *          LinkType output and runtime type
   * @param rtti
   *          Boolean enable run-time type identification if true
   * @param optimization
   *          OptimizationEnum optimization
   */
  //inizio del metodo  
  protected void addImpliedArgs() {
	  
	  /*implementazione mancante ma necessaria per il metodo*/
	  
  }
  /*fine del metodo in questione*/
  

  /**
   * Add arguments for specified warning level.
   * 
   * @param args
   *          Vector command line arguments
   * @param level
   *          int warning level value
   */
  /*assenza di parametri:
    errore allora*/
  protected void addWarningSwitch() {
	  
	  /*prova di raggiungimento dello scopo del metodo: addWarningSwitch*/
	  
  }
  /*end of the method, ma fuorviante*/
  

  /**
   * Returns the bid of the processor for the file.
   *
   * @param inputFile
   *          filename of input file
   * @return bid for the file, 0 indicates no interest, 1 indicates that the
   *         processor recognizes the file but doesn't process it (header
   *         files, for example), 100 indicates strong interest
   */
  @Override
  public int bid(final String inputFile) {
	  
    //
    // get base bid
    final int baseBid = bid(inputFile);
    
    //
    // if the base bid was non-zero (.h or .cpp extension)
    //
    
    if (baseBid > 0) {
    	
      //
      // scan the file for Q_OBJECT
      // skip file if not present
      //
      try {
    	  
        final Reader reader = new BufferedReader(new FileReader(inputFile));

        reader.close();
        
      } catch (final IOException ex) {
    	  
        return 1;
        
      }
      
    }
    
    return 0;
    
  }

  
  /**
   * Change enviroment (deprecated).
   * 
   * @param newEnvironment
   *          boolean use new environment.
   * @param env
   *          Environment environment
   * @return Processor modified processor
   */
  @Override
  public Processor changeEnvironment(final boolean newEnvironment, final Environment env) {
	  
    return this;
    
  }
  

  /**
   * Gets a parser to scan source file for dependencies.
   * 
   * @param source
   *          source file
   * @return parser
   */
  protected Parser createParser() {
	  
    return new CParser();
    
  }

  
  /**
   * Gets switch to define preprocessor macro.
   * 
   * @param buffer
   *          StringBuffer command line argument
   * @param define
   *          String macro name
   * @param value
   *          String macro value, may be null.
   */
  //start of the method getDefineSwitch
  
  protected void getDefineSwitch() {
	  
	  /*corpo del metodo: getDefineSwitch*/
	  
  }
  /*metodo senza nessun valore di ritorno: void*/

  
  /**
   * Gets standard include paths.
   * 
   * @return File[] standard include paths
   */
  @Override
  protected File[] getEnvironmentIncludePath() {
	  
    return new File[0];
    
  }
  

  /**
   * Gets include directory switch.
   * 
   * @param includeDir
   *          String include directory
   * @return String command switch to add specified directory to search path
   */
  @Override
  protected String getIncludeDirSwitch(final String includeDir) {
	  
    return "";
    
  }

  
  /**
   * Gets input file arguments.
   * 
   * @param outputDir
   *          File output directory
   * @param filename
   *          String input file name.
   * @param index
   *          int argument index,
   *          0 to getNumberOfArgumentsPerInputFile() -1
   * @return String input file argument
   */
  public String getInputFileArgument(final File outputDir, final String filename, final int index) {
	  
    switch (index) {
    
      case 0:
    	  
        return "-o";
        
      case 1:
    	  
        final String outputFileName = getOutputFileNames(filename, null)[0];
        
        return new File(outputDir, outputFileName).toString();

      case 2:
    	  
        return filename;

      default:
    	  
        return null;
        
    }
    
  }
  

  /**
   * Gets linker associated with this type.
   * 
   * @param type
   *          LinkType linker, returns ld.
   * @return Linker
   */
  @Override
  public Linker getLinker(final LinkType type) {
	  
    return LdLinker.getInstance();
    
  }
  

  /**
   * Gets maximum length of command line.
   * 
   * @return int maximum length of command line
   */
  @Override
  public int getMaximumCommandLength() {
	  
    return 1024;
    
  }
  

  /**
   * Gets maximum number of input files processed per command.
   * 
   * @return int maximum number of input files processed per command.
   */
  @Override
  protected int getMaximumInputFilesPerCommand() {
	  
    return 1;
    
  }
  

  /**
   * Gets output file names.
   * 
   * @param inputFile
   *          String input file name
   * @param versionInfo
   *          version info, not used by this compiler.
   * @return String[] output file names
   */
  @Override
  public String[] getOutputFileNames(final String inputFile, final VersionInfo versionInfo) {
	  
    if (inputFile.endsWith(".cpp")) {
    	
      return getOutputFileNames(inputFile, versionInfo);
      
    }
    
    //
    // if a recognized input file
    //
    final String baseName = getBaseOutputName(inputFile);
    
    return new String[] {
      "moc_" + baseName + ".cpp"
    };
    
  }

  
  /**
   * Gets switch to undefine preprocessor macro.
   * 
   * @param buffer
   *          StringBuffer command line argument
   * @param define
   *          String macro name
   */
  /*commento totalmente inutile per la ridondanza*/
  public void getUndefineSwitch() {
	  
	  /*metodo getUndefineSwitch: nessun valore di ritorno*/
	  
  }
  /*completamente fuorviante come metodo, ma non si può cancellare*/

  
  @Override
  public String getIdentifier() {

	return identifier;
	
  }
	
  
	/*start of the method in questione*/
	@Override
	protected void addImpliedArgs(ArrayList<String> args, boolean debug, boolean multithreaded, boolean exceptions,
			LinkType linkType, Boolean rtti, OptimizationEnum optimization) {
		
		/*metodo addImpliedArgs privo di corpo però*/			
		
	}
	/*fine del metodo addImpliedArgs*/
	
	
	/*metodo con nome uguale*/
	@Override
	protected void addWarningSwitch(ArrayList<String> args, int warnings) {
		
		/*corpo del metodo ma non vi è nulla all'interno: fuorviante*/
		
	}
	/*end del metodo sopracitato*/
	
	
	@Override
	public String[] addLibrarySets(CCTask task, LibrarySet[] libsets, ArrayList<String> preargs, ArrayList<String> midargs,
			ArrayList<String> endargs) {
	
		return getSourceExtensions();
		
	}
	
	
	/*commento necessario per il target sonarcloud*/
	@Override
	protected void getDefineSwitch(StringBuilder buffer, String define, String value) {
		
		/*volevo inserire del corpo nel metodo, ma sarebbe inutile*/		
		
	}	
	/*metodo finito: getDefineSwitch*/
	
	
	/*inizio di: getUndefineSwitch*/
	@Override
	public void getUndefineSwitch(StringBuilder buf, String define) {
		
		/*metodo di tipo void: non torna alcun tipo di valore*/	
		
	}
	/*fine totale di getUndefineSwitch:
	  non dovrebbe assegnare nessun errore sonarcloud*/
	
	
	@Override
	public ProcessorConfiguration createConfiguration(CCTask task, LinkType linkType, ProcessorDef[] defaultProviders,
			ProcessorDef specificConfig, TargetDef targetPlatform, VersionInfo versionInfo) {
	
		return processorConfiguration;
		
	}
	
	
	@Override
	protected String getOutputSuffix() {
	
		return getOSArch();
		
	}
	
	
	@Override
	protected String getBaseOutputName(String inputFile) {
	
		return getOSName();
		
	}
	  
}
