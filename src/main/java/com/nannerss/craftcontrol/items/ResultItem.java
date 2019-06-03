package com.nannerss.craftcontrol.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;
import com.nannerss.craftcontrol.Settings;

import lombok.Getter;

public class ResultItem {

    public static final Material MATERIAL = Settings.RESULT_MAT;
    public static final String NAME = Settings.RESULT_DISPLAYNAME;
    public static final List<String> LORE = Settings.RESULT_LORE;
    
    @Getter
    private static final ItemStack item;
    
    static {
        final ItemStack result = new ItemStack(MATERIAL);
        final ItemMeta meta = result.getItemMeta();
        
        meta.setDisplayName(Utils.colorize(NAME));
        meta.setLore(LORE);
        
        result.setItemMeta(meta);
        item = result;
    }

}
