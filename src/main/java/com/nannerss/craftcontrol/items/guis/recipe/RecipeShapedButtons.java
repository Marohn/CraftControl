package com.nannerss.craftcontrol.items.guis.recipe;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class RecipeShapedButtons {
    
    @Getter
    private static final ItemStack typeButton, saveButton;
    
    static {
        final ItemStack button = new ItemStack(Material.EMERALD_BLOCK);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lShaped Recipe"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&6&lClick &fto change recipe types.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        typeButton = button;
    }
    
    static {
        final ItemStack button = new ItemStack(Material.NETHER_STAR);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&d&lSave Recipe"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&6&lClick &fto save your recipe!")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        saveButton = button;
    }
    
}
