package com.github.scuwr.snitchvisualizer.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Block;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

/**
 * Render Hander for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
public class SVRenderHandler {

	private Minecraft mc = Minecraft.getMinecraft();

	public static Logger logger = LogManager.getLogger("SnitchVisualizer");

	@SuppressWarnings("incomplete-switch")
	@SubscribeEvent
	public void eventRenderWorld(RenderWorldLastEvent event) {
		if (SVFileIOHandler.isDone && SV.settings.renderEnabled) {
			try {
				float partialTickTime = event.partialTicks;

				double renderPosX = (float) (mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX)
						* partialTickTime);
				double renderPosY = (float) (mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY)
						* partialTickTime);
				double renderPosZ = (float) (mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ)
						* partialTickTime);

				for (Snitch n : SV.instance.snitchList) {
					if (n.getDistance() < SV.settings.renderDistance * 16) {
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						GL11.glLineWidth(5.0F);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_LINE_SMOOTH);
						GL11.glEnable(GL13.GL_MULTISAMPLE);
						GL11.glAlphaFunc(GL11.GL_GREATER, 0.09F);
						GL11.glDepthMask(false);

						GL11.glPushMatrix();
						double px = -(renderPosX - n.getX());
						double py = -(renderPosY - n.getY());
						double pz = -(renderPosZ - n.getZ());
						AxisAlignedBB bb = AxisAlignedBB.fromBounds(px - 10.99, py - 11.99, pz - 10.99, px + 11.99,
								py + 10.99, pz + 11.99);

						int color = getColor(n.hoursToDate());
						switch (color) {
						case 0:
							GL11.glColor4d(0, 0.56, 1, 0.25);
							break;
						case 1:
							GL11.glColor4d(0.11, 0.91, 0.0D, 0.25);
							break;
						case 2:
							GL11.glColor4d(0.88, 0.83, 0.0D, 0.25);
							break;
						case 3:
							GL11.glColor4d(0.75, 0.0, 0.91, 0.25);
							break;
						}
						drawCrossedOutlinedBoundingBox(bb);
						switch (color) {
						case 0:
							GL11.glColor4d(0, 0.56, 1, 0.1);
							break;
						case 1:
							GL11.glColor4d(0.11, 0.91, 0.0D, 0.1);
							break;
						case 2:
							GL11.glColor4d(0.88, 0.83, 0.0D, 0.1);
							break;
						case 3:
							GL11.glColor4d(0.75, 0.0, 0.91, 0.1);
							break;
						}
						drawBoundingBoxQuads(bb);
						GL11.glPopMatrix();

						GL11.glDepthMask(true);
						GL11.glDisable(GL11.GL_LINE_SMOOTH);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glDisable(GL13.GL_MULTISAMPLE);
						GL11.glEnable(GL11.GL_LIGHTING);

						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						GL11.glLineWidth(5F);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_LINE_SMOOTH);
						GL11.glEnable(GL13.GL_MULTISAMPLE);
						GL11.glAlphaFunc(GL11.GL_GREATER, 0.09F);
						GL11.glDepthMask(false);

						GL11.glPushMatrix();
						px = -(renderPosX - n.getX());
						py = -(renderPosY - n.getY());
						pz = -(renderPosZ - n.getZ());
						bb = AxisAlignedBB.fromBounds(px - 0.01, py - 0.01, pz - 0.01, px + 0.99, py + 0.99, pz + 0.99);

						switch (color) {
						case 0:
							GL11.glColor4d(0, 0.56, 1, 0.25);
							break;
						case 1:
							GL11.glColor4d(0.11, 0.91, 0.0D, 0.25);
							break;
						case 2:
							GL11.glColor4d(0.88, 0.83, 0.0D, 0.25);
							break;
						case 3:
							GL11.glColor4d(0.75, 0.0, 0.91, 0.25);
							break;
						}
						drawCrossedOutlinedBoundingBox(bb);
						switch (color) {
						case 0:
							GL11.glColor4d(0, 0.56, 1, 0.25);
							break;
						case 1:
							GL11.glColor4d(0.11, 0.91, 0.0D, 0.25);
							break;
						case 2:
							GL11.glColor4d(0.88, 0.83, 0.0D, 0.25);
							break;
						case 3:
							GL11.glColor4d(0.75, 0.0, 0.91, 0.25);
							break;
						}
						drawBoundingBoxQuads(bb);
						GL11.glPopMatrix();

						GL11.glDepthMask(true);
						GL11.glDisable(GL11.GL_LINE_SMOOTH);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glDisable(GL13.GL_MULTISAMPLE);
						GL11.glEnable(GL11.GL_LIGHTING);
					}
				}
				for (Block b : SV.instance.blockList) {
					if (b.getDistance() < SV.settings.renderDistance * 16 && !(b.getX() == 0 && b.getY() == 0 && b.getZ() == 0)) {
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						GL11.glLineWidth(5F);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_LINE_SMOOTH);
						GL11.glEnable(GL13.GL_MULTISAMPLE);
						GL11.glAlphaFunc(GL11.GL_GREATER, 0.09F);
						GL11.glDepthMask(false);

						GL11.glPushMatrix();
						double px = -(renderPosX - b.getX());
						double py = -(renderPosY - b.getY());
						double pz = -(renderPosZ - b.getZ());
						AxisAlignedBB bb = AxisAlignedBB.fromBounds(px - 0.01, py - 0.01, pz - 0.01, px + 0.99,
								py + 0.99, pz + 0.99);

						switch (b.getType()) {
						case USED:
							GL11.glColor4d(0.55, 0.98, 0.81, 0.25);
							break;
						case REMOVED:
							GL11.glColor4d(1.0, 0.24, 0.2, 0.25);
							break;
						case PLACED:
							GL11.glColor4d(1.0, 0.85, 0.2, 0.25);
							break;
						}
						drawCrossedOutlinedBoundingBox(bb);
						switch (b.getType()) {
						case USED:
							GL11.glColor4d(0.55, 0.98, 0.81, 0.25);
							break;
						case REMOVED:
							GL11.glColor4d(1.0, 0.24, 0.2, 0.25);
							break;
						case PLACED:
							GL11.glColor4d(1.0, 0.85, 0.2, 0.25);
							break;
						}
						drawBoundingBoxQuads(bb);

						GL11.glPopMatrix();

						GL11.glDepthMask(true);
						GL11.glDisable(GL11.GL_LINE_SMOOTH);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glDisable(GL13.GL_MULTISAMPLE);
						GL11.glEnable(GL11.GL_LIGHTING);
					}
				}
			} catch (NullPointerException e) {
				logger.error("An exception has been thrown!", e);
			} catch (Exception e) {
				logger.error("Unexpected exception while rendering snitch in world", e);
			}
		}
	}

	private void drawBoundingBoxQuads(AxisAlignedBB bb) {
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.startDrawingQuads();
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.minX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.minZ);
		wr.addVertex(bb.minX, bb.minY, bb.minZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.minZ);
		wr.addVertex(bb.maxX, bb.minY, bb.minZ);
		tess.draw();
		wr.startDrawingQuads();
		wr.addVertex(bb.minX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.minY, bb.minZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.minZ);
		wr.addVertex(bb.minX, bb.minY, bb.minZ);
		wr.addVertex(bb.minX, bb.maxY, bb.minZ);
		tess.draw();
		wr.startDrawingQuads();
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.minY, bb.minZ);
		wr.addVertex(bb.maxX, bb.minY, bb.minZ);
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.minY, bb.minZ);
		wr.addVertex(bb.minX, bb.minY, bb.minZ);
		wr.addVertex(bb.minX, bb.minY, bb.maxZ);
		tess.draw();
		wr.startDrawingQuads();
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.minZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.minZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.minZ);
		wr.addVertex(bb.minX, bb.maxY, bb.minZ);
		wr.addVertex(bb.minX, bb.maxY, bb.maxZ);
		tess.draw();
		wr.startDrawingQuads();
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.minZ);
		wr.addVertex(bb.maxX, bb.minY, bb.minZ);
		wr.addVertex(bb.minX, bb.maxY, bb.minZ);
		wr.addVertex(bb.minX, bb.minY, bb.minZ);
		wr.addVertex(bb.minX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.minX, bb.minY, bb.maxZ);
		tess.draw();
		wr.startDrawingQuads();
		wr.addVertex(bb.maxX, bb.minY, bb.minZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.minZ);
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.minX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.minX, bb.minY, bb.minZ);
		wr.addVertex(bb.minX, bb.maxY, bb.minZ);
		tess.draw();
	}

	public static void drawCrossedOutlinedBoundingBox(AxisAlignedBB bb) {
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		wr.startDrawing(GL11.GL_LINE_STRIP);
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.minZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.minZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		tess.draw();
		wr.startDrawing(GL11.GL_LINE_STRIP);
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.minY, bb.minZ);
		wr.addVertex(bb.maxX, bb.minY, bb.minZ);
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		tess.draw();
		wr.startDrawing(GL11.GL_LINE_STRIP);
		wr.addVertex(bb.maxX, bb.minY, bb.maxZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		tess.draw();
		wr.startDrawing(GL11.GL_LINE_STRIP);
		wr.addVertex(bb.maxX, bb.minY, bb.minZ);
		wr.addVertex(bb.maxX, bb.maxY, bb.minZ);
		tess.draw();
		wr.startDrawing(GL11.GL_LINE_STRIP);
		wr.addVertex(bb.minX, bb.minY, bb.maxZ);
		wr.addVertex(bb.minX, bb.maxY, bb.maxZ);
		tess.draw();
		wr.startDrawing(GL11.GL_LINE_STRIP);
		wr.addVertex(bb.minX, bb.minY, bb.minZ);
		wr.addVertex(bb.minX, bb.maxY, bb.minZ);
		tess.draw();
	}

	// TODO: configurable
	public int getColor(Double time) {
		if (time < 0) {
			return 3;
		}
		if (time < 24) {
			return 2;
		}
		if (time < 168) {
			return 1;
		}
		return 0;
	}
}
