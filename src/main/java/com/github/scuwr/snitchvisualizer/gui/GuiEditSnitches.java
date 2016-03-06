package com.github.scuwr.snitchvisualizer.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Block;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;
import com.github.scuwr.snitchvisualizer.handlers.SVFileIOHandler;

/**
 * Defines the on-screen gui for editing the snitch list
 * 
 * @author Scuwr
 */
@SideOnly(Side.CLIENT)
public class GuiEditSnitches extends GuiScreen {

	public GuiScreen parentScreen;
	
	public ArrayList<Snitch> removeSnitches = new ArrayList<Snitch>();

	private GuiSnitchList guiSnitchList;
	public KeyBinding buttonId = null;

	public GuiEditSnitches(GuiScreen guiscreen) {
		this.parentScreen = guiscreen;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.guiSnitchList.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public void initGui() {
		byte b0 = -20;

		this.guiSnitchList = new GuiSnitchList(this, this.mc);

		this.buttonList.clear();
		this.buttonList.add(new GuiButton(4, this.width / 2 + 2, this.height - 4 + b0, 98, 18, StatCollector
				.translateToLocal("gui.done")));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height - 4 + b0, 98, 18, StatCollector
				.translateToLocal("svoptions.resetSnitchList")));
		
		this.guiSnitchList.registerScrollButtons(4, 5);
	}

	public void actionPerformed(GuiButton button) {
		if (!button.enabled) return;
		switch (button.id) {
		case 4:
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
			removeSnitches();
			SVFileIOHandler.saveSettings();
			SVFileIOHandler.saveList();
			SVFileIOHandler.saveWorlds();
			break;
		case 5:
			SV.instance.blockList = new ArrayList<Block>();
			removeSnitches = new ArrayList<Snitch>();
			this.guiSnitchList = new GuiSnitchList(this, this.mc);
		}
	}
	
	private void removeSnitches(){
		for (Snitch s : removeSnitches){
			SV.instance.snitchList.remove(s);
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		this.guiSnitchList.mouseClicked(mouseX, mouseY, mouseEvent);
		
		//LogManager.getLogger("SnitchVisualizer").info("MousePress on SnitchGUI Detected!");

		//if (p_73864_3_ != 0) {
			try {
				super.mouseClicked(mouseX, mouseY, mouseEvent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		//}
	}
}