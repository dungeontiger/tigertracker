package com.kingtigerbooks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			createSimpleTable("user");
			createSimpleTable("eventType");
			createSimpleTable("mob");
			createSimpleTable("item");
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
	
	public void writeEvent(Event event) {	
		Integer eventTypeId = getDatabaseId(getEventTypeString(event.type), "eventType");
		Integer userId = getDatabaseId(event.player, "user");
		Integer itemId = getDatabaseId(event.item, "item");
		Integer mobId = getDatabaseId(event.mob, "mob");
		String sql = "INSERT " + database + ".event (eventTime, eventTypeId, userId, x, y, z, mobId, itemId, details) VALUES ('" +
		      event.time + "','" + eventTypeId + "', " + userId + "," + event.x + "," + event.y + "," + event.z + "," +
		      mobId + "," + itemId + ",\"" + event.details + "\")";
		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot insert event.");
			e.printStackTrace();
		}		
	}
	
	private Integer getDatabaseId(String value, String table) {
		if (value != null){
			Statement statement;
			String sql = "SELECT id FROM " + database + "." + table + " WHERE " + table + " = '" + value + "'";
			try {
				statement = connection.createStatement();
				ResultSet results = statement.executeQuery(sql);
				if (results.next()) {
					return results.getInt("id");
				}
				else
				{
					// TODO: Do I need all these extra connections?
					statement = connection.createStatement();
					statement.execute("INSERT " + database + "." + table + " (" + table + ") VALUES ('" + value + "')");
					statement = connection.createStatement();
					results = statement.executeQuery(sql);
					if (results.next()) {
						return results.getInt("id");
					}
				}
			} catch (SQLException e) {
				TigerTracker.logException(e, "Cannot get database id for " + value + " in table " + table);
				e.printStackTrace();
			}
		}
		return null;
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

	private void createSimpleTable(String table) {
		Statement statement;
		String sql = "CREATE TABLE " + database + "." + table + " (id int AUTO_INCREMENT PRIMARY KEY, " + table + " VARCHAR(50))";
		try {
			statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			TigerTracker.logException(e, "Cannot create table " + table);
			e.printStackTrace();
		}
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
