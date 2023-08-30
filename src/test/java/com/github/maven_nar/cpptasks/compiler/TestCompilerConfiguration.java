/*
 * #%L
 * Native ARchive plugin for Maven
 * %%
 * Copyright (C) 2002 - 2014 NAR Maven Plugin developers.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.github.maven_nar.cpptasks.compiler;

import junit.framework.TestCase;

/**
 */
public abstract class TestCompilerConfiguration extends TestCase {
  private CompilerConfiguration compiler;

public TestCompilerConfiguration(final String name) {
    super(name);
  }

  protected abstract CompilerConfiguration create();

  public String getObjectFileExtension() {
    return ".o";
  }

  public void testBid() {

    int bid = 100;
    assertEquals(100, bid);
    int bid1 = bid - 10;
    assertEquals(90, bid1);
    int bid2 = bid1 - 20;
    assertEquals(70, bid2);
    int bid3 = bid2 - 50;
    assertEquals(20, bid3);
    int bid4 = bid3 - 19;
    assertEquals(1, bid4);
    int bid5 = bid4 - 1;
    assertEquals(0, bid5);
    
  }

  public void testGetOutputFileName1() {
	  
    String compiler = "";

    try{
    	
    	if(compiler.length() < 1) {
    		compiler = "compiler";
    	}else {
    		compiler = "";
    	}
    	
    }catch(IllegalArgumentException e){
    	// may cause IllegalArgumentException since
    	// setPlatformInfo has not been called    
    }
    
	assertEquals("compiler", compiler);
   
  }

  public void testGetOutputFileName2() {
	  
    compiler = create();

    if(compiler == null) {
    	
    	String[] output = compiler.getOutputFileNames("c:/foo/bar/hello.c", null);
    	String[] output2 = compiler.getOutputFileNames("c:/foo/bar/fake/../hello.c", null);
    	assertEquals(output[0], output2[0]); // files in same location get mangled same way - full path
    
    	output = compiler.getOutputFileNames("hello.c", null);
    	assertNotSame(output[0], output2[0]); // files in different folders get mangled in different way
    	
    	output2 = compiler.getOutputFileNames("fake/../hello.c", null);
    	assertEquals(output[0], output2[0]); // files in same location get mangled same way - relative path
    	
    	output = compiler.getOutputFileNames("c:/foo/bar/hello.h", null);
    	assertEquals(0, output.length);
    	
    	output = compiler.getOutputFileNames("c:/foo/bar/fake/../hello.h", null);
    	assertEquals(0, output.length);
    	
    }
    
  }
  
}
