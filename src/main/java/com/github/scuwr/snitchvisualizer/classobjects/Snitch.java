package com.github.scuwr.snitchvisualizer.classobjects;

import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

public class Snitch implements Comparable<Snitch>{
	public int x;
	public int y;
	public int z;
	public int fieldMinX;
	public int fieldMinY;
	public int fieldMinZ;
	public int fieldMaxX;
	public int fieldMaxY;
	public int fieldMaxZ;
	public Date cullTime;
	public String ctGroup;
	public String type;
	public static int HOURS_IN_MILLIS = 3600000;

	public Snitch(int x, int y, int z, double cullTime, String ctGroup, String type){
		this.x = x;
		this.y = y;
		this.z = z;
		this.fieldMinX = x - 11;
		this.fieldMinY = y - 11;
		this.fieldMinZ = z - 11;
		this.fieldMaxX = x + 11;
		this.fieldMaxY = y + 11;
		this.fieldMaxZ = z + 11;
		this.cullTime = changeToDate(cullTime);
		this.ctGroup = ctGroup;
		if(type == null){
			if(cullTime > 336) this.type = "Alert";
			else this.type = "Snitch";
		}else this.type = type;
	}

	@Override
	public int compareTo(Snitch n){
		if(this.x > n.x) return 1;
		else if(n.x > this.x) return -1;
		else{
			if(this.z > n.z) return 1;
			else if(n.z > this.z) return -1;
			else{
				if(this.y > n.y) return 1;
				else if(n.y > this.y) return -1;
			}
		}
		return 0;
	}
	
	public double getDistance(){
		EntityClientPlayerMP thePlayer = Minecraft.getMinecraft().thePlayer;
		return Math.sqrt(Math.pow((thePlayer.posX - this.x), 2) + Math.pow((thePlayer.posZ - this.z), 2));
	}
	
	public static Date changeToDate(Double d){
		Date oldDate = new Date();
		return new Date((long)((oldDate.getTime() + (d * HOURS_IN_MILLIS))));
	}
	
	public double hoursToDate(){
		Date oldDate = new Date();
		return (this.cullTime.getTime() - oldDate.getTime()) / HOURS_IN_MILLIS;
	}
}
