package com.github.scuwr.snitchvisualizer.classobjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * Lightweight representation of a block manipulation as reported by a snitch.
 * 
 * @author Scuwr
 *
 */
public class Block {
	public int x;
	public int y;
	public int z;
	public int type;
	public String playerName;
	public String details;
	
	/**
	 * TODO: Replace with Enumeration
	 */
	public final static int USED = 1;
	public final static int REMOVED = 2;
	public final static int PLACED = 3;
	public final static int ENTRY = 4;
	
	/**
	 * Creates a new block interaction record
	 * 
	 * @param x X coord location of interaction
	 * @param y Y coord location of interaction
	 * @param z Z coord location of interaction
	 * @param t type of interaction
	 * @param playerName The name of the player as reported
	 * @param details Details of the interaction
	 */
	public Block(int x, int y, int z, int t, String playerName, String details){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = t;
		this.playerName = playerName;
		this.details = details;
	}
	
	/**
	 * Quick check of distance from the interaction event to the player.
	 * 
	 * @return the distance (Cartesian) of the block interaction from the player.
	 */
	public double getDistance(){
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		return Math.sqrt(Math.pow((thePlayer.posX - this.x), 2) + Math.pow((thePlayer.posZ - this.z), 2));
	}
}
