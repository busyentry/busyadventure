/**
 * Implements Converter interface for Zip file extraction
 * (ref: http://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java)
 * 
 * @author ray@busyentry.com
 * 
 */

package com.busyadventure.converters;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.busylib.utility.Utility;

public class ConverterZip implements Converter {

	private static final int BUFFER_SIZE = 4096;
	
	@Override
	public Map<String, String> convert(String file) {
		
		Map<String, String> result = new HashMap<>();
		
		try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(file))) {
			
			ZipEntry entry = zipIn.getNextEntry();
	        
	        // iterates over entries in the zip file
	        while (entry != null) {
	            
	            if (!entry.isDirectory()) {
	                // if the entry is a file, read it
	            	readFile(zipIn, entry.getName(), result);
	            }
	            
	            zipIn.closeEntry();
	            
	            // get next Entry in the zip file
	            entry = zipIn.getNextEntry();
	         }
	        
	        zipIn.close();
		} catch (Exception ex) {
			// do nothing for now
		}
	
        return result;
	}
	
	private void readFile(ZipInputStream zipIn, String fileName, Map<String, String> result) {
		
		if(Utility.getFileType(fileName).equals("text/plain")) {
			StringBuilder sb = new StringBuilder();
			
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int read = 0;
	        
	        try {
				while ((read = zipIn.read(buffer)) != -1) {
				    sb.append(new String(buffer, 0, read));
				}
			} catch (IOException e) {} // do nothing for now
	        
	        result.put(fileName, sb.toString());
	 	}
	}
}
