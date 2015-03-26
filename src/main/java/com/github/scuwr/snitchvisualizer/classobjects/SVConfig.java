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
	public static int renderDistance;
	public static int keyBinding;
	
	/**
	 * Change plugin settings
	 * 
	 * @param rendering enable snitch rendering on-screen
	 * @param updateDetection enable auto-update when a players moves through the snitch
	 */
	public SVConfig(boolean rendering, boolean updateDetection, int renderDistance, int keyBinding){
		this.rendering = rendering;
		this.updateDetection = updateDetection;
		this.keyBinding = keyBinding;
		
		if(renderDistance < 1){
			this.renderDistance = 6;
		}
		else {
			this.renderDistance = renderDistance;
		}
	}
}
