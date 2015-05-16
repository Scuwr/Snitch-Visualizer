package com.github.scuwr.snitchvisualizer.classobjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Block {
	public int x;
	public int y;
	public int z;
	public int type;
	public String playerName;
	public String details;
	
	public final static int USED = 1;
	public final static int REMOVED = 2;
	public final static int PLACED = 3;
	public final static int ENTRY = 4;
	
	public Block(int x, int y, int z, int t, String playerName, String details){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = t;
		this.playerName = playerName;
		this.details = details;
	}
	
	public double getDistance(){
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		return Math.sqrt(Math.pow((thePlayer.posX - this.x), 2) + Math.pow((thePlayer.posZ - this.z), 2));
	}
}
