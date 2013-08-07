package com.kingtigerbooks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class DatabaseLogger {
	
	private Connection connection;
	private final String database;
	
	// Database structure:
	//
	// event
	//	eventTime: datetime
	//	eventTypeId: int
	// 	userId: int
	//  x: int
	//	y: int
	//	z: int
	//	itemId: int
	// 	mobId: int
	//	details: varchar(250)
	
	// user
	//	id: autoincrement
	//	userName: varchar(50)
	
	// mob
	//  id: autoincrement
	//	mobName: varchar(50)
	
	// item
	//  id: autoincrement
	//	itemName: varchar(50)
	
	public DatabaseLogger() {
		database = TigerTracker.getSettings().databaseName();
	}

	public void connect() {
		String url = TigerTracker.getSettings().connectionString();
		String userId = TigerTracker.getSettings().userId();
		String password = TigerTracker.getSettings().password();
		try {
			connection = DriverManager.getConnection(url, userId, password);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot connect to database server.");
			e.printStackTrace();
		}

		if (noDatabase()) {
			createDatabase();
			createEventTable();
			createUserTable();
			createEventTypeTable();
			createMobTable();
			createItemTable();
		}
	}
	
	public void release() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				TigerTracker.logException(e, "Cannot close database connection.");
				e.printStackTrace();
			}
		}
	}
	
	// TODO: these could be generalized better
	public void writeEvent(Event e) {
		switch (e.type) 
		{
		case EventType.PLUGIN_DISABLED:
		case EventType.PLUGIN_ENABLED:
			writePluginEvent(e);
			break;
		case EventType.PLAYER_CONNECT:
		case EventType.PLAYER_DISCONNECT:
			writePlayerConnectEvent(e);
			break;
		case EventType.PLAYER_DEATH:
		case EventType.PLAYER_CHAT:
		case EventType.PlAYER_PICKUP:
			writePlayerDetailsEvent(e);
			break;
		case EventType.MOB_DEATH:
			writeMobDeathEvent(e);
			break;
		default:
			TigerTracker.logError("Unknown event type in database write event.");
		}
	}
	
	private boolean noDatabase() {
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT schema_name FROM information_schema.schemata WHERE schema_name = '" +  database + "'");
			boolean exists = resultSet.next();
			resultSet.close();
			return !exists;
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot determine if database already exists.");
			e.printStackTrace();
		}
		return false;
	}
	
	private void createDatabase() {
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.execute("CREATE DATABASE " + database);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot create database.");
			e.printStackTrace();
		}
	}
	
	private void createEventTable() {
		Statement statement;
		String sql = "CREATE TABLE " + database + ".event (eventTime DATETIME, eventTypeId int, userId int, x int, y int, z int, itemId int, mobId int, details VARCHAR(250))";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot create event table.");
			e.printStackTrace();
		}
	}
	
	private void createUserTable() {
		Statement statement;
		String sql = "CREATE TABLE " + database + ".user (id int AUTO_INCREMENT PRIMARY KEY, userName VARCHAR(50))";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot create user table.");
			e.printStackTrace();
		}
	}
	
	private void createEventTypeTable() {
		Statement statement;
		String sql = "CREATE TABLE " + database + ".eventType (id int AUTO_INCREMENT PRIMARY KEY, eventType VARCHAR(50))";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot create eventType table.");
			e.printStackTrace();
		}
	}
	
	private void createMobTable() {
		Statement statement;
		String sql = "CREATE TABLE " + database + ".mob (id int AUTO_INCREMENT PRIMARY KEY, mobName VARCHAR(50))";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot create mob table.");
			e.printStackTrace();
		}
	}
	
	private void createItemTable() {
		Statement statement;
		String sql = "CREATE TABLE " + database + ".item (id int AUTO_INCREMENT PRIMARY KEY, itemName VARCHAR(50))";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot create item table.");
			e.printStackTrace();
		}
	}

	private void writePluginEvent(Event event) {
		Statement statement;
		String sql = "INSERT " + database + ".event (eventTime, eventTypeId) VALUES ('" + getDateTimeString(event.time) + "','" + getEventTypeId(event.type) + "')";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot insert plugin event.");
			e.printStackTrace();
		}
	}
	
	private void writePlayerConnectEvent(Event event) {
		
		int userId = getUserId(event.player);
		
		Statement statement;
		String sql = "INSERT " + database + ".event (eventTime, eventTypeId, userId, x, y, z) VALUES ('" +
		      getDateTimeString(event.time) + "','" + getEventTypeId(event.type) + "', " + userId + "," +
		      event.x + "," + event.y + "," + event.z + ")";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot insert player connect event.");
			e.printStackTrace();
		}
	}
	
	private void writePlayerDetailsEvent(Event event) {
		int userId = getUserId(event.player);
		
		Statement statement;
		String sql = "INSERT " + database + ".event (eventTime, eventTypeId, userId, x, y, z, details) VALUES ('" +
		      getDateTimeString(event.time) + "','" + getEventTypeId(event.type) + "', " + userId + "," +
		      event.x + "," + event.y + "," + event.z + ",\"" + event.details + "\")";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot insert player connect event.");
			e.printStackTrace();
		}		
	}
	
	// TODO: split into mob id
	private void writeMobDeathEvent(Event event) {
		int userId = getUserId(event.player);
		
		Statement statement;
		String sql = "INSERT " + database + ".event (eventTime, eventTypeId, userId, x, y, z, details) VALUES ('" +
		      getDateTimeString(event.time) + "','" + getEventTypeId(event.type) + "', " + userId + "," +
		      event.x + "," + event.y + "," + event.z + ",\"" + event.details + "\")";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot insert mob death event.");
			e.printStackTrace();
		}		
	}
	
	private int getUserId(String userName) {
		Statement statement;
		String sql = "SELECT id FROM " + database + ".user WHERE userName = '" + userName + "'";
		try {
			statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			if (results.next()) {
				return results.getInt("id");
			}
			else
			{
				// insert the new user
				statement = connection.createStatement();
				statement.execute("INSERT " + database + ".user (userName) VALUES ('" + userName + "')");
				// now that we know the user exists, we can safely call this recursively
				// TODO: Still this scares me
				return getUserId(userName);
			}
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot get user id.");
			e.printStackTrace();
		}
		// TODO: deal with bailing out.
		return -1;
	}

	private int getEventTypeId(int eventType) {
		Statement statement;
		String eventTypeString = getEventTypeString(eventType);
		String sql = "SELECT id FROM " + database + ".eventType WHERE eventType = '" + eventTypeString + "'";
		try {
			statement = connection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			if (results.next()) {
				return results.getInt("id");
			}
			else
			{
				// insert the new user
				statement = connection.createStatement();
				statement.execute("INSERT " + database + ".eventType (eventType) VALUES ('" + eventTypeString + "')");
				// now that we know the event exists, we can safely call this recursively
				// TODO: Still this scares me
				return getEventTypeId(eventType);
			}
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot get eventType id.");
			e.printStackTrace();
		}
		// TODO: deal with bailing out.
		return -1;
	}
	
	// TODO: Clean this up, either don't use it or make it a utility
	private static String getDateTimeString(Calendar time) {
		// TODO: These Calendar functions are locale specific
		String dateTime = String.valueOf(time.get(Calendar.YEAR)) + "-" + String.valueOf(time.get(Calendar.MONTH) + 1) + "-" + String.valueOf(time.get(Calendar.DAY_OF_MONTH));
		dateTime += " ";
		dateTime += String.valueOf(time.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(time.get(Calendar.MINUTE)) + ":" + String.valueOf(time.get(Calendar.SECOND));
		return dateTime;
	}
	
	// TODO: Clean this up, either don't use it or make it a utility
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
