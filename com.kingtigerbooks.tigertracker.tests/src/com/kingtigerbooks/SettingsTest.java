package com.kingtigerbooks;


import static org.junit.Assert.*;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

public class SettingsTest {

	private String sYaml = "database:\n   use: true\n   databaseName: tt\n   userId: scott\n   password: tiger\n" +
			"   connectionString: myConnect\nflatfile:\n   use: true\n   filename: x.csv\n";

	@Test
	public void testCreate() throws InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		config.loadFromString(sYaml);
		Settings settings = new Settings(config, "./data");
		assertTrue(settings.useDatabase());
		assertTrue(settings.databaseName().compareTo("tt") == 0);
		assertTrue(settings.userId().compareTo("scott") == 0);
		assertTrue(settings.password().compareTo("tiger") == 0);
		assertTrue(settings.connectionString().compareTo("myConnect") == 0);
		assertTrue(settings.useFlatFile());
		assertTrue(settings.flatFileName().compareTo("x.csv") == 0);
		assertTrue(settings.dataFolder().compareTo("./data") == 0);
		assertTrue(settings.logFilePath().compareTo("./data/tigertracker.log") == 0);
	}
}
