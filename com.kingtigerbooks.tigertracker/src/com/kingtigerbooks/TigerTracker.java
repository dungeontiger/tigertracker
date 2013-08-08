package com.kingtigerbooks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bukkit it plug in the logs and tracks player activity
 * @author gibsons
 *
 */

// TODO: Go back to the last place you died
// TODO: handle bad configuration file

public class TigerTracker extends JavaPlugin {
	
	private static ConsoleCommandSender console;
	private static Settings settings;
	private static Log log;

    @Override
    public void onEnable() {
    	saveDefaultConfig();
    	settings = new Settings(getConfig(), getDataFolder().getAbsolutePath());
    	console = getServer().getConsoleSender();
    	log = new Log(settings.logFilePath());
    	
    	getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    	getServer().getPluginManager().registerEvents(new MobListener(), this);

    	EventManager.notify(EventFactory.createPluginEnabled());
    }
 
    @Override
    public void onDisable() {
    	EventManager.notify(EventFactory.createPluginDisabled());
    	log.release();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	EventManager.notify(EventFactory.createCommnad(sender, label, args)); 
    	return super.onCommand(sender, command, label, args);
    }
      
    public static Settings getSettings() {
    	return settings;
    }
    
    public static void consoleMessage(String msg) {
    	console.sendMessage(msg);
    }
    
    public static Log getLog() {
    	return log;
    }
    
    public static void logException(Exception e, String msg) {
    	log.writeException(e, msg);
    }
    
    public static void logError(String msg) {
    	log.writeError(msg);
    }
}
