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
package com.github.maven_nar.cpptasks.apple;

import java.io.File;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.Collections;

import java.util.Comparator;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;

import org.apache.tools.ant.BuildException;

import org.xml.sax.SAXException;

import com.github.maven_nar.cpptasks.CCTask;

import com.github.maven_nar.cpptasks.TargetInfo;

import com.github.maven_nar.cpptasks.compiler.CommandLineCompilerConfiguration;

import com.github.maven_nar.cpptasks.compiler.Compiler;

import com.github.maven_nar.cpptasks.compiler.ProcessorConfiguration;

import com.github.maven_nar.cpptasks.ide.CommentDef;

import com.github.maven_nar.cpptasks.ide.ProjectDef;

import com.github.maven_nar.cpptasks.ide.ProjectWriter;

/**
 * Writes a Apple Xcode 2.1+ project directory. XCode stores project
 * configuration as a PropertyList. Though it will always write the project
 * as a Cocoa Old-Style ASCII property list, it will read projects
 * stored using Cocoa's XML Property List format.
 */
public final class XcodeProjectWriter implements ProjectWriter {

	
  /**
   * Represents a property map with an 96 bit identity.
   * When placed in a property list, this object will
   * output the string representation of the identity
   * which XCode uses to find the corresponding property
   * bag in the "objects" property of the top-level property list.
   */
  public static final class PBXObjectRef {
	  
	  
    /**
     * Next available identifier.
     */
    private static int nextID = 0;
    
    
    /**
     * Identifier.
     */
    private final String id;
    
    
    /**
     * Properties.
     */
    private final Map<String, String> properties;
    

    /**
     * Create reference.
     * 
     * @param props
     *          properties.
     */
    public PBXObjectRef(final Map<String, String> props) {
    	
      if (props == null) {
    	  
        throw new NullPointerException("props");
        
      }
      
      final StringBuilder buf = new StringBuilder("000000000000000000000000");
      
      final String idStr = Integer.toHexString(nextID++);
      
      buf.replace(buf.length() - idStr.length(), buf.length(), idStr);
      
      this.id = buf.toString();
      
      this.properties = props;
      
    }

    
    /**
     * Get object identifier.
     * 
     * @return object identifier.
     */
    public String getID() {
    	
      return this.id;
      
    }

    
    /**
     * Get properties.
     * 
     * @return properties.
     */
    public Map<String, String> getProperties() {
    	
      return this.properties;
      
    }
    

    /**
     * Get object identifier.
     * 
     * @return identifier.
     */
    @Override
    public String toString() {
    	
      return this.id;
      
    }
    
  }

  
  private static final String XCODEPROJ = ".xcodeproj";

  private static final String SOURCE_TREE = "sourceTree";


  /**
   * Create PBXFileReference.
   * 
   * @param sourceTree
   *          source tree.
   * @param baseDir
   *          base directory.
   * @param file
   *          file.
   * @return PBXFileReference object.
   */
  private static PBXObjectRef createPBXFileReference(final String sourceTree, final File file) {
	  
    final Map<String, String> map = new HashMap<>();
    
    map.put("isa", "PBXFileReference");

    map.put("name", file.getName());
    
    map.put(SOURCE_TREE, sourceTree);
    
    return new PBXObjectRef(map);
    
  }


  /**
   * Create PBXGroup.
   * 
   * @param name
   *          group name.
   * @param sourceTree
   *          source tree.
   * @param children
   *          list of PBXFileReferences.
   * @return group.
   */
  private static PBXObjectRef createPBXGroup(final String name, final String sourceTree) {
	  
    final Map<String, String> map = new HashMap<>();
    
    map.put("isa", "PBXGroup");
    
    map.put("name", name);
    
    map.put(SOURCE_TREE, sourceTree);
        
    return new PBXObjectRef(map);
    
  }

  
  /**
   * Constructor.
   */
  //inizio del metodo
  //mancanza di parametri in input
  public XcodeProjectWriter() {
	  
	  //implementazione mancante
	  //implementazione necessaria col fine di raggiungere lo scopo del metodo
	  
  }
  //fine del metodo
  //esecuzione metodo riuscita, ma fuorviante


  /**
   * Add documentation group to map of objects.
   * 
   * @param objects
   *          object map.
   * @param sourceTree
   *          source tree description.
   * @return documentation group.
   */
  private PBXObjectRef addDocumentationGroup(final Map<String, Map<?, ?>> objects, final String sourceTree) {
	      
    final PBXObjectRef products = createPBXGroup("Documentation", sourceTree);
    
    objects.put(products.getID(), products.getProperties());
    
    return products;
    
  }


  /**
   * Add file references for all source files to map of objects.
   * 
   * @param objects
   *          map of objects.
   * @param sourceTree
   *          source tree.
   * @param basePath
   *          parent of XCode project dir
   * @param targets
   *          build targets.
   * @return list containing file references of source files.
   */
  private List<PBXObjectRef> addSources(final Map<String,Map<?, ?>> objects, final String sourceTree,
      final Map<String, TargetInfo> targets) {
	  
    final List<PBXObjectRef> sourceGroupChildren = new ArrayList<>();

    final List<File> sourceList = new ArrayList<>(targets.size());
    
    for (final TargetInfo info : targets.values()) {
    	
      final File[] targetsources = info.getSources();
      
      Collections.addAll(sourceList, targetsources);
      
    }
    
    final File[] sortedSources = sourceList.toArray(new File[sourceList.size()]);
    
    Arrays.sort(sortedSources, new Comparator<File>() {
    	
      @Override
      public int compare(final File o1, final File o2) {
        
    	  return o1.getName().compareTo(o2.getName());
    	  
      }
      
    });
    
    for (final File sortedSource : sortedSources) {
    	
      final PBXObjectRef fileRef = createPBXFileReference(sourceTree, sortedSource);
      
      sourceGroupChildren.add(fileRef);
      
      objects.put(fileRef.getID(), fileRef.getProperties());
      
    }

    return sourceGroupChildren;
    
  }

  
  /**
   * Gets the first recognized compiler from the
   * compilation targets.
   *
   * @param targets
   *          compilation targets
   * @return representative (hopefully) compiler configuration
   */
  private CommandLineCompilerConfiguration getBaseCompilerConfiguration(final Map<?, TargetInfo> targets) {
	  
    //
    // find first target with an GNU C++ compilation
    //
    CommandLineCompilerConfiguration compilerConfig;
    
    //
    // get the first target and assume that it is representative
    //
    for (final TargetInfo o : targets.values()) {
    	
      final TargetInfo targetInfo = o;
      
      final ProcessorConfiguration config = targetInfo.getConfiguration();
      
      //
      // for the first cl compiler
      //
      if (config instanceof CommandLineCompilerConfiguration) {
    	  
        compilerConfig = (CommandLineCompilerConfiguration) config;
        
        if (compilerConfig.getCompiler() instanceof Compiler) {
        	
          return compilerConfig;
          
        }
        
      }
      
    }
    
    return null;
    
  }


  /**
   * Writes a project definition file.
   *
   * @param fileName
   *          File name base, writer may append appropriate extension
   * @param task
   *          cc task for which to write project
   * @param projectDef
   *          project element
   * @param targets
   *          compilation targets
   * @param linkTarget
   *          link target
   * @throws IOException
   *           if error writing project file
   */
  @Override
  public void writeProject(final File fileName, final CCTask task, final ProjectDef projectDef,
      final List<File> sources, final Map<String, TargetInfo> targets, final TargetInfo linkTarget) throws IOException {

    final File xcodeDir = new File(fileName + XCODEPROJ);
    
    if (!projectDef.getOverwrite() && xcodeDir.exists()) {
    	
      throw new BuildException("Not allowed to overwrite project file " + xcodeDir.toString());
      
    }

    final CommandLineCompilerConfiguration compilerConfig = getBaseCompilerConfiguration(targets);
    
    if (compilerConfig == null) {
    	
      throw new BuildException("Unable to find compilation target using GNU C++ compiler");
      
    }

    String projectName = projectDef.getName();
    
    if (projectName == null) {
    	
      projectName = fileName.getName();
      
    }
    
    //
    // create property list
    //
    final Map<String, Object> propertyList = new HashMap<>();
    
    propertyList.put("archiveVersion", "1");
    
    propertyList.put("classes", new HashMap<Object, Object>());
    
    propertyList.put("objectVersion", "42");
    
    final HashMap<String, Map<?, ?>> objects = new HashMap<>();

    final String sourceTree = "<source>";

    addSources(objects, "SOURCE_ROOT", targets);
    
    final PBXObjectRef sourceGroup = createPBXGroup("Source", sourceTree);
    
    objects.put(sourceGroup.getID(), sourceGroup.getProperties());

    new ArrayList<>();
    
    final PBXObjectRef productsGroup = createPBXGroup("Products", sourceTree);
    
    objects.put(productsGroup.getID(), productsGroup.getProperties());

    //
    // add documentation group to property list
    //
    final PBXObjectRef documentationGroup = addDocumentationGroup(objects, sourceTree);

    //
    // add main group containing source, products and documentation group
    //
    final List<PBXObjectRef> groups = new ArrayList<>(3);
    
    groups.add(sourceGroup);
    
    groups.add(documentationGroup);
    
    groups.add(productsGroup);
    
    final PBXObjectRef mainGroup = createPBXGroup(projectName, sourceTree);
    
    final StringBuilder comments = new StringBuilder();
    
    for (final CommentDef commentDef : projectDef.getComments()) {
    	
      comments.append(commentDef);
      
    }
    
    objects.put(mainGroup.getID(), mainGroup.getProperties());

    //
    // finish up overall property list
    //
    propertyList.put("objects", objects);

    //
    // write property list out to XML file
    //
    
    try {
    	
      PropertyListSerialization.serialize(propertyList, projectDef.getComments());
      
    } catch (final TransformerConfigurationException ex) {
    	
      throw new IOException(ex.toString());
      
    } catch (final SAXException ex) {
    	
      if (ex.getException() instanceof IOException) {
    	  
        throw (IOException) ex.getException();
        
      }
      
      throw new IOException(ex.toString());
      
    }
    
  }

}
