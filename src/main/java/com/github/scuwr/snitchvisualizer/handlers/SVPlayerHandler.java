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
 * @author ProgrammerDan
 */
public class SVPlayerHandler {

	private static Logger logger = LogManager.getLogger("SnitchVisualizer");

	public static int snitchIndex = -1;
	public static boolean updateSnitchName = false;

	private static boolean playerIsInSnitchArea = false;
	
	private long lastExecute = 0l;
	private static long delay = 100l;
	private long skips = 0l;

	@SubscribeEvent
	public void onPlayerEvent(ClientTickEvent event) {
		if (!SV.settings.updateDetection) return;
		if (lastExecute != 0l && ((System.currentTimeMillis() - lastExecute) < SVPlayerHandler.delay)) {
			skips++;
			return;
		}
		try {
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
					skips = 0l;
				}
			};
		} catch (Exception e) {
			logger.error("Unexpected error during client tick event handler for player movement.", e);
		}
	}

	public void onPlayerMove(EntityPlayerSP player) {
		try {
			logger.info("(" + skips + " skipped events) Current world: " + player.worldObj.provider.getDimensionName() + " or "
					+ player.worldObj.getWorldInfo().getWorldName()
					+ player.worldObj.getWorldType().getWorldTypeName());
			String world = player.worldObj.provider.getDimensionId() + player.worldObj.provider.getDimensionName();
			String worldX = SV.instance.worldList.get(world);
			if (worldX == null) {
				worldX = world;
				logger.info("Unable to map " + world + " to a snitch-world");
			}
			checkSnitchArea(worldX, (int) Math.floor(player.posX), (int) Math.floor(player.posY) - 1,
					(int) Math.floor(player.posZ), SV.instance.snitchList, false);
		} catch (Exception e) {
			logger.error("Unexpected error during player move handling", e);
		}
	}
	public static long searchChecks = 0l;

	/**
	 * Checks for a snitch. Uses three divide-and-conquer searches on world, x, z (in that order) to
	 *    narrow search within a sorted list. This works because the sorted list is sorted using keys
	 *    world, x, z, y in that order. First the slice holding snitches in the same world is found,
	 *    then the slice of that holding the X of interest, then Z. Finally the results are iterated
	 *    over to find the closest.
	 *    
	 * @param world
	 *            world ID
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
	public static void checkSnitchArea(String world, int x, int y, int z, ArrayList<Snitch> snitchList, boolean removeSnitch) {
		try {
			searchChecks = 0l;
			
			int min = 0;
			findLowerLimit(Search.WORLD, world, 0, snitchList.size() - 1, snitchList);
			int max = snitchList.size()-1;
			findUpperLimit(Search.WORLD, world, min, snitchList.size() - 1, snitchList);
			for (Search k : Search.values()) {
				min = findLowerLimit(k, k == Search.WORLD ? world : k == Search.X ? x: z, min, max, snitchList);
				if (max < min) return;
				max = findUpperLimit(k, k == Search.WORLD ? world : k == Search.X ? x: z, min, max, snitchList);
				if (max < min) return;
				if (max - min <= 1) break;
			}
			
			logger.info("Performed " + searchChecks + " checks -- final bounds: " + min + ", " + max);
			logger.info("Total snitches: " + snitchList.size() + " -- searched " +
						Math.round(((double)(searchChecks + max-min+1) / (double)snitchList.size()) * 10000.0d)/100.0d + "% of total");
	
			int index = -1;
			double sqDistance = Double.MAX_VALUE;
	
			for (int i = min; i <= max; i++) {
				Snitch n = snitchList.get(i);
				if (n.contains(world, x, y, z)) {
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

				//Only refresh the cull time if cull time is turned on
				//(Having a null raw cull time means its turned off)
				if(n.getRawCullTime() != null)
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

	private enum Search {
		X,
		Z,
		WORLD;
	}
	
	private static int findUpperLimit(Search s, Object obj, int min, int max, ArrayList<Snitch> snitchList) {
		if (max - min <= 1) {
			return max;
		}

		int mid = (min + max) /2;
		Snitch n = snitchList.get(mid);

		searchChecks++;
		if (	(s.equals(Search.X) && (Integer) obj > n.getFieldMinX()) ||
				(s.equals(Search.Z) && (Integer) obj > n.getFieldMinZ()) ||
				(s.equals(Search.WORLD) && ((String) obj).compareTo(n.getWorld()) > 0)) {
			return findUpperLimit(s, obj, mid + 1, max, snitchList);
		}
		searchChecks++;
		if (	(s.equals(Search.X) && (Integer) obj < n.getFieldMinX()) ||
				(s.equals(Search.Z) && (Integer) obj < n.getFieldMinZ()) ||
				(s.equals(Search.WORLD) && ((String) obj).compareTo(n.getWorld()) < 0)) {
			return findUpperLimit(s, obj, min, mid - 1, snitchList);
		}
		
		int nmid = mid ++;
		while (	nmid <= max &&
				(s.equals(Search.X) && (Integer) obj == snitchList.get(nmid).getFieldMinX()) ||
				(s.equals(Search.Z) && (Integer) obj == snitchList.get(nmid).getFieldMinZ()) ||
				(s.equals(Search.WORLD) && ((String) obj).compareTo(snitchList.get(nmid).getWorld()) == 0)) {
			searchChecks++;
			nmid ++;
		}
		searchChecks++;
		mid = nmid --;

		return mid;
	}

	private static int findLowerLimit(Search s, Object obj, int min, int max, ArrayList<Snitch> snitchList) {
		if (max - min <= 1) {
			return min;
		}

		int mid = (min + max) /2;
		Snitch n = snitchList.get(mid);

		searchChecks++;
		if (	(s.equals(Search.X) && (Integer) obj > n.getFieldMaxX()) ||
				(s.equals(Search.Z) && (Integer) obj > n.getFieldMaxZ()) ||
				(s.equals(Search.WORLD) && ((String) obj).compareTo(n.getWorld()) > 0)) {
			return findLowerLimit(s, obj, mid + 1, max, snitchList);
		}
		searchChecks++;
		if (	(s.equals(Search.X) && (Integer) obj < n.getFieldMaxX()) ||
				(s.equals(Search.Z) && (Integer) obj < n.getFieldMaxZ()) ||
				(s.equals(Search.WORLD) && ((String) obj).compareTo(n.getWorld()) < 0)) {
			return findLowerLimit(s, obj, min, mid - 1, snitchList);
		}
		
		int nmid = mid --;
		while (	nmid >= min &&
				(s.equals(Search.X) && (Integer) obj == snitchList.get(nmid).getFieldMaxX()) ||
				(s.equals(Search.Z) && (Integer) obj == snitchList.get(nmid).getFieldMaxZ()) ||
				(s.equals(Search.WORLD) && ((String) obj).compareTo(snitchList.get(nmid).getWorld()) == 0)) {
			searchChecks++;
			nmid --;
		}
		searchChecks++;
		mid = nmid ++;

		return mid;
	}
}
