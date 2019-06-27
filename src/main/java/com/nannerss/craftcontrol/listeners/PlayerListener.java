package com.nannerss.craftcontrol.listeners;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.cache.Cache;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.utils.GUISounds;
import com.nannerss.craftcontrol.utils.Updater;

public class PlayerListener implements Listener {
    
    private static Inventory adminGui;
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        
        CraftControl.getCache(p.getUniqueId());
        
        new BukkitRunnable() {
            
            @Override
            public void run() {
                if (p.hasPermission("craftcontrol.update") && Updater.updateAvailable()) {
                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
                    p.spigot().sendMessage(Updater.getUpdateMessage());
                }
            }
            
        }.runTaskLater(CraftControl.getInstance(), 50);
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final Cache<UUID, String> editSessions = CraftControl.getEditSessions();
    
        if (editSessions.asMap().containsKey(p.getUniqueId())) {
            GUISounds.playBassSound(p);
            editSessions.invalidate(p.getUniqueId());
        }
        
        CraftControl.getInventoryCache().invalidate(p.getUniqueId());
    }
    
}
