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

package com.github.maven_nar.cpptasks.msvc;

import java.util.ArrayList;

import junit.framework.TestCase;


/**
 * Test Microsoft C/C++ compiler adapter
 * 
 */
public class TestMsvc2005CCompiler extends TestCase {
	
  public TestMsvc2005CCompiler(final String name) {
    super(name);
  }

  public void testDebug() {
	  
    final Msvc2005CCompiler compiler = Msvc2005CCompiler.getInstance();
    final ArrayList<String> args = new ArrayList();
    
    args.add("/Zi");
    args.add("/Od");
    args.add("/RTC1");
    args.add("/D_DEBUG");
    args.add("/Fd");
    
    compiler.addDebugSwitch(args);
    compiler.addPathSwitch(args);
    
    assertEquals(9, args.size());
    assertEquals("/Zi", args.get(0));
    assertEquals("/Od", args.get(1));
    assertEquals("/RTC1", args.get(2));
    assertEquals("/D_DEBUG", args.get(3));
    assertEquals("/Fd", args.get(4));
    
  }
  
}
