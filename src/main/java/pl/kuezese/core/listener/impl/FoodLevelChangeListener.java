package pl.kuezese.core.listener.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import pl.kuezese.core.listener.Listener;

public class FoodLevelChangeListener extends Listener {

    @EventHandler(ignoreCancelled = true)
    public void onChange(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (p.hasPermission("cm.nofood")) {
            e.setCancelled(true);
        }
    }
}
