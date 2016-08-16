/**
 * This class provides common functions
 * 
 * @author ray@busyentry.com
 * 
 */

package com.busylib.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.busylib.common.ServiceBase;

public class Utility extends ServiceBase {

	/**
	 * Collect all the files in a given path recursively 
	 * 
	 * @param fileNames
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public static List<String> getAllFiles(List<String> fileNames, Path path) throws IOException {
		
	    try(DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
	        for (Path subPath : stream) {
	            if(subPath.toFile().isDirectory()) {
	            	getAllFiles(fileNames, subPath);
	            } else {
	            	String fileName = subPath.toAbsolutePath().toString();
	            	
	            	//getLogger().info(String.format("File type: %s - %s", getFileType(fileName), fileName));
	            	
	                fileNames.add(fileName);
	            }
	        }
	    } catch(IOException e) {
	        throw e;
	    }
	    
	    return fileNames;
	} 
	
	/**
	 * Determine a file type 
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName) {
	   String fileType = null;
	   File file = new File(fileName);
	   
	   try {
		   
		    // first check if it's a directory
		    if(file.isDirectory()) return "dir";
		    
		    // if it's not a directory, we will try find out the file type.. 
		   	fileType = Files.probeContentType(file.toPath());
	      
		    if(fileType == null) {
	    	  // FUTURE DEVELOPMENT is needed here
	    	  int index = fileName.lastIndexOf('.');
	    	  
	    	  if (index >= 0) {
	    		 fileType = fileName.substring(index + 1);
	    		  
	    		 if(fileType.toLowerCase().equals("zip") || fileType.toLowerCase().equals("gz")) {
	    			 fileType = "application/zip";
	    		 } else if(fileType.toLowerCase().equals("txt")) {
	    			 fileType = "text/plain";
	    		 }
	    	  } else {
	    		  fileType = "text/plain";
	    	  }
		   }
	   }
	   catch (IOException ioException) {
		   // do nothing for now
	   }
	   
	   return fileType;
	}
	
	public static File writeTextToFile(String fileUri, String text) throws Exception {
		return writeTextToFile(fileUri, text, false, false);
	}
		
	public static File writeTextToFile(String fileUri, String text, Boolean checkExistingFile, Boolean appendToExistingFile) throws Exception {
		File file = null;
		
		try	{
			file = new File(fileUri); 
		} 
		catch (Exception e) {
			throw e;
        } 
		
		return writeTextToFile(file, text, checkExistingFile, appendToExistingFile);
	}
		
	public static File writeTextToFile(File file, String text, Boolean checkExistingFile, Boolean appendToExistingFile) throws Exception {
		BufferedWriter output = null;
		
        try {
          
        	if(file.exists() && checkExistingFile) {
       		  return null;
        	}
          
        	output = new BufferedWriter(new FileWriter(file, appendToExistingFile));
        	output.write(text);
        	
        } catch ( Exception e ) {
        } finally {
        	try {
        		if(output != null) output.close();
        	} 
        	catch (Exception ex) {
        		throw ex;
        	}
        }
        
        return file;
	}
}
