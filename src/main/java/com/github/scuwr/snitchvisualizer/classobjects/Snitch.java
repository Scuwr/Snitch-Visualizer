package com.github.scuwr.snitchvisualizer.classobjects;

import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * Defines the Snitch object for use by the mod
 * 
 * @author Scuwr
 *
 */
public class Snitch implements Comparable<Snitch> {
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
	public String name;
	public static int HOURS_IN_MILLIS = 3600000;

	/**
	 * Initializes Snitch object
	 * 
	 * @param x
	 *            location of Snitch object on x axis
	 * @param y
	 *            location of Snitch object on y axis
	 * @param z
	 *            location of Snitch object on z axis
	 * @param cullTime
	 *            length in time before a Snitch object is culled
	 * @param ctGroup
	 *            group that owns the Snitch object
	 * @param name
	 *            Snitch object now have names!
	 */
	public Snitch(int x, int y, int z, double cullTime, String ctGroup,
			String name) {
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
		if (name == null || name.equals("Alert") || name.equals("Snitch")
				|| name.equals("Unkown")) {
			this.name = "Unknown";
		} else {
			this.name = name;
		}
	}

	/**
	 * Defines how to compare one Snitch object against another
	 */
	@Override
	public int compareTo(Snitch n) {
		if (this.x > n.x) {
			return 1;
		} else if (n.x > this.x) {
			return -1;
		} else {
			if (this.z > n.z) {
				return 1;
			} else if (n.z > this.z) {
				return -1;
			} else {
				if (this.y > n.y) {
					return 1;
				} else if (n.y > this.y) {
					return -1;
				}
			}
		}
		return 0;
	}

	/**
	 * Gets the distance from the player to the Snitch object
	 * 
	 * @return distance from player to Snitch object
	 */
	public double getDistance() {
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
		return Math.sqrt(Math.pow((thePlayer.posX - this.x), 2)
				+ Math.pow((thePlayer.posZ - this.z), 2));
	}

	/**
	 * Converts hours to milliseconds in the future from the current date
	 * 
	 * Used to specify the cullTime of the Snitch object
	 * 
	 * @param d
	 *            time in hours
	 * @return date in milliseconds
	 */
	public static Date changeToDate(Double d) {
		Date oldDate = new Date();
		return new Date((long) ((oldDate.getTime() + (d * HOURS_IN_MILLIS))));
	}

	/**
	 * Computes the amount of hours remaining until cullTime
	 * 
	 * @return hours until Snitch cullTime
	 */
	public double hoursToDate() {
		Date oldDate = new Date();
		return (this.cullTime.getTime() - oldDate.getTime()) / HOURS_IN_MILLIS;
	}
}
