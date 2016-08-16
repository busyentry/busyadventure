/**
 * WordCount service
 * 
 * @author ray@busyentry.com
 * 
 */

package com.busyadventure.wordcount;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.busyadventure.converters.Converter;
import com.busylib.common.ServiceBase;
import com.busylib.common.ServiceInterface;
import com.busylib.utility.Utility;

public class WordCount extends ServiceBase implements ServiceInterface {

	////////////////////////////////////////////////////
	// Spring injected vars  
	@Autowired private Converter converter = null;
	private String inputFolder; 
	private String outputFile;
	//
	//////////////////////////////////////////////////
	
	public void setInputFolder(String value) {
		this.inputFolder = new File(value).getAbsolutePath();
	}
	
	public void setOutputFile(String value) {
		this.outputFile = new File(value).getAbsolutePath();
	}

	/**
	 * Run the service
	 */
	@Override
	public void run() {

		getLogger().info("Start!");
		
		try {
			
			// the variable stores the final word counts
			Map<String, Integer> result = new TreeMap<>(); 
			
			// counting the words
			countWords(inputFolder, result);
			
			// output the result
			outputResult(result);
			
		} catch (Exception ex) {
			getExceptionHandler().onException(ex);
		}
		
		getLogger().info("Complete!");
	}
	
	/**
	 * Recursively count words in given text
	 * 
	 * @param file : the file or directory name
	 * @param result
	 * @throws Exception 
	 */
	private void countWords(String file, Map<String, Integer> result) throws Exception {
		
		String fileType = Utility.getFileType(file);
		
		// 3 cases here:
		
		// case 1: for text file - read entire file then count
		if(fileType.startsWith("text")) {
			// read entire content from the text file and count
			List<String> text = null;
			
			try {
				text = Files.readAllLines(Paths.get(file));
			} catch (Exception ex){}
			
			if(text != null) {
				for(String line : text) {
					countStringWords(line, result);
				}
			}
		}
		
		// case 2: for a directory, read all the files in directory then call this method again
		else if(fileType == "dir") {
			List<String> files = new ArrayList<String>(); // stores all the files
			
			Utility.getAllFiles(files, Paths.get(inputFolder));
			
			int size = files.size();
			
			for(int i=0;i<size;i++) {
				String fileName = files.get(i);
				String type = Utility.getFileType(fileName);
							
				getLogger().info(String.format("processing %d of %d: \"%s\" %s", i+1, size, type, fileName));
				
				countWords(fileName, result);
			}
		} 
		
		// case 3: for a zip file, read all the files in the zip file then call the countStringWords
		else if(fileType.indexOf("zip") != -1) {
			Map<String, String> zipContent = converter.convert(file);
			Set<String> files = zipContent.keySet();
			int size = files.size();
			int count = 0;
			
			for(String fileInZip : files) {
				String text = zipContent.get(fileInZip);
							
				getLogger().info(String.format("processing %d of %d: %s - %s", ++count, size, file, fileInZip));
				
				countStringWords(text, result);
			}
		}
	}
	
	/**
	 * Count words from given text 
	 * 
	 * @param text
	 * @param result
	 */
	private void countStringWords(String text, Map<String, Integer> result) {
		
		// the Pattern still need future development
		Pattern p = Pattern.compile("[\\w']+");
		Matcher m = p.matcher(text);

		while ( m.find() ) {
			String word = text.substring(m.start(), m.end());
			
			// increase the counter if the word exists in the hashMap
		    if(result.containsKey(word)) {
		    	Integer count = result.get(word);
		    	result.put(word, ++count);
		    } else { // otherwise add the word to the hashMap
		    	result.put(word, 1);
		    }
		}
	}
	
	/**
	 * Output the result (future development could include RESTful service datafeed, JSON format as well as charting/reporting
	 * 
	 * @param result
	 * @throws IOException
	 */
	private void outputResult( Map<String, Integer> result) throws IOException {
		
		getLogger().info("Writting to the output file...");
		
		File file = new File(outputFile);
		
		// delete the existing result file
		if(file.exists()) {
			file.delete();
		}
		
		// write to the final result file
		try( BufferedWriter writer = new BufferedWriter( new FileWriter( outputFile))) {
			for(String key : result.keySet()) {
				writer.write(String.format("%s %s%s", key, result.get(key), System.lineSeparator()));
			}
		}
		catch ( IOException e)	{
			throw e;
		}
		
		getLogger().info(String.format("Result is written to output file: %s", outputFile));
	}
}
