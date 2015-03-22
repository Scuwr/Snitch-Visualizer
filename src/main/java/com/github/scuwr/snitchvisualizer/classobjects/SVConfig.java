package com.github.scuwr.snitchvisualizer.classobjects;

/**
 * Contains methods pertaining the config and config options
 * 
 * @author Scuwr
 *
 */
public class SVConfig {
	
	public static boolean rendering;
	public static boolean updateDetection;
	
	/**
	 * Change plugin settings
	 * 
	 * @param rendering enable snitch rendering on-screen
	 * @param updateDetection enable auto-update when a players moves through the snitch
	 */
	public SVConfig(boolean rendering, boolean updateDetection){
		this.rendering = rendering;
		this.updateDetection = updateDetection;
	}
}
