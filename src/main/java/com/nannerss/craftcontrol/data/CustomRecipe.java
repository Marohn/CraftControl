package com.nannerss.craftcontrol.data;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.nannerss.bananalib.config.ConfigManager;
import com.nannerss.craftcontrol.CraftControl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomRecipe {
    
    private static ConfigManager cfg;
    
    private String name;
    private CustomRecipeType type;
    private ItemStack result;
    
    private ItemStack slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9;
    private ItemStack input;
    private int cookTime;
    private int exp;
    
    public enum CustomRecipeType {
        SHAPELESS,
        SHAPED,
        FURNACE
    }
    
    public CustomRecipe(final String name) {
        this.name = name;
        
        load();
    }
    
    private void load() {
        cfg = CraftControl.getCustomRecipes();
        
        type = cfg.isSet("custom-recipes." + name + ".type") ? CustomRecipeType.valueOf(cfg.getString("custom-recipes." + name + ".type", "")) : null;
        
        if (type == CustomRecipeType.SHAPELESS) {
            slot1 = cfg.getItemStack("custom-recipes." + name + ".slot1");
            slot2 = cfg.getItemStack("custom-recipes." + name + ".slot2");
            slot3 = cfg.getItemStack("custom-recipes." + name + ".slot3");
            slot4 = cfg.getItemStack("custom-recipes." + name + ".slot4");
            slot5 = cfg.getItemStack("custom-recipes." + name + ".slot5");
            slot6 = cfg.getItemStack("custom-recipes." + name + ".slot6");
            slot7 = cfg.getItemStack("custom-recipes." + name + ".slot7");
            slot8 = cfg.getItemStack("custom-recipes." + name + ".slot8");
            slot9 = cfg.getItemStack("custom-recipes." + name + ".slot9");
            result = cfg.getItemStack("custom-recipes." + name + ".result");
            
            final ShapelessRecipe recipe = new ShapelessRecipe(result);
            
            for (Integer i = 1; i <= 9; i++) {
                try {
                    recipe.addIngredient(cfg.getItemStack("custom-recipes." + name + ".slot" + i).getData());
                } catch (final NullPointerException ignored) {}
            }
    
            Bukkit.getServer().addRecipe(recipe);
        } else if (type == CustomRecipeType.SHAPED) {
            slot1 = cfg.getItemStack("custom-recipes." + name + ".slot1");
            slot2 = cfg.getItemStack("custom-recipes." + name + ".slot2");
            slot3 = cfg.getItemStack("custom-recipes." + name + ".slot3");
            slot4 = cfg.getItemStack("custom-recipes." + name + ".slot4");
            slot5 = cfg.getItemStack("custom-recipes." + name + ".slot5");
            slot6 = cfg.getItemStack("custom-recipes." + name + ".slot6");
            slot7 = cfg.getItemStack("custom-recipes." + name + ".slot7");
            slot8 = cfg.getItemStack("custom-recipes." + name + ".slot8");
            slot9 = cfg.getItemStack("custom-recipes." + name + ".slot9");
            result = cfg.getItemStack("custom-recipes." + name + ".result");
            
            final ShapedRecipe recipe = new ShapedRecipe(result);
            recipe.shape("123", "456", "789");
            
            for (Integer i = 1; i <= 9; i++) {
                try {
                    recipe.setIngredient(i.toString().toCharArray()[0], cfg.getItemStack("custom-recipes." + name + ".slot" + i).getData());
                } catch (final NullPointerException ignored) {}
            }
    
            Bukkit.getServer().addRecipe(recipe);
        } else if (type == CustomRecipeType.FURNACE) {
            input = cfg.getItemStack("custom-recipes." + name + ".input");
            result = cfg.getItemStack("custom-recipes." + name + ".result");
            cookTime = cfg.getInt("custom-recipes." + name + ".cook-time");
            exp = cfg.getInt("custom-recipes." + name + ".exp");
            
            final FurnaceRecipe recipe = new FurnaceRecipe(result, input.getData());
    
            recipe.setCookingTime(cookTime);
            recipe.setExperience(exp);
            
            Bukkit.getServer().addRecipe(recipe);
        }
    }
    
    public void save() {
        cfg = CraftControl.getCustomRecipes();
        
        if (type == CustomRecipeType.SHAPELESS) {
            cfg.set("custom-recipes." + name + ".type", type.toString());
            
            cfg.set("custom-recipes." + name + ".slot1", slot1);
            cfg.set("custom-recipes." + name + ".slot2", slot2);
            cfg.set("custom-recipes." + name + ".slot3", slot3);
            cfg.set("custom-recipes." + name + ".slot4", slot4);
            cfg.set("custom-recipes." + name + ".slot5", slot5);
            cfg.set("custom-recipes." + name + ".slot6", slot6);
            cfg.set("custom-recipes." + name + ".slot7", slot7);
            cfg.set("custom-recipes." + name + ".slot8", slot8);
            cfg.set("custom-recipes." + name + ".slot9", slot9);
            
            cfg.set("custom-recipes." + name + ".result", result);
        } else if (type == CustomRecipeType.SHAPED) {
            cfg.set("custom-recipes." + name + ".type", type.toString());
    
            cfg.set("custom-recipes." + name + ".slot1", slot1);
            cfg.set("custom-recipes." + name + ".slot2", slot2);
            cfg.set("custom-recipes." + name + ".slot3", slot3);
            cfg.set("custom-recipes." + name + ".slot4", slot4);
            cfg.set("custom-recipes." + name + ".slot5", slot5);
            cfg.set("custom-recipes." + name + ".slot6", slot6);
            cfg.set("custom-recipes." + name + ".slot7", slot7);
            cfg.set("custom-recipes." + name + ".slot8", slot8);
            cfg.set("custom-recipes." + name + ".slot9", slot9);
    
            cfg.set("custom-recipes." + name + ".result", result);
        } else if (type == CustomRecipeType.FURNACE) {
            cfg.set("custom-recipes." + name + ".type", type.toString());
    
            cfg.set("custom-recipes." + name + ".input", input);
            cfg.set("custom-recipes." + name + ".result", result);
    
            cfg.set("custom-recipes." + name + ".cook-time", cookTime);
            cfg.set("custom-recipes." + name + ".exp", exp);
        }
        
        cfg.saveConfig();
    }
    
    public void loadRecipe() {
        if (type == CustomRecipeType.SHAPELESS) {
            final ShapelessRecipe recipe = new ShapelessRecipe(result);
    
            if (slot1 != null) {
                recipe.addIngredient(slot1.getData());
            }
            if (slot2 != null) {
                recipe.addIngredient(slot2.getData());
            }
            if (slot3 != null) {
                recipe.addIngredient(slot3.getData());
            }
            if (slot4 != null) {
                recipe.addIngredient(slot4.getData());
            }
            if (slot5 != null) {
                recipe.addIngredient(slot5.getData());
            }
            if (slot6 != null) {
                recipe.addIngredient(slot6.getData());
            }
            if (slot7 != null) {
                recipe.addIngredient(slot7.getData());
            }
            if (slot8 != null) {
                recipe.addIngredient(slot8.getData());
            }
            if (slot9 != null) {
                recipe.addIngredient(slot9.getData());
            }
    
            Bukkit.getServer().addRecipe(recipe);
        } else if (type == CustomRecipeType.SHAPED) {
            final ShapedRecipe recipe = new ShapedRecipe(result);
            recipe.shape("123", "456", "789");
    
            if (slot1 != null) {
                recipe.setIngredient('1', slot1.getData());
            }
            if (slot2 != null) {
                recipe.setIngredient('2', slot2.getData());
            }
            if (slot3 != null) {
                recipe.setIngredient('3', slot3.getData());
            }
            if (slot4 != null) {
                recipe.setIngredient('4', slot4.getData());
            }
            if (slot5 != null) {
                recipe.setIngredient('5', slot5.getData());
            }
            if (slot6 != null) {
                recipe.setIngredient('6', slot6.getData());
            }
            if (slot7 != null) {
                recipe.setIngredient('7', slot7.getData());
            }
            if (slot8 != null) {
                recipe.setIngredient('8', slot8.getData());
            }
            if (slot9 != null) {
                recipe.setIngredient('9', slot9.getData());
            }
    
            Bukkit.getServer().addRecipe(recipe);
        } else if (type == CustomRecipeType.FURNACE) {
            final FurnaceRecipe recipe = new FurnaceRecipe(result, input.getData());
            
            recipe.setCookingTime(cookTime);
            recipe.setExperience(exp);
            
            Bukkit.getServer().addRecipe(recipe);
        }
    }
    
}
