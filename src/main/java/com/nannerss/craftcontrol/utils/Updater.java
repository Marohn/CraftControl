package com.nannerss.craftcontrol.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.scheduler.BukkitRunnable;

import com.nannerss.bananalib.messages.Component;
import com.nannerss.craftcontrol.CraftControl;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public abstract class Updater extends BukkitRunnable {
    
    private static int projectId;
    private static String latest = "";
    
    public Updater(final int projectId) {
        Updater.projectId = projectId;
    }
    
    public static boolean updateAvailable() {
        return !latest.equals(CraftControl.getInstance().getDescription().getVersion());
    }
    
    public static BaseComponent[] getUpdateMessage() {
        final CraftControl instance = CraftControl.getInstance();
        
        return Component.builder("").append("\n").append("\n").append("               &3&l" + instance.getDescription().getName() + "\n").append("\n").append("&bA new version of " + instance.getDescription().getName() + " is available!\n").append("&bYou are on &fv" + instance.getDescription().getVersion() + "&b, update to &fv" + getLatest() + " &bhere:\n").append("\n").append("                &3&l[DOWNLOAD]\n").onHover("&7Click to open download page!").onClick(Action.OPEN_URL, "https://spigotmc.org/resources/" + getProjectId()).append("\n").append("").create();
    }
    
    public static int getProjectId() {
        return projectId;
    }
    
    public static String getLatest() {
        return latest;
    }
    
    @Override
    public void run() {
        try {
            final URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectId);
            final URLConnection connection = url.openConnection();
            
            try (final BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                latest = r.readLine();
            }
            
            if (updateAvailable()) {
                onUpdateAvailable();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public abstract void onUpdateAvailable();
    
}
