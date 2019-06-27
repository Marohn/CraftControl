package com.nannerss.craftcontrol.items.guis.main;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class MainButtons {
    
    @Getter
    private static final ItemStack blacklistButton, recipeButton;
    
    static {
        final ItemStack button = new ItemStack(Material.FILLED_MAP);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&c&lBlacklisted Items"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fManage items that are unable to be crafted.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        blacklistButton = button;
    }
    
    static {
        final ItemStack button = new ItemStack(Material.WRITABLE_BOOK);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&b&lCustom Recipes"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fManage custom recipes for the server.")
        ));
        
        button.setItemMeta(meta);
        recipeButton = button;
    }
    
}
