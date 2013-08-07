package com.kingtigerbooks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class EventManager {
	
	private static DatabaseLogger dbLogger;
	
	public static void notify(Event event) {
		try {
			
			if (TigerTracker.getSettings().useFlatFile()) {
				temporaryCSV(event);
			}
			
			if (TigerTracker.getSettings().useDatabase()) {
				dbLogger = new DatabaseLogger();
				// TODO: temporary
				dbLogger.connect();
				dbLogger.writeEvent(event);
				dbLogger.release();
			}
		}
		catch (Exception e)
		{
			// TODO: Do something good
		}
	}
	
	private static void temporaryCSV(Event event) throws IOException {	
		File eventFile = new File(TigerTracker.getSettings().flatFilePath());
		boolean bWriteHeader = !eventFile.exists();
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(eventFile, true)));
		
		if (bWriteHeader) {
			writer.println("DATE_TIME,TYPE,SOURCE,X,Y,Z,DETAILS");
		}
		
		//
		// dateTime,type,source,x,y,z,details
		//
		String line = getDateTimeString(event.time) + "," + getEventTypeString(event.type) + "," + event.player + ",";
		if (event.x != null && event.y != null && event.z != null) {
			line += String.valueOf(event.x) + "," + String.valueOf(event.y) + "," + String.valueOf(event.z) ;
		} else {
			
		}
		line += ",";
		if (event.details != null) {
			line += "\"" + event.details + "\"";
		}
		writer.println(line);
		writer.close();
	}
	
	private static String getEventTypeString(int type) {
		switch (type)
		{
		case EventType.PLAYER_CONNECT:
			return "PLAYER_CONNECT";
		case EventType.PLAYER_CHAT:
			return "PLAYER_CHAT";
		case EventType.COMMAND:
			return "COMMAND";
		case EventType.PLAYER_DEATH:
			return "PLAYER_DEATH";
		case EventType.PLAYER_DISCONNECT:
			return "PlAYER_DISCONNECT";
		case EventType.PLUGIN_DISABLED:
			return "PLUGIN_DISABLED";
		case EventType.PLUGIN_ENABLED:
			return "PLUGIN_ENABLED";
		case EventType.PlAYER_PICKUP:
			return "PLAYER_PICKUP";
		case EventType.MOB_DEATH:
			return "MOB_DEATH";
		}
		return "UNKNOWN";
	}
}
