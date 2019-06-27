package com.nannerss.craftcontrol.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.google.common.cache.Cache;
import com.nannerss.bananalib.messages.Messages;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.data.PlayerCache;
import com.nannerss.craftcontrol.utils.GUISounds;

public class CloseListener implements Listener {
    
    @EventHandler
    public void onClose(final InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }
        
        final Player p = (Player) e.getPlayer();
        final PlayerCache cache = CraftControl.getInventoryCache().getIfPresent(p.getUniqueId());
        final Cache<UUID, String> editSessions = CraftControl.getEditSessions();
        
        if (e.getInventory().equals(cache.getGui()) && editSessions.asMap().containsKey(p.getUniqueId())) {
            GUISounds.playBassSound(p);
            editSessions.invalidate(p.getUniqueId());
    
            Messages.sendMessage(p, "&cCancelled recipe edit session.");
        }
    }
    
}
