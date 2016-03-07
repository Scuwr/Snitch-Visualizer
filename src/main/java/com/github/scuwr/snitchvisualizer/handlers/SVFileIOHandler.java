package com.github.scuwr.snitchvisualizer.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.SVSettings;
import com.github.scuwr.snitchvisualizer.classobjects.Block;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

/**
 * File I/O Handler for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
public class SVFileIOHandler {

	private static String folderDir = "/mods/Snitch-Visualizer";
	private static String folderReport = "/Reports";

	public static File oldSnitchList = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/SnitchList.txt");
	public static File snitchList = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir
			+ "/SnitchList.csv");
	public static File worldList = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir
			+ "/WorldList.csv");
	public static File svSettings = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir
			+ "/SVSettings.txt");
	public static File reportDir = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir + folderReport);
	public static File svDir = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir);
	public static boolean isDone = false;

	private static Logger logger = LogManager.getLogger("SnitchVisualizer");

	private static boolean prepareFile(File basedir, File savefile) throws IOException {
		if (!basedir.exists()) {
			logger.info("Creating Snitch Visualizer Directory");
			if (!basedir.mkdirs()) {
				logger.error("Failed to create Snitch Visualizer Directory!");
				return false;
			}
		}
		if (!savefile.exists()) {
			logger.info("Creating new file: " + savefile.getName());
			savefile.createNewFile();
		}
		return true;
	}
	
	public static void saveWorlds() {
		isDone = false;
		try{
			prepareFile(svDir, worldList);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(worldList));
			
			for (Map.Entry<String,String> entry : SV.instance.worldList.entrySet()) {
				bw.write(entry.getKey() + "," + entry.getValue() + ",\r\n");
			}
			
			bw.close();
		} catch (IOException e) {
			logger.error("Failed to write to WorldList.csv!", e);
		} catch (Exception f) {
			logger.error("General failure while writing WorldList.csv", f);
		}
		isDone = true;
	}
	
	public static void saveList() {
		isDone = false;
		try {
			prepareFile(svDir, snitchList);

			BufferedWriter bw = new BufferedWriter(new FileWriter(snitchList));
			logger.info("Saving Snitch list.. " + SV.instance.snitchList.size() + " snitches to save.");
			for (Snitch n : SV.instance.snitchList) {
				bw.write(n.getWorld() + "," + n.getX() + "," + n.getY() + "," + n.getZ() + "," + 
							((n.getRawCullTime() != null) ? n.getRawCullTime().getTime() : " ") + "," +
							n.getCtGroup() + "," + n.getName() + ",\r\n");
			}
			bw.close();
		} catch (IOException e) {
			logger.error("Failed to write to SnitchList.csv!", e);
		} catch (Exception f) {
			logger.error("General failure while writing SnitchList.csv", f);
		}
		isDone = true;
	}

	public static void saveSettings() {
		isDone = false;
		try {
			prepareFile(svDir, svSettings);

			BufferedWriter bw = new BufferedWriter(new FileWriter(svSettings));
			bw.write(SV.settings.getKeyBinding(SVSettings.Options.UPDATE_DETECTION) + ";\r\n");
			bw.write(SV.settings.getKeyBinding(SVSettings.Options.RENDER_DISTANCE) + ";\r\n");
			bw.write(SV.settings.getKeyBinding(SVSettings.Options.RENDER_ENABLED) + ";\r\n");

			bw.close();
		} catch (IOException e) {
			logger.error("Failed to write to SVSettings.txt!", e);
		} catch (Exception f) {
			logger.error("Failure while writing to SVSettings.txt", f);
		}
		isDone = true;
	}

	public static void loadWorlds() {
		isDone = false;
		try{
			if (!worldList.exists()) {
				saveWorlds();
			}
			
			BufferedReader br = new BufferedReader(new FileReader(worldList));
			
			String line = br.readLine();
			while (line != null) {
				String tokens[] = line.split(",");
				if (tokens.length > 1) {
					SV.instance.worldList.put(tokens[0], tokens[1]);
					ArrayList<String> worldsIds = SV.instance.inverseWorldList.get(tokens[1]);
					if (worldsIds == null) {
						worldsIds = new ArrayList<String>();
						SV.instance.inverseWorldList.put(tokens[1], worldsIds);
					}
					worldsIds.add(tokens[0]);
				} else {
					logger.info("Unknown world mapping: " + line);
				}
			}
			
			br.close();
		} catch (IOException e) {
			logger.error("Failed to write to WorldList.csv!", e);
		} catch (Exception f) {
			logger.error("General failure while writing WorldList.csv", f);
		}
		isDone = true;
	}
	
	public static void loadList() {
		isDone = false;
		try {
			if (!snitchList.exists()) {
				saveList();
			}

			BufferedReader br = null;

			if (oldSnitchList.exists()) {
				br = new BufferedReader(new FileReader(oldSnitchList));
			} else {
				br = new BufferedReader(new FileReader(snitchList));
			}
			String line = br.readLine();
			while (line != null) {
				String tokens[] = line.split(",|;");
				if (tokens.length > 6) { // new
					double cullTime = (!tokens[4].trim().equals("")) ? hoursToDate(Long.parseLong(tokens[4])) : -1;
					SV.instance.snitchList.add(new Snitch(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),
							Integer.parseInt(tokens[3]), cullTime, tokens[5], tokens[6]));
				} else if (tokens.length > 5) { //old
					double cullTime = (!tokens[3].trim().equals("")) ? hoursToDate(Long.parseLong(tokens[3])) : -1;
					SV.instance.snitchList.add(new Snitch(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),
							Integer.parseInt(tokens[2]), cullTime, tokens[4], tokens[5]));
				} else {
					logger.info("Snitch line failed to import: " + line);
				}
				line = br.readLine();
			}
			br.close();

			if (oldSnitchList.exists()) {
				oldSnitchList.delete();
				saveList();
			}
		} catch (IOException e) {
			logger.error("Failed to load SnitchList.csv!", e);
		} catch (NullPointerException e) {
			logger.error("SnitchList.csv does not exist!", e);
		} catch (NumberFormatException e) {
			logger.error("Could not parse integer from list!", e);
		} catch (Exception e) {
			logger.error("Failure while parsing SnitchList.csv", e);
		}
		isDone = true;
	}

	public static void loadSettings() {
		isDone = false;
		try {
			if (!svSettings.exists()) {
				saveSettings();
			}
			logger.info("Loading Settings..");

			BufferedReader br = new BufferedReader(new FileReader(svSettings));
			String line = br.readLine();
			while (line != null) {
				String tokens[] = line.split(": |;");
				if (tokens.length > 1) {

					if (tokens[0].contains(StatCollector.translateToLocal(SVSettings.Options.UPDATE_DETECTION
							.getEnumString()))) {
						if (tokens[1].contains(StatCollector.translateToLocal("options.on")))
							SV.settings.setOptionValue(SVSettings.Options.UPDATE_DETECTION, true);
						else
							SV.settings.setOptionValue(SVSettings.Options.UPDATE_DETECTION, false);
					} else if (tokens[0].contains(StatCollector.translateToLocal(SVSettings.Options.RENDER_ENABLED
							.getEnumString()))) {
						if (tokens[1].contains(StatCollector.translateToLocal("options.on")))
							SV.settings.setOptionValue(SVSettings.Options.RENDER_ENABLED, true);
						else
							SV.settings.setOptionValue(SVSettings.Options.RENDER_ENABLED, false);
					} else if (tokens[0].contains(StatCollector.translateToLocal(SVSettings.Options.RENDER_DISTANCE
							.getEnumString()))) {
						String token[] = tokens[1].split(" ");

						if (token[0].contains("MAX"))
							SV.settings.setOptionFloatValue(SVSettings.Options.RENDER_DISTANCE,
									SVSettings.Options.RENDER_DISTANCE.getValueMax());
						else if (token[0].contains("MIN"))
							SV.settings.setOptionFloatValue(SVSettings.Options.RENDER_DISTANCE,
									SVSettings.Options.RENDER_DISTANCE.getValueMin());
						else
							SV.settings.setOptionFloatValue(SVSettings.Options.RENDER_DISTANCE,
									(float) Integer.parseInt(token[0]));
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			logger.error("Failed to load SnitchList.csv!", e);
		} catch (NullPointerException e) {
			logger.error("SnitchList.csv does not exist!", e);
		} catch (Exception e) {
			logger.error("Failure while loading SnitchList.csv", e);
		}
		isDone = true;
	}

	private static double hoursToDate(long l) {
		Date oldDate = new Date();
		return (l - oldDate.getTime()) / Snitch.HOURS_IN_MILLIS;
	}

	public static void saveSnitchReport(String snitchName) {
		isDone = false;
		try {
			if (!snitchName.equals("")) {
				try {
					if(!reportDir.exists()) reportDir.mkdirs();
					File snitchReport = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir + folderReport
							+ "/" + snitchName + ".csv");
					snitchReport.createNewFile();
	
					BufferedWriter bw = new BufferedWriter(new FileWriter(snitchReport));
					logger.info("Saving Snitch list.. " + SVChatHandler.tempList.size() + " snitches to save.");
					for (Block b : SVChatHandler.tempList) {
						String type = "";
						switch (b.getType()) {
						case USED:
							type = "Used";
							break;
						case REMOVED:
							type = "Removed";
							break;
						case PLACED:
							type = "Placed";
							break;
						case ENTRY:
							type = "Entry";
							break;
						case EXCHANGE:
							type = "Exchanged";
							break;
						case DESTROYED:
							type = "Destroyed";
						case NOP:
							type = "Unrecognized";
							break;
						default:
							break;
						}
						bw.write(b.getPlayerName() + "," + type + "," + b.getDetails() + "," + b.getX() + "," + b.getY() + "," + b.getZ() + ","
								+ "\r\n");
					}
					bw.close();
				} catch (IOException e) {
					logger.error("Failed to write to Snitch Report!", e);
				}
			}
		} catch (Exception e) {
			logger.error("General error while writing to Snitch Report", e);
		}
		isDone = true;
	}
}
