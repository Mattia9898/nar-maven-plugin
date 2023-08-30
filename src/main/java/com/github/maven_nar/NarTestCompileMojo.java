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

import org.apache.maven.artifact.Artifact;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugin.MojoFailureException;

import org.apache.maven.plugins.annotations.LifecyclePhase;

import org.apache.maven.plugins.annotations.Mojo;

import org.apache.maven.plugins.annotations.Parameter;

import org.apache.maven.plugins.annotations.ResolutionScope;

import org.apache.maven.shared.artifact.filter.collection.ScopeFilter;


/**
 * Compiles native test source files.
 * 
 * @author Mark Donszelmann
 */
@Mojo(name = "nar-testCompile", defaultPhase = LifecyclePhase.TEST_COMPILE,
  requiresDependencyResolution = ResolutionScope.TEST)
public class NarTestCompileMojo extends AbstractCompileMojo {
	
	
  /**
   * Skip running of NAR integration test plugins.
   */
  @Parameter(property = "skipNar")
  protected boolean skipNar;
  

  /**
   * List the dependencies needed for tests compilations, those dependencies are
   * used to get the include paths needed
   * for compilation and to get the libraries paths and names needed for
   * linking.
   */
  @Override
  protected ScopeFilter getArtifactScopeFilter() {
	  
    // Was Artifact.SCOPE_TEST  - runtime??
    return new ScopeFilter( Artifact.SCOPE_TEST, null );
    
  }

  
  @Override
  protected File getUnpackDirectory() {
	  
    return getTestUnpackDirectory() == null ? super.getUnpackDirectory() : getTestUnpackDirectory();
    
  }

  
  @Override
  public final void narExecute() throws MojoExecutionException, MojoFailureException {
	  
    if (this.skipTests) {
    	
      getLog().info("Not compiling test sources");
      
    } else {
      
      if (replay != null) {
    	  
        File compileCommandFile = new File(replay.getOutputDirectory(), NarConstants.REPLAY_TEST_COMPILE_NAME);
        
        NarUtil.writeCommandFile(compileCommandFile, testCompileCommands);
        
        File linkCommandFile = new File(replay.getOutputDirectory(), NarConstants.REPLAY_TEST_LINK_NAME);
        
        NarUtil.writeCommandFile(linkCommandFile, testLinkCommands);
        
      }
      
    }
    
  }

}
