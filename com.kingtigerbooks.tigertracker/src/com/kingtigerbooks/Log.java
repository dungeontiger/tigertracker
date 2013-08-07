package com.kingtigerbooks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

// TIME, TYPE, MSG, DETAILS

public class Log {

	private PrintWriter writer;
	
	public Log(String file) {
		try
		{
			File logFile = new File(file);
			writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
		}
		catch (IOException e) 
		{
			// kind of bad if we get an exception trying to create the log to record exceptions
			e.printStackTrace();
		}
	}
	
	public void writeException(Exception e, String msg) {
		writer.println(getDateTimeString(Calendar.getInstance()) + "," + "EXCEPTION" + ",\"" + msg + "\",\"" + e.getMessage() + "\"" );
		writer.flush();
	}
	
	public void writeError(String msg) {
		writer.println(getDateTimeString(Calendar.getInstance()) + ",,\"" + msg + "\",");
		writer.flush();
	}
	
	public void release() {
		writer.close();
	}
	
	private static String getDateTimeString(Calendar time) {
		// TODO: These Calendar functions are locale specific
		String dateTime = String.valueOf(time.get(Calendar.YEAR)) + "-" + String.valueOf(time.get(Calendar.MONTH) + 1) + "-" + String.valueOf(time.get(Calendar.DAY_OF_MONTH));
		dateTime += " ";
		dateTime += String.valueOf(time.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(time.get(Calendar.MINUTE)) + ":" + String.valueOf(time.get(Calendar.SECOND));
		return dateTime;
	}
}
