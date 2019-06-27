package com.nannerss.craftcontrol.items.guis;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;

public class Filler {
    
    @Getter
    private static final ItemStack item;
    
    static {
        final ItemStack filler = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        final ItemMeta meta = filler.getItemMeta();
        
        meta.setDisplayName(" ");
        
        filler.setItemMeta(meta);
        item = filler;
    }
    
}
