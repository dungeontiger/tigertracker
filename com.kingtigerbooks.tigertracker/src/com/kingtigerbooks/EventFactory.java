package com.kingtigerbooks;

import java.util.Calendar;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public final class EventFactory {

	public static Event createPluginEnabled() {
		return new Event(EventType.PLUGIN_ENABLED, "SERVER", Calendar.getInstance(), null, null, null, null);
	}
	
	public static Event createPluginDisabled() {
		return new Event(EventType.PLUGIN_DISABLED, "SERVER", Calendar.getInstance(), null, null, null, null);
	}
	
	public static Event createPlayerLogin(Player player) {
		Location location = player.getLocation();
		return new Event(EventType.PLAYER_CONNECT, player.getDisplayName(), Calendar.getInstance(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), null);
	}

	public static Event createPlayerLogoff(Player player) {
		Location location = player.getLocation();
		return new Event(EventType.PLAYER_DISCONNECT, player.getDisplayName(), Calendar.getInstance(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), null);
	}
	
	public static Event createCommnad(CommandSender sender, String label, String[] args) {
		String source = "SERVER";
		Location location = null;
		if (sender instanceof Player) {
			Player player = (Player)sender;
			source = player.getDisplayName();
			location = player.getLocation();
		}
		String command = label + " ";
		for (int i = 0; i < args.length; i++) {
			command += args[i] + " ";
		}
		return new Event(EventType.COMMAND, source, Calendar.getInstance(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), command);
	}
	
	public static Event createPlayerChat(Player player, String msg) {
		Location location = player.getLocation();
		return new Event(EventType.PLAYER_CHAT, player.getDisplayName(), Calendar.getInstance(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), msg);
	}
	
	public static Event createPlayerDeath(Player player, String msg) {
		Location location = player.getLocation();
		return new Event(EventType.PLAYER_DEATH, player.getDisplayName(), Calendar.getInstance(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), msg);
	}
	
	public static Event createMobDeath(Player player, EntityType type) {
		// TODO: split into actual mob id and not details
		Location location = player.getLocation();
		return new Event(EventType.MOB_DEATH, player.getDisplayName(), Calendar.getInstance(),location.getBlockX(), location.getBlockY(), location.getBlockZ(), String.valueOf(type));		
	}
	
	public static Event createPlayerPickup(Player player, Item item) {
		Location location = player.getLocation();
		String itemName = item.getItemStack().getType().name();
		return new Event(EventType.PlAYER_PICKUP, player.getDisplayName(), Calendar.getInstance(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), itemName);
	}

}
