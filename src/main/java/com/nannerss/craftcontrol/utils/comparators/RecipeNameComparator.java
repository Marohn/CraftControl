package com.nannerss.craftcontrol.utils.comparators;

import java.util.Comparator;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class RecipeNameComparator implements Comparator<ItemStack> {
    
    @Override
    public int compare(final ItemStack item1, final ItemStack item2) {
        String name1 = "";
        String name2 = "";
        
        for (String line : item1.getItemMeta().getLore()) {
            if (line.contains(ChatColor.stripColor("Name"))) {
                name1 = line.replace(" ", "");
            }
        }
    
        for (String line : item2.getItemMeta().getLore()) {
            if (line.contains(ChatColor.stripColor("Name"))) {
                name2 = line.replace(" ", "");
            }
        }
        
        final String finalName1 = name1.split(":")[1];
        final String finalName2 = name2.split(":")[1];
        
        return finalName1.compareTo(finalName2);
    }
    
}
