package com.nannerss.craftcontrol.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.nannerss.bananalib.config.ConfigManager;
import com.nannerss.bananalib.messages.Messages;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.data.CustomRecipe;
import com.nannerss.craftcontrol.data.CustomRecipe.CustomRecipeType;
import com.nannerss.craftcontrol.data.PlayerCache;
import com.nannerss.craftcontrol.gui.AdminGUI;
import com.nannerss.craftcontrol.utils.GUISounds;
import com.nannerss.craftcontrol.utils.Pagination;
import com.nannerss.craftcontrol.utils.RecipeUtils;

public class RecipeCommand extends Command {
    
    public RecipeCommand() {
        super("recipe");
        
        setAliases(Arrays.asList("rcp", "re"));
    }
    
    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cYou must be a player to use this command!");
            return false;
        }
        
        final Player p = (Player) sender;
        final PlayerCache cache = CraftControl.getInventoryCache().getIfPresent(p.getUniqueId());
        final Pagination pagination = CraftControl.getRecipePagination();
        
        if (args.length < 1) {
            Messages.sendMessage(p, "", "     &3&lShowing help for /recipe...", "", "&b/recipe &3add <name> &8- &7Opens the GUI for adding a recipe.", "&b/recipe &3edit <name> &8- &7Opens the GUI to edit the recipe of the specified name.", "&b/recipe &3remove <name> &8- &7Removes the recipe of the specified name.", "&b/recipe &3gui &8- &7Opens the recipe GUI.", "");
            return false;
        }
        
        final String parameter = args[0];
        
        if ("add".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.recipe.add")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            if (args.length < 2) {
                Messages.sendMessage(p, "&cSpecify a name for the recipe!");
                return false;
            }
            
            final String name = args[1];
            
            if (name.length() > 32) {
                Messages.sendMessage(p, "&cYou cannot have a recipe name that is more than 32 characters long!");
                return false;
            }
            
            if (CraftControl.getEditSessions().asMap().containsValue(name)) {
                Messages.sendMessage(p, "&cThat recipe is currently being edited!");
                return false;
            }
            
            for (CustomRecipe recipe : CraftControl.getRecipeCache().asMap().values()) {
                if (name.equals(recipe.getName())) {
                    Messages.sendMessage(p, "&cRecipe with name " + name + " already exists!");
                    return false;
                }
            }
            
            cache.setGui(Bukkit.createInventory(null, 27, "Choose Recipe Type"));
            final Inventory inv = cache.getGui();
            
            AdminGUI.setRecipeChooseTypeView(inv);
            cache.setPage(0);
            
            GUISounds.playOpenSound(p);
            p.openInventory(inv);
            
            CraftControl.getEditSessions().put(p.getUniqueId(), name);
        } else if ("edit".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.recipe.edit")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            if (args.length < 2) {
                Messages.sendMessage(p, "&cSpecify the name of the recipe to edit!");
                return false;
            }
            
            final String name = args[1];
            
            if (CraftControl.getEditSessions().asMap().containsValue(name)) {
                Messages.sendMessage(p, "&cThat recipe is currently being edited!");
                return false;
            }
            
            if (!CraftControl.getRecipeCache().asMap().containsKey(name)) {
                Messages.sendMessage(p, "&cRecipe does not exist!");
                return false;
            }
            
            final CustomRecipe recipe = CraftControl.getRecipe(name);
            
            if (recipe.getType() == CustomRecipeType.SHAPELESS) {
                cache.setGui(Bukkit.createInventory(null, 45, "Edit Recipe: " + recipe.getName()));
                final Inventory inv = cache.getGui();
                
                AdminGUI.setRecipeEditShapelessView(inv, recipe);
                cache.setPage(0);
                
                p.openInventory(inv);
                GUISounds.playOpenSound(p);
                
                CraftControl.getEditSessions().put(p.getUniqueId(), name);
            } else if (recipe.getType() == CustomRecipeType.SHAPED) {
                cache.setGui(Bukkit.createInventory(null, 45, "Edit Recipe: " + recipe.getName()));
                final Inventory inv = cache.getGui();
                
                AdminGUI.setRecipeEditShapedView(inv, recipe);
                cache.setPage(0);
                
                p.openInventory(inv);
                GUISounds.playOpenSound(p);
                
                CraftControl.getEditSessions().put(p.getUniqueId(), name);
            } else if (recipe.getType() == CustomRecipeType.FURNACE) {
                cache.setGui(Bukkit.createInventory(null, 45, "Edit Recipe: " + recipe.getName()));
                final Inventory inv = cache.getGui();
                
                AdminGUI.setRecipeEditFurnaceView(inv, recipe);
                cache.setPage(0);
                
                p.openInventory(inv);
                GUISounds.playOpenSound(p);
                
                CraftControl.getEditSessions().put(p.getUniqueId(), name);
            }
        } else if ("remove".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.recipe.remove")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            if (args.length < 2) {
                Messages.sendMessage(p, "&cSpecify the name of the recipe to remove!");
                return false;
            }
            
            final String name = args[1];
            
            if (CraftControl.getEditSessions().asMap().containsValue(name)) {
                Messages.sendMessage(p, "&cThat recipe is currently being edited!");
                return false;
            }
            
            if (!CraftControl.getRecipeCache().asMap().containsKey(name)) {
                Messages.sendMessage(p, "&cRecipe does not exist!");
                return false;
            }
            
            final CustomRecipe recipe = CraftControl.getRecipe(name);
            final List<ItemStack> recipeItems = CraftControl.getRecipeItemCache();
            
            CraftControl.getRecipeCache().invalidate(name);
            
            final ConfigManager cfg = CraftControl.getCustomRecipes();
            cfg.set("custom-recipes." + name, null);
            cfg.saveConfig();
            
            for (final ItemStack item : CraftControl.getRecipeItemCache()) {
                if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(name)) {
                    CraftControl.getRecipePagination().remove(recipeItems.get(recipeItems.indexOf(item)));
                    recipeItems.remove(item);
                    break;
                }
            }
            
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
    
            Messages.sendMessage(p, "&bRemoved recipe " + name + "!");
        } else if ("gui".equalsIgnoreCase(parameter)) {
            if (!p.hasPermission("craftcontrol.gui")) {
                Messages.sendMessage(p, "&cInsufficient permissions!");
                return false;
            }
            
            cache.setGui(Bukkit.createInventory(null, 54, "Craft Control: Custom Recipes"));
            final Inventory inv = cache.getGui();
            
            if (pagination.totalPages() == 0) {
                AdminGUI.setBlankRecipePageView(inv);
                cache.setPage(0);
            } else {
                AdminGUI.setRecipePageView(inv, 1);
                cache.setPage(1);
            }
            
            GUISounds.playOpenSound(p);
            p.openInventory(inv);
        }
        
        return false;
    }
    
}
