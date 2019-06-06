package com.nannerss.craftcontrol.commands;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nannerss.bananalib.messages.Component;
import com.nannerss.bananalib.messages.Messages;
import com.nannerss.bananalib.utils.Utils;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.Settings;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class CraftControlCommand extends Command {
    
    public CraftControlCommand() {
        super("craftcontrol");
        
        setDescription("The base command for the CraftControl plugin!");
        setAliases(Arrays.asList("ccontrol", "cc"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final List<Material> BANNED_MATERIALS = Settings.BANNED_MATERIALS;
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cYou must be a player to use this command!");
            return false;
        }
        
        Player p = (Player) sender;
        
        if (args.length < 1) {
            Messages.sendMessage(p, "&bRunning &3CraftControl &bv" + CraftControl.getInstance().getDescription().getVersion() + "!");
            return false;
        }
        
        String parameter = args[0];
        
        if ("help".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.help")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            Messages.sendMessage(p,
                    "",
                    "     &3&lShowing help for CraftControl...",
                    "",
                    "&b/craftcontrol &3help &8- &7Displays this help page.",
                    "&b/craftcontrol &3list &8- &7Lists items that are unable to be crafted.",
                    "&b/craftcontrol &3add <material> &8- &7Adds the specified material to the blacklisted recipes list.",
                    "&b/craftcontrol &3remove <material> &8- &7Removes the specified material from the blacklisted recipes list.",
                    "&b/craftcontrol &3reload &8- &7Reloads the config.",
                    "");
        } else if ("list".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.list")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            Messages.sendMessage(p,
                    "",
                    "",
                    "     &3&lShowing blacklisted items...",
                    "");
            
            for (Material material : BANNED_MATERIALS) {
                BaseComponent[] message = Component.builder(" &8- ").append("&b" + WordUtils.capitalize(material.toString().toLowerCase().replace("_", " "))).onHover("&b&lClick &r&7to remove!").onClickRunCmd("/craftcontrol remove " + material.toString()).create();
                p.spigot().sendMessage(message);
            }
    
            if (BANNED_MATERIALS.isEmpty()) {
                Messages.sendMessage(p, " &c&oNo materials have been added to the list...");
            }
            
            Messages.sendMessage(p, "", "");
        } else if ("reload".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.reload")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            CraftControl.getSettings().loadConfig();
            Settings.load();
            
            Messages.sendMessage(p, "&bSuccessfully reloaded the config!");
        } else if ("add".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.add")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            if (args.length < 2) {
                Messages.sendMessage(p, "&cUsage: /craftcontrol add <material>");
                return false;
            }
            
            String materialName = args[1].toUpperCase();
            Material material;
            
            try {
                material = Material.valueOf(materialName);
            } catch (IllegalArgumentException e) {
                Messages.sendMessage(p, "&c" + materialName + " is not a valid material!");
                return false;
            }
    
            CraftControl.getSettings().loadConfig();
            Settings.load();
            
            if (Settings.BANNED_MATERIALS.contains(material)) {
                Messages.sendMessage(p, "&c" + materialName + " is already blacklisted!");
                return false;
            }
            
            Settings.BANNED_MATERIALS.add(material);
            Settings.save();
            
            Messages.sendMessage(p, "&bAdded " + materialName + " to the list of blacklisted recipes!");
        } else if ("remove".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.remove")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
    
            if (args.length < 2) {
                Messages.sendMessage(p, "&cUsage: /craftcontrol remove <material>");
                return false;
            }
    
            String materialName = args[1].toUpperCase();
            Material material;
    
            try {
                material = Material.valueOf(materialName);
            } catch (IllegalArgumentException e) {
                Messages.sendMessage(p, "&c" + materialName + " is not a valid material!");
                return false;
            }
    
            CraftControl.getSettings().loadConfig();
            Settings.load();
    
            if (!Settings.BANNED_MATERIALS.contains(material)) {
                Messages.sendMessage(p, "&c" + materialName + " is not a blacklisted recipe!");
                return false;
            }
    
            Settings.BANNED_MATERIALS.remove(material);
            Settings.save();
    
            Messages.sendMessage(p, "&bRemoved " + materialName + " from the list of blacklisted recipes!");
        }
        
        return false;
    }
}
