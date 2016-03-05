package com.github.scuwr.snitchvisualizer.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.gui.SVGui;

/**
 * Key handler for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
public class SVKeyHandler {

	public KeyBinding keySVGui = new KeyBinding("SV Settings", (int) SV.settings.svSettingsKey, "Snitch Visualizer");
	public KeyBinding keyRemoveSnitch = new KeyBinding("Remove Snitch", Keyboard.KEY_R, "Snitch Visualizer");

	private static Logger logger = LogManager.getLogger("SnitchVisualizer");

	public SVKeyHandler() {
		ClientRegistry.registerKeyBinding(keySVGui);
		ClientRegistry.registerKeyBinding(keyRemoveSnitch);
	}

	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent event) {
		if (keySVGui.isPressed()) {
			try {
				Minecraft.getMinecraft().displayGuiScreen(new SVGui(null));
			} catch (Exception e) {
				logger.error("Unexpected error displaying SV gui", e);
			}
			return;
		}
		if (SV.settings.renderEnabled && keyRemoveSnitch.isPressed()) {
			try {
				logger.info("Delete Snitch Key Pressed!");
				EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
				logger.info("Current world: " + player.worldObj.getProviderName());
				SVPlayerHandler.checkSnitchArea(player.worldObj.getProviderName(), (int) Math.floor(player.posX), (int) Math.floor(player.posY) - 1,
						(int) Math.floor(player.posZ), SV.instance.snitchList, true);
			} catch (Exception e) {
				logger.error("Unexpected error while deleting a snitch", e);
			}
			return;
		}
	}

}
