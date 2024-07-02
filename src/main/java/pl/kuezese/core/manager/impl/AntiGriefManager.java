package pl.kuezese.core.manager.impl;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AntiGriefManager {

    private final @Getter Map<BlockData, Long> blocks;

    public AntiGriefManager() {
        this.blocks = new ConcurrentHashMap<>();
    }

    public Optional<BlockData> get(Block block) {
        return this.blocks.keySet().stream().filter(blockData -> blockData.getBlock() == block).findAny();
    }

    public static @Getter class BlockData {

        private final Block block;
        private final Material previous;

        public BlockData(Block block, Material previous) {
            this.block = block;
            this.previous = previous;
        }
    }
}