package com.nannerss.craftcontrol.items.guis.blacklist;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class BlacklistButtons {
    
    @Getter
    private static final ItemStack previousButton, nextButton, backButton;
    
    static {
        final ItemStack button = new ItemStack(Material.ARROW);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&a&lPrevious Page"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&6&lClick &fto view the previous page.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        previousButton = button;
    }
    
    static {
        final ItemStack button = new ItemStack(Material.ARROW);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&a&lNext Page"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&6&lClick &fto view the next page.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        nextButton = button;
    }
    
    static {
        final ItemStack button = new ItemStack(Material.BARRIER);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&c&lBack"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&6&lClick &fto go back.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        backButton = button;
    }
    
}
