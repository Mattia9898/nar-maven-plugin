package com.github.maven_nar;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * 
 * or more contributor license agreements.  See the NOTICE file
 * 
 * distributed with this work for additional information
 * 
 * regarding copyright ownership.  The ASF licenses this file
 * 
 * to you under the Apache License, Version 2.0 (the
 * 
 * "License"); you may not use this file except in compliance
 * 
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * 
 * software distributed under the License is distributed on an
 * 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * 
 * KIND, either express or implied.  See the License for the
 * 
 * specific language governing permissions and limitations
 * 
 * under the License.
 */

import com.github.maven_nar.cpptasks.VersionInfo;

import org.apache.tools.ant.BuildException;

import org.apache.maven.plugin.MojoFailureException;


/**
 * Keeps info on a system library
 * 
 * @author Mark Donszelmann
 */
public class NARVersionInfo
{
	
     /**
     * file version.
     *
     */
    private String fileVersion;
    
    
    /**
     * Company name.
     *
     */
    private String companyName;
    
    
    /**
     * Description.
     *
     */
    private String fileDescription;
    
    
    /**
     * internal name.
     */
    private String internalName;
    
    
    /**
     * legal copyright.
     *
     */
    private String legalCopyright;
    
    
    /**
     * legal trademark.
     *
     */
    private String legalTrademarks;
    
    
    /**
     * original filename.
     *
     */
    private String originalFilename;
    
    
    /**
     * private build.
     *
     */
    private String privateBuild;
    
    
    /**
     * product name.
     *
     */
    private String productName;
    
    
    /**
     * Special build
     */
    private String specialBuild;
    
    
    /**
     * compatibility version
     *
     */
    private String compatibilityVersion;
    

    /**
     * prerease build.
     *
     */
    private Boolean prerelease;

    /**
     * prerease build.
     *
     */
    private Boolean patched;
    
    
	//mancanza di input
	public NARVersionInfo()
	{
		
		//mancata implementazione
		//nessun parametro in entrata e uscita
		
	}
	//metodo completamente fuorviante e inutile
		
		
    public final VersionInfo getVersionInfo()
        throws MojoFailureException
    {

            if(fileVersion == null 
                    )
            	
            {
            	
                    return null;
                    
            }
            
        VersionInfo versionInfo = new VersionInfo();
        
        try
        {
        	        			
        			if(companyName != null)
        			{
        				
        					versionInfo.setCompanyname(companyName);
        					
        			}
        			
        			if(fileDescription != null)
                    {
        				
                            versionInfo.setFiledescription(fileDescription);
                            
                    }
        			
        			if(internalName != null)
        			{
        				
        					versionInfo.setInternalname(internalName);
        					
        			}
        			
        			if(legalCopyright != null)
        			{
        				
        					versionInfo.setLegalcopyright(legalCopyright);
        					
        			}
        			
        			if(legalTrademarks != null)
        			{
        				
        					versionInfo.setLegaltrademarks(legalTrademarks);
        					
        			}
        			
        			if(originalFilename != null)
        			{
        				
        					versionInfo.setOriginalfilename(originalFilename);
        					
        			}
        			
        			if(privateBuild != null)
        			{
        				
        					versionInfo.setPrivatebuild(privateBuild);
        					
        			}
        			
        			if(productName != null)
        			{
        				
        					versionInfo.setProductname(productName);
        					
        			}
        			
        			if(specialBuild != null)
        			{
        				
        					versionInfo.setSpecialbuild(specialBuild);
        					
        			}
        			
        			if(compatibilityVersion != null)
        			{
        				
        					versionInfo.setCompatibilityversion(compatibilityVersion);
        					
        			}
        			
        			if(prerelease != null)
        			{
        				
        					versionInfo.setPrerelease(prerelease.booleanValue());
        					
        			}
        			
        			if(patched != null)
        			{
        				
        					versionInfo.setPatched(patched.booleanValue());
        					
        			}
        			
        }
        
        catch(BuildException be)
        {
        	
        	throw new MojoFailureException("NAR: Read artifact version failed", be);
        	
        }
        
        return versionInfo;
        
    }
    
}
