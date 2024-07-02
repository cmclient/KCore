package pl.kuezese.core.task.impl;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.guild.Region;
import panda.std.Option;
import pl.kuezese.core.manager.impl.AntiGriefManager;
import pl.kuezese.core.task.Task;

import java.util.Iterator;
import java.util.Map;

public class AntiGriefTask extends Task {

    private final FunnyGuilds funnyGuilds;
    private final long expirationTimeMillis = 3600000L; // 1 hour in milliseconds

    public AntiGriefTask() {
        super(200L, 200L, false);
        this.funnyGuilds = FunnyGuilds.getInstance();
    }

    @Override
    public void run() {
        Map<AntiGriefManager.BlockData, Long> blocks = this.core.getAntiGriefManager().getBlocks();
        long currentTime = System.currentTimeMillis();

        // Use iterator to avoid ConcurrentModificationException
        for (Iterator<Map.Entry<AntiGriefManager.BlockData, Long>> it = blocks.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<AntiGriefManager.BlockData, Long> entry = it.next();
            AntiGriefManager.BlockData data = entry.getKey();
            Long time = entry.getValue();

            if (currentTime >= time + expirationTimeMillis) {
                Option<Region> regionAtLocation = this.funnyGuilds.getRegionManager().findRegionAtLocation(data.getBlock().getLocation());
                if (regionAtLocation.isEmpty()) {
                    data.getBlock().setType(data.getPrevious());
                }
                it.remove(); // Remove the entry directly from the iterator
            }
        }
    }
}
