package com.nannerss.craftcontrol.tasks;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.nannerss.bananalib.messages.Messages;
import com.nannerss.bananalib.utils.Utils;
import com.nannerss.craftcontrol.CraftControl;
import com.nannerss.craftcontrol.data.PlayerCache;
import com.nannerss.craftcontrol.gui.AdminGUI;
import com.nannerss.craftcontrol.utils.GUISounds;

public class RecipeNamePrompt {
    
    public RecipeNamePrompt(final Player p) {
        final PlayerCache cache = CraftControl.getInventoryCache().getIfPresent(p.getUniqueId());
        final Conversation conversation = new ConversationFactory(CraftControl.getInstance()).withLocalEcho(false).withModality(true).withEscapeSequence("cancel").thatExcludesNonPlayersWithMessage(Utils.colorize("&cOnly players can create custom recipes!")).addConversationAbandonedListener(abandonedEvent -> {
            final Conversable c = abandonedEvent.getContext().getForWhom();
            final Map<Object, Object> m = abandonedEvent.getContext().getAllSessionData();
            
            if (abandonedEvent.gracefulExit()) {
                cache.setGui(Bukkit.createInventory(null, 27, "Choose Recipe Type"));
                final Inventory inv = cache.getGui();
                
                AdminGUI.setRecipeChooseTypeView(inv);
                cache.setPage(0);
                
                GUISounds.playOpenSound(p);
                p.openInventory(inv);
                
                CraftControl.getEditSessions().put(p.getUniqueId(), m.get(Data.NAME).toString());
                Messages.sendMessage(p, "&bCreating recipe " + m.get(Data.NAME).toString() + "...");
            } else {
                GUISounds.playBassSound(p);
                Messages.sendMessage(p, "&cCancelled recipe creation.");
            }
        }).withFirstPrompt(new NamePrompt()).buildConversation(p);
        p.beginConversation(conversation);
    }
    
    enum Data {
        NAME
    }
    
    class NamePrompt extends ValidatingPrompt {
        
        @Override
        public String getPromptText(final ConversationContext context) {
            return Utils.colorize("&bEnter the name of the recipe.\n&7&oType \"cancel\" to cancel creating a recipe.");
        }
        
        @Override
        protected boolean isInputValid(final ConversationContext context, final String input) {
            return input.length() <= 32 && !CraftControl.getRecipeCache().asMap().containsKey(input);
        }
        
        @Override
        protected Prompt acceptValidatedInput(final ConversationContext context, final String input) {
            context.setSessionData(Data.NAME, input.replace(" ", "_"));
            
            return Prompt.END_OF_CONVERSATION;
        }
        
        @Override
        protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
            return Utils.colorize("&cThe name must not exceed 32 characters and must not already exist!");
        }
        
    }
    
}
