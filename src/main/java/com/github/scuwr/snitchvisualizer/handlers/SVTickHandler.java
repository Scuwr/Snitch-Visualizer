package com.github.scuwr.snitchvisualizer.handlers;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.scuwr.snitchvisualizer.SV;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Tick Handler for Snitch Visualizer
 * 
 * New tick handling code parses TPS from server /tps command. On default, I set
 * the tick value very high to prevent the player from being kicked due to
 * spamming.
 * 
 * In other words, the game is laggy and thinks you're spamming if it sends out
 * too many messages in a given amount of time, but if data is available delay
 * is adjusted to match current conditions.
 * 
 * @author Scuwr
 *
 */
public class SVTickHandler {

	public static Logger logger = LogManager.getLogger("SnitchVisualizer");
	
	public int playerTicks = 0;
	public static double waitTime = 4;
	public static int tickTimeout = 20;
	public static Date start = new Date();

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		try {
			/*if (SV.instance.playerHandler != null)
				SV.instance.playerHandler.onPlayerEvent(event);*/
			
			if (((new Date()).getTime() - (waitTime * 1000)) > start.getTime()) {
				if (SVChatHandler.updateSnitchList) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/jalist " + SVChatHandler.jalistIndex);
					SVChatHandler.jalistIndex++;
					start = new Date();
				}
				if (SVPlayerHandler.updateSnitchName || SVChatHandler.snitchReport) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/jainfo " + SVChatHandler.jainfoIndex);
					if (SVChatHandler.snitchReport) {
						SVChatHandler.jainfoIndex++;
					}
					start = new Date();
					if (SVPlayerHandler.updateSnitchName) { // Do this once then stop.
						SVPlayerHandler.updateSnitchName = false;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Something went wrong during tick handling", e);
		}
	}
}
