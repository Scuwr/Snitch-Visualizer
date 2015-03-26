package com.github.scuwr.snitchvisualizer.handlers;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.settings.KeyBinding;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.gui.SVGui;

/**
 * Key handler for Snitch Visualizer
 * 
 * Currently is hard-coded to the 'V' key
 * 
 * @author Scuwr
 *
 */
public class SVKeyHandler{
	
	public KeyBinding keySVGui = new KeyBinding("SV Settings", (int)SV.settings.svSettingsKey, "Snitch Visualizer");
	
	public SVKeyHandler(){
		ClientRegistry.registerKeyBinding(keySVGui);
	}
	
	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent event){
		if(keySVGui.isPressed()){
			Minecraft.getMinecraft().displayGuiScreen(new SVGui(null));
		}
	}
	
}
