package com.github.scuwr.snitchvisualizer.handlers;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

/**
 * Player Handler for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
public class SVPlayerHandler {

	private static Logger logger = LogManager.getLogger("SnitchVisualizer");

	public static int snitchIndex = -1;
	public static boolean updateSnitchName = false;

	private static boolean playerIsInSnitchArea = false;
	
	private long lastExecute = 0l;
	private static long delay = 50l;

	@SubscribeEvent
	public void onPlayerEvent(ClientTickEvent event) {
		if (!SV.settings.updateDetection) return;
		if (lastExecute != 0l && ((System.currentTimeMillis() - lastExecute) < SVPlayerHandler.delay)) return; 
		try {
			logger.info("Captured player event");
			lastExecute = System.currentTimeMillis();
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player != null) {
				if (Math.floor(player.prevPosX) != Math.floor(player.posX)
						|| Math.floor(player.prevPosY) != Math.floor(player.posY)
						|| Math.floor(player.prevPosZ) != Math.floor(player.posZ)) {
					player.prevPosX = player.posX;
					player.prevPosY = player.posY;
					player.prevPosZ = player.posZ;
					onPlayerMove(player);
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error during client tick event handler for player movement.", e);
		}
	}

	public void onPlayerMove(EntityPlayerSP player) {
		try {
			logger.info("Current world: " + player.worldObj.getProviderName());
			checkSnitchArea(player.worldObj.getProviderName(), (int) Math.floor(player.posX), (int) Math.floor(player.posY) - 1,
					(int) Math.floor(player.posZ), SV.instance.snitchList, false);
		} catch (Exception e) {
			logger.error("Unexpected error during player move handling", e);
		}
	}

	/**
	 * Snitch detection is now 100% accurate to my knowledge, and shows no signs
	 * of missing snitches.
	 * 
	 * Editorial TODO: It's less snitches are missed, and more snitches when
	 * overlapping are ignored. Instead of using static variables and public
	 * parameter passing, this should return a list of snitches the player is
	 * "intersecting". (PD 5/2015)
	 *
	 * @param x
	 *            pos
	 * @param y
	 *            pos
	 * @param z
	 *            pos
	 * @param snitchList
	 *            ArrayList
	 * @param removeSnitch
	 *            flag
	 */
	public static long searchChecks = 0l;
	public static void checkSnitchArea(String world, int x, int y, int z, ArrayList<Snitch> snitchList, boolean removeSnitch) {
		try {
			searchChecks = 0l;
			int min = findLowerWorldLimit(world, 0, snitchList.size() - 1, snitchList);
			int max = findUpperWorldLimit(world, min, snitchList.size() - 1, snitchList);
			min = findLowerXLimit(x, min, max, snitchList);
			max = findUpperXLimit(x, min, max, snitchList);
			min = findLowerYLimit(x, min, max, snitchList);
			max = findUpperYLimit(x, min, max, snitchList);
			
			logger.info("Performed " + searchChecks + " checks -- final bounds: " + min + ", " + max);
			logger.info("Total snitches: " + snitchList.size() + " -- searched " +
						Math.round(((double)(searchChecks + max-min+1) / (double)snitchList.size()) * 10000.0d)/100.0d + "% of total");
	
			int index = -1;
			double sqDistance = Double.MAX_VALUE;
	
			for (int i = min; i <= max; i++) {
				Snitch n = snitchList.get(i);
				if (x >= n.getFieldMinX() && x <= n.getFieldMaxX() && z >= n.getFieldMinZ() && z <= n.getFieldMaxZ() && 
						y >= n.getFieldMinY() && y <= n.getFieldMaxY()) {
					// Get closest snitch
					double temp = Minecraft.getMinecraft().thePlayer.getDistanceSq(n.getX(), n.getY(), n.getZ());
					if (temp < sqDistance) {
						sqDistance = temp;
						index = i;
					}
				}
			}
	
			if (index != -1) {
				snitchIndex = index;
				Snitch n = SV.instance.snitchList.get(index);
				n.setRawCullTime(Snitch.changeToDate(672.0));
				playerIsInSnitchArea = true;
				updateSnitchName = false;
				if (removeSnitch) {
					SV.instance.snitchList.remove(index);
					logger.info("Snitch Removed!");
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[" + SV.MODNAME
							+ "] You have just deleted a Snitch!"));
				} else if (n.getName().equals("Unknown")) {
					updateSnitchName = true;
				}
			} else if (playerIsInSnitchArea) {
				playerIsInSnitchArea = false;
				updateSnitchName = false;
				snitchIndex = -1;
				SVFileIOHandler.saveList();
			}
		} catch (Exception e) {
			logger.error("Failure while checking snitch area!", e);
		}
	}
	/*
	 * New strategy:
	 * 
	 * Find lower x limit, then find upper x limit, then test for distance from
	 * player to snitch
	 * 
	 * maybe use a map?
	 */

	private static int findUpperYLimit(int i, int min, int max, ArrayList<Snitch> snitchList) {
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) {
			return max;
		}
		Snitch n = snitchList.get(mid);

		searchChecks++;
		if (i > n.getFieldMinY()) {
			return findUpperYLimit(i, mid, max, snitchList);
		}
		searchChecks++;
		if (i < n.getFieldMinY()) {
			return findUpperYLimit(i, min, mid, snitchList);
		}
		int nmid = mid ++;
		while (i == snitchList.get(nmid).getFieldMinY()) {
			searchChecks++;
			nmid ++;
		}
		searchChecks++;
		mid = nmid --;

		return mid;
	}

	private static int findLowerYLimit(int i, int min, int max, ArrayList<Snitch> snitchList) {
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) {
			return min;
		}
		Snitch n = snitchList.get(mid);

		searchChecks++;
		if (i > n.getFieldMaxY()) {
			return findLowerYLimit(i, mid, max, snitchList);
		}
		searchChecks++;
		if (i < n.getFieldMaxY()) {
			return findLowerYLimit(i, min, mid, snitchList);
		}
		int nmid = mid --;
		while (i == snitchList.get(nmid).getFieldMaxY()) {
			searchChecks++;
			nmid --;
		}
		searchChecks++;
		mid = nmid ++;
		return mid;
	}

	private static int findUpperXLimit(int i, int min, int max, ArrayList<Snitch> snitchList) {
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) {
			return max;
		}
		Snitch n = snitchList.get(mid);

		searchChecks++;
		if (i > n.getFieldMinX()) {
			return findUpperXLimit(i, mid, max, snitchList);
		}
		
		searchChecks++;
		if (i < n.getFieldMinX()) {
			return findUpperXLimit(i, min, mid, snitchList);
		}
		int nmid = mid ++;
		while (i == snitchList.get(nmid).getFieldMinX()) {
			searchChecks++;
			nmid ++;
		}
		searchChecks++;
		mid = nmid --;

		return mid;
	}

	private static int findLowerXLimit(int i, int min, int max, ArrayList<Snitch> snitchList) {
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) {
			return min;
		}
		Snitch n = snitchList.get(mid);

		searchChecks++;
		if (i > n.getFieldMaxX()) {
			return findLowerXLimit(i, mid, max, snitchList);
		}
		searchChecks++;
		if (i < n.getFieldMaxX()) {
			return findLowerXLimit(i, min, mid, snitchList);
		}
		int nmid = mid --;
		while (i == snitchList.get(nmid).getFieldMaxX()) {
			searchChecks++;
			nmid --;
		}
		searchChecks++;
		mid = nmid ++;
		return mid;
	}

	
	private static int findUpperWorldLimit(String world, int min, int max, ArrayList<Snitch> snitchList) {
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) {
			return max;
		}
		Snitch n = snitchList.get(mid);
		
		searchChecks++;
		if (world.compareTo(n.getWorld()) > 0) {
			return findUpperWorldLimit(world, mid, max, snitchList);
		}
		searchChecks++;
		if (world.compareTo(n.getWorld()) < 0) {
			return findUpperWorldLimit(world, min, mid, snitchList);
		}
		int nmid = mid ++;
		while (world.compareTo(snitchList.get(nmid).getWorld()) == 0) {
			searchChecks++;
			nmid ++;
		}
		searchChecks++;
		mid = nmid --;

		return mid;
	}

	private static int findLowerWorldLimit(String world, int min, int max, ArrayList<Snitch> snitchList) {
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) {
			return min;
		}
		Snitch n = snitchList.get(mid);

		searchChecks++;
		if (world.compareTo(n.getWorld()) > 0) {
			return findLowerWorldLimit(world, mid, max, snitchList);
		}
		searchChecks++;
		if (world.compareTo(n.getWorld()) < 0) {
			return findLowerWorldLimit(world, min, mid, snitchList);
		}
		int nmid = mid --;
		while (world.compareTo(snitchList.get(nmid).getWorld()) == 0) {
			searchChecks++;
			nmid --;
		}
		searchChecks++;
		mid = nmid ++;

		return mid;
	}

	
}
