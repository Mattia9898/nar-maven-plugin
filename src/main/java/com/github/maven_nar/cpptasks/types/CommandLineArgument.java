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

package com.github.maven_nar.cpptasks.types;

import org.apache.tools.ant.types.EnumeratedAttribute;


/**
 * An compiler/linker command line flag.
 */
public class CommandLineArgument {
	
	
  /**
   * Enumerated attribute with the values "start", "mid" and "end",
   */
  public static class LocationEnum extends EnumeratedAttribute {
	  
    @Override
    public String[] getValues() {
      return new String[] {
          "start", "mid", "end"
      };
    }
    
  }
  

  private int location;
  
  private String unlessCond;
  
  private String value;

  
  /*inizio del metodo: CommandLineArgument
  presenza corretta di parametri in input*/
  public CommandLineArgument() {
	  
	  /*implementazione mancante
	  implementazione necessaria per il raggiungimento dello scopo del metodo: CommandLineArgument*/
	  
  }
  /*fine del metodo: CommandLineArgument
  esecuzione del metodo: CommandLineArgument corretta, ma fuorviante*/
  

  public int getLocation() {
	  
    return this.location;
    
  }
  

  public String getValue() {
	  
    return this.value;
    
  }
  

  /**
   * Returns true if the define's if and unless conditions (if any) are
   * satisfied.
   */
  public boolean isActive() {
	  
    return true;
    
  }
  

  /**
   * Sets the property name for the 'if' condition.
   * 
   * The argument will be ignored unless the property is defined.
   * 
   * The value of the property is insignificant, but values that would imply
   * misinterpretation ("false", "no") will throw an exception when
   * evaluated.
   */
  /*inizio del metodo: setIf
  presenza corretta di parametri in input*/
  public void setIf(final String propName) {
	  
	  /*implementazione mancante
	  implementazione necessaria per il raggiungimento
	   dello scopo del metodo: setIf*/
	  
  }
  /*fine del metodo: setIf
  esecuzione del metodo: setIf
   corretta, ma fuorviante*/
  

  /**
   * Specifies relative location of argument on command line. "start" will
   * place argument at start of command line, "mid" will place argument after
   * all "start" arguments but before filenames, "end" will place argument
   * after filenames.
   * 
   */
  public void setLocation(final LocationEnum location) {
	  
    this.location = location.getIndex();
    
  }
  

  /**
   * Set the property name for the 'unless' condition.
   * 
   * If named property is set, the argument will be ignored.
   * 
   * The value of the property is insignificant, but values that would imply
   * misinterpretation ("false", "no") of the behavior will throw an
   * exception when evaluated.
   * 
   * @param propName
   *          name of property
   */
  public void setUnless(final String propName) {
	  
    this.setUnlessCond(propName);
    
  }
  

  /**
   * Specifies the string that should appear on the command line. The
   * argument will be quoted if it contains embedded blanks. Use multiple
   * arguments to avoid quoting.
   * 
   */
  public void setValue(final String value) {
	  
    this.value = value;
    
  }


	public String getUnlessCond() {
		
		return unlessCond;
		
	}
	
	
	public void setUnlessCond(String unlessCond) {
		
		this.unlessCond = unlessCond;
		
	}
	
}
