package com.nannerss.craftcontrol.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.nannerss.bananalib.messages.Console;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.data.CustomRecipe;
import com.nannerss.craftcontrol.data.CustomRecipe.CustomRecipeType;
import com.nannerss.craftcontrol.items.ResultItem;
import com.nannerss.craftcontrol.utils.GUISounds;

public class CraftListener implements Listener {
    
    private static final ItemStack BANNED_RESULT = ResultItem.getItem();
    
    @EventHandler(priority = EventPriority.MONITOR)
    public final void onPrepareCraft(final PrepareItemCraftEvent e) {
        for (final LivingEntity le : e.getViewers()) {
            if (!(le instanceof Player)) {
                break;
            }
            
            final Player p = (Player) le;
            
            if (p.hasPermission("craftcontrol.bypass")) {
                return;
            }
        }
        
        final Recipe recipe = e.getRecipe();
        
        for (final ItemStack item : CraftControl.getBlacklistItemCache()) {
            if (e.getRecipe() == null) {
                return;
            }
            
            final ItemStack result = e.getRecipe().getResult().clone();
            result.setAmount(1);
            
            if (result.equals(item)) {
                e.getInventory().setResult(BANNED_RESULT);
                return;
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public final void onSmelt(final FurnaceSmeltEvent e) {
        if (e.getSource() == null) {
            return;
        }
    
        for (Recipe recipe : CraftControl.getVanillaRecipes()) {
            if (recipe instanceof FurnaceRecipe) {
                final FurnaceRecipe vanillaRecipe = (FurnaceRecipe) recipe;
                
                if (e.getSource().isSimilar(vanillaRecipe.getInput())) {
                    e.setResult(vanillaRecipe.getResult());
                    return;
                }
                
                if ((e.getSource().getType().toString().contains("WOOD") || e.getSource().getType().toString().contains("LOG")) && !e.getSource().hasItemMeta()) {
                    e.setResult(new ItemStack(Material.CHARCOAL));
                    return;
                }
            }
        }
        
        for (CustomRecipe recipe : CraftControl.getRecipeCache().asMap().values()) {
            if (recipe.getType() == CustomRecipeType.FURNACE) {
                if (e.getSource().isSimilar(recipe.getInput()) && e.getResult().isSimilar(recipe.getResult())) {
                    e.setResult(recipe.getResult());
                    return;
                }
            }
        }
        
        e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public final void onBurn(final FurnaceBurnEvent e) {
        if (e.getBlock().getState() == null) {
            return;
        }
        
        final Furnace furnace = (Furnace) e.getBlock().getState();
    
        for (Recipe recipe : CraftControl.getVanillaRecipes()) {
            if (recipe instanceof FurnaceRecipe) {
                final FurnaceRecipe vanillaRecipe = (FurnaceRecipe) recipe;
                Console.log(vanillaRecipe.getInput().toString());
                if (furnace.getInventory().getSmelting().isSimilar(vanillaRecipe.getInput()) || ((furnace.getInventory().getSmelting().getType().toString().contains("WOOD") || furnace.getInventory().getSmelting().getType().toString().contains("LOG")) && !furnace.getInventory().getSmelting().hasItemMeta())) {
                    return;
                }
            }
        }
        
        if (furnace.getInventory() == null || furnace.getInventory().getSmelting() == null) {
            return;
        }
    
        for (CustomRecipe recipe : CraftControl.getRecipeCache().asMap().values()) {
            if (recipe.getType() == CustomRecipeType.FURNACE) {
                if (furnace.getInventory().getSmelting().isSimilar(recipe.getInput())) {
                    return;
                }
            }
        }
        
        e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public final void onCraft(final PrepareItemCraftEvent e) {
        if (e.getRecipe() == null) {
            return;
        }
        
        for (Recipe recipe : CraftControl.getVanillaRecipes()) {
            if (e.getRecipe() instanceof ShapelessRecipe && recipe instanceof ShapelessRecipe) {
                final ShapelessRecipe inputRecipe = (ShapelessRecipe) e.getRecipe();
                final ShapelessRecipe vanillaRecipe = (ShapelessRecipe) recipe;
                
                if (inputRecipe.getIngredientList().equals(vanillaRecipe.getIngredientList()) && inputRecipe.getResult().equals(vanillaRecipe.getResult())) {
                    return;
                }
            } else if (e.getRecipe() instanceof ShapedRecipe && recipe instanceof ShapedRecipe) {
                final ShapedRecipe inputRecipe = (ShapedRecipe) e.getRecipe();
                final ShapedRecipe vanillaRecipe = (ShapedRecipe) recipe;
                
                if (inputRecipe.getIngredientMap().equals(vanillaRecipe.getIngredientMap()) && inputRecipe.getResult().equals(vanillaRecipe.getResult())) {
                    return;
                }
            } else if (e.getRecipe() instanceof FurnaceRecipe && recipe instanceof FurnaceRecipe) {
                return;
            }
        }
        
        for (CustomRecipe recipe : CraftControl.getRecipeCache().asMap().values()) {
            if (recipe.getType() == CustomRecipeType.SHAPELESS) {
                if (e.getInventory().getMatrix().length < 9) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                    return;
                }
                
                final Recipe inputRecipe = e.getRecipe();
                final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(recipe.getResult());
                
                final List<ItemStack> slots = Arrays.asList(recipe.getSlot1(), recipe.getSlot2(), recipe.getSlot3(), recipe.getSlot4(), recipe.getSlot5(), recipe.getSlot6(), recipe.getSlot7(), recipe.getSlot8(), recipe.getSlot9());
    
                final List<ItemStack> recipeIngredients = new ArrayList<>();
                final List<ItemStack> inputIngredients = new ArrayList<>();
                
                for (ItemStack item : slots) {
                    if (item != null && item.getType() != Material.AIR) {
                        final ItemStack clone = item.clone();
                        clone.setAmount(1);
                        recipeIngredients.add(clone);
                    }
                }
                
                for (ItemStack item : e.getInventory().getMatrix()) {
                    if (item != null && item.getType() != Material.AIR) {
                        final ItemStack clone = item.clone();
                        clone.setAmount(1);
                        inputIngredients.add(clone);
                    }
                }
                
                if (inputRecipe.getResult().equals(shapelessRecipe.getResult()) && inputIngredients.equals(recipeIngredients)) {
                    e.getInventory().setResult(recipe.getResult());
                    return;
                }
                
                e.getInventory().setResult(new ItemStack(Material.AIR));
            } else if (recipe.getType() == CustomRecipeType.SHAPED) {
                if (e.getInventory().getMatrix().length < 9) {
                    return;
                }
                
                final Recipe inputRecipe = e.getRecipe();
                final ShapedRecipe shapedRecipe = new ShapedRecipe(recipe.getResult());
                shapedRecipe.shape("123", "456", "789");
    
                final List<ItemStack> slots = Arrays.asList(recipe.getSlot1(), recipe.getSlot2(), recipe.getSlot3(), recipe.getSlot4(), recipe.getSlot5(), recipe.getSlot6(), recipe.getSlot7(), recipe.getSlot8(), recipe.getSlot9());
    
                final List<ItemStack> ingredients = new ArrayList<>();
                final List<ItemStack> matrix = new ArrayList<>();
    
                for (ItemStack item : slots) {
                    if (item != null && item.getType() != Material.AIR) {
                        final ItemStack clone = item.clone();
                        clone.setAmount(1);
                        ingredients.add(clone);
                    }
                }
                
                for (ItemStack item : e.getInventory().getMatrix()) {
                    if (item != null && item.getType() != Material.AIR) {
                        final ItemStack clone = item.clone();
                        clone.setAmount(1);
                        matrix.add(clone);
                    }
                }
                
                if (inputRecipe.getResult().equals(shapedRecipe.getResult())) {
                    if (ingredients.equals(matrix)) {
                        e.getInventory().setResult(recipe.getResult());
                        return;
                    }
                }
    
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }
    
    @EventHandler
    public final void onClick(final InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        
        if (e.getCurrentItem() == null) {
            return;
        }
        
        if (!e.getCurrentItem().hasItemMeta()) {
            return;
        }
        
        final Player p = (Player) e.getWhoClicked();
        
        if ((e.getClickedInventory().getType() == InventoryType.WORKBENCH || e.getClickedInventory().getType() == InventoryType.CRAFTING) && e.getCurrentItem().getItemMeta().equals(BANNED_RESULT.getItemMeta())) {
            if (p.hasPermission("craftcontrol.bypass")) {
                return;
            }
            
            GUISounds.playBassSound(p);
            
            e.setCancelled(true);
        }
    }
    
}
