package com.github.scuwr.snitchvisualizer.handlers;

import java.util.ArrayList;
import java.util.Date;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
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
		int min = findLowerXLimit(x, 0, snitchList.size() -1, snitchList);
		int max = findUpperXLimit(x, min, snitchList.size() -1, snitchList);
  		//min = findLowerZLimit(z, min, max, snitchList);
		//max = findUpperZLimit(z, min, max, snitchList);
		//min = findLowerYLimit(y, min, max, snitchList);
		//max = findUpperYLimit(y, min, max, snitchList);
		
		int index = -1;
		double sqDistance = Double.MAX_VALUE;
		
		for (int i = min; i < max; i++){
			Snitch n = snitchList.get(i);
			if(x > n.fieldMinX && x < n.fieldMaxX && z > n.fieldMinZ && z < n.fieldMaxZ && y > n.fieldMinY && y < n.fieldMaxY){
				// Get closest snitch
				double temp = Minecraft.getMinecraft().thePlayer.getDistanceSq(n.x, n.y, n.z);
				if(temp < sqDistance){
					sqDistance = temp;
					index = i;
				}
			}
		}
		
		if(index != -1){
			Snitch n = SV.instance.snitchList.get(index);
			n.cullTime = Snitch.changeToDate(672.0);
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
	
	/* New strategy:
	 * 
	 * Find lower x limit, then find upper x limit, then find lower z limit, then find upper z limit, then find lower y limit, then find upper y limit
	 */
	
	private static int findUpperXLimit(int i, int min, int max, ArrayList<Snitch> snitchList){
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) return max;
		Snitch n = snitchList.get(mid);
		
		if (i > n.fieldMinX) return findUpperXLimit(i, mid, max, snitchList);
		if (i < n.fieldMinX) return findUpperXLimit(i, min, mid, snitchList);
		
		return mid;
	}
	
	private static int findLowerXLimit(int i, int min, int max, ArrayList<Snitch> snitchList){
		int mid = min + ((max - min) / 2);
		if (max - min <= 1) return min;
		Snitch n = snitchList.get(mid);
		
		if (i > n.fieldMaxX) return findLowerXLimit(i, mid, max, snitchList);
		if (i < n.fieldMaxX) return findLowerXLimit(i, min, mid, snitchList);
		
		return mid;
	}
}
