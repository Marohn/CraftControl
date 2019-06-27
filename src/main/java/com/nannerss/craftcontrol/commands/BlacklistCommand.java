package com.nannerss.craftcontrol.commands;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.nannerss.bananalib.messages.Component;
import com.nannerss.bananalib.messages.Messages;
import com.nannerss.bananalib.utils.Utils;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.data.BlacklistedItems;
import com.nannerss.craftcontrol.utils.comparators.ItemComparator;
import com.nannerss.craftcontrol.data.PlayerCache;
import com.nannerss.craftcontrol.gui.AdminGUI;
import com.nannerss.craftcontrol.utils.GUISounds;
import com.nannerss.craftcontrol.utils.Pagination;

import net.md_5.bungee.api.chat.HoverEvent.Action;

public class BlacklistCommand extends Command {
    
    public BlacklistCommand() {
        super("blacklist");
        
        setAliases(Arrays.asList("bl", "blist"));
    }
    
    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cYou must be a player to use this command!");
            return false;
        }
        
        final Player p = (Player) sender;
        final PlayerCache cache = CraftControl.getInventoryCache().getIfPresent(p.getUniqueId());
        final Pagination pagination = CraftControl.getBlacklistPagination();
        
        if (args.length < 1) {
            Messages.sendMessage(p, "", "     &3&lShowing help for /blacklist...", "", "&b/blacklist &3add &8- &7Adds the item in your hand to the blacklist.", "&b/blacklist &3remove &8- &7Removes the item in your hand from the blacklist.", "&b/blacklist &3list &8- &7Lists the blacklisted items in chat.", "&b/blacklist &3gui &8- &7Opens the blacklist GUI.", "");
            return false;
        }
        
        final String parameter = args[0];
        
        if ("add".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.blacklist.add")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            if (p.getItemInHand().getType() == Material.AIR) {
                Messages.sendMessage(p, Utils.colorize("&cYou can't add nothing, silly!"));
                return false;
            }
            
            final List<ItemStack> items = CraftControl.getBlacklistItemCache();
            final ItemStack item = p.getItemInHand();
            final ItemStack clone = item.clone();
            clone.setAmount(1);
            
            if (items.contains(clone)) {
                Messages.sendMessage(p, Utils.colorize("&cThat item is already blacklisted!"));
                return false;
            }
            
            items.add(clone);
            items.sort(new ItemComparator());
            CraftControl.getBlacklistPagination().add(clone);
            
            BlacklistedItems.setItems(items);
            BlacklistedItems.save();
            
            Messages.sendMessage(p, "&bAdded item in hand to the blacklisted items list!");
        } else if ("remove".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.blacklist.remove")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            if (p.getItemInHand().getType() == Material.AIR) {
                Messages.sendMessage(p, Utils.colorize("&cYou can't add nothing, silly!"));
                return false;
            }
            
            final List<ItemStack> items = CraftControl.getBlacklistItemCache();
            final ItemStack item = p.getItemInHand();
            final ItemStack clone = item.clone();
            clone.setAmount(1);
            
            if (!items.contains(clone)) {
                Messages.sendMessage(p, Utils.colorize("&cThat item isn't blacklisted!"));
                return false;
            }
            
            items.remove(clone);
            items.sort(new ItemComparator());
            CraftControl.getBlacklistPagination().remove(clone);
            
            BlacklistedItems.setItems(items);
            BlacklistedItems.save();
            
            Messages.sendMessage(p, "&bRemoved item in hand from the blacklisted items list!");
        } else if ("list".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.blacklist.list")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            final List<ItemStack> items = CraftControl.getBlacklistItemCache();
            items.sort(new ItemComparator());
            
            Messages.sendMessage(p, "", "     &3&lShowing blacklisted items...", "");
            
            for (final ItemStack item : items) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().getDisplayName() == null) {
                        Component.builder(" &8- ").append("&b" + WordUtils.capitalize(item.getType().toString().toLowerCase().replace("_", " "))).onHover(Action.SHOW_ITEM, item).send(p);
                    } else {
                        Component.builder(" &8- ").append(item.getItemMeta().getDisplayName()).onHover(Action.SHOW_ITEM, item).send(p);
                    }
                } else {
                    Component.builder(" &8- ").append("&b" + WordUtils.capitalize(item.getType().toString().toLowerCase().replace("_", " "))).onHover(Action.SHOW_ITEM, item).send(p);
                }
            }
            
            Messages.sendMessage(p, "");
        } else if ("gui".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.gui")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
    
            cache.setGui(Bukkit.createInventory(null, 54, "Craft Control: Blacklisted Items"));
            final Inventory inv = cache.getGui();
    
            if (pagination.totalPages() == 0) {
                AdminGUI.setBlankBlacklistPageView(inv);
            } else {
                AdminGUI.setBlacklistPageView(inv, 1);
            }
            cache.setPage(1);
    
            GUISounds.playOpenSound(p);
            p.openInventory(inv);
        }
        
        return false;
    }
}
