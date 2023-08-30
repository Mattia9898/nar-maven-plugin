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


/**
 * Enumeration of cpu types.
 *
 * @author Curt Arnold
 *
 */
public final class OSFamilyEnum extends EnumeratedAttribute {
	
	
  /**
   * Constructor.
   *
   * Set by default to "pentium3"
   *
   * @see java.lang.Object#Object()
   */
  public OSFamilyEnum() {
	  
    setValue("windows");
    
  }
  

  /**
   * Gets list of acceptable values.
   *
   * @see org.apache.tools.ant.types.EnumeratedAttribute#getValues()
   */
  @Override
  public String[] getValues() {
	  
    return new String[] {
        "windows", "dos", "mac", "unix", "netware", "os/2", "tandem", "win9x", "z/os", "os/400", "openvms"
    };
    
  }
  
 
  /**
   * Sets optimization.
   * 
   * @param optimization
   */
  public void setOptimize(final OptimizationEnum optimization) {
	  
    CCTask.compilerDef.setOptimize(optimization);
    
  }
  

  /**
   * Sets the output file type. Supported values "executable", "shared", and
   * "static".
   */
  public static void setOuttype(final OutputTypeEnum outputType) {
	  
    CCTask.linkType.setOutputType(outputType);
    
  }


  /**
   * Sets the type of runtime library, possible values "dynamic", "static".
   */
  public static void setRuntime(final RuntimeType rtlType) {
	  
    CCTask.linkType.setStaticRuntime(rtlType.getIndex() == 1);
    
  }


  /**
   * Sets the nature of the subsystem under which that the program will
   * execute.
   * 
   * <table width="100%" border="1">
   * <thead>Supported subsystems </thead>
   * <tr>
   * <td>gui</td>
   * <td>Graphical User Interface</td>
   * </tr>
   * <tr>
   * <td>console</td>
   * <td>Command Line Console</td>
   * </tr>
   * <tr>
   * <td>other</td>
   * <td>Other</td>
   * </tr>
   * </table>
   * 
   * @param subsystem
   *          subsystem
   * @throws NullPointerException
   *           if subsystem is null
   */
  public static void setSubsystem(final SubsystemEnum subsystem) {
	  
    if (subsystem == null) {
    	
      throw new NullPointerException("subsystem");
      
    }
    
    CCTask.linkType.setSubsystem(subsystem);
    
  }
  

  /**
   * Enumerated attribute with the values "none", "severe", "default",
   * "production", "diagnostic", and "aserror".
   */
  public void setWarnings(final WarningLevelEnum level) {
	  
    CCTask.compilerDef.setWarnings(level);
    
  }
  
}
