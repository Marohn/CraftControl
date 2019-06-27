package com.nannerss.craftcontrol.items.guis.recipe;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class RecipeTypeButtons {
    
    @Getter
    private static final ItemStack shapelessButton, shapedButton, furnaceButton;
    
    static {
        final ItemStack button = new ItemStack(Material.DIAMOND_BLOCK);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lShapeless Recipe"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fCreate a new shapeless recipe.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        shapelessButton = button;
    }
    
    static {
        final ItemStack button = new ItemStack(Material.EMERALD_BLOCK);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lShaped Recipe"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fCreate a new shaped recipe.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        shapedButton = button;
    }
    
    static {
        final ItemStack button = new ItemStack(Material.FURNACE);
        final ItemMeta meta = button.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lFurnace Recipe"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fCreate a new furnace recipe.")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        button.setItemMeta(meta);
        furnaceButton = button;
    }
    
}
