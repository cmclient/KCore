package pl.kuezese.core.listener.impl;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import pl.kuezese.core.listener.Listener;

public class ExplodeListener extends Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntityType() == EntityType.PRIMED_TNT && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("BOSS TNT")) {
            event.blockList().clear();
            return;
        }

        event.blockList().forEach(block -> this.core.getGeneratorManager()
                .find(block.getLocation())
                .ifPresent(generator -> this.core.getGeneratorManager().remove(generator.getLoc(), null)));
    }
}
