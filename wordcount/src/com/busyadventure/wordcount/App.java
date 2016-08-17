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

	//private static Logger log = Logger.getLogger(App.class);
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
			} else {
				System.out.println("Invalid input directory!");
				System.exit(1);
			}

			// check if the output file exists or not. If not, we will at least check if the path is valid or not
			if(args.length > 1) {
				file = new File(args[1]);
				
				if(file.exists()) { // the file exists
					outputFile = file.getAbsolutePath();
				} else { // check the path
					String path = file.getAbsolutePath();
					path = path.substring(0, path.lastIndexOf(File.separator));
					
					File filePath = new File(path);
					
					if(filePath.exists() && filePath.isDirectory()) {
						outputFile = file.getAbsolutePath();
					} else {
						System.out.println("Invalid output file directory!");
						System.exit(1);
					}
				}
			}
		}
		
		// start process
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring/application.xml" })) {
			
			WordCount service = (WordCount) context.getBean("wordCount");
			
			// set inputFolder passed from arguments
			// If user didn't supply the argument, a default inputFolder is defined in resources/spring/application.xml
			if(inputFolder != null) service.setInputFolder(inputFolder);
			
			// set inputFolder passed from arguments
			// If user didn't supply the argument, a default outputFile is defined in resources/spring/application.xml
			if(outputFile != null) service.setOutputFile(outputFile);
			
			// kick off the process
			service.run();
			
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
	}
}