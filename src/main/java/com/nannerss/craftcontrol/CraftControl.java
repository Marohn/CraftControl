package com.nannerss.craftcontrol;

import org.bukkit.plugin.java.JavaPlugin;

import com.nannerss.bananalib.BananaLib;
import com.nannerss.bananalib.config.ConfigManager;
import com.nannerss.bananalib.utils.Registrar;

import lombok.Getter;

public class CraftControl extends JavaPlugin {
    
    @Getter
    private static CraftControl instance;
    
    @Getter
    private static ConfigManager settings;
    
    @Override
    public void onEnable() {
        instance = this;
        BananaLib.setInstance(this);
    
        settings = new ConfigManager("settings.yml", true);
        Settings.load();
    
        Registrar.registerCommand(new ControlCommand());
        
        getServer().getPluginManager().registerEvents(new CraftListener(), this);
    }
    
    @Override
    public void onDisable() {
        instance = null;
    }
    
}
