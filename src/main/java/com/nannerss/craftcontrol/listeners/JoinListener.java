package com.nannerss.craftcontrol.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.utils.Updater;

public class JoinListener implements Listener {
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        
        new BukkitRunnable() {
            
            @Override
            public void run() {
                if (p.hasPermission("craftcontrol.update") && Updater.updateAvailable()) {
                    if (Bukkit.getVersion().contains("1.8")) {
                        p.playSound(p.getLocation(), Sound.valueOf("CHICKEN_EGG_POP"), 1F, 1F);
                    } else {
                        p.playSound(p.getLocation(), Sound.valueOf("ENTITY_CHICKEN_EGG"), 1F, 1F);
                    }
                    
                    p.spigot().sendMessage(Updater.getUpdateMessage());
                }
            }
            
        }.runTaskLater(CraftControl.getInstance(), 50);
    }
    
}
