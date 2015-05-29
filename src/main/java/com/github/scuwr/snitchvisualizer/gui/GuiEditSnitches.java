package com.github.scuwr.snitchvisualizer.gui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Block;
import com.github.scuwr.snitchvisualizer.handlers.SVFileIOHandler;

/**
 * Defines the on-screen gui for editing the snitch list
 * 
 * @author Scuwr
 *
 */

@SideOnly(Side.CLIENT)
public class GuiEditSnitches extends GuiScreen {

	public GuiScreen parentScreen;

	private GuiSnitchList guiSnitchList;
	public KeyBinding buttonId = null;

	public GuiEditSnitches(GuiScreen guiscreen) {
		this.parentScreen = guiscreen;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.guiSnitchList.drawScreen(par1, par2, par2);
		super.drawScreen(par1, par2, par3);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public void initGui() {
		byte b0 = -16;

		this.guiSnitchList = new GuiSnitchList(this, this.mc);

		this.buttonList.clear();
		this.buttonList.add(new GuiButton(4, this.width / 2 + 2, this.height - 4 + b0, 98, 18, StatCollector
				.translateToLocal("gui.done")));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height - 4 + b0, 98, 18, StatCollector
				.translateToLocal("svoptions.resetBlockList")));
	}

	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 4:
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
			SVFileIOHandler.saveSettings();
			SVFileIOHandler.saveList();
			break;
		case 5:
			SV.instance.blockList = new ArrayList<Block>();
			button.enabled = false;
		}
	}

	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (p_73864_3_ != 0) {
			try {
				super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}