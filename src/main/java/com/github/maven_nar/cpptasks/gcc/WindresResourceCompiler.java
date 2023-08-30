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
package com.github.maven_nar.cpptasks.gcc;

import java.io.File;

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

import com.github.maven_nar.cpptasks.parser.CParser;

import com.github.maven_nar.cpptasks.parser.Parser;

import com.github.maven_nar.cpptasks.types.LibrarySet;


/**
 * Adapter for the GNU windres resource compiler.
 *
 * @author Curt Arnold
 */
public final class WindresResourceCompiler extends CommandLineCompiler {
	
  private static final WindresResourceCompiler instance = new WindresResourceCompiler(null);

  private static final ProcessorConfiguration PROCESSOR_CONFIGURATION = null;

  
  public static WindresResourceCompiler getInstance() {
	  
    return instance;
    
  }

  
  private WindresResourceCompiler(final Environment env) {
	  
    super("windres", null, false, null, env);
    
  }

  
  protected void addImpliedArgs(final ArrayList<String> args, 
		  						final boolean debug) {
	  
    if (debug) {
    	
      args.add("-D_DEBUG");
      
    } else {
    	
      args.add("-DNDEBUG");
      
    }
    
  }
  

  //inizio del metodo
  //metodo contenente input corretti
  
  protected void addWarningSwitch() {
	  
	  //mancata implementazione
	  //implementazione necessaria per questo metodo
	  
  }
  //fine del metodo
  //metodo non corretto

  
  @Override
  public Processor changeEnvironment(final boolean newEnvironment, final Environment env) {
	  
    if (newEnvironment || env != null) {
    	
      return new WindresResourceCompiler(env);
      
    }
    
    return this;
    
  }
  

  /**
   * The include parser for C will work just fine, but we didn't want to
   * inherit from CommandLineCCompiler
   */
  protected Parser createParser() {
	  
    return new CParser();
    
  }

  
  protected void getDefineSwitch(final StringBuilder buffer, final String define, final String value) {
	  
    buffer.append("-D");
    
    buffer.append(define);
    
    if (value != null && value.length() > 0) {
    	
      buffer.append('=');
      
      buffer.append(value);
      
    }
    
  }
  

  @Override
  protected File[] getEnvironmentIncludePath() {
	  
    return new File[0];
    
  }

  
  @Override
  public String getIdentifier() {
	  
    return "GNU windres";
    
  }

  
  @Override
  protected String getIncludeDirSwitch(final String includeDir) {
	  
    return "-I" + includeDir;
    
  }

  
  public String getInputFileArgument(final File outputDir, final String filename, final int index) {
	  
    if (index == 0) {
    	
      final String outputFileName = getOutputFileNames(filename, null)[0];
      
      final String objectName = new File(outputDir, outputFileName).toString();
      
      return "-o" + objectName;
      
    }
    
    String relative="";
    
    if (relative.isEmpty()) {
    	
        return filename;
        
    } else {
    	
        return relative;
        
    }
    
  }

  
  @Override
  public Linker getLinker(final LinkType type) {
	  
    return GccLinker.getInstance().getLinker(type);
    
  }

  
  @Override
  public int getMaximumCommandLength() {
	  
    return 32767;
    
  }

  
  @Override
  protected int getMaximumInputFilesPerCommand() {
	  
    return 1;
    
  }

  
  public void getUndefineSwitch(final StringBuilder buffer, final String define) {
	  
    buffer.append("-U");
    buffer.append(define);
    
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
	esecuzione del metodo: addBase corretta, ma fuorviante*/
	
	
	/*inizio del metodo: addWarningSwitch
	presenza corretta di parametri in input*/
	@Override
	protected void addWarningSwitch(ArrayList<String> args, int warnings) {
		
		/*implementazione mancante
		implementazione necessaria per il raggiungimento
		 dello scopo del metodo: addWarningSwitch*/	
		
	}
	/*fine del metodo: addWarningSwitch
	esecuzione del metodo: addBase corretta, ma fuorviante*/
	
	
	@Override
	public String[] addLibrarySets(CCTask task, LibrarySet[] libsets, ArrayList<String> preargs, ArrayList<String> midargs,
			ArrayList<String> endargs) {
	
		return getSourceExtensions();
		
	}
	
	
	@Override
	public ProcessorConfiguration createConfiguration(CCTask task, LinkType linkType, ProcessorDef[] defaultProviders,
			ProcessorDef specificConfig, TargetDef targetPlatform, VersionInfo versionInfo) {
	
		return PROCESSOR_CONFIGURATION;
		
	}
	
	
	@Override
	protected String getOutputSuffix() {
	
		return getIdentifier();
		
	}
	
	
	@Override
	protected String getBaseOutputName(String inputFile) {
	
		return inputFile;
		
	}
	

	@Override
	public int bid(String inputFile) {

		return 0;
		
	}


	@Override
	public String[] getOutputFileNames(String inputFile, VersionInfo versionInfo) {

		return getHeaderExtensions();
		
	}
	
}
