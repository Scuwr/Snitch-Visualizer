package com.github.scuwr.snitchvisualizer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.LogManager;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

@SideOnly(Side.CLIENT)
public class GuiSnitchList extends GuiListExtended {
	private final GuiEditSnitches guiSnitches;
	private final Minecraft mc;
	private final GuiListExtended.IGuiListEntry[] iGuiList;
	
	private int worldWidth = 0;
	private int entryWidth = 0;
	private int coordWidth = 0;
	private int ctGroupWidth = 0;
	private int nameWidth = 0;
	private int nameSpace = 0;
	//private static final String __OBFID = "CL_00000732";
	private final String worldHeader = "World";
	private final String xHeader = "X";
	private final String yHeader = "Y";
	private final String zHeader = "Z";
	private final String groupHeader = "Group";
	private final String nameHeader = "Snitch Name";
	
	public GuiSnitchList(GuiEditSnitches guiSnitches, Minecraft mc) {
		super(mc, 
				guiSnitches.width,		// width
				guiSnitches.height, 		// height
				32, 						// top
				guiSnitches.height - 32, 	// bottom
				20);						// slot size
		
		this.guiSnitches = guiSnitches;
		this.mc = mc;
		
		int listSize = SV.instance.snitchList.size(); // TODO: replace with method
		this.iGuiList = new GuiListExtended.IGuiListEntry[listSize];
		
		int i = 0;

		this.nameWidth = 1;
		this.ctGroupWidth = mc.fontRendererObj.getStringWidth("WWWWWWWWWWWWWWWWWWWW ");
		this.worldWidth = mc.fontRendererObj.getStringWidth("WWWWWWWWWWW ");
		this.coordWidth = mc.fontRendererObj.getStringWidth("-9999  ");
		this.nameSpace = this.width - this.coordWidth * 3 - this.worldWidth - this.ctGroupWidth - 100;
		this.entryWidth = 1;

		for (int k = 0; k < listSize; ++k) {
			Snitch snitch = SV.instance.snitchList.get(k);

			int l = mc.fontRendererObj.getStringWidth(snitch.getCtGroup() + "  ");
			if (l > this.ctGroupWidth) {
				this.ctGroupWidth = l;
			}

			l = mc.fontRendererObj.getStringWidth(snitch.getName() + "  ");
			if (l > this.nameWidth) {
				this.nameWidth = l;
			}
			
			l = mc.fontRendererObj.getStringWidth(snitch.getWorld() + "  ");
			if (l > this.worldWidth) {
				this.worldWidth = l;
			}

			l = this.coordWidth * 3 + this.ctGroupWidth + this.nameWidth + this.worldWidth;
			if (l > this.entryWidth) {
				this.entryWidth = l;
			}

			this.iGuiList[i++] = new GuiSnitchList.ListEntry(snitch);
		}

		if (this.nameWidth > this.nameSpace) {
			this.nameWidth = this.nameSpace;
		}
		if (this.ctGroupWidth > this.nameSpace) {
			this.ctGroupWidth = this.nameSpace;
		}
		
		this.setHasListHeader(true, (int) ( (float) GuiSnitchList.this.mc.fontRendererObj.FONT_HEIGHT * 1.5));
	}

    protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_)
    {
        String root = EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.BOLD;
        
		int xPosition = p_148129_1_;
		int yFinal = p_148129_2_ + GuiSnitchList.this.mc.fontRendererObj.FONT_HEIGHT - 1;

		int sum = 0; 
		
		GuiSnitchList.this.mc.fontRendererObj.drawString(root + this.worldHeader, 
				xPosition + sum + (GuiSnitchList.this.worldWidth - mc.fontRendererObj.getStringWidth(root+this.worldHeader)) / 2,
				yFinal, 16777215);
		sum += GuiSnitchList.this.worldWidth;
		GuiSnitchList.this.mc.fontRendererObj.drawString(root + this.xHeader,
				xPosition + sum + (GuiSnitchList.this.coordWidth - mc.fontRendererObj.getStringWidth(root+this.xHeader)) / 2,
				yFinal, 16777215);
		sum += GuiSnitchList.this.coordWidth;
		GuiSnitchList.this.mc.fontRendererObj.drawString(root + this.yHeader, 
				xPosition + sum + (GuiSnitchList.this.coordWidth - mc.fontRendererObj.getStringWidth(root+this.yHeader)) / 2, 
				yFinal, 16777215);
		sum += GuiSnitchList.this.coordWidth;
		GuiSnitchList.this.mc.fontRendererObj.drawString(root + this.zHeader, 
				xPosition + sum + (GuiSnitchList.this.coordWidth - mc.fontRendererObj.getStringWidth(root+this.zHeader)) / 2,
				yFinal, 16777215);
		sum += GuiSnitchList.this.coordWidth;
		GuiSnitchList.this.mc.fontRendererObj.drawString(root + this.groupHeader,
				xPosition + sum + (GuiSnitchList.this.ctGroupWidth - mc.fontRendererObj.getStringWidth(root+this.groupHeader)) / 2,
				yFinal, 16777215);
		sum += GuiSnitchList.this.ctGroupWidth;
		GuiSnitchList.this.mc.fontRendererObj.drawString(root + this.nameHeader,
				xPosition + sum + (GuiSnitchList.this.nameWidth - mc.fontRendererObj.getStringWidth(root+this.nameHeader)) / 2,
				yFinal, 16777215);
    }
    
	protected int getSize() {
		return this.iGuiList.length;
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	public GuiListExtended.IGuiListEntry getListEntry(int index) {
		return this.iGuiList[index];
	}

	protected int getScrollBarX() {
		return this.width - 16;
	}
	
	/**
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return this.width;
	}

	@SideOnly(Side.CLIENT)
	public class ListEntry implements GuiListExtended.IGuiListEntry {
		private final Snitch snitch;
		private final String world;
		private final String xCoord;
		private final String yCoord;
		private final String zCoord;
		private final String ctGroup;
		private final String snitchName;
		private final GuiButton btnRemove;

		private final int worldWidth;
		private final int nameWidth;
		private final int groupWidth;

		//private static final String __OBFID = "CL_00000735";

		private ListEntry(Snitch p_i45029_2_) {
			this.snitch = p_i45029_2_;
			
			this.xCoord = Integer.toString(snitch.getX());
			this.yCoord = Integer.toString(snitch.getY());
			this.zCoord = Integer.toString(snitch.getZ());
			this.btnRemove = new GuiButton(10, GuiSnitchList.this.width - 60, 0, 50, 18, I18n.format("Remove", new Object[0]));

			this.nameWidth = mc.fontRendererObj.getStringWidth(snitch.getName());
			/*if (this.nameWidth > GuiSnitchList.this.nameSpace) {
				double l = (double) this.nameWidth / (double) GuiSnitchList.this.nameSpace;
				this.snitchName = this.snitch.getName().substring(0, (int) (snitch.getName().length() / l) - 2) + "~";
			} else {
				this.snitchName = snitch.getName();
			}*/
			this.snitchName = snitch.getName();

			this.groupWidth = mc.fontRendererObj.getStringWidth(snitch.getCtGroup());
			/*if (this.groupWidth > GuiSnitchList.this.nameSpace) {
				double l = (double) this.groupWidth / (double) GuiSnitchList.this.nameSpace;
				this.ctGroup = this.snitch.getCtGroup().substring(0, (int) (snitch.getCtGroup().length() / l) - 2) + "~";
			} else {
				this.ctGroup = snitch.getCtGroup();
			}*/
			this.ctGroup = snitch.getCtGroup();
			
			this.worldWidth = mc.fontRendererObj.getStringWidth(snitch.getWorld());
			this.world = snitch.getWorld();
		}

		public void drawEntry(int p_148279_1_, int xPosition, int yPosition, int p_148279_4_, int p_148279_5_,
				int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
			//xPosition = xPosition - 1;
			int yFinal = yPosition + (p_148279_5_ + GuiSnitchList.this.mc.fontRendererObj.FONT_HEIGHT) / 2;

			int sum = 0; 
			
			GuiSnitchList.this.mc.fontRendererObj.drawString(this.world, 
					xPosition + sum + (GuiSnitchList.this.worldWidth - this.worldWidth) / 2,
					yFinal, 16777215);
			sum += GuiSnitchList.this.worldWidth;
			GuiSnitchList.this.mc.fontRendererObj.drawString(this.xCoord,
					xPosition + sum + (GuiSnitchList.this.coordWidth - mc.fontRendererObj.getStringWidth(this.xCoord)) / 2,
					yFinal, 16777215);
			sum += GuiSnitchList.this.coordWidth;
			GuiSnitchList.this.mc.fontRendererObj.drawString(this.yCoord, 
					xPosition + sum + (GuiSnitchList.this.coordWidth - mc.fontRendererObj.getStringWidth(this.yCoord)) / 2, 
					yFinal, 16777215);
			sum += GuiSnitchList.this.coordWidth;
			GuiSnitchList.this.mc.fontRendererObj.drawString(this.zCoord, 
					xPosition + sum + (GuiSnitchList.this.coordWidth - mc.fontRendererObj.getStringWidth(this.zCoord)) / 2,
					yFinal, 16777215);
			sum += GuiSnitchList.this.coordWidth;
			GuiSnitchList.this.mc.fontRendererObj.drawString(this.ctGroup,
					xPosition + sum + (GuiSnitchList.this.ctGroupWidth - this.groupWidth) / 2,
					yFinal, 16777215);
			sum += GuiSnitchList.this.ctGroupWidth;
			if (this.snitchName != null) {
				GuiSnitchList.this.mc.fontRendererObj.drawString(this.snitchName,
						xPosition + sum + (GuiSnitchList.this.nameWidth - this.nameWidth) / 2,
						yFinal, 16777215);
			}

			this.btnRemove.xPosition = xPosition + GuiSnitchList.this.width - btnRemove.width - 16;
			this.btnRemove.yPosition = yPosition + this.btnRemove.height / 4;
			this.btnRemove.drawButton(GuiSnitchList.this.mc, p_148279_7_, p_148279_8_);
		}

		/**
		 * Returns true if the mouse has been pressed on this control.
		 */
		public boolean mousePressed(int index, int xPos, int yPos, int mouseEvent, int relX, int relY) {
			
			//LogManager.getLogger("SnitchVisualizer").info("MousePress on SnitchListItem Detected!");
			
			if (this.btnRemove.enabled && this.btnRemove.mousePressed(GuiSnitchList.this.mc, xPos, yPos)) {
				doRemoval();
				return true;
			}

			return false;
		}
		
		private void doRemoval() {
			if (guiSnitches.removeSnitches.add(this.snitch)) {
				this.btnRemove.displayString = "Removed!";
				this.btnRemove.enabled = false;
			}
		}

		/**
		 * Fired when the mouse button is released. Arguments: index, x, y,
		 * mouseEvent, relativeX, relativeY
		 */
		
		public void mouseReleased(int index, int xPos, int yPos, int mouseEvent, int relX, int relY) {
			this.btnRemove.mouseReleased(xPos, yPos);
		}

		ListEntry(Snitch p_i45030_2_, Object notUsed) {
			this(p_i45030_2_);
		}

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
			// TODO Auto-generated method stub

		}
	}
	
	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
		if (isDoubleClick || slotIndex < 0 || slotIndex >= iGuiList.length) return;
		((ListEntry) getListEntry(slotIndex)).mousePressed(slotIndex, mouseX, mouseY, 0, 0, 0);
	}
}
