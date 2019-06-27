package com.nannerss.craftcontrol.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.messages.Messages;
import com.nannerss.bananalib.utils.Utils;

public class ItemFactoryCommand extends Command {
    
    public ItemFactoryCommand() {
        super("itemfactory");
        
        setAliases(Arrays.asList("itf", "ifactory", "ifac"));
    }
    
    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cYou must be a player to use this command!");
            return false;
        }
        
        final Player p = (Player) sender;
        
        if (args.length < 1) {
            Messages.sendMessage(p,
                    "",
                    "     &3&lShowing help for /itemfactory...",
                    "",
                    "&b/itemfactory &3name <name> &8- &7Changes the name of the item you're holding.",
                    "&b/itemfactory &3lore <add|remove|clear> &8- &7Manage the lore of the item you're holding.",
                    "&b/itemfactory &3enchant <enchant> <level> &8- &7Adds an enchantmen to the item in your hand.",
                    "&b/itemfactory &3glow &8- &7Adds or removes a glowing effect from the item in your hand.",
                    "");
            return false;
        }
    
        final String parameter = args[0];
    
        if ("name".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.itemfactory.name")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            if (args.length < 2) {
                Messages.sendMessage(p, "&cSpecify the name to give the item!");
                return false;
            }
            
            if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
                Messages.sendMessage(p, "&cYou must have an item in your hand!");
                return false;
            }
            
            final StringBuilder sb = new StringBuilder();
            String space = "";
            
            for (int i = 1; i < args.length; i++) {
                sb.append(space);
                space = " ";
                sb.append(args[i]);
            }
            
            final String name = sb.toString();
            
            final ItemStack item = p.getItemInHand();
            final ItemMeta meta = item.getItemMeta();
            
            meta.setDisplayName(Utils.colorize(name));
            
            item.setItemMeta(meta);
            
            Messages.sendMessage(p, "&bSuccessfully renamed the item in your hand!");
            return false;
        } else if ("lore".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.itemfactory.lore")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
    
            if (args.length < 2) {
                Messages.sendMessage(p, "", "     &3&lUsages for /itemfactory lore...", "", "&b/itemfactory &3lore add <lore> &8- &7Adds a line of lore to the item.", "&b/itemfactory &3lore edit <line> <lore> &8- &7Edits an existing line of lore on the item.", "&b/itemfactory &3lore remove <line> &8- &7Removes the line of lore at the specified line number.", "&b/itemfactory &3lore clear &8- &7Clears all lore on the item.", "");
                return false;
            }
    
            if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
                Messages.sendMessage(p, "&cYou must have an item in your hand!");
                return false;
            }
            
            final String action = args[1];
            final ItemStack item = p.getItemInHand();
            final ItemMeta meta = item.getItemMeta();
            final List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
            
            if ("add".equalsIgnoreCase(action)) {
                if (args.length < 3) {
                    Messages.sendMessage(p, "&cSpecify the lore you would like to add!");
                    return false;
                }
    
                final StringBuilder sb = new StringBuilder();
                String space = "";
    
                for (int i = 2; i < args.length; i++) {
                    sb.append(space);
                    space = " ";
                    sb.append(args[i]);
                }
    
                final String line = sb.toString();
    
                lore.add(Utils.colorize(line));
                meta.setLore(lore);
    
                item.setItemMeta(meta);
    
                Messages.sendMessage(p, "&bSuccessfully added lore to the item in hand!");
                return false;
            } else if ("edit".equalsIgnoreCase(action)) {
                if (args.length < 4) {
                    Messages.sendMessage(p, "&cSpecify the number of the line you want to edit and what you'd like to replace the line with!");
                    return false;
                }
    
                final Integer lineNumber;
    
                try {
                    lineNumber = Integer.parseInt(args[2]);
                } catch (final NumberFormatException e) {
                    Messages.sendMessage(p, "&cPlease specify a whole number!");
                    return false;
                }
    
                final StringBuilder sb = new StringBuilder();
                String space = "";
    
                for (int i = 3; i < args.length; i++) {
                    sb.append(space);
                    space = " ";
                    sb.append(args[i]);
                }
    
                final String line = sb.toString();
    
                try {
                    lore.set(lineNumber - 1, Utils.colorize(line));
                    meta.setLore(lore);
        
                    item.setItemMeta(meta);
                } catch (final IndexOutOfBoundsException e) {
                    Messages.sendMessage(p, "&cCould not find line " + lineNumber + " in the lore!");
                    return false;
                }
    
                Messages.sendMessage(p, "&bEdit line " + lineNumber + " of the lore!");
                return false;
            } else if ("remove".equalsIgnoreCase(action)) {
                if (args.length < 3) {
                    Messages.sendMessage(p, "&cSpecify the number of the line you want to remove!");
                    return false;
                }
                
                final Integer lineNumber;
                
                try {
                    lineNumber = Integer.parseInt(args[2]);
                } catch (final NumberFormatException e) {
                    Messages.sendMessage(p, "&cPlease specify a whole number!");
                    return false;
                }
                
                try {
                    lore.remove(lineNumber - 1);
                    meta.setLore(lore);
    
                    item.setItemMeta(meta);
                } catch (final IndexOutOfBoundsException e) {
                    Messages.sendMessage(p, "&cCould not find line " + lineNumber + " in the lore!");
                    return false;
                }
                
                Messages.sendMessage(p, "&bRemoved line " + lineNumber + " from the lore!");
                return false;
            } else if ("clear".equalsIgnoreCase(action)) {
                lore.clear();
                meta.setLore(lore);
                
                item.setItemMeta(meta);
                
                Messages.sendMessage(p, "&bCleared the lore for this item!");
                return false;
            }
        } else if ("enchant".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.itemfactory.enchant")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
    
            if (args.length < 2) {
                Messages.sendMessage(p, "", "     &3&lUsages for /itemfactory enchant...", "", "&b/itemfactory &3enchant add <enchant> &8- &7Adds the specified enchant to the item.", "&b/itemfactory &3enchant remove <enchant> &8- &7Removes the specified enchant from the item.", "");
                return false;
            }
    
            if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
                Messages.sendMessage(p, "&cYou must have an item in your hand!");
                return false;
            }
    
            final String action = args[1];
            final ItemStack item = p.getItemInHand();
            final ItemMeta meta = item.getItemMeta();
            
            if ("add".equalsIgnoreCase(action)) {
                if (args.length < 4) {
                    Messages.sendMessage(p, "&cSpecify the name and level of the enchant you want to add!");
                    return false;
                }
                
                final String enchantString = args[2];
                
                if (Enchantment.getByName(enchantString.toUpperCase()) == null) {
                    Messages.sendMessage(p, "&c" + enchantString + " is not a valid enchant!");
                    return false;
                }
                
                final Enchantment enchant = Enchantment.getByName(enchantString.toUpperCase());
                final int level;
                
                try {
                    level = Integer.parseInt(args[3]);
                } catch (final NumberFormatException e) {
                    Messages.sendMessage(p, "&cPlease specify a whole number for the level!");
                    return false;
                }
                
                meta.addEnchant(enchant, level, true);
                
                item.setItemMeta(meta);
                
                Messages.sendMessage(p, "&bAdded enchantment " + WordUtils.capitalize(args[2].toLowerCase().replace("_", " ")) + " to the item in your hand!");
            } else if ("remove".equalsIgnoreCase(action)) {
                if (args.length < 3) {
                    Messages.sendMessage(p, "&cSpecify the name of the enchant you want to remove!");
                    return false;
                }
    
                final String enchantString = args[2];
    
                if (Enchantment.getByName(enchantString.toUpperCase()) == null) {
                    Messages.sendMessage(p, "&c" + enchantString + " is not a valid enchant!");
                    return false;
                }
    
                final Enchantment enchant = Enchantment.getByName(enchantString.toUpperCase());
                meta.removeEnchant(enchant);
    
                item.setItemMeta(meta);
    
                Messages.sendMessage(p, "&bRemoved enchantment " + WordUtils.capitalize(args[2].toLowerCase().replace("_", " ")) + " from the item in your hand!");
            }
        } else if ("glow".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.itemfactory.glow")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
    
            if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
                Messages.sendMessage(p, "&cYou must have an item in your hand!");
                return false;
            }
    
            final ItemStack item = p.getItemInHand();
            final ItemMeta meta = item.getItemMeta();
            
            if (!meta.hasEnchant(Enchantment.DURABILITY)) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                
                item.setItemMeta(meta);
                
                Messages.sendMessage(p, "&bAdded a glow to the item in your hand!");
            } else if (meta.hasEnchant(Enchantment.DURABILITY)) {
                meta.removeEnchant(Enchantment.DURABILITY);
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                
                item.setItemMeta(meta);
                
                Messages.sendMessage(p, "&bRemoved glow from the item in your hand!");
            }
        }
        
        return false;
    }
    
    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        if (sender.hasPermission("craftcontrol.itemfactory.enchant") && args.length == 3 && "enchant".equalsIgnoreCase(args[0])) {
            final String arg = args[2];
            final List<String> tab = new ArrayList<>();
            
            for (final Enchantment enchant : Enchantment.values()) {
                if (enchant.getName().startsWith(arg.toUpperCase())) {
                    tab.add(enchant.getName());
                }
            }
            
            return tab;
        }
        
        return super.tabComplete(sender, alias, args);
    }
}
