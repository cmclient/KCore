package pl.kuezese.core.listener.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.object.Safe;
import pl.kuezese.core.object.User;

public class InventoryCloseListener extends Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, null);
            return;
        }

        Player p = (Player) e.getPlayer();

        if (e.getInventory().getName().startsWith(ChatHelper.color("&8>> &7Sejf &8(&a"))) {
            User user = this.userManager.get(p.getName());
            String name = e.getInventory().getName();
            int id = Integer.parseInt(name.split("\\(§a")[1].split("§8\\)")[0]);
            Safe safe = user.getSafes()[id - 1];
            safe.setItems(e.getInventory().getContents());
            return;
        }

        if (e.getInventory().getName().startsWith(ChatHelper.color("&8>> &7Kit")) && e.getInventory().getHolder() != null) {
            boolean drop = false;
            ItemStack[] contents = e.getInventory().getContents();

            for (ItemStack is : contents) {
                if (is != null) {
                    if (!drop) {
                        drop = true;
                    }
                    p.getLocation().getWorld().dropItem(p.getLocation(), is);
                }
            }

            if (drop) {
                p.sendMessage(ChatHelper.color(" &8>> &aNie zabrales wszystkich przedmiotow wiec pozostale przedmioty zostaly wyrzucone w twojej lokalizacji."));
            }
        }
    }
}
