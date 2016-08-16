/**
 * Defines interface for Converters.
 * 
 * @author ray@busyentry.com
 * 
 */

package com.busyadventure.converters;

import java.util.Map;

public interface Converter {
	/**
	 * Converts a file to HashMap
	 * 
	 * @param file Name of the file
	 * @return HashMap with key = file name and value = file content
	 * @throws Exception 
	 */
	public Map<String, String> convert(String file) throws Exception;
}
