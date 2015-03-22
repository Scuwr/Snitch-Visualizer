package com.github.scuwr.snitchvisualizer.handlers;

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

	public int serverTicks = 0;
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event){
		new SVPlayerHandler().onPlayerEvent(event);
		if(serverTicks <= 40) serverTicks++;
		if(SVChatHandler.updateSnitchList && serverTicks > 40){
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/jalist " + SVChatHandler.jalistIndex);
			serverTicks = 0;
			SVChatHandler.jalistIndex++;
		}
	}
}
