package com.kingtigerbooks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobListener implements Listener {

   @EventHandler
    public void onEntityDeath(final EntityDeathEvent event)
    {
	   LivingEntity killer = event.getEntity().getKiller();
	   if (killer != null && killer instanceof Player)
	   {
		   EventManager.notify(EventFactory.createMobDeath((Player)killer, event.getEntityType()));
	   }
    }
}
