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
public class GuiEditSnitches extends GuiScreen{
	
	public GuiScreen parentScreen;
	
	private GuiSnitchList guiSnitchList;
	public KeyBinding buttonId = null;
	
	public GuiEditSnitches(GuiScreen guiscreen){
		this.parentScreen = guiscreen;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.guiSnitchList.drawScreen(par1, par2, par2);
        super.drawScreen(par1, par2, par3);
    }
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	public void initGui(){
		byte b0 = -16;
		
		this.guiSnitchList = new GuiSnitchList(this, this.mc);
		
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height + 2 * b0, StatCollector.translateToLocal("gui.done")));
	}
	
	public void actionPerformed(GuiButton button){
		switch(button.id){
			case 4:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                SVFileIOHandler.saveSettings();
                SVFileIOHandler.saveList();
                break;
		}
	}
	
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_){		
        if (p_73864_3_ != 0 || !this.guiSnitchList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_))
        {
            super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }
	
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        if (p_146286_3_ != 0 || !this.guiSnitchList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_))
        {
            super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
        }
    }
}