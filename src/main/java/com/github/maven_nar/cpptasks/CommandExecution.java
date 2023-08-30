package com.github.maven_nar.cpptasks;

import org.apache.tools.ant.Project;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.compiler.Linker;

import com.github.maven_nar.cpptasks.compiler.ProcessorConfiguration;

import java.io.*;

import java.util.List;


class StreamGobbler extends Thread {
	
    InputStream is;
    
    String type;
    
    CCTask task;


    StreamGobbler(InputStream is, String type, CCTask task) {
    	
        this.is = is;
        
        this.type = type;
        
        this.task = task;
        
    }

    
    @Override
    public void run() {
    	
        try {
        	
            InputStreamReader isr = new InputStreamReader(is);
            
            BufferedReader br = new BufferedReader(isr);
            
            String line;
            
            while ((line = br.readLine()) != null)
            	
                task.log(type +">"+ line );

        } catch (Exception e) {
        	
        	throw new NullPointerException();
        	
        }

    }
    
}


public class CommandExecution {

	public static final ProcessorConfiguration linkerConfig = null;

	public static final Linker selectedLinker = null;

	public static final TargetDef targetPlatform = LinkerParam.getTargetPlatform();

	public static final FileVisitor objCollector = null;

	public static final FileVisitor sysLibraryCollector = null;

	private CommandExecution() {}

    public static int runCommand(String[] cmdArgs, File workDir, CCTask task, List<Environment.Variable> env) throws  IOException{


        try {

            //Create ProcessBuilder with the command arguments
            ProcessBuilder pb = new ProcessBuilder(cmdArgs);

            //Redirect the stderr to the stdout
            pb.redirectErrorStream(true);

            pb.directory(workDir);

            for (Environment.Variable myVariable:env) {
            	
                pb.environment().put(myVariable.getKey(), myVariable.getValue());
                
                task.log("Environment variable: " + myVariable.getKey() + "=" + myVariable.getValue(), Project.MSG_VERBOSE);
                
            }

            //Start the new process
            Process process = pb.start();


            // Adding to log the command
            StringBuilder builder = new StringBuilder();
            
            for(String s : cmdArgs) {

                builder.append(s);
                
                //Append space
                builder.append(" ");
                
            }
            
            task.log("Executing - " + builder.toString(), task.getCommandLogLevel());


            //Create the StreamGobbler to read the process output
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT",task);

            outputGobbler.start();

            int exitValue;

            //Wait for the process to finish
            exitValue = process.waitFor();


            return exitValue;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            
            return -2;
            
        }

    }
    
    
    public ProcessorConfiguration getLinkerconfig() {
    	
		return linkerConfig;
		
	}
    

	public Linker getSelectedlinker() {
		
		return selectedLinker;
		
	}
	

	public TargetDef getTargetplatform() {
		
		return targetPlatform;
		
	}
	

	public FileVisitor getObjcollector() {
		
		return objCollector;
		
	}
	

	public FileVisitor getSyslibrarycollector() {
		
		return sysLibraryCollector;
		
	}

}
