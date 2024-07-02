package pl.kuezese.core.listener.impl;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.listener.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CraftItemListener extends Listener {

    private final List<Material> blockedItems;

    public CraftItemListener() {
        this.blockedItems = Arrays.asList(Material.JUKEBOX, Material.NOTE_BLOCK, Material.BOAT);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPrepare(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack result = recipe.getResult();

        if (result.hasItemMeta() && result.getItemMeta().getDisplayName() != null && result.getItemMeta().getDisplayName().equals(ChatHelper.color("&8>> &7Rzucane TNT"))) {
            boolean correctRecipe = Stream.of(event.getInventory().getMatrix())
                    .allMatch(stack -> stack == null || (stack.getType() != Material.TNT || stack.getAmount() == 64));

            if (!correctRecipe)
                event.getInventory().setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();

        if (this.blockedItems.contains(result.getType())) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(ChatHelper.color(" &8>> &7Tworzenie tego przedmiotu jest &czablokowane&7."));
            return;
        }

        if (result.getItemMeta() != null && result.getItemMeta().getDisplayName() != null
                && result.getItemMeta().getDisplayName().equals(ChatHelper.color("&8>> &7Rzucane TNT"))) {

            event.getInventory().clear();
            event.getWhoClicked().setItemOnCursor(result);
        }
    }
}
