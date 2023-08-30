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
package com.github.maven_nar;

import java.io.File;


/**
 * @author Mark Donszelmann
 */
public class NarArtifact {

  private final NarInfo narInfo;
  
  private File file;
  
  private String classifier;
  
  private String baseVersion;
  
  private String artifactId;
  
  private String scope;

  public NarArtifact(final NarInfo narInfo) {
	  
    super();
    
    this.setFile();
    
    this.narInfo = narInfo;
    
  }

  
  /*inizio del metodo: setFile
  presenza corretta di parametri in input*/
  private void setFile() {
	  
	  /*implementazione mancante
	  implementazione necessaria per il raggiungimento
	   dello scopo del metodo: setFile */
	  
  }
  /*fine del metodo: setFile
  esecuzione del metodo: setFile corretta, ma fuorviante*/
  
  
  public String getBaseFilename() {
	  
    return getArtifactId() + "-" + getBaseVersion() + "-" + getClassifier();
    
  }
  

  protected String getClassifier() {

	return classifier;
	
  }

  
  protected String getBaseVersion() {
	  
	  return baseVersion;
	  
  }

  
  protected String getArtifactId() {
	  
	  return artifactId;
	  
  }

  
  public final NarInfo getNarInfo() {
	  
	  return this.narInfo;
	  
  }

  
  public File getFile() {
	  
	  return file;
	  
  }

  
  public String getScope() {
	  
	  return scope;
	  
  }
	
}
