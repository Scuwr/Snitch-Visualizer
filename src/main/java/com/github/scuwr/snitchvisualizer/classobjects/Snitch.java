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
	private String world;
	private int x;
	private int y;
	private int z;
	private int fieldMinX;
	private int fieldMinY;
	private int fieldMinZ;
	private int fieldMaxX;
	private int fieldMaxY;
	private int fieldMaxZ;
	private Date cullTime;
	private String ctGroup;
	private String name;
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
		this("world", x, y, z, cullTime, ctGroup, name);
	}

	/**
	 * Initializes Snitch object
	 * 
	 * @param world
	 *            world of Snitch object (name)
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
	public Snitch(String world, int x, int y, int z, double cullTime, 
			String ctGroup, String name) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.fieldMinX = x - 11;
		this.fieldMinY = y - 11;
		this.fieldMinZ = z - 11;
		this.fieldMaxX = x + 11;
		this.fieldMaxY = y + 11;
		this.fieldMaxZ = z + 11;
		this.setCullTime(cullTime);
		this.ctGroup = ctGroup;
		if (name == null || name.equals("Alert") || name.equals("Snitch")
				|| name.equals("Unkown")) {
			this.name = "Unknown";
		} else {
			this.name = name;
		}
	}

	public void setCullTime(double cullTime) {
		this.cullTime = (cullTime < 0) ? null : changeToDate(cullTime);
	}

	public double getCullTime() {
		return hoursToDate();
	}
	
	public void setRawCullTime(Date cullTime) {
		this.cullTime = cullTime;
	}
	
	public Date getRawCullTime() {
		return this.cullTime;
	}
	

	/**
	 * Defines how to compare one Snitch object against another
	 */
	@Override
	public int compareTo(Snitch n) {
		if (this.world.compareTo(n.world) != 0) {
			return this.world.compareTo(n.world);
		} 
		if (this.x > n.x) {
			return 1;
		} else if (n.x > this.x) {
			return -1;
		}
		if (this.z > n.z) {
			return 1;
		} else if (n.z > this.z) {
			return -1;
		}
		if (this.y > n.y) {
			return 1;
		} else if (n.y > this.y) {
			return -1;
		}
		return 0;
	}
	
	public boolean contains(String world, int x, int y, int z) {
		return world.equals(getWorld()) && x >= getFieldMinX() && x <= getFieldMaxX() && z >= getFieldMinZ() && z <= getFieldMaxZ() && 
				y >= getFieldMinY() && y <= getFieldMaxY();
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
		if (this.cullTime == null) return 200;
		Date oldDate = new Date();
		return (this.cullTime.getTime() - oldDate.getTime()) / HOURS_IN_MILLIS;
	}

	public final String getWorld() {
		return world;
	}

	public final void setWorld(String world) {
		this.world = world;
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

	public final int getFieldMinX() {
		return fieldMinX;
	}

	public final void setFieldMinX(int fieldMinX) {
		this.fieldMinX = fieldMinX;
	}

	public final int getFieldMinY() {
		return fieldMinY;
	}

	public final void setFieldMinY(int fieldMinY) {
		this.fieldMinY = fieldMinY;
	}

	public final int getFieldMinZ() {
		return fieldMinZ;
	}

	public final void setFieldMinZ(int fieldMinZ) {
		this.fieldMinZ = fieldMinZ;
	}

	public final int getFieldMaxX() {
		return fieldMaxX;
	}

	public final void setFieldMaxX(int fieldMaxX) {
		this.fieldMaxX = fieldMaxX;
	}

	public final int getFieldMaxY() {
		return fieldMaxY;
	}

	public final void setFieldMaxY(int fieldMaxY) {
		this.fieldMaxY = fieldMaxY;
	}

	public final int getFieldMaxZ() {
		return fieldMaxZ;
	}

	public final void setFieldMaxZ(int fieldMaxZ) {
		this.fieldMaxZ = fieldMaxZ;
	}

	public final String getCtGroup() {
		return ctGroup;
	}

	public final void setCtGroup(String ctGroup) {
		this.ctGroup = ctGroup;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}
}
