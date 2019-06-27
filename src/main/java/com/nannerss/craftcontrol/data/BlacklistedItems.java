package com.nannerss.craftcontrol.data;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.nannerss.bananalib.config.ConfigManager;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.utils.comparators.ItemComparator;

import lombok.Getter;
import lombok.Setter;

public class BlacklistedItems {
    
    private static ConfigManager cfg;
    
    @Getter
    @Setter
    private static List<ItemStack> items;
    
    public BlacklistedItems() {
        load();
    }
    
    private void load() {
        cfg = CraftControl.getBlacklistedItems();
        
        items = (List<ItemStack>) cfg.get("blacklisted-items");
        if (!items.isEmpty()) {
            items.sort(new ItemComparator());
        }
        
        if (!items.isEmpty()) {
            CraftControl.getBlacklistItemCache().addAll(items);
        }
    }
    
    public static void save() {
        cfg = CraftControl.getBlacklistedItems();
        
        cfg.set("blacklisted-items", items);
        
        cfg.saveConfig();
    }
    
}
