package com.nannerss.craftcontrol.items.guis.recipe;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class RecipeDenyItems {
    
    @Getter
    private static final ItemStack invalidRecipeItem, valueOutOfRangeButton;
    
    static {
        final ItemStack button = new ItemStack(Material.BARRIER);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&c&lInvalid Recipe"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fYou haven't set ingredients and/or a result!")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        invalidRecipeItem = button;
    }
    
    static {
        final ItemStack button = new ItemStack(Material.BARRIER);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&c&lInvalid Value"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fThe specified value goes out of range!")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        valueOutOfRangeButton = button;
    }
    
}
