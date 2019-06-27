package com.nannerss.craftcontrol.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.nannerss.bananalib.utils.Utils;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.data.CustomRecipe;
import com.nannerss.craftcontrol.items.guis.Filler;
import com.nannerss.craftcontrol.items.guis.blacklist.BlacklistButtons;
import com.nannerss.craftcontrol.items.guis.blacklist.BlacklistInfo;
import com.nannerss.craftcontrol.items.guis.main.MainButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeFurnaceButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeInfo;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeShapedButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeShapelessButtons;
import com.nannerss.craftcontrol.items.guis.recipe.RecipeTypeButtons;
import com.nannerss.craftcontrol.utils.Pagination;
import com.nannerss.craftcontrol.utils.comparators.ItemComparator;
import com.nannerss.craftcontrol.utils.comparators.RecipeNameComparator;
import com.nannerss.craftcontrol.utils.comparators.RecipeTypeComparator;

import lombok.Getter;

public class AdminGUI {
    
    private static final ItemStack filler = Filler.getItem();
    @Getter
    private static Inventory mainGui;
    private static List<Integer> skipFillSlots;
    
    public static void setupMainGui() {
        mainGui = Bukkit.createInventory(null, 27, "Craft Control");
        skipFillSlots = Arrays.asList(11, 15);
        
        for (int slot = 0; slot < mainGui.getSize(); slot++) {
            if (skipFillSlots.contains(slot)) {
                if (slot == 11) {
                    mainGui.setItem(slot, MainButtons.getBlacklistButton());
                } else if (slot == 15) {
                    mainGui.setItem(slot, MainButtons.getRecipeButton());
                }
            } else {
                mainGui.setItem(slot, filler);
            }
        }
    }
    
    public static void setBlacklistPageView(final Inventory inv, final int page) {
        final List<ItemStack> items = CraftControl.getBlacklistItemCache();
        items.sort(new ItemComparator());
        CraftControl.getBlacklistPagination().clear();
        CraftControl.getBlacklistPagination().addAll(items);
        final Pagination pagination = CraftControl.getBlacklistPagination();
        final List<ItemStack> contents = pagination.getPage(page);
        skipFillSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
        
        if (!pagination.exists(page)) {
            throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + pagination.totalPages());
        }
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        inv.setItem(4, BlacklistInfo.getItem());
        inv.setItem(49, BlacklistButtons.getBackButton());
        
        if (page > 1) {
            final ItemStack previousButton = BlacklistButtons.getPreviousButton();
            final ItemMeta meta = previousButton.getItemMeta();
            
            meta.setDisplayName(Utils.colorize("&a&lPrevious Page (&7" + (page - 1) + "&a/&7" + pagination.totalPages() + "&a&l)"));
            
            previousButton.setItemMeta(meta);
            
            inv.setItem(48, previousButton);
        }
        
        if (pagination.totalPages() > page) {
            final ItemStack nextButton = BlacklistButtons.getNextButton();
            final ItemMeta meta = nextButton.getItemMeta();
            
            meta.setDisplayName(Utils.colorize("&a&lNext Page (&7" + (page + 1) + "&a/&7" + pagination.totalPages() + "&a&l)"));
            
            nextButton.setItemMeta(meta);
            
            inv.setItem(50, nextButton);
        }
        
        for (int index = 0; index < contents.size(); index++) {
            final ItemStack item = contents.get(index);
            final ItemStack clone = item.clone();
            final ItemMeta meta = clone.getItemMeta();
            List<String> lore = new ArrayList<>();
            
            if (clone.hasItemMeta() && clone.getItemMeta().hasLore()) {
                lore = clone.getItemMeta().getLore();
            }
            lore.add(0, Utils.colorize("&8&m---------------------------------"));
            lore.add(1, Utils.colorize("&6&lClick &fto remove!"));
            lore.add(2, Utils.colorize("&8&m---------------------------------"));
            
            meta.setLore(lore);
            clone.setItemMeta(meta);
            
            inv.setItem(skipFillSlots.get(index), clone);
        }
    }
    
    public static void setBlankBlacklistPageView(final Inventory inv) {
        skipFillSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        inv.setItem(4, BlacklistInfo.getItem());
        inv.setItem(49, BlacklistButtons.getBackButton());
    }
    
    public static void setRecipePageView(final Inventory inv, final int page) {
        final List<ItemStack> items = CraftControl.getRecipeItemCache();
        items.sort(new RecipeTypeComparator().reversed().thenComparing(new RecipeNameComparator()));
        CraftControl.getRecipePagination().clear();
        CraftControl.getRecipePagination().addAll(items);
        final Pagination pagination = CraftControl.getRecipePagination();
        final List<ItemStack> contents = pagination.getPage(page);
        skipFillSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
        
        if (!pagination.exists(page)) {
            throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + pagination.totalPages());
        }
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        inv.setItem(4, RecipeInfo.getItem());
        inv.setItem(46, RecipeButtons.getAddButton());
        inv.setItem(49, RecipeButtons.getBackButton());
        
        if (page > 1) {
            final ItemStack previousButton = RecipeButtons.getPreviousButton();
            final ItemMeta meta = previousButton.getItemMeta();
            
            meta.setDisplayName(Utils.colorize("&a&lPrevious Page (&7" + (page - 1) + "&a/&7" + pagination.totalPages() + "&a&l)"));
            
            previousButton.setItemMeta(meta);
            
            inv.setItem(48, previousButton);
        }
        
        if (pagination.totalPages() > page) {
            final ItemStack nextButton = RecipeButtons.getNextButton();
            final ItemMeta meta = nextButton.getItemMeta();
            
            meta.setDisplayName(Utils.colorize("&a&lNext Page (&7" + (page + 1) + "&a/&7" + pagination.totalPages() + "&a&l)"));
            
            nextButton.setItemMeta(meta);
            
            inv.setItem(50, nextButton);
        }
        
        for (int index = 0; index < contents.size(); index++) {
            final ItemStack item = contents.get(index);
            final ItemStack clone = item.clone();
            final ItemMeta meta = clone.getItemMeta();
            List<String> lore = new ArrayList<>();
            
            if (clone.hasItemMeta() && clone.getItemMeta().hasLore()) {
                lore = clone.getItemMeta().getLore();
            }
            lore.add(0, Utils.colorize("&8&m---------------------------------"));
            lore.add(1, Utils.colorize("&6&lLeft Click &fto edit!"));
            lore.add(2, Utils.colorize("&6&lRight Click &fto remove!"));
            lore.add(3, Utils.colorize("&8&m---------------------------------"));
            
            meta.setLore(lore);
            clone.setItemMeta(meta);
            
            inv.setItem(skipFillSlots.get(index), clone);
        }
    }
    
    public static void setBlankRecipePageView(final Inventory inv) {
        skipFillSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        inv.setItem(4, RecipeInfo.getItem());
        inv.setItem(46, RecipeButtons.getAddButton());
        inv.setItem(49, RecipeButtons.getBackButton());
    }
    
    public static void setRecipeChooseTypeView(final Inventory inv) {
        skipFillSlots = Arrays.asList(11, 13, 15);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        inv.setItem(11, RecipeTypeButtons.getShapelessButton());
        inv.setItem(13, RecipeTypeButtons.getShapedButton());
        inv.setItem(15, RecipeTypeButtons.getFurnaceButton());
    }
    
    public static void setRecipeCreateShapelessView(final Inventory inv) {
        skipFillSlots = Arrays.asList(19, 12, 13, 14, 21, 22, 23, 30, 31, 32, 25, 43);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        inv.setItem(19, RecipeShapelessButtons.getTypeButton());
        inv.setItem(43, RecipeShapelessButtons.getSaveButton());
    }
    
    public static void setRecipeEditShapelessView(final Inventory inv, final CustomRecipe recipe) {
        skipFillSlots = Arrays.asList(19, 12, 13, 14, 21, 22, 23, 30, 31, 32, 25, 43);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        final ItemStack typeButton = RecipeShapelessButtons.getTypeButton().clone();
        final ItemMeta meta = typeButton.getItemMeta();
        final List<String> lore = meta.getLore();
        
        lore.clear();
        meta.setLore(lore);
        
        typeButton.setItemMeta(meta);
        
        inv.setItem(19, typeButton);
        inv.setItem(43, RecipeShapelessButtons.getSaveButton());
        
        if (recipe.getSlot1() != null) {
            inv.setItem(12, recipe.getSlot1());
        }
        if (recipe.getSlot2() != null) {
            inv.setItem(13, recipe.getSlot2());
        }
        if (recipe.getSlot3() != null) {
            inv.setItem(14, recipe.getSlot3());
        }
        if (recipe.getSlot4() != null) {
            inv.setItem(21, recipe.getSlot4());
        }
        if (recipe.getSlot5() != null) {
            inv.setItem(22, recipe.getSlot5());
        }
        if (recipe.getSlot6() != null) {
            inv.setItem(23, recipe.getSlot6());
        }
        if (recipe.getSlot7() != null) {
            inv.setItem(30, recipe.getSlot7());
        }
        if (recipe.getSlot8() != null) {
            inv.setItem(31, recipe.getSlot8());
        }
        if (recipe.getSlot9() != null) {
            inv.setItem(32, recipe.getSlot9());
        }
        
        inv.setItem(25, recipe.getResult());
    }
    
    public static void setRecipeCreateShapedView(final Inventory inv) {
        skipFillSlots = Arrays.asList(19, 12, 13, 14, 21, 22, 23, 30, 31, 32, 25, 43);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        inv.setItem(19, RecipeShapedButtons.getTypeButton());
        inv.setItem(43, RecipeShapedButtons.getSaveButton());
    }
    
    public static void setRecipeEditShapedView(final Inventory inv, final CustomRecipe recipe) {
        skipFillSlots = Arrays.asList(19, 12, 13, 14, 21, 22, 23, 30, 31, 32, 25, 43);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
    
        final ItemStack typeButton = RecipeShapedButtons.getTypeButton().clone();
        final ItemMeta meta = typeButton.getItemMeta();
        final List<String> lore = meta.getLore();
    
        lore.clear();
        meta.setLore(lore);
    
        typeButton.setItemMeta(meta);
    
        inv.setItem(19, typeButton);
        inv.setItem(43, RecipeShapedButtons.getSaveButton());
        
        if (recipe.getSlot1() != null) {
            inv.setItem(12, recipe.getSlot1());
        }
        if (recipe.getSlot2() != null) {
            inv.setItem(13, recipe.getSlot2());
        }
        if (recipe.getSlot3() != null) {
            inv.setItem(14, recipe.getSlot3());
        }
        if (recipe.getSlot4() != null) {
            inv.setItem(21, recipe.getSlot4());
        }
        if (recipe.getSlot5() != null) {
            inv.setItem(22, recipe.getSlot5());
        }
        if (recipe.getSlot6() != null) {
            inv.setItem(23, recipe.getSlot6());
        }
        if (recipe.getSlot7() != null) {
            inv.setItem(30, recipe.getSlot7());
        }
        if (recipe.getSlot8() != null) {
            inv.setItem(31, recipe.getSlot8());
        }
        if (recipe.getSlot9() != null) {
            inv.setItem(32, recipe.getSlot9());
        }
        
        inv.setItem(25, recipe.getResult());
    }
    
    public static void setRecipeCreateFurnaceView(final Inventory inv) {
        skipFillSlots = Arrays.asList(19, 21, 14, 32, 25, 43);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
        
        inv.setItem(19, RecipeFurnaceButtons.getTypeButton());
        inv.setItem(14, RecipeFurnaceButtons.getCookTimeButton().clone());
        inv.setItem(32, RecipeFurnaceButtons.getExpButton().clone());
        inv.setItem(43, RecipeFurnaceButtons.getSaveButton());
        
        final ItemStack cookTimeButton = RecipeFurnaceButtons.getCookTimeButton();
        final ItemStack expButton = RecipeFurnaceButtons.getExpButton();
        
        final ItemMeta cookTimeMeta = cookTimeButton.getItemMeta();
        final List<String> cookTimeLore = cookTimeMeta.getLore();
        cookTimeLore.set(0, Utils.colorize("&3Current Value&f: 200"));
        cookTimeMeta.setLore(cookTimeLore);
        cookTimeButton.setItemMeta(cookTimeMeta);
        
        final ItemMeta expMeta = expButton.getItemMeta();
        final List<String> expLore = expMeta.getLore();
        expLore.set(0, Utils.colorize("&3Current Value&f: 10"));
        expMeta.setLore(expLore);
        expButton.setItemMeta(expMeta);
        
        inv.setItem(14, cookTimeButton);
        inv.setItem(32, expButton);
    }
    
    public static void setRecipeEditFurnaceView(final Inventory inv, final CustomRecipe recipe) {
        skipFillSlots = Arrays.asList(19, 21, 14, 32, 25, 43);
        
        inv.clear();
        
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (!skipFillSlots.contains(slot)) {
                inv.setItem(slot, filler);
            }
        }
    
        final ItemStack typeButton = RecipeFurnaceButtons.getTypeButton().clone();
        final ItemMeta meta = typeButton.getItemMeta();
        final List<String> lore = meta.getLore();
    
        lore.clear();
        meta.setLore(lore);
    
        typeButton.setItemMeta(meta);
    
        inv.setItem(19, typeButton);
        inv.setItem(43, RecipeFurnaceButtons.getSaveButton());
        
        inv.setItem(21, recipe.getInput());
        inv.setItem(25, recipe.getResult());
        
        final ItemStack cookTimeButton = RecipeFurnaceButtons.getCookTimeButton();
        final ItemStack expButton = RecipeFurnaceButtons.getExpButton();
        
        final ItemMeta cookTimeMeta = cookTimeButton.getItemMeta();
        final List<String> cookTimeLore = cookTimeMeta.getLore();
        cookTimeLore.set(0, Utils.colorize("&3Current Value&f: " + recipe.getCookTime()));
        cookTimeMeta.setLore(cookTimeLore);
        cookTimeButton.setItemMeta(cookTimeMeta);
        
        final ItemMeta expMeta = expButton.getItemMeta();
        final List<String> expLore = expMeta.getLore();
        expLore.set(0, Utils.colorize("&3Current Value&f: " + recipe.getExp()));
        expMeta.setLore(expLore);
        expButton.setItemMeta(expMeta);
        
        inv.setItem(14, cookTimeButton);
        inv.setItem(32, expButton);
    }
    
}
