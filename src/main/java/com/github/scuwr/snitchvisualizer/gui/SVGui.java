package com.github.scuwr.snitchvisualizer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.handlers.SVChatHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVFileIOHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Defines the on-screen gui
 * 
 * @author Scuwr
 *
 */
@SideOnly(Side.CLIENT)
public class SVGui extends GuiScreen{
	
	public GuiScreen parentScreen;
	public String snitchRendering = SV.instance.config.rendering ? "Snitch Rendering: ON" : "Snitch Rendering: OFF";
	public String updateDetecting = SV.instance.config.updateDetection ? "Update Detection: ON  (EXPERIMENTAL!)" : "Update Detection: OFF";
	
	public SVGui(GuiScreen guiscreen){
		this.parentScreen = guiscreen;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Game menu", this.width / 2, 40, 16777215);
        super.drawScreen(par1, par2, par3);
    }
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	public void initGui(){
		byte b0 = -16;
		
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 24 + b0, "Force Snitchlist Update"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48 + b0, snitchRendering));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72 + b0, updateDetecting));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96 + b0, "Done"));
	}
	
	public void actionPerformed(GuiButton button){
		switch(button.id){
			case 0:
				SVChatHandler.updateSnitchList();
				break;
			case 1: 
				if(SV.instance.config.rendering){
					SV.instance.config.rendering = false;
					((GuiButton)this.buttonList.get(1)).displayString = "Snitch Rendering: OFF";
				}else{
					SV.instance.config.rendering = true;
					((GuiButton)this.buttonList.get(1)).displayString = "Snitch Rendering: ON";
				}
				break;
			case 2: 
				if(SV.instance.config.updateDetection){
					SV.instance.config.updateDetection = false;
					((GuiButton)this.buttonList.get(2)).displayString = "Update Detection: OFF";
				}else{
					SV.instance.config.updateDetection = true;
					((GuiButton)this.buttonList.get(2)).displayString = "Update Detection: ON (EXPERIMENTAL!)";
				}
				break;
			case 3:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                SVFileIOHandler.saveList();
                break;
		}
	}
}
