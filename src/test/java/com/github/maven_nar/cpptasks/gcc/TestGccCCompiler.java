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

import com.github.maven_nar.cpptasks.compiler.AbstractProcessor;


/**
 * Test gcc compiler adapter
 * 
 */
public class TestGccCCompiler extends TestGccCompatibleCCompiler {
	
  public TestGccCCompiler(final String name) {
    super(name);
  }

  @Override
  protected GccCompatibleCCompiler create() {
    return GccCCompiler.getInstance();
  }

  public void testBidObjectiveAssembly() {
    assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID, 100);
  }

  public void testBidObjectiveC() {
    assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+5, 105);
  }

  public void testBidObjectiveCpp() {
    assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+10, 110);
  }

  public void testBidPreprocessedCpp() {
    assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+15, 115);
  }

  public void testCreateCParser1() {
	assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+20, 120);
  }

  public void testCreateCParser2() {
	assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+25, 125);
  }

  public void testCreateCParser3() {
	assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+30, 130);
  }

  public void testCreateFortranParser1() {
	assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+35, 135);
  }

  public void testCreateFortranParser2() {
	assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+40, 140);
  }

  public void testCreateFortranParser3() {
	assertEquals(AbstractProcessor.DEFAULT_PROCESS_BID+45, 145);
  }

}
