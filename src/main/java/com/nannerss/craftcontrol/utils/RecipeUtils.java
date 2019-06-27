package com.nannerss.craftcontrol.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeUtils {
    
    public static void removeShapelessRecipe(final ShapelessRecipe inputRecipe) {
        final Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();
        
        while (iterator.hasNext()) {
            final Recipe iteratedRecipe = iterator.next();
            
            if (iteratedRecipe instanceof ShapelessRecipe) {
                final ShapelessRecipe iteratedShapeless = (ShapelessRecipe) iteratedRecipe;
                
                final List<ItemStack> it = iteratedShapeless.getIngredientList();
                final List<ItemStack> in = inputRecipe.getIngredientList();
                
                if (it.equals(in)) {
                    if (iteratedShapeless.getResult().equals(inputRecipe.getResult())) {
                        iterator.remove();
                    }
                }
            }
        }
    }
    
    public static void removeShapedRecipe(final ShapedRecipe inputRecipe) {
        final Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();
        
        while (iterator.hasNext()) {
            final Recipe iteratedRecipe = iterator.next();
            
            if (iteratedRecipe instanceof ShapedRecipe) {
                final ShapedRecipe iteratedShaped = (ShapedRecipe) iteratedRecipe;
                
                final Map<Character, ItemStack> it = iteratedShaped.getIngredientMap();
                final Map<Character, ItemStack> in = inputRecipe.getIngredientMap();
                
                if (it.values().equals(in.values())) {
                    final String[] list = iteratedShaped.getShape();
                    final String listString = list[0] + list[1] + list[2];
    
                    final String[] list2 = iteratedShaped.getShape();
                    final String listString2 = list2[0] + list2[1] + list2[2];
                    
                    for (int i = 0; i < listString.length(); i++) {
                        if (it.get(listString.charAt(i)) == null || in.get(listString2.charAt(i)) == null) {
                            continue;
                        }
                        
                        if (!it.get(listString.charAt(i)).equals(in.get(listString2.charAt(i)))) {
                            return;
                        }
                    }
                    
                    if (iteratedShaped.getResult().equals(inputRecipe.getResult())) {
                        iterator.remove();
                    }
                }
            }
        }
    }
    
    public static void removeFurnaceRecipe(final FurnaceRecipe inputRecipe) {
        final Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();
        
        while (iterator.hasNext()) {
            final Recipe iteratedRecipe = iterator.next();
            
            if (iteratedRecipe instanceof FurnaceRecipe) {
                final FurnaceRecipe iteratedShaped = (FurnaceRecipe) iteratedRecipe;
                
                final ItemStack it = iteratedShaped.getInput();
                final ItemStack in = inputRecipe.getInput();
                
                if (it.equals(in)) {
                    if (iteratedShaped.getResult().equals(inputRecipe.getResult())) {
                        iterator.remove();
                    }
                }
            }
        }
    }
    
}
