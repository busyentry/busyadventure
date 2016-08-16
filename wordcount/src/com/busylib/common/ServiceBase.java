/**
 * A base class providing some generic needs such as Logging and Exception handling
 * 
 * @author ray@busyentry.com
 * 
 */
package com.busylib.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServiceBase {
	
	private static Logger log;
	private String serviceId;
	public boolean enabled = false;
	@Autowired private ExceptionHandler exceptionHandler;
	
	public ServiceBase() {
		log = Logger.getLogger(this.getClass());
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		getLogger().info(getClass().getName() + " is " + (enabled ? "Enabled" : "Disabled"));
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	
	public static Logger getLogger() {
		return log;
	}
}
