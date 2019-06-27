package com.nannerss.craftcontrol.commands;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nannerss.bananalib.messages.Messages;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.gui.AdminGUI;
import com.nannerss.craftcontrol.utils.GUISounds;

public class CraftControlCommand extends Command {
    
    private Random r = new Random();
    
    public CraftControlCommand() {
        super("craftcontrol");
        
        setDescription("The base command for the CraftControl plugin!");
        setAliases(Arrays.asList("ccontrol", "cc"));
    }
    
    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cYou must be a player to use this command!");
            return false;
        }
        
        final Player p = (Player) sender;
        
        if (args.length < 1) {
            Messages.sendMessage(p, "&bRunning &3CraftControl &bv" + CraftControl.getInstance().getDescription().getVersion() + "!");
            return false;
        }
        
        final String parameter = args[0];
        
        if ("help".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.help")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            // Shorten main help later. REMOVE WHEN DONE
            Messages.sendMessage(p,
                    "",
                    "     &3&lShowing commands for CraftControl...",
                    "",
                    "&b/craftcontrol &3help &8- &7Displays this help page.",
                    "&b/craftcontrol &3gui &8- &7Opens the main admin GUI.",
                    "&b/blacklist &8- &7Shows subcommands for /blacklist.",
                    "&b/recipe &8- &7Shows subcommands for /recipe.",
                    "&b/itemfactory &8 &7Shows subcommands for /itemfactory.",
                    "");
        } else if ("gui".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.list")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            CraftControl.getInventoryCache().getIfPresent(p.getUniqueId()).setPage(0);
            GUISounds.playOpenSound(p);
            p.openInventory(AdminGUI.getMainGui());
        }
        
        return false;
    }
}
