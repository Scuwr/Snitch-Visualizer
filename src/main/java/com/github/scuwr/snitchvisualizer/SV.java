package com.github.scuwr.snitchvisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.scuwr.snitchvisualizer.classobjects.Block;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;
import com.github.scuwr.snitchvisualizer.handlers.SVChatHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVFileIOHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVKeyHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVPlayerHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVRenderHandler;
import com.github.scuwr.snitchvisualizer.handlers.SVTickHandler;

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
	public static final String MODVERSION = "1.1.6";

	@Instance("SV")
	public static SV instance;

	/**
	 * TODO replace with better structure. TreeSet won't work.
	 */
	public ArrayList<Snitch> snitchList;
	public ArrayList<Block> blockList;
	public Map<String, String> worldList; 
	public Map<String, ArrayList<String>> inverseWorldList;
	public static SVSettings settings;
	public static Logger logger = LogManager.getLogger("SnitchVisualizer");
	public SVPlayerHandler playerHandler;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Loading: SnitchVisualizer preInit");
		instance = this;
		this.snitchList = new ArrayList<Snitch>();
		this.blockList = new ArrayList<Block>();
		this.worldList = new HashMap<String,String>();
		this.inverseWorldList = new HashMap<String,ArrayList<String>>();
		SV.settings = new SVSettings(this);
		SVFileIOHandler.loadSettings();
		SVFileIOHandler.loadList();
		SVFileIOHandler.loadWorlds();

		if (!(this.snitchList instanceof ArrayList)) {
			logger.info("Snitch List failed to instantiate!");
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Loading: SnitchVisualizer init");
		FMLCommonHandler.instance().bus().register(new SVKeyHandler());
		MinecraftForge.EVENT_BUS.register(new SVRenderHandler());
		MinecraftForge.EVENT_BUS.register(new SVChatHandler());
		playerHandler = new SVPlayerHandler();
		FMLCommonHandler.instance().bus().register(playerHandler);
		FMLCommonHandler.instance().bus().register(new SVTickHandler());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
