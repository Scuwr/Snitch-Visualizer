package com.github.scuwr.snitchvisualizer.handlers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Block;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Render Hander for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
public class SVRenderHandler {
	
	@SubscribeEvent
	public void eventRenderWorld(RenderWorldLastEvent event){
		if(SVFileIOHandler.isDone && SV.settings.renderEnabled){
			try{
				for(Snitch n : SV.instance.snitchList){
					if(n.getDistance() < SV.settings.renderDistance * 16){
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
				        double px = -(RenderManager.renderPosX - n.x);
				        double py = -(RenderManager.renderPosY - n.y);
				        double pz = -(RenderManager.renderPosZ - n.z);
				        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(px - 10.99, py - 11.99, pz - 10.99, px + 11.99, py + 10.99, pz + 11.99);
					
				        int color = getColor(n.hoursToDate());
				        switch(color){
				        	case 0: GL11.glColor4d(0, 0.56, 1, 0.25); break;
				        	case 1: GL11.glColor4d(0.11, 0.91, 0.0D, 0.25); break;
				        	case 2: GL11.glColor4d(0.88, 0.83, 0.0D, 0.25); break;
				        	case 3: GL11.glColor4d(0.75, 0.0, 0.91, 0.25); break;
				        }
				        drawCrossedOutlinedBoundingBox(bb);
				        switch(color){
			        		case 0: GL11.glColor4d(0, 0.56, 1, 0.1); break;
			        		case 1: GL11.glColor4d(0.11, 0.91, 0.0D, 0.1); break;
			        		case 2: GL11.glColor4d(0.88, 0.83, 0.0D, 0.1); break;
			        		case 3: GL11.glColor4d(0.75, 0.0, 0.91, 0.1); break;
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
				        px = -(RenderManager.renderPosX - n.x);
				        py = -(RenderManager.renderPosY - n.y);
				        pz = -(RenderManager.renderPosZ - n.z);
				        bb = AxisAlignedBB.getBoundingBox(px - 0.01, py - 0.01, pz - 0.01, px + 0.99, py + 0.99, pz + 0.99);
				        
				        switch(color){
				        	case 0: GL11.glColor4d(0, 0.56, 1, 0.25); break;
				        	case 1: GL11.glColor4d(0.11, 0.91, 0.0D, 0.25); break;
				        	case 2: GL11.glColor4d(0.88, 0.83, 0.0D, 0.25); break;
				        	case 3: GL11.glColor4d(0.75, 0.0, 0.91, 0.25); break;
				        }
			        	drawCrossedOutlinedBoundingBox(bb);
			        	switch(color){
				        	case 0: GL11.glColor4d(0, 0.56, 1, 0.25); break;
				        	case 1: GL11.glColor4d(0.11, 0.91, 0.0D, 0.25); break;
				        	case 2: GL11.glColor4d(0.88, 0.83, 0.0D, 0.25); break;
				        	case 3: GL11.glColor4d(0.75, 0.0, 0.91, 0.25); break;
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
				if(SV.instance.blockList != null){
					for(Block b : SV.instance.blockList){
						if(b.getDistance() < SV.settings.renderDistance * 16){
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
					        double px = -(RenderManager.renderPosX - b.x);
					        double py = -(RenderManager.renderPosY - b.y);
					        double pz = -(RenderManager.renderPosZ - b.z);
					        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(px - 0.01, py - 0.01, pz - 0.01, px + 0.99, py + 0.99, pz + 0.99);
					        
					        int blockType = b.type;
					        switch(blockType){
					        	case Block.USED: GL11.glColor4d(0.55, 0.98, 0.81, 0.25); break;
					        	case Block.REMOVED: GL11.glColor4d(1.0, 0.24, 0.2, 0.25); break;
					        	case Block.PLACED: GL11.glColor4d(1.0, 0.85, 0.2, 0.25); break;
					        }
				        	drawCrossedOutlinedBoundingBox(bb);
				        	switch(blockType){
					        	case Block.USED: GL11.glColor4d(0.55, 0.98, 0.81, 0.25); break;
					        	case Block.REMOVED: GL11.glColor4d(1.0, 0.24, 0.2, 0.25); break;
					        	case Block.PLACED: GL11.glColor4d(1.0, 0.85, 0.2, 0.25); break;
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
				}
			}catch(NullPointerException e){System.out.println("An exception has been thrown!\n" + e.getMessage());}
		}
	}
	
	private void drawBoundingBoxQuads(AxisAlignedBB bb) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.maxZ);
		t.addVertex(bb.minX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.minZ);
		t.addVertex(bb.minX, bb.minY, bb.minZ);
		t.addVertex(bb.maxX, bb.maxY, bb.minZ);
		t.addVertex(bb.maxX, bb.minY, bb.minZ);
		t.draw();
		t.startDrawingQuads();
		t.addVertex(bb.minX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.maxZ);
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.addVertex(bb.maxX, bb.minY, bb.minZ);
		t.addVertex(bb.maxX, bb.maxY, bb.minZ);
		t.addVertex(bb.minX, bb.minY, bb.minZ);
		t.addVertex(bb.minX, bb.maxY, bb.minZ);
		t.draw();
		t.startDrawingQuads();
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.minY, bb.minZ);
		t.addVertex(bb.maxX, bb.minY, bb.minZ);
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.addVertex(bb.maxX, bb.minY, bb.minZ);
		t.addVertex(bb.minX, bb.minY, bb.minZ);
		t.addVertex(bb.minX, bb.minY, bb.maxZ);
		t.draw();
		t.startDrawingQuads();
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.minZ);
		t.addVertex(bb.maxX, bb.maxY, bb.minZ);
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.addVertex(bb.maxX, bb.maxY, bb.minZ);
		t.addVertex(bb.minX, bb.maxY, bb.minZ);
		t.addVertex(bb.minX, bb.maxY, bb.maxZ);
		t.draw();
		t.startDrawingQuads();
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.addVertex(bb.maxX, bb.maxY, bb.minZ);
		t.addVertex(bb.maxX, bb.minY, bb.minZ);
		t.addVertex(bb.minX, bb.maxY, bb.minZ);
		t.addVertex(bb.minX, bb.minY, bb.minZ);
		t.addVertex(bb.minX, bb.maxY, bb.maxZ);
		t.addVertex(bb.minX, bb.minY, bb.maxZ);
		t.draw();
		t.startDrawingQuads();
		t.addVertex(bb.maxX, bb.minY, bb.minZ);
		t.addVertex(bb.maxX, bb.maxY, bb.minZ);
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.addVertex(bb.minX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.maxZ);
		t.addVertex(bb.minX, bb.minY, bb.minZ);
		t.addVertex(bb.minX, bb.maxY, bb.minZ);
		t.draw();
	}

	public static void drawCrossedOutlinedBoundingBox(AxisAlignedBB bb){
		Tessellator t = Tessellator.instance;
		t.startDrawing(GL11.GL_LINE_STRIP);
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.minZ);
		t.addVertex(bb.maxX, bb.maxY, bb.minZ);
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.draw();
		t.startDrawing(GL11.GL_LINE_STRIP);
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.minY, bb.minZ);
		t.addVertex(bb.maxX, bb.minY, bb.minZ);
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.draw();
		t.startDrawing(GL11.GL_LINE_STRIP);
		t.addVertex(bb.maxX, bb.minY, bb.maxZ);
		t.addVertex(bb.maxX, bb.maxY, bb.maxZ);
		t.draw();
		t.startDrawing(GL11.GL_LINE_STRIP);
		t.addVertex(bb.maxX, bb.minY, bb.minZ);
		t.addVertex(bb.maxX, bb.maxY, bb.minZ);
		t.draw();
		t.startDrawing(GL11.GL_LINE_STRIP);
		t.addVertex(bb.minX, bb.minY, bb.maxZ);
		t.addVertex(bb.minX, bb.maxY, bb.maxZ);
		t.draw();
		t.startDrawing(GL11.GL_LINE_STRIP);
		t.addVertex(bb.minX, bb.minY, bb.minZ);
		t.addVertex(bb.minX, bb.maxY, bb.minZ);
		t.draw();
	}
	
	public int getColor(Double time){
		if (time < 0) return 3;
		if (time < 24) return 2;
		if (time < 168) return 1;
		return 0;
	}
}
