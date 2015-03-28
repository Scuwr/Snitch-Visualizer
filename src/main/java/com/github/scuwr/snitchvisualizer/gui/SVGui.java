package com.github.scuwr.snitchvisualizer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.SVSettings;
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
	
	private static final SVSettings.Options renderDistance = SVSettings.Options.RENDER_DISTANCE;
	
	private GuiSnitchList keyBindingList;
	public KeyBinding buttonId = null;
	
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
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 24 + b0, StatCollector.translateToLocal("svoptions.listUpdate")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 48 + b0, SV.settings.getKeyBinding(SVSettings.Options.UPDATE_DETECTION)));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72 + b0, SV.settings.getKeyBinding(SVSettings.Options.RENDER_ENABLED)));
		this.buttonList.add(new SVGuiOptionSlider(100, this.width / 2 - 100, this.height / 4 + 96 +b0, renderDistance));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 120 + b0, StatCollector.translateToLocal("svoptions.editSnitchList")));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 120 + 24 + b0, StatCollector.translateToLocal("gui.done")));
	}
	
	public void actionPerformed(GuiButton button){
		switch(button.id){
			case 0:
				SVChatHandler.updateSnitchList();
				this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
				break;
			case 1: 
				SV.settings.setOptionValue(SVSettings.Options.UPDATE_DETECTION);
				button.displayString = SV.settings.getKeyBinding(SVSettings.Options.UPDATE_DETECTION);
				SVFileIOHandler.saveSettings();
				break;
			case 2: 
				SV.settings.setOptionValue(SVSettings.Options.RENDER_ENABLED);
				button.displayString = SV.settings.getKeyBinding(SVSettings.Options.RENDER_ENABLED);
				SVFileIOHandler.saveSettings();
				break;
			case 3:
				this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiEditSnitches(this));
				break;
			case 4:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                SVFileIOHandler.saveSettings();
                break;
		}
	}
}
