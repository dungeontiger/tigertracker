package com.kingtigerbooks;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings {
	
	private final boolean useFlatFile;
	private final String flatFileName;
	
	private final boolean useDatabase;
	private final String databaseName;
	private final String userId;
	private final String password; 				// TODO: How not to keep password in memory?
	private final String connectionString;
	private final String dataFolder;
	private final String logFile;
	
	public Settings(FileConfiguration config, String theDataFolder) {
		useFlatFile = config.getBoolean("flatfile.use");
		flatFileName = config.getString("flatfile.filename");
		
		useDatabase = config.getBoolean("database.use");
		databaseName = config.getString("database.databaseName");
		userId = config.getString("database.userId");
		password = config.getString("database.password");
		connectionString = config.getString("database.connectionString");
		dataFolder = theDataFolder;
		logFile = "tigertracker.log";
	}
	
	public boolean useDatabase() {
		return useDatabase;
	}

	public String databaseName() {
		return databaseName;
	}
	
	public boolean useFlatFile() {
		return useFlatFile;
	}
	
	public String flatFileName() {
		return flatFileName;
	}
	
	public String userId() {
		return userId;
	}
	
	public String password() {
		return password;
	}
	
	public String connectionString() {
		return connectionString;
	}
	
	public String dataFolder() {
		return dataFolder;
	}
	
	public String flatFilePath() {
		return dataFolder + File.separator + flatFileName;
	}
	
	public String logFilePath() {
		return dataFolder + File.separator + logFile;
	}
}
