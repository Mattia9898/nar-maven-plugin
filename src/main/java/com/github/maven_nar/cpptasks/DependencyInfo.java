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
package com.github.maven_nar.cpptasks;

import java.io.File;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;

import org.xml.sax.SAXNotRecognizedException;

import org.xml.sax.SAXNotSupportedException;


/**
 * @author Curt Arnold
 */
public final class DependencyInfo {
	
	
  /**
   * Last modified time of this file or anything that it depends on.
   * 
   * Not persisted since almost any change could invalidate it. Initialized
   * to long.MIN_VALUE on construction.
   */
  private final String includePathIdentifier;
  
  private final String[] includes;
  
  private final String source;
  
  private final long sourceLastModified;
  
  private final String[] sysIncludes;
  
  // FREEHEP
  private Object tag = null;

  public DependencyInfo(final String includePathIdentifier, final String source, final long sourceLastModified,
      final List<String> includes2, final List<String> sysIncludes2) {
	  
    if (source == null) {
    	
      throw new NullPointerException("source");
      
    }
    
    if (includePathIdentifier == null) {
    	
      throw new NullPointerException("includePathIdentifier");
      
    }
    
    this.source = source;
    
    this.sourceLastModified = sourceLastModified;
    
    this.includePathIdentifier = includePathIdentifier;
    
    this.includes = new String[includes2.size()];
    
    // ENDFREEHEP
    this.sysIncludes = new String[sysIncludes2.size()];
    
    // FREEHEP
    
  }

  
  // ENDFREEHEP
  public String getIncludePathIdentifier() {
	  
    return this.includePathIdentifier;
    
  }

  
  public String[] getIncludes() {
	  
    return this.includes.clone();

  }

  
  public String getSource() {
	  
    return this.source;
    
  }

  
  public long getSourceLastModified() {
	  
    return this.sourceLastModified;
    
  }

  
  public String[] getSysIncludes() {
	  
    return this.sysIncludes.clone();

  }

  
  // BEGINFREEHEP
  /**
   * Returns true, if dependency info is tagged with object t.
   * 
   * @param t
   *          object to compare with
   * 
   * @return boolean, true, if tagged with t, otherwise false
   */
  public boolean hasTag(final Object t) {
	  
    return this.tag == t;
    
  }

  
  /**
   * Returns the latest modification date of the source or anything that it
   * depends on.
   * 
   * @returns the composite lastModified time, returns Long.MIN_VALUE if not
   *          set
   */
  
  // BEGINFREEHEP
  
  public void setTag(final Object t) {
	  
    this.tag = t;
    
  }
  
  protected TargetHistoryTable getLinkHistory(final TargetHistoryTable objHistory) throws BuildException, SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
	  
	    final File outputFileDir = new File(CCTask.outfile.getParent());
	    //
	    // if the output file is being produced in the link
	    // directory, then we can use the same history file
	    //
	    
	    if (CCTask.objDir.equals(outputFileDir)) {
	    	
	      return objHistory;
	      
	    }
	    
	    return new TargetHistoryTable(new CCTask(), outputFileDir);
	    
  }
  
}
