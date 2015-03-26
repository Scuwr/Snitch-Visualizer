package com.github.scuwr.snitchvisualizer.handlers;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.settings.KeyBinding;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.gui.SVGui;

/**
 * Key handler for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
public class SVKeyHandler{
	
	public KeyBinding keySVGui = new KeyBinding("SV Settings", (int)SV.settings.svSettingsKey, "Snitch Visualizer");
	public KeyBinding keyRemoveSnitch = new KeyBinding("Remove Snitch", Keyboard.KEY_R, "Snitch Visualizer");
	
	public SVKeyHandler(){
		ClientRegistry.registerKeyBinding(keySVGui);
		ClientRegistry.registerKeyBinding(keyRemoveSnitch);
	}
	
	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent event){
		if(keySVGui.isPressed()){
			Minecraft.getMinecraft().displayGuiScreen(new SVGui(null));
		}
		if(keyRemoveSnitch.isPressed() && SV.settings.renderEnabled){
			SV.instance.logger.info("Delete Snitch Key Pressed!");
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			SVPlayerHandler.checkSnitchArea((int)Math.floor(player.posX), (int)Math.floor(player.posY) - 1, (int)Math.floor(player.posZ), SV.instance.snitchList, true);
		}
	}
	
}
