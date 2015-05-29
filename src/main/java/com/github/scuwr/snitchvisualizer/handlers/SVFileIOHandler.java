package com.github.scuwr.snitchvisualizer.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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
	public static File svSettings = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir
			+ "/SVSettings.txt");
	public static File reportDir = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir + folderReport);
	public static File svDir = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir);
	public static boolean isDone = false;

	private static Logger logger = LogManager.getLogger("SnitchVisualizer");

	public static void saveList() {
		isDone = false;
		try {
			if (!svDir.exists()) {
				logger.info("Creating Snitch Visualizer Directory");
				if (!svDir.mkdirs()) {
					logger.error("Failed to create Snitch Visualizer Directory!");
				}
			}
			if (!snitchList.exists()) {
				logger.info("Creating new file: SnitchList.csv");
				snitchList.createNewFile();
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(snitchList));
			logger.info("Saving Snitch list.. " + SV.instance.snitchList.size() + " snitches to save.");
			for (Snitch n : SV.instance.snitchList) {
				bw.write(n.x + "," + n.y + "," + n.z + "," + n.cullTime.getTime() + "," + n.ctGroup + "," + n.name
						+ "," + "\r\n");
			}
			bw.close();
		} catch (IOException e) {
			logger.error("Failed to write to SnitchList.csv!\n" + e.getMessage());
		}
		isDone = true;
	}

	public static void saveSettings() {
		isDone = false;
		try {
			if (!svDir.exists()) {
				logger.info("Creating Snitch Visualizer Directory");
				if (!svDir.mkdirs()) {
					logger.error("Failed to create Snitch Visualizer Directory!");
				}
			}
			if (!svSettings.exists()) {
				logger.info("Creating new file: SVSettings.txt");
				svSettings.createNewFile();
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(svSettings));
			bw.write(SV.settings.getKeyBinding(SVSettings.Options.UPDATE_DETECTION) + ";\r\n");
			bw.write(SV.settings.getKeyBinding(SVSettings.Options.RENDER_DISTANCE) + ";\r\n");
			bw.write(SV.settings.getKeyBinding(SVSettings.Options.RENDER_ENABLED) + ";\r\n");

			bw.close();
		} catch (IOException e) {
			logger.error("Failed to write to SVSettings.txt!\n" + e.getMessage());
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
				if (tokens.length > 5) {
					SV.instance.snitchList.add(new Snitch(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]),
							Integer.parseInt(tokens[2]), hoursToDate(Long.parseLong(tokens[3])), tokens[4], tokens[5]));
				}
				line = br.readLine();
			}
			br.close();

			if (oldSnitchList.exists()) {
				oldSnitchList.delete();
				saveList();
			}
		} catch (IOException e) {
			logger.error("Failed to load SnitchList.csv!\n" + e.getMessage());
		} catch (NullPointerException e) {
			logger.error("SnitchList.csv does not exist!\n" + e.getMessage());
		} catch (NumberFormatException e) {
			logger.error("Could not parse integer from list!\n" + e.getMessage());
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
			logger.error("Failed to load SnitchList.csv!\n" + e.getMessage());
		} catch (NullPointerException e) {
			logger.error("SnitchList.csv does not exist!\n" + e.getMessage());
		}
		isDone = true;
	}

	private static double hoursToDate(long l) {
		Date oldDate = new Date();
		return (l - oldDate.getTime()) / Snitch.HOURS_IN_MILLIS;
	}

	public static void saveSnitchReport(String snitchName) {
		isDone = false;
		if (!snitchName.equals("")) {
			try {
				if (!svDir.exists()) {
					logger.info("Creating Snitch Visualizer Directory");
					if (!svDir.mkdirs()) {
						logger.error("Failed to create Snitch Visualizer Directory!");
					}
				}
				if (!reportDir.exists()) {
					logger.info("Creating Snitch Report Directory");
					reportDir.createNewFile();
				}
				File snitchReport = new File(Minecraft.getMinecraft().mcDataDir.toString() + folderDir + folderReport
						+ "/" + snitchName + ".csv");
				snitchReport.createNewFile();

				BufferedWriter bw = new BufferedWriter(new FileWriter(snitchReport));
				logger.info("Saving Snitch list.. " + SVChatHandler.tempList.size() + " snitches to save.");
				for (Block b : SVChatHandler.tempList) {
					String type = "";
					switch (b.type) {
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
					case NOP:
						type = "Unrecognized";
						break;
					default:
						break;
					}
					bw.write(b.playerName + "," + type + "," + b.details + "," + b.x + "," + b.y + "," + b.z + ","
							+ "\r\n");
				}
				bw.close();
			} catch (IOException e) {
				logger.error("Failed to write to Snitch Report!\n" + e.getMessage());
			}
		}
		isDone = true;
	}
}
