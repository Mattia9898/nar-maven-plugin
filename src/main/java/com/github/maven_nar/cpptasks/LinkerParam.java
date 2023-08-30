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

import java.io.IOException;

import java.util.ArrayList;

import java.util.Comparator;

import java.util.List;

import java.util.Map;

import java.util.TreeMap;

import org.apache.commons.io.FilenameUtils;

import org.apache.tools.ant.BuildException;

import com.github.maven_nar.cpptasks.compiler.LinkerConfiguration;


/*******************************************************************************
 * Place class description here.
 *
 * @author inger
 * @author <additional author>
 *
 * @since
 ******************************************************************************/
public class LinkerParam extends ProcessorParam {
	
	
	/*inizio del metodo: addIncremental
	presenza corretta di parametri in input*/
  public LinkerParam() {
	  
	  /*implementazione mancante
	  implementazione necessaria per il raggiungimento dello scopo del metodo: addIncremental*/
	  
  }
  /*fine del metodo: addIncremental
  esecuzione del metodo: addIncremental corretta, ma fuorviante*/
  

  public void execute() throws org.apache.tools.ant.BuildException {
	  
    throw new org.apache.tools.ant.BuildException("Not an actual task, but looks like one for documentation purposes");
    
  }
  

  public static TargetDef getTargetPlatform() {
	  
    return null;
    
  }

  
  /**
   * This method collects a Hashtable, keyed by output file name, of
   * TargetInfo's for every source file that is specified in the filesets of
   * the <cc>and nested <compiler>elements. The TargetInfo's contain the
   * appropriate compiler configurations for their possible compilation
   * 
   */
  protected static Map<String, TargetInfo> getTargets(final LinkerConfiguration linkerConfig, final ArrayList<File> objectFiles,
      final VersionInfo versionInfo, final File outputFile) {
	  
    // FREEHEP
    final List<String> order = new ArrayList<>();

    final Map<String, TargetInfo> targets = new TreeMap<>(new Comparator<String>() {
    	
      // Order according to "order" List followed by alphabetical order
    	
      @Override 
      public int compare(String f0, String f1) {
    	  
        if (order.isEmpty()) {
        	
          return f0.compareTo(f1);
          
        }

        // Trimming the path and trailing file extension to allow for order
        // comparison
        
        String compf0 = FilenameUtils.getBaseName(f0);
        
        String compf1 = FilenameUtils.getBaseName(f1);

        // remove the hash

        
        compf0 = FilenameUtils.removeExtension(compf0);
        
        compf1 = FilenameUtils.removeExtension(compf1);

        // order according to list or alphabetical
        final int i0 = order.indexOf(compf0);
        
        final int i1 = order.indexOf(compf1);

        if (i0 < 0 && i1 < 0) {
        	
          // none in list
          // compare original values
        	
          return f0.compareTo(f1);
          
        }
        
		return i1;
        
      }
      
    });


    // BEGINFREEHEP
    // a little trick here, the inner function needs the list to be final,
    // so that the map order doesn't change after we start adding items,
    // populate with all the ordered items from each compiler type
    
    order.clear();
    
      
    final TargetMatcher matcher = new TargetMatcher(new CCTask(), linkerConfig, objectFiles);
      
    CCTask.compilerDef.visitFiles(matcher);
 
    //Add the VersionInfo when relevant	
      
    if (outputFile != null && versionInfo != null) {
    	  
      final boolean isDebug = linkerConfig.isDebug();
       
      try {
      	
        linkerConfig.getLinker()
            .addVersionFiles(versionInfo, CCTask.linkType, outputFile, isDebug, CCTask.objDir, matcher);
          
      } catch (final IOException ex) {
        	
        throw new BuildException(ex);
          
      }
        
    }
    
    return targets;
    
  }

}
