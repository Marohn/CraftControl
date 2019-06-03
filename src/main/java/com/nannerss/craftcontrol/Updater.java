package com.nannerss.craftcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.scheduler.BukkitRunnable;

import com.nannerss.bananalib.utils.Utils;

public abstract class Updater extends BukkitRunnable {
    
    private static int projectId;
    private static String latest = "";
    
    public Updater(int projectId) {
        Updater.projectId = projectId;
    }
    
    @Override
    public void run() {
        try {
            final URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectId);
            final URLConnection connection = url.openConnection();
            
            try (BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                latest = r.readLine();
            }
            
            if (updateAvailable()) {
                onUpdateAvailable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public abstract void onUpdateAvailable();
    
    public static boolean updateAvailable() {
        return !latest.equals(CraftControl.getInstance().getDescription().getVersion());
    }
    
    public static String[] getUpdateMessage() {
        final CraftControl instance = CraftControl.getInstance();
        
        return new String[] {
                "",
                "",
                Utils.colorize("               &3&l" + instance.getDescription().getName() + ""),
                "",
                Utils.colorize("&bA new version of " + instance.getDescription().getName() + " is available!"),
                Utils.colorize("&bYou are on &fv" + instance.getDescription().getVersion() + "&b."),
                Utils.colorize("&bGrab &fv" + getLatest() + " &bhere:"),
                Utils.colorize("&e&nhttps://spigotmc.org/resources/" + getProjectId()),
                "",
                ""
        };
    }
    
    public static int getProjectId() {
        return projectId;
    }
    
    public static String getLatest() {
        return latest;
    }
    
}
