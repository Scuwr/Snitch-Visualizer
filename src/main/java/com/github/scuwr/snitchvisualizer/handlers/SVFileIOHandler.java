package com.github.scuwr.snitchvisualizer.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.github.scuwr.snitchvisualizer.SV;
import com.github.scuwr.snitchvisualizer.classobjects.Snitch;

import net.minecraft.client.Minecraft;

public class SVFileIOHandler {

	public static File snitchList = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/SnitchList.txt");
	public static boolean isDone = false;
	
	public static void saveList(){
		isDone = false;
		try {
			if(!snitchList.exists()){
				SV.instance.logger.info("Creating new file: SnitchList.txt");
				snitchList.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(snitchList));
			bw.write("Config:\r\n");
			bw.write("\tRendering: " + SV.instance.config.rendering + "\r\n");
			bw.write("\tUpdateDetection: " + SV.instance.config.updateDetection + "\r\n");
			bw.write("SnitchList:\r\n");
			for(Snitch n : SV.instance.snitchList){
				bw.write(n.x + ";" + n.y + ";" + n.z + ";" + n.cullTime.getTime() + ";" + n.ctGroup + ";" + n.type + ";" + "\r\n");
			}
			bw.close();
		} catch (IOException e){
			SV.instance.logger.error("Failed to write to SnitchList.txt!");
		}
		isDone = true;
	}
	
	public static void loadList(){
		isDone = false;
		try{
			if(!snitchList.exists()) saveList();
			
			BufferedReader br = new BufferedReader(new FileReader(snitchList));
			String line = br.readLine();
			while(!line.contains("SnitchList:"))
				line = br.readLine();
			while(line != null){
				String tokens[] = line.split(";");
				if(tokens.length > 5){
					SV.instance.snitchList.add(new Snitch(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), hoursToDate(Long.parseLong(tokens[3])), tokens[4], tokens[5]));
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e){
			SV.instance.logger.error("Failed to load SnitchList.txt!");
		}
		isDone = true;
	}

	private static double hoursToDate(long l) {
		Date oldDate = new Date();
	    return (l - oldDate.getTime()) / Snitch.HOURS_IN_MILLIS;
	}
}
