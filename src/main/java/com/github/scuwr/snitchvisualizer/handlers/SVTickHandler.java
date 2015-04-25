package com.github.scuwr.snitchvisualizer.handlers;

import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Tick Handler for Snitch Visualizer
 * 
 * I was unable to query server-side ticks, so I set the tick value very high to
 * prevent the player from being kicked due to spamming
 * 
 * In other words, the game is laggy and thinks you're spamming if it sends out too many
 * messages in a given amount of time.
 * 
 * @author Scuwr
 *
 */
public class SVTickHandler{

	public int playerTicks = 0;
	public static double waitTime = 4;
	public static int tickTimeout = 20;
	public static Date start = new Date();
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event){
		new SVPlayerHandler().onPlayerEvent(event);
		/*if(playerTicks <= 40) playerTicks++;
		if(SVChatHandler.updateSnitchList && playerTicks > 40){
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/jalist " + SVChatHandler.jalistIndex);
			playerTicks = 0;
			SVChatHandler.jalistIndex++;
		}*/
		if(((new Date()).getTime() - (waitTime*1000)) > start.getTime()){
			if(SVChatHandler.updateSnitchList){
				Minecraft.getMinecraft().thePlayer.sendChatMessage("/jalist " + SVChatHandler.jalistIndex);
				SVChatHandler.jalistIndex++;
				start = new Date();
			}
			if(SVPlayerHandler.updateSnitchName){
				Minecraft.getMinecraft().thePlayer.sendChatMessage("/jainfo " + SVChatHandler.jainfoIndex);
				if(SVChatHandler.snitchReport) SVChatHandler.jainfoIndex++;
				start = new Date();
			}
		}
		
	}
}
