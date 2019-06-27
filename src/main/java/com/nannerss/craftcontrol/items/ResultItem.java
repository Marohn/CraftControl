package com.nannerss.craftcontrol.items;

import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class ResultItem {
    
    @Getter
    private static final ItemStack item;
    
    static {
        final ItemStack result = new ItemStack(Material.BARRIER);
        final ItemMeta meta = result.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&c&lRecipe Disabled"));
        meta.setLore(Collections.singletonList(Utils.colorize("&fYou are not allowed to craft this item!")));
        
        result.setItemMeta(meta);
        item = result;
    }

}
