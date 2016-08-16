/**
 * Entry point of the app
 * 
 * @author ray@busyentry.com
 * 
 */

package com.busyadventure.wordcount;

import java.io.File;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class App {  

	public static final String NAME ="busyadventure.wordcount";
		
	public static void main(String[] args) { 
		// validate inputs
		String inputFolder = null;
		String outputFile = null;
		
		if(args != null && args.length > 0) {
			File file = new File(args[0]);
			
			// check if the input folder exists
			if(file.exists() && file.isDirectory()) {
				inputFolder = file.getAbsolutePath();
			}

			// check if the output file exists or not. If not, we will check if the path is valid or not
			if(args.length > 1) {
				file = new File(args[1]);
				
				if(file.exists()) {
					outputFile = file.getAbsolutePath();
				} else {
					String path = file.getAbsolutePath();
					path = path.substring(0, path.lastIndexOf(File.separator));
					
					File filePath = new File(path);
					
					if(filePath.exists() && filePath.isDirectory()) {
						outputFile = file.getAbsolutePath();
					}
				}
			}
		}
		
		// start process
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring/*.xml" })) {
			
			WordCount service = (WordCount) context.getBean("wordCount");
			
			if(inputFolder != null) service.setInputFolder(inputFolder);
			if(outputFile != null) service.setOutputFile(outputFile);
			
			// kick off the process
			service.run();
			
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
	}
}
