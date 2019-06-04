package com.nannerss.craftcontrol;

import org.bukkit.plugin.java.JavaPlugin;

import com.nannerss.bananalib.BananaLib;
import com.nannerss.bananalib.config.ConfigManager;
import com.nannerss.bananalib.messages.Console;
import com.nannerss.bananalib.utils.Registrar;
import com.nannerss.craftcontrol.commands.CraftControlCommand;
import com.nannerss.craftcontrol.listeners.CraftListener;
import com.nannerss.craftcontrol.listeners.JoinListener;
import com.nannerss.craftcontrol.utils.Metrics;
import com.nannerss.craftcontrol.utils.Updater;

import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;

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
    
        Registrar.registerCommand(new CraftControlCommand());
        
        getServer().getPluginManager().registerEvents(new CraftListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        
        new Updater(67903) {
    
            @Override
            public void onUpdateAvailable() {
                Console.log(TextComponent.toPlainText(getUpdateMessage()));
            }
            
        }.runTaskAsynchronously(this);
        
        Metrics metrics = new Metrics(this);
    }
    
    @Override
    public void onDisable() {
        instance = null;
    }
    
}
