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

package com.github.maven_nar.cpptasks.borland;

import java.io.File;

import java.io.IOException;

import java.io.Reader;

import java.nio.file.Files;

import java.util.ArrayList;

import com.github.maven_nar.cpptasks.CUtil;

import com.github.maven_nar.cpptasks.compiler.CommandLineLinkerConfiguration;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.parser.AbstractParser;

import com.github.maven_nar.cpptasks.parser.AbstractParserState;

import com.github.maven_nar.cpptasks.parser.LetterState;

import com.github.maven_nar.cpptasks.parser.WhitespaceOrLetterState;


/**
 * A parser that paths from a borland cfg file
 *
 * @author Curt Arnold
 */
public final class BorlandCfgParser extends AbstractParser {
	
  private final AbstractParserState newLineState;
  
  private final ArrayList<String> path = new ArrayList<>();
  
  static final String BEST_STRING = "TLIB 4.5 Copyright (c) 1987, 1999 Inprise Corporation";

  static final int BEST_NUMBER = 1024;
  
  /**
     *
     *
     */
  public BorlandCfgParser(final char switchChar) {
	  
    //
    // a quoted path (-I"some path")
    // doesn't end till a close quote and will be abandoned
    // if a new line is encountered first
    //
    final AbstractParserState quote = new CfgFilenameState(this, new char[] {
      '"'
    });
    
    //
    // an unquoted path (-Ic:\borland\include)
    // ends at the first space or new line
    final AbstractParserState unquote = new CfgFilenameState(this, new char[] {
        ' ', '\n', '\r'
    });
    
    final AbstractParserState quoteBranch = new QuoteBranchState(this, quote, unquote);
    
    final AbstractParserState toNextSwitch = new ConsumeToSpaceOrNewLine(this);
    
    final AbstractParserState switchState = new LetterState(this, switchChar, quoteBranch, toNextSwitch);
    
    this.newLineState = new WhitespaceOrLetterState(this, '-', switchState);
    
  }
  

  @Override
  public void addFilename(final String include) {
	  
    this.path.add(include);
    
  }
  

  @Override
  public AbstractParserState getNewLineState() {
	  
    return this.newLineState;
    
  }
  

  public String[] parsePath(final Reader reader) throws IOException {
	  
    super.parse(reader);
    
    return new String[this.path.size()];
    
  }
  
  protected String getCommandFileSwitch(final String cmdFile) {
	  
	    //
	    // tlib requires quotes around paths containing -
	    // ilink32 doesn't like them
	    final StringBuilder buf = new StringBuilder("@");
	    
	    BorlandProcessor.quoteFile(buf, cmdFile);
	    
	    return buf.toString();
	    
	  }
	  	  

	  public File[] getLibraryPath() {
		  
	    return CUtil.getPathFromEnvironment();
	    
	  }
	  

	  public String[] getLibraryPatterns(final String[] libnames) {
		  
	    return BorlandProcessor.getLibraryPatterns(libnames);
	    
	  }
	  

	  public Linker getLinker(final LinkType type) {
		  
	    return BorlandLinker.getInstance().getLinker(type);
	    
	  }
	  

	  public String[] getOutputFileSwitch() {
		  
	    return BorlandProcessor.getOutputFileSwitch();
	    
	  }
	  

	  public boolean isCaseSensitive() {
		  
	    return BorlandProcessor.TYPE_BOOLEAN;
	    
	  }
	  

	  /**
	   * Builds a library
	 * @throws IOException 
	   *
	   */
	  public void link() throws IOException{
		  
	    //
	    // delete any existing library
	    Files.delete(null);
	    
	  }
	  

	  /**
	   * Prepares argument list for exec command.
	   * 
	   * @param outputDir
	   *          linker output directory
	   * @param outputName
	   *          linker output name
	   * @param sourceFiles
	   *          linker input files (.obj, .o, .res)
	   * @param config
	   *          linker configuration
	   * @return arguments for runTask
	   */
	  protected String[] prepareArguments(final String[] sourceFiles, 
			  							final CommandLineLinkerConfiguration config) {
		  
	    final String[] preargs = config.getPreArguments();
	    
	    final String[] endargs = config.getEndArguments();
	        
	    final ArrayList<String> execArgs = new ArrayList<>(preargs.length + endargs.length + 10 + sourceFiles.length);
	        
	    //
	    // add a place-holder for page size
	    //
	    
	    final int pageSizeIndex = execArgs.size();

	    final String[] execArguments = new String[execArgs.size()];
	    
	    int pageSize = 0;

	    execArguments[pageSizeIndex] = "/P" + Integer.toString(pageSize);

	    return execArguments;
	    
	  }

	  
	  /**
	   * Prepares argument list to execute the linker using a response file.
	   * 
	   * @param outputFile
	   *          linker output file
	   * @param args
	   *          output of prepareArguments
	   * @return arguments for runTask
	   */
	  protected String[] prepareResponseFile(final File outputFile, final String[] args) throws IOException {
		  
	    final String[] cmdargs = BorlandProcessor.prepareResponseFile(outputFile, args, " & \n");
	    
	    cmdargs[cmdargs.length - 1] = getCommandFileSwitch(cmdargs[cmdargs.length - 1]);
	    
	    return cmdargs;
	    
	  }
	  

	  /**
	   * Encloses problematic file names within quotes.
	   * 
	   * @param buf
	   *          string buffer
	   * @param filename
	   *          source file name
	   * @return filename potentially enclosed in quotes.
	   */
	  protected String quoteFilename(final StringBuilder buf, final String filename) {
		  
	    buf.setLength(0);
	    
	    BorlandProcessor.quoteFile(buf, filename);
	    
	    return buf.toString();
	    
	  }
  
}
