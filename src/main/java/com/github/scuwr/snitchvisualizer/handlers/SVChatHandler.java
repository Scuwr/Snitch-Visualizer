package com.github.scuwr.snitchvisualizer.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Block;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

/**
 * Chat handler for Snitch Visualizer
 * 
 * @author Scuwr
 *
 */
public class SVChatHandler {

	public static boolean updateSnitchList = false;
	public static boolean snitchReport = false;
	public static String snitchReportName = "";
	public static int jalistIndex = 1;
	public static int jainfoIndex = 1;
	public static ArrayList<Block> tempList;

	private static Logger logger = LogManager.getLogger("SnitchVisualizer");

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) {
		if (event != null && event.message != null) {
			String msg = event.message.getUnformattedText();
			if (msg == null) {
				return;
			}
			if (!msg.contains("*") && msg.contains("world")) {
				parseSnitch(msg);
			} else if (msg.contains("*") && msg.contains("snitch at")) {
				// parseSnitch(msg)
			} else if (msg.contains("Used") || msg.contains("Block Break") || msg.contains("Block Place")) {
				// render block place/break/use
				parseBlock(msg);
			} else if (msg.contains("Entry")) {
				if (snitchReport)
					parseEntry(msg);
			} else if (msg.contains("Snitch Log for") || msg.contains("Page 1 is empty for snitch")) {
				// export jainfo to csv
				if (SVPlayerHandler.snitchIndex > -1) { // fix issue
					Snitch n = SV.instance.snitchList.get(SVPlayerHandler.snitchIndex);
					String name = parseSnitchName(msg);
					n.name = name;
					if (SVPlayerHandler.updateSnitchName) {
						SVPlayerHandler.updateSnitchName = false; // done!
					}
					SVFileIOHandler.saveList();

					if (snitchReport) {
						if (snitchReportName.equals("")) {
							snitchReportName = name;
						}
						if (!snitchReportName.equals(name)) {
							snitchReport = false;
							SVFileIOHandler.saveSnitchReport(snitchReportName);
							snitchReportName = "";
						}
					}
				}
			} else if (msg.contains("Unknown command") || msg.contains(" is empty")) {
				jalistIndex = 1;
				jainfoIndex = 1;
				updateSnitchList = false;
				if (snitchReport) {
					snitchReport = false;
					SVFileIOHandler.saveSnitchReport(snitchReportName);
					snitchReportName = "";
				}
			} else if (msg.contains("TPS from last 1m, 5m, 15m:")) {
				ParseTPS(msg);
			}
		}
	}

	public static void updateSnitchList() {
		Minecraft.getMinecraft().thePlayer.sendChatMessage("/tps");
		SVTickHandler.start = new Date();
		updateSnitchList = true;
	}

	public void ParseTPS(String msg) {
		msg = msg.substring(msg.indexOf(':') + 1);
		String[] tokens = msg.split(", +"); // " 11.13, 11.25, 11.32"
		if (tokens.length > 2) {
			double a = 20.0;
			double b = 20.0;
			double c = 20.0;
			try {
				a = Double.parseDouble(tokens[0]);
				b = Double.parseDouble(tokens[1]);
				c = Double.parseDouble(tokens[2]);
			} catch (Exception e) {
				// replace with something specific, but when TPS
				// is 20, game instagibs here.
				logger.error("Failed to parse TPS:" + e.getMessage());
			}
			if (a < b && a < c) {
				SVTickHandler.waitTime = SVTickHandler.tickTimeout / a;
			} else if (b < a && b < c) {
				SVTickHandler.waitTime = SVTickHandler.tickTimeout / b;
			} else {
				SVTickHandler.waitTime = SVTickHandler.tickTimeout / c;
			}
		} else {
			SVTickHandler.waitTime = 4;
		}

		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Timeout between commands is "
				+ Double.toString(SVTickHandler.waitTime) + " seconds."));
	}

	public void parseSnitch(String msg) {
		if (msg.contains("[")) {
			msg = msg.substring(msg.indexOf("[") + 1);
			logger.info("Parsing string [" + msg);
			String[] tokens = msg.split("[ \\[\\]]+");
			if (tokens.length == 5) {
				try {
					int x = Integer.parseInt(tokens[0]);
					int y = Integer.parseInt(tokens[1]);
					int z = Integer.parseInt(tokens[2]);
					double cullTime = Double.parseDouble(tokens[3]);
					String ctGroup = tokens[4];

					Snitch n = new Snitch(x, y, z, cullTime, ctGroup, null);

					int index = Collections.binarySearch(SV.instance.snitchList, n);
					if (index < 0 || 0 != n.compareTo(SV.instance.snitchList.get(index))) {
						SV.instance.snitchList.add(n);
						Collections.sort(SV.instance.snitchList);
						SVFileIOHandler.saveList();
					} else {
						SV.instance.snitchList.get(index).ctGroup = ctGroup;
						SV.instance.snitchList.get(index).cullTime = Snitch.changeToDate(cullTime);
						SVFileIOHandler.saveList();
					}
				} catch (NumberFormatException e) {
					logger.error("Failed to parse snitch from chat!");
				} catch (NullPointerException e) {
					logger.error("Failed to create snitch instance!");
				}
			}
		}
	}

	public void parseBlock(String msg) {
		if (msg.contains(">")) {
			msg = msg.substring(msg.indexOf(">") + 1);
			logger.info("Parsing string " + msg);
			String[] tokens = msg.split(" +|\\[|\\]");
			Block.Action type = Block.Action.NOP;
			if (tokens[2].equals("Used")) {
				type = Block.Action.USED;
			} else if (tokens[1].equals("Removed")) {
				type = Block.Action.REMOVED;
			} else if (tokens[1].equals("Placed")) {
				type = Block.Action.PLACED;
			}

			try {
				int x = Integer.parseInt(tokens[5]);
				int y = Integer.parseInt(tokens[6]);
				int z = Integer.parseInt(tokens[7]);

				if (type != Block.Action.NOP) {
					if (!snitchReport) {
						SV.instance.blockList.add(new Block(x, y, z, type, tokens[1], "BlockID: " + tokens[4]));
					} else {
						tempList.add(new Block(x, y, z, type, tokens[1], "BlockID: " + tokens[4]));
					}
				}
			} catch (NumberFormatException e) {
				logger.error("Failed to parse block from chat!");
			} catch (NullPointerException e) {
				logger.error("Failed to create block instance!");
			}
		}
	}

	public void parseEntry(String msg) {
		if (msg.contains(">")) {
			msg = msg.substring(msg.indexOf(">") + 1);
			logger.info("Parsing string " + msg);
			String[] tokens = msg.split(" +");
			if (tokens[2].equals("Entry")) {
				tempList.add(new Block(0, 0, 0, Block.Action.ENTRY, tokens[1], tokens[3] + " " + tokens[4]));
			}
		}
	}

	public String parseSnitchName(String msg) {
		String tokens[] = msg.split(" +|-+");
		return tokens[tokens.length - 1];
	}
}
