package com.github.scuwr.snitchvisualizer;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.scuwr.snitchvisualizer.classobjects.SVConfig;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;
import com.github.scuwr.snitchvisualizer.handlers.SVChatHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVFileIOHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVKeyHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVPlayerHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVRenderHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVTickHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

/**
 * Main class file for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
@Mod(modid = SV.MODID, name = SV.MODNAME, version = SV.MODVERSION)
public class SV {
	
	public static final String MODID = "scuwrsnitchvisualizer";
	public static final String MODNAME = "Snitch Visualizer";
	public static final String MODVERSION = "1.0.1";
	
	@Instance("SV")
	public static SV instance;
	
	public ArrayList<Snitch> snitchList;
	public static SVConfig config;
	public static Logger logger = LogManager.getLogger("SnitchVisualizer");
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		logger.info("Loading: preInit");
		instance = this;
		this.snitchList = new ArrayList();
		this.config = new SVConfig(true, true);
		SVFileIOHandler.loadList();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event){
		logger.info("Loading: init");
		FMLCommonHandler.instance().bus().register(new SVKeyHandler());
		MinecraftForge.EVENT_BUS.register(new SVRenderHandler());
		MinecraftForge.EVENT_BUS.register(new SVChatHandler());
		MinecraftForge.EVENT_BUS.register(new SVPlayerHandler());
		FMLCommonHandler.instance().bus().register(new SVTickHandler());
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		
	}
}
