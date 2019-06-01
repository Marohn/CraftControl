package com.nannerss.craftcontrol;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.nannerss.bananalib.messages.Messages;

public class ControlCommand extends Command {
    
    public ControlCommand() {
        super("craftcontrol");
        
        setDescription("The base command for the CraftControl plugin!");
        setAliases(Arrays.asList("ccontrol", "cc"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final List<Material> BANNED_MATERIALS = Settings.BANNED_MATERIALS;
        
        if (args.length == 0) {
            Messages.sendMessage(sender, "&bRunning &3CraftControl &bv" + CraftControl.getInstance().getDescription().getVersion() + "!");
            return false;
        }
        
        String parameter = args[0];
        
        if ("help".equalsIgnoreCase(parameter)) {
            if (!sender.hasPermission("craftcontrol.help")) {
                Messages.sendMessage(sender, "&cInsufficient permissions!");
                return false;
            }
            
            Messages.sendMessage(sender,
                    "",
                    "     &3&lShowing help for CraftControl...",
                    "",
                    "&b/craftcontrol &3help &8- &7Displays this help page.",
                    "&b/craftcontrol &3list &8- &7Lists items that are unable to be crafted.",
                    "&b/craftcontrol &3reload &8- &7Reloads the config.",
                    "");
        } else if ("list".equalsIgnoreCase(parameter)) {
            if (!sender.hasPermission("craftcontrol.list")) {
                Messages.sendMessage(sender, "&cInsufficient permissions!");
                return false;
            }
            
            Messages.sendMessage(sender,
                    "",
                    "     &3&lShowing blacklisted items...",
                    "");
            
            for (Material material : BANNED_MATERIALS) {
                Messages.sendMessage(sender, "&8- &b" + material.toString());
            }
            
            Messages.sendMessage(sender, "");
        } else if ("reload".equalsIgnoreCase(parameter)) {
            if (!sender.hasPermission("craftcontrol.reload")) {
                Messages.sendMessage(sender, "&cInsufficient permissions!");
                return false;
            }
            
            CraftControl.getSettings().loadConfig();
            Settings.load();
            
            Messages.sendMessage(sender, "&bSuccessfully reloaded the config!");
        }
        
        return false;
    }
}
