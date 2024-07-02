package pl.kuezese.core.listener.impl;

import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import pl.kuezese.core.listener.Listener;

public class StructureGrowListener extends Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGrow(StructureGrowEvent event) {
        int hash = 0;
        for (int i = 0; i < event.getBlocks().size(); i++) {
            BlockState blockState = event.getBlocks().get(i);
            if (hash == 0) {
                hash = blockState.getBlock().hashCode();
            }
            blockState.setMetadata("TreeId", new FixedMetadataValue(this.core, hash));
        }
    }
}
