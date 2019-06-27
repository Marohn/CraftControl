package com.nannerss.craftcontrol.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.nannerss.bananalib.config.ConfigManager;
import com.nannerss.bananalib.messages.Console;
import com.nannerss.bananalib.messages.Messages;
import com.nannerss.bananalib.utils.Utils;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.data.BlacklistedItems;
import com.nannerss.craftcontrol.data.CustomRecipe;
import com.nannerss.craftcontrol.data.CustomRecipe.CustomRecipeType;
import com.nannerss.craftcontrol.data.PlayerCache;
import com.nannerss.craftcontrol.gui.AdminGUI;
import com.nannerss.craftcontrol.items.guis.Filler;
import com.nannerss.craftcontrol.items.guis.blacklist.BlacklistButtons;
import com.nannerss.craftcontrol.items.guis.blacklist.BlacklistInfo;
import com.nannerss.craftcontrol.items.guis.main.MainButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeDenyItems;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeFurnaceButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeInfo;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeShapedButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeShapelessButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeTypeButtons;
import com.nannerss.craftcontrol.tasks.RecipeNamePrompt;
import com.nannerss.craftcontrol.utils.GUISounds;
import com.nannerss.craftcontrol.utils.Pagination;
import com.nannerss.craftcontrol.utils.RecipeUtils;

public class ClickListener implements Listener {
    
    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        
        final Player p = (Player) e.getWhoClicked();
        final PlayerCache cache = CraftControl.getInventoryCache().getIfPresent(p.getUniqueId());
        final Pagination blacklistPagination = CraftControl.getBlacklistPagination();
        final Pagination recipePagination = CraftControl.getRecipePagination();
        final List<ItemStack> blacklistItems = CraftControl.getBlacklistItemCache();
        final List<ItemStack> recipeItems = CraftControl.getRecipeItemCache();
        
        if (e.getCurrentItem() == null) {
            return;
        }
        
        if (e.getClickedInventory().equals(AdminGUI.getMainGui())) {
            e.setCancelled(true);
            
            if (!e.getCurrentItem().equals(Filler.getItem())) {
                if (e.getCurrentItem().equals(MainButtons.getBlacklistButton())) {
                    cache.setGui(Bukkit.createInventory(null, 54, "Craft Control: Blacklisted Items"));
                    final Inventory inv = cache.getGui();
                    
                    if (blacklistPagination.totalPages() == 0) {
                        AdminGUI.setBlankBlacklistPageView(inv);
                    } else {
                        AdminGUI.setBlacklistPageView(inv, 1);
                    }
                    cache.setPage(1);
    
                    GUISounds.playClickSound(p);
                    p.openInventory(inv);
                } else if (e.getCurrentItem().equals(MainButtons.getRecipeButton())) {
                    cache.setGui(Bukkit.createInventory(null, 54, "Craft Control: Custom Recipes"));
                    final Inventory inv = cache.getGui();
                    
                    if (recipePagination.totalPages() == 0) {
                        AdminGUI.setBlankRecipePageView(inv);
                    } else {
                        AdminGUI.setRecipePageView(inv, 1);
                    }
                    cache.setPage(1);
                    
                    GUISounds.playClickSound(p);
                    p.openInventory(inv);
                }
            }
        } else if (e.getClickedInventory().equals(cache.getGui())) {
            if (e.getView().getTitle().contains("Blacklisted Items")) {
                e.setCancelled(true);
                
                if (!e.getCurrentItem().equals(Filler.getItem()) && !e.getCurrentItem().equals(BlacklistInfo.getItem()) && e.getCurrentItem().getType() != Material.AIR) {
                    GUISounds.playClickSound(p);
                    
                    if (e.getCurrentItem().equals(BlacklistButtons.getBackButton())) {
                        p.openInventory(AdminGUI.getMainGui());
                        cache.setPage(0);
                    } else if (e.getCurrentItem().equals(BlacklistButtons.getNextButton())) {
                        final Inventory inv = cache.getGui();
                        
                        AdminGUI.setBlacklistPageView(inv, cache.getPage() + 1);
                        cache.setPage(cache.getPage() + 1);
                    } else if (e.getCurrentItem().equals(BlacklistButtons.getPreviousButton())) {
                        final Inventory inv = cache.getGui();
                        
                        AdminGUI.setBlacklistPageView(inv, cache.getPage() - 1);
                        cache.setPage(cache.getPage() - 1);
                    } else {
                        final ItemStack clone = e.getCurrentItem().clone();
                        final ItemMeta meta = clone.getItemMeta();
                        List<String> lore = new ArrayList<>();
                        
                        if (clone.hasItemMeta() && clone.getItemMeta().hasLore()) {
                            lore = clone.getItemMeta().getLore();
                        }
                        lore.remove(Utils.colorize("&8&m---------------------------------"));
                        lore.remove(Utils.colorize("&6&lClick &fto remove!"));
                        lore.remove(Utils.colorize("&8&m---------------------------------"));
                        
                        meta.setLore(lore);
                        clone.setItemMeta(meta);
                        
                        if (blacklistItems.contains(clone)) {
                            blacklistItems.remove(clone);
                            CraftControl.getBlacklistPagination().remove(clone);
                            
                            BlacklistedItems.setItems(blacklistItems);
                            BlacklistedItems.save();
                            
                            final Inventory inv = cache.getGui();
                            
                            if (!blacklistPagination.exists(cache.getPage())) {
                                if (blacklistPagination.totalPages() == 0) {
                                    AdminGUI.setBlankBlacklistPageView(inv);
                                } else {
                                    AdminGUI.setBlacklistPageView(inv, blacklistPagination.totalPages());
                                    cache.setPage(blacklistPagination.totalPages());
                                }
                            } else {
                                AdminGUI.setBlacklistPageView(inv, cache.getPage());
                            }
                        }
                    }
                }
            } else if (e.getView().getTitle().contains("Custom Recipes")) {
                e.setCancelled(true);
                
                if (!e.getCurrentItem().equals(Filler.getItem()) && !e.getCurrentItem().equals(RecipeInfo.getItem()) && e.getCurrentItem().getType() != Material.AIR) {
                    GUISounds.playClickSound(p);
        
                    if (e.getCurrentItem().equals(RecipeButtons.getBackButton())) {
                        p.openInventory(AdminGUI.getMainGui());
                        cache.setPage(0);
                    } else if (e.getCurrentItem().equals(RecipeButtons.getNextButton())) {
                        final Inventory inv = cache.getGui();
            
                        AdminGUI.setRecipePageView(inv, cache.getPage() + 1);
                        cache.setPage(cache.getPage() + 1);
                    } else if (e.getCurrentItem().equals(RecipeButtons.getPreviousButton())) {
                        final Inventory inv = cache.getGui();
            
                        AdminGUI.setRecipePageView(inv, cache.getPage() - 1);
                        cache.setPage(cache.getPage() - 1);
                    } else if (e.getCurrentItem().equals(RecipeButtons.getAddButton())) {
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1F);
                        new RecipeNamePrompt(p);
                    } else {
                        final ItemStack clone = e.getCurrentItem().clone();
                        final ItemMeta meta = clone.getItemMeta();
                        List<String> lore = new ArrayList<>();
                        String nameLine = "";
    
                        if (e.getClick() == ClickType.LEFT) {
                            if (clone.hasItemMeta() && clone.getItemMeta().hasLore()) {
                                lore = clone.getItemMeta().getLore();
        
                                for (String line : lore) {
                                    if (line.contains(ChatColor.stripColor("Name"))) {
                                        nameLine = line.replace(" ", "");
                                    }
                                }
                            }
                            
                            final String name = nameLine.split(":")[1];
                            final CustomRecipe recipe = CraftControl.getRecipe(name);
                            
                            if (recipe.getType() == CustomRecipeType.SHAPELESS) {
                                cache.setGui(Bukkit.createInventory(null, 45, "Edit Recipe: " + recipe.getName()));
                                final Inventory inv = cache.getGui();
    
                                AdminGUI.setRecipeEditShapelessView(inv, recipe);
                                cache.setPage(0);
    
                                p.openInventory(inv);
    
                                CraftControl.getEditSessions().put(p.getUniqueId(), name);
                            } else if (recipe.getType() == CustomRecipeType.SHAPED) {
                                cache.setGui(Bukkit.createInventory(null, 45, "Edit Recipe: " + recipe.getName()));
                                final Inventory inv = cache.getGui();
    
                                AdminGUI.setRecipeEditShapedView(inv, recipe);
                                cache.setPage(0);
    
                                p.openInventory(inv);
    
                                CraftControl.getEditSessions().put(p.getUniqueId(), name);
                            } else if (recipe.getType() == CustomRecipeType.FURNACE) {
                                cache.setGui(Bukkit.createInventory(null, 45, "Edit Recipe: " + recipe.getName()));
                                final Inventory inv = cache.getGui();
    
                                AdminGUI.setRecipeEditFurnaceView(inv, recipe);
                                cache.setPage(0);
    
                                p.openInventory(inv);
    
                                CraftControl.getEditSessions().put(p.getUniqueId(), name);
                            }
                        } else if (e.getClick() == ClickType.RIGHT) {
                            if (clone.hasItemMeta() && clone.getItemMeta().hasLore()) {
                                lore = clone.getItemMeta().getLore();
                                
                                for (String line : lore) {
                                    if (line.contains(ChatColor.stripColor("Name"))) {
                                        nameLine = line.replace(" ", "");
                                    }
                                }
                            }
                            lore.remove(Utils.colorize("&8&m---------------------------------"));
                            lore.remove(Utils.colorize("&6&lLeft Click &fto edit!"));
                            lore.remove(Utils.colorize("&6&lRight Click &fto remove!"));
                            lore.remove(Utils.colorize("&8&m---------------------------------"));
        
                            meta.setLore(lore);
                            clone.setItemMeta(meta);
    
                            final String name = nameLine.split(":")[1];
                            final CustomRecipe recipe = CraftControl.getRecipe(name);
    
                            CraftControl.getRecipeCache().invalidate(name);
                            
                            final ConfigManager cfg = CraftControl.getCustomRecipes();
                            cfg.set("custom-recipes." + name, null);
                            cfg.saveConfig();
        
                            if (recipeItems.contains(clone)) {
                                recipeItems.remove(clone);
                                CraftControl.getRecipePagination().remove(clone);
    
                                if (recipe.getType() == CustomRecipeType.SHAPELESS) {
                                    final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(recipe.getResult());
                                    
                                    if (recipe.getSlot1() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot1().getData());
                                    }
                                    if (recipe.getSlot2() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot2().getData());
                                    }
                                    if (recipe.getSlot3() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot3().getData());
                                    }
                                    if (recipe.getSlot4() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot4().getData());
                                    }
                                    if (recipe.getSlot5() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot5().getData());
                                    }
                                    if (recipe.getSlot6() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot6().getData());
                                    }
                                    if (recipe.getSlot7() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot7().getData());
                                    }
                                    if (recipe.getSlot8() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot8().getData());
                                    }
                                    if (recipe.getSlot9() != null) {
                                        shapelessRecipe.addIngredient(recipe.getSlot9().getData());
                                    }
                                    
                                    RecipeUtils.removeShapelessRecipe(shapelessRecipe);
                                } else if (recipe.getType() == CustomRecipeType.SHAPED) {
                                    final ShapedRecipe shapedRecipe = new ShapedRecipe(recipe.getResult());
                                    shapedRecipe.shape("123", "456", "789");
    
                                    if (recipe.getSlot1() != null) {
                                        shapedRecipe.setIngredient('1', recipe.getSlot1().getData());
                                    }
                                    if (recipe.getSlot2() != null) {
                                        shapedRecipe.setIngredient('2', recipe.getSlot2().getData());
                                    }
                                    if (recipe.getSlot3() != null) {
                                        shapedRecipe.setIngredient('3', recipe.getSlot3().getData());
                                    }
                                    if (recipe.getSlot4() != null) {
                                        shapedRecipe.setIngredient('4', recipe.getSlot4().getData());
                                    }
                                    if (recipe.getSlot5() != null) {
                                        shapedRecipe.setIngredient('5', recipe.getSlot5().getData());
                                    }
                                    if (recipe.getSlot6() != null) {
                                        shapedRecipe.setIngredient('6', recipe.getSlot6().getData());
                                    }
                                    if (recipe.getSlot7() != null) {
                                        shapedRecipe.setIngredient('7', recipe.getSlot7().getData());
                                    }
                                    if (recipe.getSlot8() != null) {
                                        shapedRecipe.setIngredient('8', recipe.getSlot8().getData());
                                    }
                                    if (recipe.getSlot9() != null) {
                                        shapedRecipe.setIngredient('9', recipe.getSlot9().getData());
                                    }
    
                                    RecipeUtils.removeShapedRecipe(shapedRecipe);
                                } else if (recipe.getType() == CustomRecipeType.FURNACE) {
                                    final FurnaceRecipe furnaceRecipe = new FurnaceRecipe(recipe.getResult(), recipe.getInput().getData());
    
                                    furnaceRecipe.setCookingTime(recipe.getCookTime());
                                    furnaceRecipe.setExperience(recipe.getExp());
                                    
                                    RecipeUtils.removeFurnaceRecipe(furnaceRecipe);
                                }
            
                                final Inventory inv = cache.getGui();
            
                                if (!recipePagination.exists(cache.getPage())) {
                                    if (recipePagination.totalPages() == 0) {
                                        AdminGUI.setBlankRecipePageView(inv);
                                    } else {
                                        AdminGUI.setRecipePageView(inv, recipePagination.totalPages());
                                        cache.setPage(recipePagination.totalPages());
                                    }
                                } else {
                                    AdminGUI.setRecipePageView(inv, cache.getPage());
                                }
                            }
                        }
                    }
                }
            } else if (e.getView().getTitle().contains("Choose Recipe Type")) {
                e.setCancelled(true);
    
                if (!e.getCurrentItem().equals(Filler.getItem()) && e.getCurrentItem().getType() != Material.AIR) {
                    GUISounds.playClickSound(p);
    
                    if (e.getCurrentItem().equals(RecipeTypeButtons.getShapelessButton())) {
                        cache.setGui(Bukkit.createInventory(null, 45, "Add Recipe: " + CraftControl.getEditSessions().getIfPresent(p.getUniqueId())));
                        final Inventory inv = cache.getGui();
    
                        AdminGUI.setRecipeCreateShapelessView(inv);
                        cache.setPage(0);
    
                        GUISounds.playClickSound(p);
                        p.openInventory(inv);
                    } else if (e.getCurrentItem().equals(RecipeTypeButtons.getShapedButton())) {
                        cache.setGui(Bukkit.createInventory(null, 45, "Add Recipe: " + CraftControl.getEditSessions().getIfPresent(p.getUniqueId())));
                        final Inventory inv = cache.getGui();
    
                        AdminGUI.setRecipeCreateShapedView(inv);
                        cache.setPage(0);
    
                        GUISounds.playClickSound(p);
                        p.openInventory(inv);
                    } else if (e.getCurrentItem().equals(RecipeTypeButtons.getFurnaceButton())) {
                        cache.setGui(Bukkit.createInventory(null, 45, "Add Recipe: " + CraftControl.getEditSessions().getIfPresent(p.getUniqueId())));
                        final Inventory inv = cache.getGui();
    
                        AdminGUI.setRecipeCreateFurnaceView(inv);
                        cache.setPage(0);
    
                        GUISounds.playClickSound(p);
                        p.openInventory(inv);
                    }
                }
            } else if (e.getView().getTitle().contains("Add Recipe")) {
                final String type = ChatColor.stripColor(e.getView().getTopInventory().getItem(19).getItemMeta().getDisplayName());
                
                if (!e.getCurrentItem().equals(Filler.getItem())) {
                    if (type.contains("Shapeless")) {
                        if (e.getCurrentItem().equals(RecipeShapelessButtons.getTypeButton())) {
                            e.setCancelled(true);
                            GUISounds.playClickSound(p);
                            
                            cache.setGui(Bukkit.createInventory(null, 27, "Choose Recipe Type"));
                            final Inventory inv = cache.getGui();
    
                            AdminGUI.setRecipeChooseTypeView(inv);
                            cache.setPage(0);
    
                            p.openInventory(inv);
                        } else if (e.getCurrentItem().equals(RecipeShapelessButtons.getSaveButton())) {
                            e.setCancelled(true);
                            if (e.getClickedInventory().getItem(25) == null || (e.getClickedInventory().getItem(12) == null && e.getClickedInventory().getItem(13) == null && e.getClickedInventory().getItem(14) == null && e.getClickedInventory().getItem(21) == null && e.getClickedInventory().getItem(22) == null && e.getClickedInventory().getItem(23) == null && e.getClickedInventory().getItem(30) == null && e.getClickedInventory().getItem(31) == null && e.getClickedInventory().getItem(32) == null)) {
                                GUISounds.playBassSound(p);
                                
                                final ItemStack button = e.getCurrentItem().clone();
                                e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getInvalidRecipeItem());
                                
                                new BukkitRunnable() {
                                    
                                    @Override
                                    public void run() {
                                        e.getClickedInventory().setItem(e.getSlot(), button);
                                    }
                                    
                                }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                                
                                return;
                            }
    
                            GUISounds.playClickSound(p);
                            
                            final String name = CraftControl.getEditSessions().getIfPresent(p.getUniqueId());
                            final CustomRecipe recipe = CraftControl.getRecipe(name);
                            
                            recipe.setType(CustomRecipeType.SHAPELESS);
                            recipe.setSlot1(e.getClickedInventory().getItem(12));
                            recipe.setSlot2(e.getClickedInventory().getItem(13));
                            recipe.setSlot3(e.getClickedInventory().getItem(14));
                            recipe.setSlot4(e.getClickedInventory().getItem(21));
                            recipe.setSlot5(e.getClickedInventory().getItem(22));
                            recipe.setSlot6(e.getClickedInventory().getItem(23));
                            recipe.setSlot7(e.getClickedInventory().getItem(30));
                            recipe.setSlot8(e.getClickedInventory().getItem(31));
                            recipe.setSlot9(e.getClickedInventory().getItem(32));
                            recipe.setResult(e.getClickedInventory().getItem(25));
                            
                            recipe.save();
                            recipe.loadRecipe();
    
                            final ItemStack item = new ItemStack(Material.DIAMOND_BLOCK);
                            final ItemMeta meta = item.getItemMeta();
    
                            meta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            meta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            meta.addItemFlags(ItemFlag.values());
    
                            item.setItemMeta(meta);
    
                            recipeItems.add(item);
                            recipePagination.add(item);
                            
                            CraftControl.getEditSessions().invalidate(p.getUniqueId());
                            p.closeInventory();
    
                            Messages.sendMessage(p, "&bSaved " + WordUtils.capitalize(recipe.getType().toString().toLowerCase()) + " Recipe " + recipe.getName() + "!");
                        } else if (e.getCurrentItem().equals(RecipeDenyItems.getInvalidRecipeItem())) {
                            GUISounds.playBassSound(p);
                            e.setCancelled(true);
                        } else {
                            GUISounds.playClickSound(p);
                        }
                    } else if (type.contains("Shaped")) {
                        if (e.getCurrentItem().equals(RecipeShapedButtons.getTypeButton())) {
                            e.setCancelled(true);
                            GUISounds.playClickSound(p);
        
                            cache.setGui(Bukkit.createInventory(null, 27, "Choose Recipe Type"));
                            final Inventory inv = cache.getGui();
        
                            AdminGUI.setRecipeChooseTypeView(inv);
                            cache.setPage(0);
        
                            p.openInventory(inv);
                        } else if (e.getCurrentItem().equals(RecipeShapedButtons.getSaveButton())) {
                            e.setCancelled(true);
                            if (e.getClickedInventory().getItem(25) == null || (e.getClickedInventory().getItem(12) == null && e.getClickedInventory().getItem(13) == null && e.getClickedInventory().getItem(14) == null && e.getClickedInventory().getItem(21) == null && e.getClickedInventory().getItem(22) == null && e.getClickedInventory().getItem(23) == null && e.getClickedInventory().getItem(30) == null && e.getClickedInventory().getItem(31) == null && e.getClickedInventory().getItem(32) == null)) {
                                GUISounds.playBassSound(p);
            
                                final ItemStack button = e.getCurrentItem().clone();
                                e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getInvalidRecipeItem());
            
                                new BukkitRunnable() {
                
                                    @Override
                                    public void run() {
                                        e.getClickedInventory().setItem(e.getSlot(), button);
                                    }
                
                                }.runTaskLater(CraftControl.getInstance(), 20 * 5);
            
                                return;
                            }
        
                            GUISounds.playClickSound(p);
        
                            final String name = CraftControl.getEditSessions().getIfPresent(p.getUniqueId());
                            final CustomRecipe recipe = CraftControl.getRecipe(name);
    
                            recipe.setType(CustomRecipeType.SHAPED);
                            recipe.setSlot1(e.getClickedInventory().getItem(12));
                            recipe.setSlot2(e.getClickedInventory().getItem(13));
                            recipe.setSlot3(e.getClickedInventory().getItem(14));
                            recipe.setSlot4(e.getClickedInventory().getItem(21));
                            recipe.setSlot5(e.getClickedInventory().getItem(22));
                            recipe.setSlot6(e.getClickedInventory().getItem(23));
                            recipe.setSlot7(e.getClickedInventory().getItem(30));
                            recipe.setSlot8(e.getClickedInventory().getItem(31));
                            recipe.setSlot9(e.getClickedInventory().getItem(32));
                            recipe.setResult(e.getClickedInventory().getItem(25));
        
                            recipe.save();
                            recipe.loadRecipe();
        
                            final ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
                            final ItemMeta meta = item.getItemMeta();
        
                            meta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            meta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            meta.addItemFlags(ItemFlag.values());
        
                            item.setItemMeta(meta);
        
                            recipeItems.add(item);
                            recipePagination.add(item);
        
                            CraftControl.getEditSessions().invalidate(p.getUniqueId());
                            p.closeInventory();
    
                            Messages.sendMessage(p, "&bSaved " + WordUtils.capitalize(recipe.getType().toString().toLowerCase()) + " Recipe " + recipe.getName() + "!");
                        } else if (e.getCurrentItem().equals(RecipeDenyItems.getInvalidRecipeItem())) {
                            GUISounds.playBassSound(p);
                            e.setCancelled(true);
                        } else {
                            GUISounds.playClickSound(p);
                        }
                    } else if (type.contains("Furnace")) {
                        if (e.getCurrentItem().equals(RecipeFurnaceButtons.getTypeButton())) {
                            e.setCancelled(true);
                            GUISounds.playClickSound(p);
        
                            cache.setGui(Bukkit.createInventory(null, 27, "Choose Recipe Type"));
                            final Inventory inv = cache.getGui();
        
                            AdminGUI.setRecipeChooseTypeView(inv);
                            cache.setPage(0);
        
                            p.openInventory(inv);
                        } else if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals(RecipeFurnaceButtons.getCookTimeButton().getItemMeta().getDisplayName())) {
                            e.setCancelled(true);
                            
                            final ItemStack button = e.getCurrentItem();
                            final ItemStack buttonClone = e.getCurrentItem().clone();
                            final List<String> lore;
                            
                            final String valueString = ChatColor.stripColor(button.getItemMeta().getLore().get(0).replace(" ", ""));
                            int value = Integer.parseInt(valueString.split(":")[1]);
                            
                            if (e.getClick() == ClickType.LEFT) {
                                if ((value + 1) > 1200) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
    
                                    new BukkitRunnable() {
        
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
        
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                                    
                                    return;
                                }
                                
                                value += 1;
                                
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                                
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                                
                                meta.setLore(lore);
                                button.setItemMeta(meta);
    
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                                if ((value + 10) > 1200) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
        
                                    new BukkitRunnable() {
            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
        
                                    return;
                                }
    
                                value += 10;
    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
    
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.RIGHT) {
                                if ((value - 1) < 0) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
        
                                    new BukkitRunnable() {
            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
        
                                    return;
                                }
                                
                                value -= 1;
    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                                
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                                if ((value - 10) < 0) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
        
                                    new BukkitRunnable() {
            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
        
                                    return;
                                }
    
                                value -= 10;
    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
    
                                GUISounds.playClickSound(p);
                            }
                        } else if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals(RecipeFurnaceButtons.getExpButton().getItemMeta().getDisplayName())) {
                            e.setCancelled(true);
    
                            final ItemStack button = e.getCurrentItem();
                            final ItemStack buttonClone = e.getCurrentItem().clone();
                            final List<String> lore;
    
                            final String valueString = ChatColor.stripColor(button.getItemMeta().getLore().get(0).replace(" ", ""));
                            int value = Integer.parseInt(valueString.split(":")[1]);
    
                            if (e.getClick() == ClickType.LEFT) {
                                if ((value + 1) > 1200) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
            
                                    new BukkitRunnable() {
                
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
            
                                    return;
                                }
        
                                value += 1;
        
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
        
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
        
                                meta.setLore(lore);
                                button.setItemMeta(meta);
        
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                                if ((value + 10) > 1200) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
            
                                    new BukkitRunnable() {
                
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
            
                                    return;
                                }
        
                                value += 10;
        
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
        
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
        
                                meta.setLore(lore);
                                button.setItemMeta(meta);
        
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.RIGHT) {
                                if ((value - 1) < 0) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
            
                                    new BukkitRunnable() {
                
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
            
                                    return;
                                }
        
                                value -= 1;
        
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
        
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
        
                                meta.setLore(lore);
                                button.setItemMeta(meta);
        
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                                if ((value - 10) < 0) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
            
                                    new BukkitRunnable() {
                
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
            
                                    return;
                                }
        
                                value -= 10;
        
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
        
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
        
                                meta.setLore(lore);
                                button.setItemMeta(meta);
        
                                GUISounds.playClickSound(p);
                            } else if (e.getCurrentItem().equals(RecipeDenyItems.getInvalidRecipeItem())) {
                                GUISounds.playBassSound(p);
                                e.setCancelled(true);
                            }
                        } else if (e.getCurrentItem().equals(RecipeFurnaceButtons.getSaveButton())) {
                            e.setCancelled(true);
                            
                            if (e.getClickedInventory().getItem(21) == null || e.getClickedInventory().getItem(25) == null) {
                                GUISounds.playBassSound(p);
            
                                final ItemStack button = e.getCurrentItem().clone();
                                e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getInvalidRecipeItem());
            
                                new BukkitRunnable() {
                
                                    @Override
                                    public void run() {
                                        e.getClickedInventory().setItem(e.getSlot(), button);
                                    }
                
                                }.runTaskLater(CraftControl.getInstance(), 20 * 5);
            
                                return;
                            }
        
                            GUISounds.playClickSound(p);
        
                            final String name = CraftControl.getEditSessions().getIfPresent(p.getUniqueId());
                            final String cookTimeString = ChatColor.stripColor(e.getClickedInventory().getItem(14).getItemMeta().getLore().get(0)).replace(" ", "");
                            final String expString = ChatColor.stripColor(e.getClickedInventory().getItem(32).getItemMeta().getLore().get(0)).replace(" ", "");
                            
                            int cookTime = 100;
                            int exp = 1;
                            
                            try {
                                cookTime = Integer.parseInt(cookTimeString.split(":")[1]);
                                exp = Integer.parseInt(expString.split(":")[1]);
                            } catch (final NumberFormatException ex) {
                                Console.log("&cCould not parse values for cook time and/or xp!");
                                ex.printStackTrace();
                            }
                            
                            final CustomRecipe recipe = CraftControl.getRecipe(name);
                            final ItemStack input = e.getClickedInventory().getItem(21).clone();
                            input.setAmount(1);
        
                            recipe.setType(CustomRecipeType.FURNACE);
                            recipe.setInput(input);
                            recipe.setResult(e.getClickedInventory().getItem(25));
                            
                            recipe.setCookTime(cookTime);
                            recipe.setExp(exp);
        
                            recipe.save();
                            recipe.loadRecipe();
        
                            final ItemStack item = new ItemStack(Material.FURNACE);
                            final ItemMeta meta = item.getItemMeta();
    
                            meta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            meta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Cook Time&f: " + recipe.getCookTime()),
                                    Utils.colorize("&3XP Yield&f: " + recipe.getExp()),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            meta.addItemFlags(ItemFlag.values());
        
                            item.setItemMeta(meta);
        
                            recipeItems.add(item);
                            recipePagination.add(item);
        
                            CraftControl.getEditSessions().invalidate(p.getUniqueId());
                            p.closeInventory();
    
                            Messages.sendMessage(p, "&bSaved " + WordUtils.capitalize(recipe.getType().toString().toLowerCase()) + " Recipe " + recipe.getName() + "!");
                        } else if (e.getCurrentItem().equals(RecipeDenyItems.getValueOutOfRangeButton())) {
                            GUISounds.playBassSound(p);
                            e.setCancelled(true);
                        } else {
                            GUISounds.playClickSound(p);
                        }
                    }
                } else {
                    e.setCancelled(true);
                }
            } else if (e.getView().getTitle().contains("Edit Recipe")) {
                final String type = ChatColor.stripColor(e.getView().getTopInventory().getItem(19).getItemMeta().getDisplayName());
    
                if (!e.getCurrentItem().equals(Filler.getItem())) {
                    if (type.contains("Shapeless")) {
                        if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Shapeless Recipe")) {
                            e.setCancelled(true);
                        } else if (e.getCurrentItem().equals(RecipeShapelessButtons.getSaveButton())) {
                            e.setCancelled(true);
                            if (e.getClickedInventory().getItem(25) == null || (e.getClickedInventory().getItem(12) == null && e.getClickedInventory().getItem(13) == null && e.getClickedInventory().getItem(14) == null && e.getClickedInventory().getItem(21) == null && e.getClickedInventory().getItem(22) == null && e.getClickedInventory().getItem(23) == null && e.getClickedInventory().getItem(30) == null && e.getClickedInventory().getItem(31) == null && e.getClickedInventory().getItem(32) == null)) {
                                GUISounds.playBassSound(p);
                    
                                final ItemStack button = e.getCurrentItem().clone();
                                e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getInvalidRecipeItem());
                    
                                new BukkitRunnable() {
                        
                                    @Override
                                    public void run() {
                                        e.getClickedInventory().setItem(e.getSlot(), button);
                                    }
                        
                                }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                    
                                return;
                            }
                
                            GUISounds.playClickSound(p);
                
                            final String name = CraftControl.getEditSessions().getIfPresent(p.getUniqueId());
                            final CustomRecipe recipe = CraftControl.getRecipe(name);
    
                            final ItemStack before = new ItemStack(Material.DIAMOND_BLOCK);
                            final ItemMeta beforeMeta = before.getItemMeta();
    
                            beforeMeta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            beforeMeta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            beforeMeta.addItemFlags(ItemFlag.values());
    
                            before.setItemMeta(beforeMeta);
                
                            recipe.setType(CustomRecipeType.SHAPELESS);
                            recipe.setSlot1(e.getClickedInventory().getItem(12));
                            recipe.setSlot2(e.getClickedInventory().getItem(13));
                            recipe.setSlot3(e.getClickedInventory().getItem(14));
                            recipe.setSlot4(e.getClickedInventory().getItem(21));
                            recipe.setSlot5(e.getClickedInventory().getItem(22));
                            recipe.setSlot6(e.getClickedInventory().getItem(23));
                            recipe.setSlot7(e.getClickedInventory().getItem(30));
                            recipe.setSlot8(e.getClickedInventory().getItem(31));
                            recipe.setSlot9(e.getClickedInventory().getItem(32));
                            recipe.setResult(e.getClickedInventory().getItem(25));
                
                            recipe.save();
                            recipe.loadRecipe();
                
                            final ItemStack item = new ItemStack(Material.DIAMOND_BLOCK);
                            final ItemMeta meta = item.getItemMeta();
                
                            meta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            meta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            meta.addItemFlags(ItemFlag.values());
                
                            item.setItemMeta(meta);
                            
                            recipeItems.remove(before);
                            recipePagination.remove(before);
                
                            recipeItems.add(item);
                            recipePagination.add(item);
                
                            CraftControl.getEditSessions().invalidate(p.getUniqueId());
                            p.closeInventory();
    
                            Messages.sendMessage(p, "&bSaved " + WordUtils.capitalize(recipe.getType().toString().toLowerCase()) + " Recipe " + recipe.getName() + "!");
                        } else if (e.getCurrentItem().equals(RecipeDenyItems.getInvalidRecipeItem())) {
                            GUISounds.playBassSound(p);
                            e.setCancelled(true);
                        } else {
                            GUISounds.playClickSound(p);
                        }
                    } else if (type.contains("Shaped")) {
                        if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Shaped Recipe")) {
                            e.setCancelled(true);
                        } else if (e.getCurrentItem().equals(RecipeShapedButtons.getSaveButton())) {
                            e.setCancelled(true);
                            if (e.getClickedInventory().getItem(25) == null || (e.getClickedInventory().getItem(12) == null && e.getClickedInventory().getItem(13) == null && e.getClickedInventory().getItem(14) == null && e.getClickedInventory().getItem(21) == null && e.getClickedInventory().getItem(22) == null && e.getClickedInventory().getItem(23) == null && e.getClickedInventory().getItem(30) == null && e.getClickedInventory().getItem(31) == null && e.getClickedInventory().getItem(32) == null)) {
                                GUISounds.playBassSound(p);
                    
                                final ItemStack button = e.getCurrentItem().clone();
                                e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getInvalidRecipeItem());
                    
                                new BukkitRunnable() {
                        
                                    @Override
                                    public void run() {
                                        e.getClickedInventory().setItem(e.getSlot(), button);
                                    }
                        
                                }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                    
                                return;
                            }
                
                            GUISounds.playClickSound(p);
                
                            final String name = CraftControl.getEditSessions().getIfPresent(p.getUniqueId());
                            final CustomRecipe recipe = CraftControl.getRecipe(name);
    
                            final ItemStack before = new ItemStack(Material.EMERALD_BLOCK);
                            final ItemMeta beforeMeta = before.getItemMeta();
    
                            beforeMeta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            beforeMeta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            beforeMeta.addItemFlags(ItemFlag.values());
    
                            before.setItemMeta(beforeMeta);
                
                            recipe.setType(CustomRecipeType.SHAPED);
                            recipe.setSlot1(e.getClickedInventory().getItem(12));
                            recipe.setSlot2(e.getClickedInventory().getItem(13));
                            recipe.setSlot3(e.getClickedInventory().getItem(14));
                            recipe.setSlot4(e.getClickedInventory().getItem(21));
                            recipe.setSlot5(e.getClickedInventory().getItem(22));
                            recipe.setSlot6(e.getClickedInventory().getItem(23));
                            recipe.setSlot7(e.getClickedInventory().getItem(30));
                            recipe.setSlot8(e.getClickedInventory().getItem(31));
                            recipe.setSlot9(e.getClickedInventory().getItem(32));
                            recipe.setResult(e.getClickedInventory().getItem(25));
                
                            recipe.save();
                            recipe.loadRecipe();
                
                            final ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
                            final ItemMeta meta = item.getItemMeta();
                
                            meta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            meta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            meta.addItemFlags(ItemFlag.values());
                
                            item.setItemMeta(meta);
                            
                            recipeItems.remove(before);
                            recipePagination.remove(before);
                
                            recipeItems.add(item);
                            recipePagination.add(item);
                
                            CraftControl.getEditSessions().invalidate(p.getUniqueId());
                            p.closeInventory();
    
                            Messages.sendMessage(p, "&bSaved " + WordUtils.capitalize(recipe.getType().toString().toLowerCase()) + " Recipe " + recipe.getName() + "!");
                        } else if (e.getCurrentItem().equals(RecipeDenyItems.getInvalidRecipeItem())) {
                            GUISounds.playBassSound(p);
                            e.setCancelled(true);
                        } else {
                            GUISounds.playClickSound(p);
                        }
                    } else if (type.contains("Furnace")) {
                        if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains("Furnace Recipe")) {
                            e.setCancelled(true);
                        } else if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals(RecipeFurnaceButtons.getCookTimeButton().getItemMeta().getDisplayName())) {
                            e.setCancelled(true);
                
                            final ItemStack button = e.getCurrentItem();
                            final ItemStack buttonClone = e.getCurrentItem().clone();
                            final List<String> lore;
                
                            final String valueString = ChatColor.stripColor(button.getItemMeta().getLore().get(0).replace(" ", ""));
                            int value = Integer.parseInt(valueString.split(":")[1]);
                
                            if (e.getClick() == ClickType.LEFT) {
                                if ((value + 1) > 1200) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
                        
                                    new BukkitRunnable() {
                            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                        
                                    return;
                                }
                    
                                value += 1;
                    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                    
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                                if ((value + 10) > 1200) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
                        
                                    new BukkitRunnable() {
                            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                        
                                    return;
                                }
                    
                                value += 10;
                    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                    
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.RIGHT) {
                                if ((value - 1) < 0) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
                        
                                    new BukkitRunnable() {
                            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                        
                                    return;
                                }
                    
                                value -= 1;
                    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                    
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                                if ((value - 10) < 0) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
                        
                                    new BukkitRunnable() {
                            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                        
                                    return;
                                }
                    
                                value -= 10;
                    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                    
                                GUISounds.playClickSound(p);
                            }
                        } else if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().equals(RecipeFurnaceButtons.getExpButton().getItemMeta().getDisplayName())) {
                            e.setCancelled(true);
                
                            final ItemStack button = e.getCurrentItem();
                            final ItemStack buttonClone = e.getCurrentItem().clone();
                            final List<String> lore;
                
                            final String valueString = ChatColor.stripColor(button.getItemMeta().getLore().get(0).replace(" ", ""));
                            int value = Integer.parseInt(valueString.split(":")[1]);
                
                            if (e.getClick() == ClickType.LEFT) {
                                if ((value + 1) > 1200) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
                        
                                    new BukkitRunnable() {
                            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                        
                                    return;
                                }
                    
                                value += 1;
                    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                    
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.SHIFT_LEFT) {
                                if ((value + 10) > 1200) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
                        
                                    new BukkitRunnable() {
                            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                        
                                    return;
                                }
                    
                                value += 10;
                    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                    
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.RIGHT) {
                                if ((value - 1) < 0) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
                        
                                    new BukkitRunnable() {
                            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                        
                                    return;
                                }
                    
                                value -= 1;
                    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                    
                                GUISounds.playClickSound(p);
                            } else if (e.getClick() == ClickType.SHIFT_RIGHT) {
                                if ((value - 10) < 0) {
                                    e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getValueOutOfRangeButton());
                                    GUISounds.playBassSound(p);
                        
                                    new BukkitRunnable() {
                            
                                        @Override
                                        public void run() {
                                            e.getClickedInventory().setItem(e.getSlot(), buttonClone);
                                        }
                            
                                    }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                        
                                    return;
                                }
                    
                                value -= 10;
                    
                                final ItemMeta meta = button.getItemMeta();
                                lore = meta.getLore();
                    
                                lore.set(0, Utils.colorize("&3Current Value&f: " + value));
                    
                                meta.setLore(lore);
                                button.setItemMeta(meta);
                    
                                GUISounds.playClickSound(p);
                            } else if (e.getCurrentItem().equals(RecipeDenyItems.getInvalidRecipeItem())) {
                                GUISounds.playBassSound(p);
                                e.setCancelled(true);
                            }
                        } else if (e.getCurrentItem().equals(RecipeFurnaceButtons.getSaveButton())) {
                            e.setCancelled(true);
                
                            if (e.getClickedInventory().getItem(21) == null || e.getClickedInventory().getItem(25) == null) {
                                GUISounds.playBassSound(p);
                    
                                final ItemStack button = e.getCurrentItem().clone();
                                e.getClickedInventory().setItem(e.getSlot(), RecipeDenyItems.getInvalidRecipeItem());
                    
                                new BukkitRunnable() {
                        
                                    @Override
                                    public void run() {
                                        e.getClickedInventory().setItem(e.getSlot(), button);
                                    }
                        
                                }.runTaskLater(CraftControl.getInstance(), 20 * 5);
                    
                                return;
                            }
                
                            GUISounds.playClickSound(p);
                
                            final String name = CraftControl.getEditSessions().getIfPresent(p.getUniqueId());
                            final String cookTimeString = ChatColor.stripColor(e.getClickedInventory().getItem(14).getItemMeta().getLore().get(0)).replace(" ", "");
                            final String expString = ChatColor.stripColor(e.getClickedInventory().getItem(32).getItemMeta().getLore().get(0)).replace(" ", "");
                
                            int cookTime = 100;
                            int exp = 1;
                
                            try {
                                cookTime = Integer.parseInt(cookTimeString.split(":")[1]);
                                exp = Integer.parseInt(expString.split(":")[1]);
                            } catch (final NumberFormatException ex) {
                                Console.log("&cCould not parse values for cook time and/or xp!");
                                ex.printStackTrace();
                            }
                
                            final CustomRecipe recipe = CraftControl.getRecipe(name);
    
                            final ItemStack before = new ItemStack(Material.FURNACE);
                            final ItemMeta beforeMeta = before.getItemMeta();
    
                            beforeMeta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            beforeMeta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Cook Time&f: " + recipe.getCookTime()),
                                    Utils.colorize("&3XP Yield&f: " + recipe.getExp()),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            beforeMeta.addItemFlags(ItemFlag.values());
    
                            before.setItemMeta(beforeMeta);
                            
                            final ItemStack input = e.getClickedInventory().getItem(21).clone();
                            input.setAmount(1);
                
                            recipe.setType(CustomRecipeType.FURNACE);
                            recipe.setInput(input);
                            recipe.setResult(e.getClickedInventory().getItem(25));
                
                            recipe.setCookTime(cookTime);
                            recipe.setExp(exp);
                
                            recipe.save();
                            recipe.loadRecipe();
                
                            final ItemStack item = new ItemStack(Material.FURNACE);
                            final ItemMeta meta = item.getItemMeta();
                
                            meta.setDisplayName(Utils.colorize("&e&l") + recipe.getName());
                            meta.setLore(Arrays.asList(
                                    Utils.colorize("&3Name&f: ") + recipe.getName(),
                                    Utils.colorize("&3Type&f: " + WordUtils.capitalize(recipe.getType().toString().toLowerCase())),
                                    Utils.colorize("&3Cook Time&f: " + recipe.getCookTime()),
                                    Utils.colorize("&3XP Yield&f: " + recipe.getExp()),
                                    Utils.colorize("&3Result&f: " + (recipe.getResult().hasItemMeta() ? recipe.getResult().getItemMeta().getDisplayName() : WordUtils.capitalize(recipe.getResult().getType().toString().toLowerCase().replace("_", " "))))
                            ));
                            meta.addItemFlags(ItemFlag.values());
                
                            item.setItemMeta(meta);
                            
                            recipeItems.remove(before);
                            recipePagination.remove(before);
                
                            recipeItems.add(item);
                            recipePagination.add(item);
                
                            CraftControl.getEditSessions().invalidate(p.getUniqueId());
                            p.closeInventory();
                
                            Messages.sendMessage(p, "&bSaved " + WordUtils.capitalize(recipe.getType().toString().toLowerCase()) + " Recipe " + recipe.getName() + "!");
                        } else if (e.getCurrentItem().equals(RecipeDenyItems.getValueOutOfRangeButton())) {
                            GUISounds.playBassSound(p);
                            e.setCancelled(true);
                        } else {
                            GUISounds.playClickSound(p);
                        }
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        } else if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
            if (e.getView().getTitle().contains("Blacklisted Items")) {
                if (e.getCurrentItem().getType() != Material.AIR) {
                    e.setCancelled(true);
    
                    final ItemStack item = e.getCurrentItem();
                    final ItemStack clone = item.clone();
                    clone.setAmount(1);
    
                    if (blacklistItems.contains(clone)) {
                        Messages.sendMessage(p, Utils.colorize("&cThat item is already blacklisted!"));
                        return;
                    }
    
                    blacklistItems.add(clone);
                    blacklistPagination.add(clone);
    
                    BlacklistedItems.setItems(blacklistItems);
                    BlacklistedItems.save();
    
                    final Inventory inv = cache.getGui();
                    
                    AdminGUI.setBlacklistPageView(inv, cache.getPage());
                    cache.setPage(cache.getPage());
    
                    GUISounds.playClickSound(p);
                    e.getClickedInventory().setItem(e.getSlot(), null);
                }
            }
        }
    }
    
}
