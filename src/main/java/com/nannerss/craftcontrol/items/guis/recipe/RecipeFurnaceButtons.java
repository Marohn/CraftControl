package com.nannerss.craftcontrol.items.guis.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class RecipeFurnaceButtons {
    
    @Getter
    private static final ItemStack typeButton, saveButton, cookTimeButton, expButton;
    
    static {
        final ItemStack button = new ItemStack(Material.FURNACE);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lFurnace Recipe"));
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
    
    static {
        final ItemStack button = new ItemStack(Material.CLOCK);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lCook Time"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&3Current Value&f: 200"),
                "",
                Utils.colorize("&6&lLeft Click &fto increase &fthe time by 1 tick."),
                Utils.colorize("&6&lShift Left Click &fto increase &fthe time by 10 ticks."),
                Utils.colorize("&6&lRight Click &fto decrease &fthe time by 1 tick."),
                Utils.colorize("&6&lShift Right Click &fto decrease &fthe time by 10 ticks.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        cookTimeButton = button;
    }
    
    static {
        final ItemStack button = new ItemStack(Material.EXPERIENCE_BOTTLE);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lXP Yield"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&3Current Value&f: 10"),
                "",
                Utils.colorize("&6&lLeft Click &fto increase &fthe xp by 1."),
                Utils.colorize("&6&lShift Left Click &fto increase &fthe xp by 10."),
                Utils.colorize("&6&lRight Click &fto decrease &fthe xp by 1."),
                Utils.colorize("&6&lShift Right Click &fto decrease &fthe xp by 10."))
        );
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        expButton = button;
    }
    
}
