/**
 * Custom Exception handler
 * 
 * @author ray@busyentry.com
 * 
 */

package com.busylib.common;

import org.apache.log4j.Logger;

public class ExceptionHandler {
	
	private Logger log;
	
	public ExceptionHandler() {
		log = Logger.getLogger(this.getClass());
	}
	
	public Logger getLogger() {
		return log;
	}
	
	public void onException(java.lang.Exception ex) {	
		
		onException(ex, null);
	}
	
	public void onException(java.lang.Exception ex, String errorString) {	
		
		this.onException(ex, errorString, false);
	}

	public void onException(java.lang.Exception ex, String errorString, boolean sendEmail) {	
		
		String data = "";
		
		if (errorString != null && !errorString.isEmpty())
		{
			data = errorString;
			data +=	System.lineSeparator();
		}
	
		data +=	getData(ex);	
		
		handleException(ex, data, sendEmail);
	}

	private String getData(Throwable ex) {

		Throwable currentException = ex;
		StringBuilder sb = new StringBuilder();
		
		while (currentException != null) {
			if (currentException instanceof Exception) {
				if (sb.length() > 0) sb.append(" ");
				sb.append(((Exception)currentException).getMessage());
			}
			
			currentException = currentException.getCause();
		}
		
		return sb.toString();
	}
	
	private void handleException(java.lang.Exception ex, String data, boolean sendEmail) {
		
		try {			
		
			/**
			 *	custom error handling logic goes here 
			 */
			
			getLogger().error(data);
			
			throw ex;
			
		} catch (java.lang.Exception e) {
			log.error("ExceptionHandler could not log exception.", e);
		}
		
		if (sendEmail) {
			try {
				//sendExceptionEmail(ex, data);			
			} catch (java.lang.Exception e) {
				log.error("ExceptionHandler could not send exception email.", e);
			}
		}
	}
 }
