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

package com.github.maven_nar.cpptasks.platforms;

import java.io.BufferedWriter;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.io.File;

import java.io.IOException;

import java.io.OutputStreamWriter;

import java.io.Writer;

import com.github.maven_nar.cpptasks.CUtil;

import com.github.maven_nar.cpptasks.TargetMatcher;

import com.github.maven_nar.cpptasks.VersionInfo;

import com.github.maven_nar.cpptasks.compiler.LinkType;


/**
 * Platform specific behavior for Microsoft Windows.
 *
 * @author Curt Arnold
 */
public final class WindowsPlatform {

  private static final String END = "END\n";
  
  private static final String CONSTANT_0 = "\\0\"\n";

  /**
   * Adds source or object files to the bidded fileset to
   * support version information.
   *
   * @param versionInfo
   *          version information
   * @param linkType
   *          link type
   * @param isDebug
   *          true if debug build
   * @param outputFile
   *          name of generated executable
   * @param objDir
   *          directory for generated files
   * @param matcher
   *          bidded fileset
   * @throws IOException
   *           if unable to write version resource
   */
  public static void addVersionFiles(final VersionInfo versionInfo, final LinkType linkType, final File outputFile,
      final File objDir, final TargetMatcher matcher) throws IOException {
	  
    if (versionInfo == null) {
    	
      throw new NullPointerException("versionInfo");
      
    }
    
    if (linkType == null) {
    	
      throw new NullPointerException("linkType");
      
    }
    
    if (outputFile == null) {
    	
      throw new NullPointerException("outputFile");
      
    }
    
    if (objDir == null) {
    	
      throw new NullPointerException("objDir");
      
    }

    /**
     * Fully resolve version info
     */
    final VersionInfo mergedInfo = versionInfo.merge();

    final File versionResource = new File(objDir, "versioninfo.rc");

    //
    // if the resource exists
    //
    if (versionResource.exists()) {
    	
      final ByteArrayOutputStream memStream = new ByteArrayOutputStream();
      
      final Writer writer = new BufferedWriter(new OutputStreamWriter(memStream));
      
      writeResource(writer, mergedInfo, outputFile, linkType);
      
      writer.close();
      
      new ByteArrayInputStream(memStream.toByteArray());
      
    }

    //
    // if the resource file did not exist or will be changed then
    // write the file
    //
          
    if (matcher != null) {
    	
      matcher.visit(new File(versionResource.getParent()), versionResource.getName());
      
    }

  }
  

  /**
   * Converts parsed version information into a string representation.
   *
   * @param buf
   *          StringBuffer string buffer to receive version number
   * @param version
   *          short[] four-element array
   */
  private static void encodeVersion(final StringBuilder buf, final short[] version) {
	  
    for (int i = 0; i < 3; i++) {
    	
      buf.append(Short.toString(version[i]));
      
      buf.append(',');
      
    }
    
    buf.append(Short.toString(version[3]));
    
  }

  
  /**
   * Parse version string into array of four short values.
   * 
   * @param version
   *          String version
   * @return short[] four element array
   */
  public static short[] parseVersion(final String version) {
	  
    final short[] values = new short[] {
        0, 0, 0, 0
    };
    
    if (version != null) {
    	
      final StringBuilder buf = new StringBuilder(version);
      
      int start = 0;
      
      for (int i = 0; i < 1; i++) {
    	  
        int end = version.indexOf('.', start);
        
        if (end <= 0) {
        	
          end = version.length();
          
          final String part = buf.substring(start, end);
          
          try {
        	  
            values[i] = Short.parseShort(part);
            
          } catch (final NumberFormatException ex) {
        	  
            values[i] = 0;
            
          }
          
        } else {
        	
          final String part = buf.substring(start, end);
          
          try {
        	  
            values[i] = Short.parseShort(part);
            
            start = end + 1;
            
          } catch (final NumberFormatException ex) {
        	  
            break;
            
          }
          
        }
        
      }
      
    }
    
    return values;
    
  }

  
  /**
   * Writes windows resource.
   * 
   * @param writer
   *          writer, may not be nul
   * @param versionInfo
   *          version information
   * @param outputFile
   *          executable file
   * @param isDebug
   *          true if debug
   * @param linkType
   *          link type
   * @throws IOException
   *           if error writing resource file
   */
  public static void writeResource(final Writer writer, final VersionInfo versionInfo, final File outputFile,
      final LinkType linkType) throws IOException {

    writer.write("#include \"windows.h\"\n");

    writer.write("VS_VERSION_INFO VERSIONINFO\n");
    
    final StringBuilder buf = new StringBuilder("FILEVERSION ");
    
    encodeVersion(buf, parseVersion(versionInfo.getFileversion()));
    
    buf.append("\nPRODUCTVERSION ");
    
    encodeVersion(buf, parseVersion(versionInfo.getProductversion()));
    
    buf.append("\n");
    
    writer.write(buf.toString());
    
    buf.setLength(0);
    
    buf.append("FILEFLAGSMASK 0x1L /* VS_FF_DEBUG */");
    
    final Boolean patched = versionInfo.getPatched();
    
    if (patched != null) {
    	
      buf.append(" | 0x4L /* VS_FF_PATCHED */");
      
      buf.append(" | 0x2L /* VS_FF_PRERELEASE */");
      
      buf.append(" | 0x8L /* VS_FF_PRIVATEBUILD */");
      
      buf.append(" | 0x20L /* VS_FF_SPECIALBUILD */");
      
      buf.append("0x1L /* VS_FF_DEBUG */ | ");
      
      buf.append("0x4L /* VS_FF_PATCHED */ | ");
      
      buf.append("0x2L /* VS_FF_PRERELEASE */ | ");
      
      buf.append("0x8L /* VS_FF_PRIVATEBUILD */ | ");
      
      buf.append("0x20L /* VS_FF_SPECIALBUILD */ | ");

    }
    
    buf.append('\n');
    
    writer.write(buf.toString());
    
    buf.setLength(0);
    
    buf.append("FILEFLAGS ");

    if (buf.length() > 10) {
    	
      buf.setLength(buf.length() - 3);
      
      buf.append('\n');
      
    }
    
    writer.write(buf.toString());
    
    buf.setLength(0);

    writer.write("FILEOS 0x40004 /* VOS_NT_WINDOWS32 */\nFILETYPE ");
    
    if (linkType.isExecutable()) {
    	
      writer.write("0x1L /* VFT_APP */\n");
      
    } else {
    	
      if (linkType.isSharedLibrary()) {
    	  
        writer.write("0x2L /* VFT_DLL */\n");
        
      } else if (linkType.isStaticLibrary()) {
    	  
        writer.write("0x7L /* VFT_STATIC_LIB */\n");
        
        writer.write("0x0L /* VFT_UNKNOWN */\n");
        
      }
    }
    
    writer.write("FILESUBTYPE 0x0L\n");
    
    writer.write("BEGIN\n");
    
    writer.write("BLOCK \"StringFileInfo\"\n");
    
    writer.write("   BEGIN\n#ifdef UNICODE\nBLOCK \"040904B0\"\n");
    
    writer.write("#else\nBLOCK \"040904E4\"\n#endif\n");
    
    writer.write("BEGIN\n");
    
    if (versionInfo.getFilecomments() != null) {
    	
      writer.write("VALUE \"Comments\", \"");
      
      writer.write(versionInfo.getFilecomments());
      
      writer.write(CONSTANT_0);
      
      writer.write("VALUE \"CompanyName\", \"");
      
      writer.write(versionInfo.getCompanyname());
      
      writer.write(CONSTANT_0);
      
      writer.write("VALUE \"FileDescription\", \"");
      
      writer.write(versionInfo.getFiledescription());
      
      writer.write(CONSTANT_0);
      
      writer.write("VALUE \"FileVersion\", \"");
      
      writer.write(versionInfo.getFileversion());
      
      writer.write(CONSTANT_0);
      
    }
    
    final String baseName = CUtil.getBasename(outputFile);
    
    String internalName = versionInfo.getInternalname();
    
    if (internalName == null) {
    	
      internalName = baseName;
      
    }
    
    writer.write("VALUE \"InternalName\", \"");
    
    writer.write(internalName);
    
    writer.write(CONSTANT_0);
    
    if (versionInfo.getLegalcopyright() != null) {
    	
      writer.write("VALUE \"LegalCopyright\", \"");
      
      writer.write(versionInfo.getLegalcopyright());
      
      writer.write(CONSTANT_0);
      
      writer.write("VALUE \"LegalTrademarks\", \"");
      
      writer.write(versionInfo.getLegaltrademarks());
      
      writer.write(CONSTANT_0);
      
    }
    
    writer.write("VALUE \"OriginalFilename\", \"");
    
    writer.write(baseName);
    
    writer.write(CONSTANT_0);
    
    if (versionInfo.getPrivatebuild() != null) {
    	
      writer.write("VALUE \"PrivateBuild\", \"");
      
      writer.write(versionInfo.getPrivatebuild());
      
      writer.write(CONSTANT_0);
      
      writer.write("VALUE \"ProductName\", \"");
      
      writer.write(versionInfo.getProductname());
      
      writer.write(CONSTANT_0);
      
      writer.write("VALUE \"ProductVersion\", \"");
      
      writer.write(versionInfo.getProductversion());
      
      writer.write(CONSTANT_0);
      
      writer.write("VALUE \"SpecialBuild\", \"");
      
      writer.write(versionInfo.getSpecialbuild());
      
      writer.write(CONSTANT_0);
      
    }
    
    writer.write(END);
    
    writer.write(END);

    writer.write("BLOCK \"VarFileInfo\"\n");
    
    writer.write("BEGIN\n#ifdef UNICODE\n");
    
    writer.write("VALUE \"Translation\", 0x409, 1200\n");
    
    writer.write("#else\n");
    
    writer.write("VALUE \"Translation\", 0x409, 1252\n");
    
    writer.write("#endif\n");
    
    writer.write(END);
    
    writer.write(END);
    
  }

  
  /**
   * Constructor.
   */
  private WindowsPlatform() {
	  
  }

}
