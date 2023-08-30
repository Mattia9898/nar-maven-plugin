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

import java.io.FileFilter;

import java.io.PrintWriter;

import java.util.List;

import org.apache.maven.artifact.Artifact;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugin.MojoFailureException;

import org.apache.maven.plugins.annotations.LifecyclePhase;

import org.apache.maven.plugins.annotations.Mojo;

import org.apache.maven.plugins.annotations.ResolutionScope;

import org.apache.maven.shared.artifact.filter.collection.ScopeFilter;


/**
 * Create the nar.properties file.
 * 
 * @author GDomjan
 */
@Mojo(name = "nar-prepare-package", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, 
  requiresProject = true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class NarPreparePackageMojo extends AbstractCompileMojo {

  // be built, POM ~/= artifacts!
  @Override
  public final void narExecute() throws MojoExecutionException, MojoFailureException {
	  
    // let the layout decide which (additional) nars to attach
	  
    getLayout().prepareNarInfo(getTargetDirectory(), getMavenProject(), getNarInfo(), this);
    
    getNarInfo().writeToDirectory(this.classesDirectory);

    final String artifactIdVersion = getMavenProject().getArtifactId() + "-" + getMavenProject().getVersion();

    // Scan target directory to identify project classifier directories, skipping noarch
    
    File[] files = getTargetDirectory().listFiles(new FileFilter() {
    	
      @Override
      public boolean accept(File file) {
    	  
        return file.getName().startsWith(artifactIdVersion) && (!file.getName().endsWith(NarConstants.NAR_NO_ARCH));
        
      }
      
    });
    
    // Write nar info to project classifier directories
    
    getNarInfo().writeToDirectory(files);
    
  }

  public void processReplayFile(List<String> lines, Script script, PrintWriter writer) {
	  
    for (String line : lines) {
    	
      String processed = line;
      
      if (script.getSubstitutions() != null) {
    	  
        for (Substitution sub : script.getSubstitutions()) {
        	
          processed = sub.substitute(processed);
          
        }
        
      }
      
      if (script.isEchoLines()) {
    	  
        writer.print("echo " );
        
        writer.println(processed);
        
      }
      
      writer.println(processed);
      
    }
    
  }
  

  @Override
  protected ScopeFilter getArtifactScopeFilter() {
	  
    return new ScopeFilter(Artifact.SCOPE_COMPILE, null);
    
  }
  
}
