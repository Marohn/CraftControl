package com.nannerss.craftcontrol.items.guis.blacklist;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;

import lombok.Getter;

public class BlacklistInfo {
    
    @Getter
    private static final ItemStack item;
    
    static {
        final ItemStack info = new ItemStack(Material.OAK_SIGN);
        final ItemMeta meta = info.getItemMeta();
        
        meta.setDisplayName(Utils.colorize("&e&lBLACKLISTED ITEMS"));
        meta.setLore(Arrays.asList(
                Utils.colorize("&fThis menu is a list of all the items which"),
                Utils.colorize("&fare not able to be crafted on the server."),
                "",
                Utils.colorize("&6&lLeft Click &fan item in your inventory to"),
                Utils.colorize("&fadd it to the list!"),
                Utils.colorize("&6&lRight Click &fan item on the list to remove it!")
        ));
        
        meta.addItemFlags(ItemFlag.values());
        
        info.setItemMeta(meta);
        item = info;
    }
    
}
