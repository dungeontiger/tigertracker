package com.kingtigerbooks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerEvent(PlayerLoginEvent event) {
		EventManager.notify(EventFactory.createPlayerLogin(event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerEvent(PlayerQuitEvent event) {
		EventManager.notify(EventFactory.createPlayerLogoff(event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerEvent(AsyncPlayerChatEvent event) {
		EventManager.notify(EventFactory.createPlayerChat(event.getPlayer(), event.getMessage()));
	}
	
	@EventHandler
	public void onPlayerEvent(PlayerDeathEvent event) {
		// TODO: record actual mob id that killed him, or killed by thing
		// TODO: record pvp deaths, player killers
		EventManager.notify(EventFactory.createPlayerDeath(event.getEntity(), event.getDeathMessage()));
	}
	
	@EventHandler
	public void onPlayerEvent(PlayerPickupItemEvent event) {
		EventManager.notify(EventFactory.createPlayerPickup(event.getPlayer(), event.getItem()));
	}	
}	
