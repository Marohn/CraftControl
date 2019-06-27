package com.nannerss.craftcontrol.utils.comparators;

import java.util.Comparator;

import org.bukkit.inventory.ItemStack;

public class ItemComparator implements Comparator<ItemStack> {
    
    @Override
    public int compare(final ItemStack item1, final ItemStack item2) {
        return item1.getType().compareTo(item2.getType());
    }
    
}
