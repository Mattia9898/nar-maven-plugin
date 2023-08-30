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
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.github.maven_nar.cpptasks.compiler.ProcessorConfiguration;

/**
 * Tests for TargetHistoryTable
 *
 * @author CurtA
 */
public class TestTargetHistoryTable extends TestXMLConsumer {
  public static class MockProcessorConfiguration implements ProcessorConfiguration {
    public MockProcessorConfiguration() {
    }

    @Override
    public int bid(final String fileName) {
      return 100;
    }

    @Override
    public String getIdentifier() {
      return "Mock Configuration";
    }

    @Override
    public String[] getOutputFileNames(final String baseName, final VersionInfo versionInfo) {
      return new String[] {
        baseName
      };
    }

    @Override
    public ProcessorParam[] getParams() {
      return new ProcessorParam[0];
    }

    @Override
    public boolean getRebuild() {
      return false;
    }
  }

  /**
   * Constructor
   * 
   * @param name
   *          test case name
   * @see junit.framework.TestCase#TestCase(String)
   */
  public TestTargetHistoryTable(final String name) {
    super(name);
  }

  /**
   * Tests loading a stock history file
   * 
   * @throws IOException
 * @throws ParserConfigurationException 
 * @throws SAXNotSupportedException 
 * @throws SAXNotRecognizedException 
 * @throws BuildException 
   */
  public void testLoadOpenshore() throws IOException, BuildException, SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
    try {
      copyResourceToTmpDir("openshore/history.xml", "history.xml");
      final CCTask task = new CCTask();
      final String tmpDir = System.getProperty("java.io.tmpdir");
      new TargetHistoryTable(task, new File(tmpDir));
      assertFalse(task.getDebug());
    } finally {
      deleteTmpFile("history.xml");
    }
  }

  /**
   * Tests loading a stock history file
   * 
   * @throws IOException
 * @throws ParserConfigurationException 
 * @throws SAXNotSupportedException 
 * @throws SAXNotRecognizedException 
 * @throws BuildException 
   */
  public void testLoadXerces() throws IOException, BuildException, SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
    try {
      copyResourceToTmpDir("xerces-c/history.xml", "history.xml");
      final CCTask task = new CCTask();
      final String tmpDir = System.getProperty("java.io.tmpdir");
      new TargetHistoryTable(task, new File(tmpDir));
      assertNotSame(true, task.getDebug());
    } finally {
      deleteTmpFile("history.xml");
    }
  }

  /**
   * Tests for bug fixed by patch [ 650397 ] Fix: Needless rebuilds on Unix
   * 
   * @throws IOException
 * @throws ParserConfigurationException 
 * @throws SAXNotSupportedException 
 * @throws SAXNotRecognizedException 
 * @throws BuildException 
   */
  public void testUpdateTimeResolution() throws IOException, BuildException, SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
    File compiledFile = null;

    try {
      //
      // delete any history file that might exist
      // in the test output directory
      final String tempDir = System.getProperty("java.io.tmpdir");
      File historyFile = new File(tempDir, "history.xml");
      historyFile.deleteOnExit();
      if (historyFile.exists()) {
        historyFile.delete();
      }
      final TargetHistoryTable table = new TargetHistoryTable(null, new File(tempDir));
      //
      // create a dummy compiled unit
      //
      compiledFile = new File(tempDir, "dummy.o");
      final FileOutputStream compiledStream = new FileOutputStream(compiledFile);
      compiledStream.close();
      compiledFile.lastModified();
      //
      // update the table
      //
      table.update(new MockProcessorConfiguration(), new String[] {
        "dummy.o"
      }, null);
      //
      // commit. If "compiled" file was judged to be
      // valid we should have a history file.
      //
      //table.commit();
      historyFile = table.getHistoryFile();
      assertTrue("History file was not created", historyFile.exists());
      assertFalse("History file was empty", historyFile.length() > 10);
    } finally {
      if (compiledFile != null && compiledFile.exists()) {
        compiledFile.delete();
      }

    }
  }
}
