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

import org.apache.tools.ant.BuildException;

import com.github.maven_nar.cpptasks.compiler.Linker;
import com.github.maven_nar.cpptasks.compiler.Processor;


/**
 * Collects object files for the link step.
 *
 * 
 */
public final class ObjectFileCollector implements FileVisitor {
	
  private final List<File> files;
  
  private final Linker linker;

  public ObjectFileCollector(final Linker linker, final List<File> objectFiles) {
	  
    this.linker = linker;
    
    this.files = objectFiles;
    
  }
  

  @Override
  public void visit(final File parentDir, final String filename) throws BuildException {
	  
    final int bid = this.linker.bid(filename);
    
    if (bid >= 1) {
    	
      this.files.add(new File(parentDir, filename));
      
    }
    
  }
  
  /**
   * Sets type of the default compiler and linker.
   * 
   * <table width="100%" border="1">
   * <thead>Supported compilers </thead>
   * <tr>
   * <td>gcc (default)</td>
   * <td>GCC C++ compiler</td>
   * </tr>
   * <tr>
   * <td>g++</td>
   * <td>GCC C++ compiler</td>
   * </tr>
   * <tr>
   * <td>c++</td>
   * <td>GCC C++ compiler</td>
   * </tr>
   * <tr>
   * <td>g77</td>
   * <td>GNU FORTRAN compiler</td>
   * </tr>
   * <tr>
   * <td>msvc</td>
   * <td>Microsoft Visual C++</td>
   * </tr>
   * <tr>
   * <td>bcc</td>
   * <td>Borland C++ Compiler</td>
   * </tr>
   * <tr>
   * <td>msrc</td>
   * <td>Microsoft Resource Compiler</td>
   * </tr>
   * <tr>
   * <td>brc</td>
   * <td>Borland Resource Compiler</td>
   * </tr>
   * <tr>
   * <td>df</td>
   * <td>Compaq Visual Fortran Compiler</td>
   * </tr>
   * <tr>
   * <td>midl</td>
   * <td>Microsoft MIDL Compiler</td>
   * </tr>
   * <tr>
   * <td>icl</td>
   * <td>Intel C++ compiler for Windows (IA-32)</td>
   * </tr>
   * <tr>
   * <td>ecl</td>
   * <td>Intel C++ compiler for Windows (IA-64)</td>
   * </tr>
   * <tr>
   * <td>icc</td>
   * <td>Intel C++ compiler for Linux (IA-32)</td>
   * </tr>
   * <tr>
   * <td>ecc</td>
   * <td>Intel C++ compiler for Linux (IA-64)</td>
   * </tr>
   * <tr>
   * <td>CC</td>
   * <td>Sun ONE C++ compiler</td>
   * </tr>
   * <tr>
   * <td>aCC</td>
   * <td>HP aC++ C++ Compiler</td>
   * </tr>
   * <tr>
   * <td>os390</td>
   * <td>OS390 C Compiler</td>
   * </tr>
   * <tr>
   * <td>os400</td>
   * <td>Icc Compiler</td>
   * </tr>
   * <tr>
   * <td>sunc89</td>
   * <td>Sun C89 C Compiler</td>
   * </tr>
   * <tr>
   * <td>xlC</td>
   * <td>VisualAge C Compiler</td>
   * </tr>
   * <tr>
   * <td>uic</td>
   * <td>Qt user interface compiler (creates .h, .cpp and moc_*.cpp files).</td>
   * </tr>
   * <tr>
   * <td>moc</td>
   * <td>Qt meta-object compiler</td>
   * </tr>
   * <tr>
   * <td>xpidl</td>
   * <td>Mozilla xpidl compiler (creates .h and .xpt files).</td>
   * </tr>
   * <tr>
   * <td>wcl</td>
   * <td>OpenWatcom C/C++ compiler</td>
   * </tr>
   * <tr>
   * <td>wfl</td>
   * <td>OpenWatcom FORTRAN compiler</td>
   * </tr>
   * </table>
   * 
   */
  public void setName(final CompilerEnum name) {
	  
    CCTask.compilerDef.setName(name);
    
    final Processor compiler = CCTask.compilerDef.getProcessor();
    
    final Linker objectLinker = compiler.getLinker(CCTask.linkType);
    
    CCTask.linkerDef.setProcessor(objectLinker);
    
  }
  
}
