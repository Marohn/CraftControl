package com.nannerss.craftcontrol;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class CraftListener implements Listener {
    
    private static final ItemStack RESULT = ResultItem.getItem();
    
    @EventHandler(priority = EventPriority.MONITOR)
    public final void onCraft(PrepareItemCraftEvent e) {
        final List<Material> BANNED = Settings.BANNED_MATERIALS;
        
        for (LivingEntity le : e.getViewers()) {
            if (!(le instanceof Player)) {
                break;
            }
            
            Player p = (Player) le;
            
            if (!p.hasPermission("craftcontrol.bypass")) {
                return;
            }
        }
        
        Recipe recipe = e.getRecipe();
        
        for (Material material : BANNED) {
            if (e.getRecipe().getResult().getType().equals(material)) {
                e.getInventory().setResult(RESULT);
            }
        }
    }
    
    @EventHandler
    public final void onCraft(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        
        if (!e.getCurrentItem().hasItemMeta()) {
            return;
        }
        
        Player p = (Player) e.getWhoClicked();
        
        if (e.getClickedInventory().getType() == InventoryType.WORKBENCH && e.getCurrentItem().getItemMeta().equals(RESULT.getItemMeta())) {
            e.setCancelled(true);
        }
    }
    
}
