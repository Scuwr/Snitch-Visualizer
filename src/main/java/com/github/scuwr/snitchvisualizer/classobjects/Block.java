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
	public Action type;
	public String playerName;
	public String details;

	public enum Action {
		NOP, USED, REMOVED, PLACED, ENTRY;
	}

	/**
	 * Creates a new block interaction record
	 * 
	 * @param x
	 *            X coord location of interaction
	 * @param y
	 *            Y coord location of interaction
	 * @param z
	 *            Z coord location of interaction
	 * @param t
	 *            type of interaction
	 * @param playerName
	 *            The name of the player as reported
	 * @param details
	 *            Details of the interaction
	 */
	public Block(int x, int y, int z, Action t, String playerName,
			String details) {
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
	 * @return the distance (Cartesian) of the block interaction from the
	 *         player.
	 */
	public double getDistance() {
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		return Math.sqrt(Math.pow((thePlayer.posX - this.x), 2)
				+ Math.pow((thePlayer.posZ - this.z), 2));
	}
}
