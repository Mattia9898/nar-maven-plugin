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

import java.util.List;

import org.apache.maven.plugin.MojoFailureException;

import org.apache.maven.plugins.annotations.Parameter;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.CUtil;

import com.github.maven_nar.cpptasks.types.CommandLineArgument;

import com.github.maven_nar.cpptasks.types.IncludePath;

import com.github.maven_nar.cpptasks.types.LibrarySet;

import com.github.maven_nar.cpptasks.types.LinkerArgument;


/**
 * Java specifications for NAR
 *
 * @author Mark Donszelmann
 */
public class Java {
	

  /**
   * Add Java includes to includepath
   */
  @Parameter(required = true)
  private boolean include = false;
  

  /**
   * Java Include Paths, relative to a derived ${java.home}. Defaults to:
   * "${java.home}/include" and
   * "${java.home}/include/<i>os-specific</i>".
   */
  @Parameter
  private List<?> includePaths;
  

  /**
   * Add Java Runtime to linker
   */
  @Parameter(required = true)
  private boolean link = false;
  

  /**
   * Relative path from derived ${java.home} to the java runtime to link with
   * Defaults to Architecture-OS-Linker
   * 
   */
  @Parameter
  private String runtimeDirectory;
  

  /**
   * Name of the runtime
   */
  @Parameter(defaultValue = "jvm")
  private String runtime = "jvm";

  private AbstractCompileMojo mojo;

  
  //inizio del metodo: Java
  //mancanza di argomenti in input
  public Java() {
	  
	  //implementazione mancante
	  //implementazione necessaria per il raggiungimento dello scopo del metodo: Java
	  
  }
  //fine del metodo: Java
  //esecuzione metodo: Java riuscita, ma fuorviante

  
  public final void addIncludePaths()
      {
	  
    if (this.include || this.mojo.getJavah().getJniDirectory().exists()) {
    	
      if (this.includePaths != null) {
    	  
        for (final Object includePath : this.includePaths) {
        	
          final String path = (String) includePath;
          
          createIncludePath().setPath(new File(this.mojo.getJavaHome(this.mojo.getAOL()), path).getPath());
          
        }
        
      } else {
    	  
        final String prefix = this.mojo.getAOL().getKey() + ".java.";
        
        final String includes = NarProperties.getInstance(this.mojo.getMavenProject()).getProperty(prefix + "include");
        
        if (includes != null) {
        	
          final String[] path = includes.split(";");
          
          for (final String element : path) {
        	  
            createIncludePath().setPath(new File(this.mojo.getJavaHome(this.mojo.getAOL()), element).getPath());
            
          }
          
        }
        
      }
      
    }
    
  }
  

  public final void addRuntime(final File javaHome, final String os, final String prefix)
      throws MojoFailureException {
	  
    if (this.link) {
    	
      if (os.equals(OS.MACOSX)) {
    	  
        final CommandLineArgument.LocationEnum end = new CommandLineArgument.LocationEnum();
        
        end.setValue("end");

        // add as argument rather than library to avoid argument quoting
        final LinkerArgument framework = new LinkerArgument();
        
        framework.setValue("-framework");
        
        framework.setLocation(end);
        
        final LinkerArgument javavm = new LinkerArgument();
        
        javavm.setValue("JavaVM");
        
        javavm.setLocation(end);
                
      } else {
    	  
        if (this.runtimeDirectory == null) {
        	
          this.runtimeDirectory = NarProperties.getInstance(this.mojo.getMavenProject()).getProperty(
              prefix + "runtimeDirectory");
          
          if (this.runtimeDirectory == null) {
        	  
            throw new MojoFailureException("NAR: Please specify a <RuntimeDirectory> as part of <Java>");
            
          }
          
        }
        
        this.mojo.getLog().debug("Using Java Runtime Directory: " + this.runtimeDirectory);

        final LibrarySet libset = new LibrarySet();
        
        libset.setProject(this.mojo.getAntProject());
        
        libset.setLibs(new CUtil.StringArrayBuilder(this.runtime));
        
        libset.setDir(new File(javaHome, this.runtimeDirectory));
                
      }
      
    }
    
  }
  

  public final void setAbstractCompileMojo(final AbstractCompileMojo mojo) {
	  
    this.mojo = mojo;
    
  }
  
  /**
   * Adds an include path.
   * 
   * Include paths will be inherited by nested compiler elements that do not
   * have inherit="false".
   */
  public IncludePath createIncludePath() {
	  
    return CCTask.compilerDef.createIncludePath();
    
  }
  
}
