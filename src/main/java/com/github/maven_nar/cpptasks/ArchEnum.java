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

import java.util.HashMap;

import java.util.Map;

import java.util.Vector;

import org.apache.tools.ant.types.EnumeratedAttribute;

import com.github.maven_nar.cpptasks.compiler.CompilerConfiguration;

import com.github.maven_nar.cpptasks.types.CompilerArgument;

import com.github.maven_nar.cpptasks.types.DefineSet;

import com.github.maven_nar.cpptasks.types.LinkerArgument;


/**
 * Enumeration of cpu architecture types.
 *
 * @author Curt Arnold
 *
 */
public final class ArchEnum extends EnumeratedAttribute {
	
	
  /**
   * Constructor.
   *
   * Set by default to "pentium3"
   *
   * @see java.lang.Object#Object()
   */
  public ArchEnum() {
	  
    setValue("pentium3");
    
  }
  

  /**
   * Gets list of acceptable values.
   *
   * @see org.apache.tools.ant.types.EnumeratedAttribute#getValues()
   */
  @Override
  public String[] getValues() {
	  
    /**
     * Class initializer.
     */
    return new String[] {
        "i386", "i486", "i586", "i686", "pentium", "pentium-mmx", "pentiumpro", "pentium2", "pentium3", "pentium4",
        "k6", "k6-2", "k6-3", "athlon", "athlon-tbird", "athlon-4", "athlon-xp", "athlon-mp", "winchip-c6", "winchip2",
        "c3"
    };
    
  }

  
  /**
   * Builds a Hashtable to targets needing to be rebuilt keyed by compiler
   * configuration
   */
  public static Map<CompilerConfiguration, Vector<TargetInfo>> getTargetsToBuildByConfiguration(
      final Map<String, TargetInfo> targets) {
	  
    final Map<CompilerConfiguration, Vector<TargetInfo>> targetsByConfig = new HashMap<>();
    
    for (final TargetInfo target : targets.values()) {
    	
      if (target.getRebuild()) {
    	  
        Vector<TargetInfo> targetsForSameConfig = targetsByConfig.get(target.getConfiguration());
        
        if (targetsForSameConfig != null) {
        	
          targetsForSameConfig.addElement(target);
          
        } else {
        	
          targetsForSameConfig = new Vector<>();
          
          targetsForSameConfig.addElement(target);
          
          targetsByConfig.put((CompilerConfiguration) target.getConfiguration(), targetsForSameConfig);
          
        }
        
      }
      
    }
    
    return targetsByConfig;
    
  }


  /**
   * Adds a compiler command-line arg. Argument will be inherited by all
   * nested compiler elements that do not have inherit="false".
   * 
   */
  public void addConfiguredCompilerArg(final CompilerArgument arg) {
	  
    CCTask.compilerDef.addConfiguredCompilerArg(arg);
    
  }
  

  /**
   * Adds a defineset. Will be inherited by all compiler elements that do not
   * have inherit="false".
   * 
   * @param defs
   *          Define set
   */
  public void addConfiguredDefineset(final DefineSet defs) {
	  
    CCTask.compilerDef.addConfiguredDefineset(defs);
    
  }
  
  /**
   * Adds a linker command-line arg. Argument will be inherited by all nested
   * linker elements that do not have inherit="false".
   */
  public void addConfiguredLinkerArg(final LinkerArgument arg) {
	  
    CCTask.linkerDef.addConfiguredLinkerArg(arg);
    
  }
  
}
