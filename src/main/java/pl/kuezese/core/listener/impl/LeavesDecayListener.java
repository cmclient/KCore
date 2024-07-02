package pl.kuezese.core.listener.impl;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.LeavesDecayEvent;
import pl.kuezese.core.listener.Listener;

public class LeavesDecayListener extends Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeacy(LeavesDecayEvent event) {
        Block block = event.getBlock();
        this.core.getRealChop().blockBreak(block, block.getLocation().clone().add(1, 0, 0));
    }
}
