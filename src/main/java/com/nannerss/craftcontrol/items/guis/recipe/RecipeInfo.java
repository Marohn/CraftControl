package com.nannerss.craftcontrol.items.guis.recipe;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class RecipeInfo {
    
    @Getter
    private static final ItemStack item;
    
    static {
        final ItemStack info = new ItemStack(Material.OAK_SIGN);
        final ItemMeta meta = info.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lCUSTOM RECIPES"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fThis menu is a list of all the custom"),
                Utils.colorize("&frecipes that are currently on the server."),
                "",
                Utils.colorize("&6&lClick &fthe &a&lAdd Recipe &fbutton to add a recipe!"),
                Utils.colorize("&6&lLeft Click &fan item on the list to edit it!"),
                Utils.colorize("&6&lRight Click &fan item on the list to remove it!")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        info.setItemMeta(meta);
        item = info;
    }
    
}
