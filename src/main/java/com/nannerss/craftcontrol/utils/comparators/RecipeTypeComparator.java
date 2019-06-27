package com.nannerss.craftcontrol.utils.comparators;

import java.util.Comparator;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class RecipeTypeComparator implements Comparator<ItemStack> {
    
    @Override
    public int compare(final ItemStack item1, final ItemStack item2) {
        String type1 = "";
        String type2 = "";
        
        for (String line : item1.getItemMeta().getLore()) {
            if (line.contains(ChatColor.stripColor("Type"))) {
                type1 = line.replace(" ", "");
            }
        }
    
        for (String line : item2.getItemMeta().getLore()) {
            if (line.contains(ChatColor.stripColor("Type"))) {
                type2 = line.replace(" ", "");
            }
        }
        
        final String finalType1 = type1.split(":")[1];
        final String finalType2 = type2.split(":")[1];
        
        return finalType1.compareTo(finalType2);
    }
    
}
