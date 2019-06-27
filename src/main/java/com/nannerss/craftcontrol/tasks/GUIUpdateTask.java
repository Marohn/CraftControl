package com.nannerss.craftcontrol.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.data.PlayerCache;
import com.nannerss.craftcontrol.gui.AdminGUI;
import com.nannerss.craftcontrol.utils.Pagination;

public class GUIUpdateTask extends BukkitRunnable {
    
    @Override
    public void run() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.getOpenInventory() != null) {
                final PlayerCache cache = CraftControl.getInventoryCache().getIfPresent(p.getUniqueId());
                final Pagination blacklistPagination = CraftControl.getBlacklistPagination();
                final Pagination recipePagination = CraftControl.getRecipePagination();
                final Inventory inv = cache.getGui();
                
                if (p.getOpenInventory().getTopInventory().equals(cache.getGui()) && p.getOpenInventory().getTitle().contains("Blacklisted Items")) {
                    if (blacklistPagination.totalPages() == 0) {
                        AdminGUI.setBlankBlacklistPageView(inv);
                    } else {
                        AdminGUI.setBlacklistPageView(inv, cache.getPage());
                    }
                } else if (p.getOpenInventory().getTopInventory().equals(cache.getGui()) && p.getOpenInventory().getTitle().contains("Custom Recipes")) {
                    if (recipePagination.totalPages() == 0) {
                        AdminGUI.setBlankRecipePageView(inv);
                    } else {
                        AdminGUI.setRecipePageView(inv, cache.getPage());
                    }
                }
            }
        }
    }
    
}
