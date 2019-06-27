package com.nannerss.craftcontrol.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GUISounds {
    
    public static void playOpenSound(final Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 0.5F, 1F);
    }
    
    public static void playClickSound(final Player p) {
        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.5F, 1F);
    }
    
    public static void playBassSound(final Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 1F);
    }
    
}
