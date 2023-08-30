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


import org.apache.tools.ant.types.EnumeratedAttribute;

import com.github.maven_nar.cpptasks.compiler.LinkerConfiguration;


/**
 * Enumeration of cpu types.
 *
 * @author Curt Arnold
 *
 */
public final class CPUEnum extends EnumeratedAttribute {

	
  /**
   * Constructor.
   *
   * Set by default to "pentium3"
   *
   * @see java.lang.Object#Object()
   */
  public CPUEnum() {
	  
    setValue("pentium3");
    
  }

  
  /**
   * Gets list of acceptable values.
   *
   * @see org.apache.tools.ant.types.EnumeratedAttribute#getValues()
   */
  @Override
  public String[] getValues() {
	  
    return new String[] {
        "i386", "i486", "i586", "i686", "pentium", "pentium-mmx", "pentiumpro", "pentium2", "pentium3", "pentium4",
        "k6", "k6-2", "k6-3", "athlon", "athlon-tbird", "athlon-4", "athlon-xp", "athlon-mp", "winchip-c6", "winchip2",
        "c3"
    };
    
  }
  

  public static LinkerConfiguration collectExplicitObjectFiles()
  {
	  
    //
    // find the first eligible linker
    //
    
    for (int i = 0; i < CCTask.compilers.size(); i++) {
    	
      final CompilerDef currentLinkerDef = CCTask.compilers.get(i);
      
      if (currentLinkerDef.isActive()) {
    	                          	
        currentLinkerDef.visitFiles(CommandExecution.objCollector); 
                  
        break;
        
      }
      
    }
                      
    // unless there was a <linker> element that
    // explicitly did not inherit files from
    // containing <cc> element
      
    CCTask.linkerDef.visitUserLibraries(CommandExecution.selectedLinker, CommandExecution.objCollector); 
    
    CCTask.linkerDef.visitSystemLibraries(CommandExecution.selectedLinker, CommandExecution.sysLibraryCollector);
    
    // copy over any system libraries to the
    // object files vector
        
    return (LinkerConfiguration) CommandExecution.linkerConfig;
    
  }

}
