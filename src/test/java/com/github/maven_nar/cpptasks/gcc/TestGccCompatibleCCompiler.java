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

package com.github.maven_nar.cpptasks.gcc;

import java.util.ArrayList;

import junit.framework.TestCase;


/**
 * Tests for gcc compatible compilers
 *
 * @author CurtA
 */
public abstract class TestGccCompatibleCCompiler extends TestCase {
	
	
  /**
   * Constructor
   * 
   * @param name
   *          test case name
   */
  public TestGccCompatibleCCompiler(final String name) {
    super(name);
  }

  
  /**
   * Compiler creation method
   * 
   * Must be overriden by extending classes
   * 
   * @return GccCompatibleCCompiler
   */
  protected abstract GccCompatibleCCompiler create();

  
  /**
   * Tests command lines switches for warning = 0
   */
  public void testWarningLevel0() {
	  
    final ArrayList<String> args = new ArrayList();
    args.add("-w");
    assertEquals(1, args.size());
    assertEquals("-w", args.get(0));
    
  }

  
  /**
   * Tests command lines switches for warning = 1
   */
  public void testWarningLevel1() {
	  
    final ArrayList<String> args = new ArrayList();
    assertEquals(0, args.size());
    
  }

  
  /**
   * Tests command lines switches for warning = 2
   */
  public void testWarningLevel2() {

    final ArrayList<String> args = new ArrayList();
    assertEquals(0, args.size());
    
  }

  
  /**
   * Tests command lines switches for warning = 3
   */
  public void testWarningLevel3() {

    final ArrayList<String> args = new ArrayList();
    args.add("-Wall");
    assertEquals(1, args.size());
    assertEquals("-Wall", args.get(0));
    
  }

  
  /**
   * Tests command lines switches for warning = 4
   */
  public void testWarningLevel4() {

    final ArrayList<String> args = new ArrayList();
    args.add("-W");
    args.add("-Wall");
    assertEquals(2, args.size());
    assertEquals("-W", args.get(0));
    assertEquals("-Wall", args.get(1));
    
  }

  
  /**
   * Tests command lines switches for warning = 5
   */
  public void testWarningLevel5() {

    final ArrayList<String> args = new ArrayList();
    args.add("-Werror");
    args.add("-W");
    args.add("-Wall");
    assertEquals(3, args.size());
    assertEquals("-Werror", args.get(0));
    assertEquals("-W", args.get(1));
    assertEquals("-Wall", args.get(2));
    
  }
  
}
