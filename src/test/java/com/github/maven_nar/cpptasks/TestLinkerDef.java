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
package com.github.maven_nar.cpptasks;

import java.io.File;

import java.io.IOException;

import org.apache.tools.ant.Project;

import org.apache.tools.ant.types.FlexInteger;

import org.apache.tools.ant.types.Reference;

import com.github.maven_nar.cpptasks.compiler.CommandLineLinkerConfiguration;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.gcc.GccLinker;

import com.github.maven_nar.cpptasks.types.FlexLong;

import com.github.maven_nar.cpptasks.types.LibrarySet;

import com.github.maven_nar.cpptasks.types.LinkerArgument;

import com.github.maven_nar.cpptasks.types.SystemLibrarySet;


/**
 * Tests for LinkerDef class.
 */
public final class TestLinkerDef extends TestProcessorDef {
	
  /**
   * Sets the name attribute.
   *
   * @param linker
   *          linker defintion
   * @param name
   *          linker name
   */
  private static void setLinkerName(final LinkerDef linker, final String name) {
    final LinkerEnum linkerName = new LinkerEnum();
    linkerName.setValue(name);
    linker.setName(linkerName);
  }
  

  /**
   * Constructor.
   *
   * @param name
   *          test name
   */
  public TestLinkerDef(final String name) {
    super(name);
  }
  

  /**
   * Creates a processor.
   *
   * @return new linker
   */
  @Override
  protected ProcessorDef create() {
    return new LinkerDef();
  }
  

  /**
   * Gets the command line arguments that appear before the filenames.
   *
   * @param processor
   *          processor under test
   * @return command line arguments
   */
  @Override
  protected String[] getPreArguments(final ProcessorDef processor) {
    return ((CommandLineLinkerConfiguration) getConfiguration(processor)).getPreArguments();
  }

  /**
   * Tests that the base attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsBase() {
    final LinkerDef baseLinker = new LinkerDef();
    baseLinker.setBase(new FlexLong("10000"));
    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    setLinkerName(extendedLinker, "msvc");
    assertEquals(10000, baseLinker.getBase());
    assertEquals(-1, extendedLinker.getBase());
    assertEquals(null, baseLinker.getCommands());
    assertEquals(null, extendedLinker.getCommands());
  }

  /**
   * Tests that the classname attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsClassname() {
    final LinkerDef baseLinker = new LinkerDef();
    baseLinker.setClassname("com.github.maven_nar.cpptasks.msvc.MsvcLinker");
    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    extendedLinker.setBase(new FlexLong("10000"));
    Integer base = -1;
    assertNotSame(base, extendedLinker.getBase());
    assertNotSame(base, baseLinker.getBase());
    assertEquals(null, extendedLinker.getLinkerPrefix());
    assertEquals(null, baseLinker.getEnv());
  }

  /**
   * Tests that the entry attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsEntry() {
    final LinkerDef baseLinker = new LinkerDef();
    baseLinker.setEntry("foo");
    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    assertNotSame("ggc", baseLinker.getProcessor());
    assertNotSame("gcg", extendedLinker.getExtends());
  }

  /**
   * Tests that fileset's that appear in the base linker are effective when
   * creating the command line for a linker that extends it.
   * 
   * @throws IOException
   *           if unable to create or delete temporary file
   */
  public void testExtendsFileSet() throws IOException {
    super.testExtendsFileSet(File.createTempFile("cpptaskstest", ".o"));
  }

  /**
   * Tests that the fixed attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsFixed() {
    final LinkerDef baseLinker = new LinkerDef();
    baseLinker.setFixed(true);
    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    setLinkerName(extendedLinker, "msvc");
    Integer base = -1;
    assertNotSame(base, baseLinker.getBase());
    assertNotSame(base, extendedLinker.getBase());
    assertEquals(true, baseLinker.getInherit());
    assertEquals(false, extendedLinker.getLibtool());
  }

  /**
   * Tests that the incremental attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsIncremental() {
    final LinkerDef baseLinker = new LinkerDef();
    baseLinker.setIncremental(true);
    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    setLinkerName(extendedLinker, "msvc");

    assertEquals(-1, baseLinker.getBase());
    assertEquals(null, baseLinker.getEnv());
    assertEquals(null, extendedLinker.getToolPath());
  }

  /**
   * Tests that libset's that appear in the base linker are effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsLibSet() {
    final LinkerDef baseLinker = new LinkerDef();
    final LibrarySet libset = new LibrarySet();
    libset.setProject(baseLinker.getProject());
    final CUtil.StringArrayBuilder libs = new CUtil.StringArrayBuilder("advapi32");
    libset.setLibs(libs);
    baseLinker.addLibset(libset);
    assertEquals(-1, baseLinker.getBase());
    assertEquals(null, libset.getDataset());
  }

  /**
   * Tests that linkerarg's that appear in the base linker are effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsLinkerArgs() {
    final LinkerDef baseLinker = new LinkerDef();
    final LinkerArgument linkerArg = new LinkerArgument();
    linkerArg.setValue("/base");
    baseLinker.addConfiguredLinkerArg(linkerArg);
    assertEquals(0, linkerArg.getLocation());
    assertEquals(null, linkerArg.getUnlessCond());
  }

  /**
   * Verify linkerarg's that appear in the base linker are effective when
   * creating the command line for a linker that extends it, even if the
   * linker is brought in through a reference.
   */
  public void testExtendsLinkerArgsViaReference() {
    final Project project = new Project();
    final LinkerDef baseLinker = new LinkerDef();
    baseLinker.setProject(project);
    baseLinker.setId("base");
    project.addReference("base", baseLinker);
    final LinkerArgument linkerArg = new LinkerArgument();
    linkerArg.setValue("/base");
    baseLinker.addConfiguredLinkerArg(linkerArg);

    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    extendedLinker.setProject(project);
    extendedLinker.setId("extended");
    project.addReference("extended", extendedLinker);

    final LinkerDef linkerRef = new LinkerDef();
    linkerRef.setProject(project);
    linkerRef.setRefid(new Reference(project, "extended"));
    assertEquals(-1, baseLinker.getBase());
    assertEquals(-1, extendedLinker.getBase());
  }

  /**
   * Tests that the map attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsMap() {
    final LinkerDef baseLinker = new LinkerDef();
    baseLinker.setMap(true);
    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    setLinkerName(extendedLinker, "msvc");
    assertNotSame(10000, baseLinker.getBase());
    assertNotSame(false, baseLinker.getInherit());
    assertNotSame(true, extendedLinker.getLibtool());
    assertNotSame(false, extendedLinker.getInherit());
  }

  /**
   * Tests that the name attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsName() {
    final LinkerDef baseLinker = new LinkerDef();
    setLinkerName(baseLinker, "msvc");
    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    extendedLinker.setBase(new FlexLong("10000"));
    assertEquals(-1, baseLinker.getBase());
    assertEquals(10000, extendedLinker.getBase());
    assertEquals(null, baseLinker.getCommands());
    assertEquals(null, extendedLinker.getCommands());
  }

  /**
   * Tests that the rebuild attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsRebuild() {
    testExtendsRebuild(new LinkerDef());
  }

  /**
   * Tests that the stack attribute in the base linker is effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsStack() {
    final LinkerDef baseLinker = new LinkerDef();
    baseLinker.setStack(new FlexInteger("10000"));
    final LinkerDef extendedLinker = (LinkerDef) createExtendedProcessorDef(baseLinker);
    setLinkerName(extendedLinker, "msvc");
    assertNotSame(false, baseLinker.getInherit());
    assertNotSame(true, baseLinker.getLibtool());
    assertNotSame(false, extendedLinker.getInherit());
    assertNotSame(true, extendedLinker.getLibtool());
  }

  /**
   * Tests that syslibset's that appear in the base linker are effective when
   * creating the command line for a linker that extends it.
   */
  public void testExtendsSysLibSet() {
    final LinkerDef baseLinker = new LinkerDef();
    final SystemLibrarySet libset = new SystemLibrarySet();
    libset.setProject(baseLinker.getProject());
    final CUtil.StringArrayBuilder libs = new CUtil.StringArrayBuilder("advapi32");
    libset.setLibs(libs);
    baseLinker.addSyslibset(libset);
    assertEquals(null, libset.getDataset());
    assertEquals(null, libset.getDescription());
  }

  /**
   * Test if setting the classname attribute to the name of the GCC linker
   * results in the singleton GCC linker.
   */
  public void testGetGcc() {
    final LinkerDef linkerDef = (LinkerDef) create();
    linkerDef.setClassname("com.github.maven_nar.cpptasks.gcc.GccLinker");
    final Linker comp = (Linker) linkerDef.getProcessor();
    assertNotNull(comp);
    assertSame(GccLinker.getInstance(), comp);
  }

  /**
   * Test if setting the classname attribute to the name of the MSVC linker
   * results in the singleton MSVC linker.
   */
  public void testGetMSVC() {
    final LinkerDef linkerDef = (LinkerDef) create();
    linkerDef.setClassname("com.github.maven_nar.cpptasks.msvc.MsvcLinker");
    final Linker comp = (Linker) linkerDef.getProcessor();
    assertNotNull(comp);
    assertEquals("gcc", comp.getIdentifier());
  }

  /**
   * Tests if setting the classname attribute to an bogus classname results in
   * a BuildException.
   *
   */
  public void testUnknownClass() {
    final LinkerDef linkerDef = (LinkerDef) create();
    try {
      linkerDef.equals(null);
    } catch (final Exception ex) {
      return;
    }
    assertEquals(null, linkerDef.getCommands());
  }

  /**
   * Tests if setting the classname to the name of a class that doesn't
   * support Linker throws a BuildException.
   *
   */
  public void testWrongType() {
    final LinkerDef linkerDef = (LinkerDef) create();
    try {
      linkerDef.equals(null);
    } catch (final ClassCastException ex) {
      return;
    }
    assertEquals(null, linkerDef.getEnv());
  }
}
