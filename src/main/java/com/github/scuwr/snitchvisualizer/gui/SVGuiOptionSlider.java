package com.github.scuwr.snitchvisualizer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.SVSettings;

@SideOnly(Side.CLIENT)
public class SVGuiOptionSlider extends GuiButton
{
    private float field_146134_p;
    public boolean field_146135_o;
    private SVSettings.Options option;
    private final float lowerBound;
    private final float upperBound;
    private static final String __OBFID = "CL_00000680";

    public SVGuiOptionSlider(int id, int width, int height, SVSettings.Options option)
    {
        this(id, width, height, option, 0.0F, 1.0F);
    }

    public SVGuiOptionSlider(int id, int width, int height, SVSettings.Options option, float lowerBound, float upperBound)
    {
        super(id, width, height, 200, 20, "");
        this.field_146134_p = 1.0F;
        this.option = option;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.field_146134_p = option.normalizeValue(SV.settings.getOptionFloatValue(option));
        this.displayString = SV.settings.getKeyBinding(option);
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    public int getHoverState(boolean p_146114_1_)
    {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_)
    {
        if (this.visible)
        {
            if (this.field_146135_o)
            {
                this.field_146134_p = (float)(p_146119_2_ - (this.xPosition + 4)) / (float)(this.width - 8);

                if (this.field_146134_p < 0.0F)
                {
                    this.field_146134_p = 0.0F;
                }

                if (this.field_146134_p > 1.0F)
                {
                    this.field_146134_p = 1.0F;
                }

                float f = this.option.denormalizeValue(this.field_146134_p);
                SV.settings.setOptionFloatValue(this.option, f);
                this.field_146134_p = this.option.normalizeValue(f);
                this.displayString = SV.settings.getKeyBinding(this.option);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.field_146134_p * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.field_146134_p * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_)
    {
        if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_))
        {
            this.field_146134_p = (float)(p_146116_2_ - (this.xPosition + 4)) / (float)(this.width - 8);

            if (this.field_146134_p < 0.0F)
            {
                this.field_146134_p = 0.0F;
            }

            if (this.field_146134_p > 1.0F)
            {
                this.field_146134_p = 1.0F;
            }

            SV.settings.setOptionFloatValue(this.option, this.option.denormalizeValue(this.field_146134_p));
            this.displayString = SV.settings.getKeyBinding(this.option);
            this.field_146135_o = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int p_146118_1_, int p_146118_2_)
    {
        this.field_146135_o = false;
    }
}
