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

	@SubscribeEvent
	public void onPlayerEvent(ClientTickEvent event) {
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
	}

	public void onPlayerMove(EntityPlayerSP player) {
		if (SV.settings.updateDetection)
			checkSnitchArea((int) Math.floor(player.posX), (int) Math.floor(player.posY) - 1,
					(int) Math.floor(player.posZ), SV.instance.snitchList, false);
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
	public static void checkSnitchArea(int x, int y, int z, ArrayList<Snitch> snitchList, boolean removeSnitch) {
		int min = findLowerXLimit(x, 0, snitchList.size() - 1, snitchList);
		int max = findUpperXLimit(x, min, snitchList.size() - 1, snitchList);

		int index = -1;
		double sqDistance = Double.MAX_VALUE;

		for (int i = min; i < max; i++) {
			Snitch n = snitchList.get(i);
			if (x >= n.fieldMinX && x <= n.fieldMaxX && z >= n.fieldMinZ && z <= n.fieldMaxZ && y >= n.fieldMinY
					&& y <= n.fieldMaxY) {
				// Get closest snitch
				double temp = Minecraft.getMinecraft().thePlayer.getDistanceSq(n.x, n.y, n.z);
				if (temp < sqDistance) {
					sqDistance = temp;
					index = i;
				}
			}
		}

		if (index != -1) {
			snitchIndex = index;
			Snitch n = SV.instance.snitchList.get(index);
			n.cullTime = Snitch.changeToDate(672.0);
			playerIsInSnitchArea = true;
			updateSnitchName = false;
			if (removeSnitch) {
				SV.instance.snitchList.remove(index);
				logger.info("Snitch Removed!");
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[" + SV.MODNAME
						+ "] You have just deleted a Snitch!"));
			} else if (n.name.equals("Unknown")) {
				updateSnitchName = true;
			}
		} else if (playerIsInSnitchArea) {
			playerIsInSnitchArea = false;
			updateSnitchName = false;
			snitchIndex = -1;
			SVFileIOHandler.saveList();
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

	private static int findUpperXLimit(int i, int min, int max, ArrayList<Snitch> snitchList) {
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) {
			return max;
		}
		Snitch n = snitchList.get(mid);

		if (i > n.fieldMinX) {
			return findUpperXLimit(i, mid, max, snitchList);
		}
		if (i < n.fieldMinX) {
			return findUpperXLimit(i, min, mid, snitchList);
		}

		return mid;
	}

	private static int findLowerXLimit(int i, int min, int max, ArrayList<Snitch> snitchList) {
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) {
			return min;
		}
		Snitch n = snitchList.get(mid);

		if (i > n.fieldMaxX) {
			return findLowerXLimit(i, mid, max, snitchList);
		}
		if (i < n.fieldMaxX) {
			return findLowerXLimit(i, min, mid, snitchList);
		}

		return mid;
	}

	public static Snitch getSnitch() {
		// TODO Auto-generated method stub
		return null;
	}
}
