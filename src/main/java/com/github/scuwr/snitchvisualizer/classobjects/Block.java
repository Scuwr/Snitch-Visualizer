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
	private int x;
	private int y;
	private int z;
	private Action type;
	private String playerName;
	private String details;

	public enum Action {
		NOP, USED, REMOVED, PLACED, ENTRY, EMPTY, LOGIN, LOGOUT, EXCHANGE, DESTROYED;
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

	public final int getX() {
		return x;
	}

	public final void setX(int x) {
		this.x = x;
	}

	public final int getY() {
		return y;
	}

	public final void setY(int y) {
		this.y = y;
	}

	public final int getZ() {
		return z;
	}

	public final void setZ(int z) {
		this.z = z;
	}

	public final Action getType() {
		return type;
	}

	public final void setType(Action type) {
		this.type = type;
	}

	public final String getPlayerName() {
		return playerName;
	}

	public final void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public final String getDetails() {
		return details;
	}

	public final void setDetails(String details) {
		this.details = details;
	}
}
