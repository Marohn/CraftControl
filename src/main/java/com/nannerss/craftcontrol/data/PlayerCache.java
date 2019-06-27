package com.nannerss.craftcontrol.data;

import org.bukkit.inventory.Inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCache {

    private Inventory gui;
    private int page;
    
    public PlayerCache(final Inventory gui, final int page) {
        this.gui = gui;
        this.page = page;
    }

}
