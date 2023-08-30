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

import java.util.List;

import com.github.maven_nar.cpptasks.compiler.LinkType;

import com.github.maven_nar.cpptasks.compiler.Linker;


/**
 * A add-in class for Microsoft Developer Studio processors
 *
 * 
 */
public class MsvcProcessor {
	
  public static void addWarningSwitch(final List<String> args, final int level) {
	  
    switch (level) {
    
      case 0:
    	  
        args.add("/W0");
        
        break;
        
      case 1:
    	  
        args.add("/W1");
        
        break;
        
      case 2:
    	  
        break;
        
      case 3:
    	  
        args.add("/W3");
        
        break;
        
      case 4:
    	  
        args.add("/W4");
        
        break;
        
      case 5:
    	  
        args.add("/WX");
        
        break;
        
      default: 
    	  
    	throw new IllegalArgumentException("unreachable");
    	
    }
    
  }
  
  
  public static String getCommandFileSwitch(final String cmdFile) {
	  
    final StringBuilder buf = new StringBuilder("@");
    
    if (cmdFile.indexOf(' ') >= 0) {
    	
      buf.append('\"');
      
      buf.append(cmdFile.replace('/', '\\'));
      
      buf.append('\"');
      
    } else {
    	
      buf.append(cmdFile);
      
    }
    
    return buf.toString();
    
  }
  

  public static void getDefineSwitch(final StringBuilder buffer, final String define, final String value) {
	  
    buffer.append("/D");
    
    buffer.append(define);
    
    if (value != null && value.length() > 0) {
    	
      buffer.append('=');
      
      buffer.append(value);
      
    }
    
  }
  

  public static String getIncludeDirSwitch(final String includeDir) {
	  
    return "/I" + includeDir.replace('/', '\\');
    
  }
  

  public static String[] getOutputFileSwitch(final String outPath) {
	  
    final StringBuilder buf = new StringBuilder("/Fo");
    
    if (outPath.indexOf(' ') >= 0) {
    	
      buf.append('\"');
      
      buf.append(outPath);
      
      buf.append('\"');
      
    } else {
    	
      buf.append(outPath);
      
    }
    
    return new String[] {
      buf.toString()
    };

  }

  
  public static void getUndefineSwitch(final StringBuilder buffer, final String define) {
	  
    buffer.append("/U");
    
    buffer.append(define);
    
  }

  
  private MsvcProcessor() {
	  
  }
  
  
  public Linker getLinker(final LinkType type) {
	  
	return MsvcLinker.getInstance().getLinker(type);
	    
  }
	  
}
