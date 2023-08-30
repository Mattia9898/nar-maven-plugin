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

import java.util.List;

import org.apache.maven.artifact.Artifact;

import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugin.MojoFailureException;

import org.apache.maven.plugins.annotations.LifecyclePhase;

import org.apache.maven.plugins.annotations.Mojo;

import org.apache.maven.plugins.annotations.Parameter;

import org.apache.maven.plugins.annotations.ResolutionScope;

import org.apache.maven.project.MavenProject;

import org.apache.maven.shared.artifact.filter.collection.ScopeFilter;


/**
 * Tests NAR files. Runs Native Tests and executables if produced.
 *
 * @author Mark Donszelmann
 */
@Mojo(name = "nar-test", defaultPhase = LifecyclePhase.TEST, requiresProject = true,
  requiresDependencyResolution = ResolutionScope.TEST)
public class NarTestMojo extends AbstractCompileMojo {
	
	
  /**
   * The classpath elements of the project being tested.
   */
  @Parameter(defaultValue = "${project.testClasspathElements}", required = true, readonly = true)
  private List<String> classpathElements;
  

  /**
   * Directory for test resources. Defaults to src/test/resources
   */
  @Parameter(defaultValue = "${basedir}/src/test/resources", required = true)
  private File testResourceDirectory;
  

  /**
   * List the dependencies needed for tests executions and for executables
   * executions, those dependencies are used
   * to declare the paths of shared libraries for execution.
   */
  @Override
  protected ScopeFilter getArtifactScopeFilter() {
	  
    return new ScopeFilter( Artifact.SCOPE_TEST, null );
    
  }

  
  @Override
  protected File getUnpackDirectory() {
	  
    return getTestUnpackDirectory() == null ? super.getUnpackDirectory() : getTestUnpackDirectory();
    
  }

  
  @Override
  public final void narExecute() throws MojoExecutionException, MojoFailureException, IOException {
	  
    if (this.skipTests || this.dryRun) {
    	
      getLog().info("Tests are skipped");
      
    } else {
    	
      for (final Library element : getLibraries()) {
    	  
        runExecutable(element);
        
      }
      
    }
    
  }

  
  private void runExecutable(final Library library) throws MojoExecutionException, MojoFailureException {
	  
    if (library.getType().equals(Library.EXECUTABLE) && library.shouldRun()) {
    	
      final MavenProject project = getMavenProject();

      final String extension = getOS().equals(OS.WINDOWS) ? ".exe" : "";
      
      final File executable = new File(getLayout().getBinDirectory(getTargetDirectory(),
          getMavenProject().getArtifactId(), getMavenProject().getVersion(), getAOL().toString()),
          project.getArtifactId() + extension);
      
      if (!executable.exists()) {
    	  
        getLog().warn("Skipping non-existing executable " + executable);
        
        return;
        
      }
      
      getLog().info("Running executable " + executable);
      
      final List<?> args = library.getArgs();
      
      final int result = NarUtil.runCommand(executable.getPath(), args.toArray(new String[args.size()]),
          null, getLog());
      
      if (result != 0) {
    	  
        throw new MojoFailureException("Test " + executable + " failed with exit code: " + result + " 0x"
            + Integer.toHexString(result));
        
      }
      
    }
    
  }
  
}
