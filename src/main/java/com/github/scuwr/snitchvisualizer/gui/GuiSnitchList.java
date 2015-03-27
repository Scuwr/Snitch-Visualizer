package com.github.scuwr.snitchvisualizer.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.ArrayUtils;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

@SideOnly(Side.CLIENT)
public class GuiSnitchList extends GuiListExtended
{
    private final SVGui svGui;
    private final Minecraft mc;
    private final GuiListExtended.IGuiListEntry[] iGuiList;
    private int entryWidth = 0;
    private int coordWidth = 0;
    private int ctGroupWidth = 0;
    private int nameWidth = 0;
    private static final String __OBFID = "CL_00000732";

    public GuiSnitchList(SVGui svGui, Minecraft mc)
    {
        super(mc, svGui.width, svGui.height, 32, svGui.height - 32, 20);
        this.svGui = svGui;
        this.mc = mc;
        
        
        
        //KeyBinding[] akeybinding = (KeyBinding[])ArrayUtils.clone(mc.gameSettings.keyBindings);
        int listSize = SV.instance.snitchList.size();
        this.iGuiList = new GuiListExtended.IGuiListEntry[listSize + 1];        
        int i = 0;
        String s = null;
        //KeyBinding[] akeybinding1 = akeybinding;
        
        this.iGuiList[i++] = new GuiSnitchList.CategoryEntry();
        
        for (int k = 0; k < listSize; ++k)
        {
        	Snitch snitch = SV.instance.snitchList.get(k);
            //KeyBinding keybinding = akeybinding1[k];
            //String s1 = keybinding.getKeyCategory();

            /*if (!s1.equals(s))
            {
                s = s1;
                this.iGuiList[i++] = new GuiSnitchList.CategoryEntry(s1);
            }*/

            int l = mc.fontRenderer.getStringWidth("-9999  -9999  -9999  " + snitch.ctGroup + "  " + snitch.name + "  ");
            if (l > this.entryWidth) this.entryWidth = l;
            
            l = mc.fontRenderer.getStringWidth("-9999  ");
            if (l > this.coordWidth) this.coordWidth = l;
            
            l = mc.fontRenderer.getStringWidth(snitch.ctGroup + "  ");
            if (l > this.ctGroupWidth) this.ctGroupWidth = l;
            
            l = mc.fontRenderer.getStringWidth(snitch.name + "  ");
            if (l > this.nameWidth) this.nameWidth = l;
            
            this.iGuiList[i++] = new GuiSnitchList.ListEntry(snitch);
        }
    }

    protected int getSize()
    {
        return this.iGuiList.length;
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListExtended.IGuiListEntry getListEntry(int index)
    {
        return this.iGuiList[index];
    }

    protected int getScrollBarX()
    {
    	return this.width - 32;
        //return super.getScrollBarX() + 15;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return this.width / 2;
    	//return super.getListWidth() + 32;
    }

    @SideOnly(Side.CLIENT)
    public class CategoryEntry implements GuiListExtended.IGuiListEntry
    {
        private final String xHeader = "X";
        private final String yHeader = "Y";
        private final String zHeader = "Z";
        private final String groupHeader = "Group";
        private final String nameHeader = "Name";
        private static final String __OBFID = "CL_00000734";

        public void drawEntry(int p_148279_1_, int xPosition, int yPosition, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
        {
            //GuiSnitchList.this.mc.fontRenderer.drawString(this.categoryName, GuiSnitchList.this.mc.currentScreen.width / 2 - this.categoryWidth / 2, p_148279_3_ + p_148279_5_ - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.xHeader, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.coordWidth - mc.fontRenderer.getStringWidth(this.xHeader)) / 2, yPosition + p_148279_5_ - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.yHeader, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.coordWidth - mc.fontRenderer.getStringWidth(this.yHeader)) / 2 + GuiSnitchList.this.coordWidth, yPosition + p_148279_5_ - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.zHeader, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.coordWidth - mc.fontRenderer.getStringWidth(this.zHeader)) / 2 + GuiSnitchList.this.coordWidth * 2, yPosition + p_148279_5_ - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.groupHeader, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.ctGroupWidth - mc.fontRenderer.getStringWidth(this.groupHeader)) / 2 + GuiSnitchList.this.coordWidth * 3, yPosition + p_148279_5_ - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.nameHeader, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.nameWidth - mc.fontRenderer.getStringWidth(this.nameHeader)) / 2 + GuiSnitchList.this.coordWidth * 3 + GuiSnitchList.this.ctGroupWidth, yPosition + p_148279_5_ - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }
        
        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
        {
            return false;
        }
        
        public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}
    }

    @SideOnly(Side.CLIENT)
    public class ListEntry implements GuiListExtended.IGuiListEntry
    {
        private final Snitch snitch;
        private final String xCoord;
        private final String yCoord;
        private final String zCoord;
        private final String ctGroup;
        private final String snitchName;
        private final GuiButton btnRemove;
        private static final String __OBFID = "CL_00000735";

        private ListEntry(Snitch p_i45029_2_)
        {
            this.snitch = p_i45029_2_;
            this.xCoord = Integer.toString(snitch.x);
            this.yCoord = Integer.toString(snitch.y);
            this.zCoord = Integer.toString(snitch.z);
            this.ctGroup = snitch.ctGroup;
            this.snitchName = snitch.name;
            this.btnRemove = new GuiButton(0, 0, 0, 50, 18, I18n.format("Remove", new Object[0]));
        }

        public void drawEntry(int p_148279_1_, int xPosition, int yPosition, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
        {
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.xCoord, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.coordWidth - mc.fontRenderer.getStringWidth(this.xCoord)) / 2, yPosition + p_148279_5_ / 2 - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.yCoord, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.coordWidth - mc.fontRenderer.getStringWidth(this.yCoord)) / 2 + GuiSnitchList.this.coordWidth, yPosition + p_148279_5_ / 2 - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.zCoord, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.coordWidth - mc.fontRenderer.getStringWidth(this.zCoord)) / 2 + GuiSnitchList.this.coordWidth * 2, yPosition + p_148279_5_ / 2 - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.ctGroup, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.ctGroupWidth - mc.fontRenderer.getStringWidth(this.ctGroup)) / 2 + GuiSnitchList.this.coordWidth * 3, yPosition + p_148279_5_ / 2 - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
        	GuiSnitchList.this.mc.fontRenderer.drawString(this.snitchName, xPosition + 185 - GuiSnitchList.this.entryWidth + (GuiSnitchList.this.nameWidth - mc.fontRenderer.getStringWidth(this.snitchName)) / 2 + GuiSnitchList.this.coordWidth * 3 + GuiSnitchList.this.ctGroupWidth, yPosition + p_148279_5_ / 2 - GuiSnitchList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
        	
            this.btnRemove.xPosition = xPosition + 190;
            this.btnRemove.yPosition = yPosition;
            this.btnRemove.enabled = true;
            this.btnRemove.drawButton(GuiSnitchList.this.mc, p_148279_7_, p_148279_8_);
        }

        /**
         * Returns true if the mouse has been pressed on this control.
         */
        public boolean mousePressed(int index, int xPos, int yPos, int mouseEvent, int relX, int relY)
        {
            if (this.btnRemove.mousePressed(GuiSnitchList.this.mc, xPos, yPos))
            {
                return true;
            }
            
            return false;
        }

        /**
         * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
         */
        public void mouseReleased(int index, int xPos, int yPos, int mouseEvent, int relX, int relY)
        {
            this.btnRemove.mouseReleased(xPos, yPos);
        }

        ListEntry(Snitch p_i45030_2_, Object notUsed)
        {
            this(p_i45030_2_);
        }
    }
}
