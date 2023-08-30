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

import java.io.IOException;

import java.util.ArrayList;

import java.util.Collections;

import java.util.List;

import org.apache.maven.artifact.Artifact;

import org.apache.maven.execution.MavenSession;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugin.MojoFailureException;

import org.apache.maven.plugins.annotations.LifecyclePhase;

import org.apache.maven.plugins.annotations.Mojo;

import org.apache.maven.plugins.annotations.Parameter;

import org.apache.maven.plugins.annotations.ResolutionScope;

import org.apache.maven.shared.artifact.filter.collection.ScopeFilter;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.Project;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.OSFamilyEnum;

import com.github.maven_nar.cpptasks.OutputTypeEnum;

import com.github.maven_nar.cpptasks.RuntimeType;

import com.github.maven_nar.cpptasks.SubsystemEnum;


/**
 * Compiles native source files.
 * 
 * @requiresSession
 * @author Mark Donszelmann
 */
@Mojo(name = "nar-compile", defaultPhase = LifecyclePhase.COMPILE, requiresProject = true,
  requiresDependencyResolution = ResolutionScope.COMPILE)
public class NarCompileMojo extends AbstractCompileMojo {
	
	
  /**
   * Specify that the final manifest should be embedded in the output (default
   * true) or false for side by side.
   */
  @Parameter(property = "nar.embedManifest", defaultValue = "true")
  protected boolean embedManifest = true;
  

  /**
   * The current build session instance.
   */
  @Parameter(defaultValue = "${session}", readonly = true)
  protected MavenSession session;

  private void copyInclude(final Compiler c) throws IOException, MojoExecutionException, MojoFailureException {
	  
    if (c == null) {
    	
      return;
      
    }
    
    c.copyIncludeFiles(
        getLayout().getIncludeDirectory(getTargetDirectory(), getMavenProject().getArtifactId(),
            getMavenProject().getVersion()));
    
  }

  
  private void createLibrary(final Project antProject, final Library library)
      throws MojoExecutionException, MojoFailureException {
	  
    getLog().debug("Creating Library " + library);
    
    // configure task
    
    final CCTask task = new CCTask();
    
    task.setCommandLogLevel(this.commandLogLevel);
    
    task.setProject(antProject);

    task.setDecorateLinkerOptions(this.decorateLinkerOptions);

    // subsystem
    
    final SubsystemEnum subSystem = new SubsystemEnum();
    
    subSystem.setValue(library.getSubSystem());
    
    OSFamilyEnum.setSubsystem(subSystem);

    // set max cores
    
    task.setMaxCores(getMaxCores(getAOL()));

    // outtype
    
    final OutputTypeEnum outTypeEnum = new OutputTypeEnum();
    
    final String type = library.getType();
    
    outTypeEnum.setValue(type);
    
    OSFamilyEnum.setOuttype(outTypeEnum);

    // stdc++
    
    task.setLinkCPP(library.linkCPP());

    // fortran
    
    task.setLinkFortran(library.linkFortran());
    
    task.setLinkFortranMain(library.linkFortranMain());
    
    if (getOS().equals(OS.AIX)) {
    	
       CCTask.setSharedObjectName(sharedObjectName);
       
    }

    // object directory        
    // failOnError, libtool
    
    task.setFailonerror(failOnError(getAOL()));
    
    task.setLibtool(useLibtool(getAOL()));

    // runtime
    
    final RuntimeType runtimeType = new RuntimeType();
    
    runtimeType.setValue(getRuntime(getAOL()));
    
    OSFamilyEnum.setRuntime(runtimeType);

    // add java include paths
    getJava().addIncludePaths();

    getMsvc().configureCCTask(task);

    // add dependency libraries

    // not add all libraries, see NARPLUGIN-96

    // Add JVM to linker
    getJava().addRuntime(getJavaHome(getAOL()), getOS(), getAOL().getKey() + ".java.");

    // execute
    try {
    	
      task.execute();
      
    } catch (final BuildException e) {
    	
      throw new MojoExecutionException("NAR: Compile failed", e);
      
    }

  }

  
  /**
   * List the dependencies needed for compilation, those dependencies are used
   * to get the include paths needed for
   * compilation and to get the libraries paths and names needed for linking.
   */
  @Override
  protected ScopeFilter getArtifactScopeFilter() {
	  
    return new ScopeFilter(Artifact.SCOPE_COMPILE, null);
    
  }

  
  private List<Object> getSourcesFor(final Compiler compiler) {
	  
    if (compiler == null) {
    	
      return Collections.emptyList();
      
    }

    try {
    	
      return new ArrayList<>();
            
    } catch (final Exception e) {
    	
      return Collections.emptyList();
      
    }
    
  }

  
  @Override
  public final void narExecute() throws MojoExecutionException, MojoFailureException {

    // make sure destination is there
    getTargetDirectory().mkdirs();

    // check for source files
    int noOfSources = 0;
    
    noOfSources += getSourcesFor(getCpp()).size();
    
    noOfSources += getSourcesFor(getC()).size();
    
    noOfSources += getSourcesFor(getFortran()).size();
    
      if(getOS().equals( OS.WINDOWS ) && getArchitecture().equals("amd64"))
      {
    	  
          noOfSources += getSourcesFor(getAssembler()).size();
          
      }

    if (noOfSources > 0) {
    	
      getLog().info("Compiling " + noOfSources + " native files");
      
      for (final Library library : getLibraries()) {
    	  
        createLibrary(getAntProject(), library);
        
      }
      
    } else {
    	
      getLog().info("Nothing to compile");
      
    }

    try {
    	    	
      copyInclude(getCpp());
      
      copyInclude(getC());
      
      copyInclude(getFortran());
      
    } catch (final IOException e) {
    	
      throw new MojoExecutionException("NAR: could not copy include files", e);
      
    }

    getNarInfo().writeToDirectory(this.classesDirectory);
    
    if (replay != null) {
    	
      File compileCommandFile = new File(replay.getOutputDirectory(), NarConstants.REPLAY_COMPILE_NAME);
      
      NarUtil.writeCommandFile(compileCommandFile, compileCommands);
      
      File linkCommandFile = new File(replay.getOutputDirectory(), NarConstants.REPLAY_LINK_NAME);
      
      NarUtil.writeCommandFile(linkCommandFile, linkCommands);
      
    }
    
  }

  public boolean isEmbedManifest() {
	  
    return embedManifest;
    
  }

}
