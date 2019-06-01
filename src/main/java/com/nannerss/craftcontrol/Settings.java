package com.nannerss.craftcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Material;

import com.nannerss.bananalib.config.ConfigManager;
import com.nannerss.bananalib.utils.Utils;

public class Settings {
    
    private static ConfigManager cfg;
    
    public static Material RESULT_MAT;
    public static String RESULT_DISPLAYNAME;
    public static List<String> RESULT_LORE;
    public static List<Material> BANNED_MATERIALS;
    
    public static void load() {
        cfg = CraftControl.getSettings();
        
        RESULT_MAT = getMaterial("banned-result-item.material");
        RESULT_DISPLAYNAME = cfg.getString("banned-result-item.displayname");
        RESULT_LORE = getLoreList("banned-result-item.lore");
        BANNED_MATERIALS = getMaterialList("banned-materials");
    }
    
    private static List<String> getLoreList(String path) {
        List<String> original = cfg.getStringList("banned-result-item.lore");
        List<String> colorized = new ArrayList<>();
        
        for (String line : original) {
            colorized.add(Utils.colorize(line));
        }
        
        return colorized;
    }
    
    private static Material getMaterial(String path) {
        String fixedName = Objects.requireNonNull(cfg.getString(path)).toUpperCase().replace(" ", "_");
        
        try {
            return Material.valueOf(fixedName);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("The material value listed for the Banned Result Item is NOT a valid material!");
        }
    }
    
    private static List<Material> getMaterialList(String path) {
        final List<Material> list = new ArrayList<>();
        
        for (String line : cfg.getStringList(path)) {
            String fixedName = line.toUpperCase().replace(" ", "_");
            
            try {
                Material mat = Material.valueOf(fixedName);
                
                list.add(mat);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("String " + fixedName + " is NOT a valid material!");
            }
        }
        
        return list;
    }
    
}
