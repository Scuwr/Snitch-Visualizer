package com.github.scuwr.snitchvisualizer.handlers;

import java.util.ArrayList;
import java.util.Date;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

/**
 * Player Handler for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
public class SVPlayerHandler {
	
	private static boolean playerIsInSnitchArea = false;
	
	@SubscribeEvent
	public void onPlayerEvent(ClientTickEvent event){
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if(player != null){
			if(Math.floor(player.prevPosX) != Math.floor(player.posX) || Math.floor(player.prevPosY) != Math.floor(player.posY) || Math.floor(player.prevPosZ) != Math.floor(player.posZ)){
				player.prevPosX = player.posX;
				player.prevPosY = player.posY;
				player.prevPosZ = player.posZ;
				onPlayerMove(player);
			}
		}
	}
	
	public void onPlayerMove(EntityClientPlayerMP player){
		if(SV.settings.updateDetection) checkSnitchArea((int)Math.floor(player.posX), (int)Math.floor(player.posY) - 1, (int)Math.floor(player.posZ), SV.instance.snitchList, false);
	}
	
	public static void checkSnitchArea(int x, int y, int z, ArrayList<Snitch> snitchList, boolean removeSnitch){
		int index = checkSnitchAreaRecursion(x, y, z, 0, snitchList.size()-1, snitchList);
		if(index != -1){
			Snitch n = SV.instance.snitchList.get(index);
			if(n.type == "Alert") n.cullTime = Snitch.changeToDate(672.0);
			else n.cullTime = Snitch.changeToDate(336.0);
			playerIsInSnitchArea = true;
			if(removeSnitch){
				SV.instance.snitchList.remove(index);
				SV.instance.logger.info("Snitch Removed!");
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[" + SV.MODNAME + "] You have just deleted a Snitch!"));
			}
		}
		else if (playerIsInSnitchArea){
			playerIsInSnitchArea = false;			
			SVFileIOHandler.saveList();
		}
	}

	/**
	 * Recursive 3D Binary Search Detection Algorithm
	 * 
	 * Works 98% of the time for some reason
	 * 
	 * @param x location of player in x axis
	 * @param y location of player in y axis
	 * @param z location of player in z axis
	 * @param min lowest index in Snitchlist array
	 * @param max highest index in Snitchlist array
	 * @param snitchList array containing a list of Snitch objects
	 * @return
	 */
	private static int checkSnitchAreaRecursion(int x, int y, int z, int min, int max, ArrayList<Snitch> snitchList) {
		if (max < min)
			return -1;
		else{
			int mid = min + ((max - min) / 2);
			Snitch n = snitchList.get(mid);
			if(x > n.fieldMaxX)
				return checkSnitchAreaRecursion(x, y, z, mid + 1, max, snitchList);
			if (x < n.fieldMinX)
				return checkSnitchAreaRecursion(x, y, z, min, mid - 1, snitchList);
			
			if(mid + 1 < snitchList.size() && n.x != snitchList.get(mid+1).x){
				int i = checkSnitchAreaRecursion(x, y, z, mid + 1, max, snitchList);
				if(i != -1)
					return i;
			}
			if(mid > 0 && n.x != snitchList.get(mid-1).x){
				int i = checkSnitchAreaRecursion(x, y, z, min, mid - 1, snitchList);
				if(i != -1)
					return i;
			}
			
			if(z > n.fieldMaxZ)
				return checkSnitchAreaRecursion(x, y, z, mid + 1, max, snitchList);
			if (z < n.fieldMinZ)
				return checkSnitchAreaRecursion(x, y, z, min, mid - 1, snitchList);
			if(y > n.fieldMaxY)
				return checkSnitchAreaRecursion(x, y, z, mid + 1, max, snitchList);
			if (y < n.fieldMinY)
				return checkSnitchAreaRecursion(x, y, z, min, mid - 1, snitchList);
			
			return mid;
		}
	}
}
