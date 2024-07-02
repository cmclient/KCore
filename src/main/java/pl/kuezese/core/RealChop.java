package pl.kuezese.core;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;

@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class RealChop {

    private final CorePlugin core;
    private final Set<Material> lightMaterials = new HashSet<>(Arrays.asList(
            Material.LEAVES, Material.LEAVES_2, Material.AIR, Material.TORCH, Material.LONG_GRASS,
            Material.RED_MUSHROOM, Material.YELLOW_FLOWER, Material.VINE, Material.SNOW, Material.ARROW,
            Material.COCOA, Material.LADDER, Material.WEB, Material.SAPLING, Material.WATER,
            Material.STATIONARY_WATER
    ));

    public void blockBreak(Block block, Location location) {
        Material breakBlockType = block.getType();
        Location breakBlockLocation = block.getLocation();

        int treeId = 0;
        if (breakBlockType != Material.LOG && breakBlockType != Material.LOG_2)
            treeId = 1;

        if (block.getMetadata("TreeId").iterator().hasNext()) {
            treeId = block.getMetadata("TreeId").iterator().next().asInt();
        }
        World world = block.getWorld();
        Vector direction = new Vector(location.getX() - breakBlockLocation.getX(), 0, location.getZ() - breakBlockLocation.getZ());
        direction.normalize();
        float angle = direction.angle(new Vector(0, 0, 1));
        // double angle2 = angle * 180 / Math.PI;
        int angle1 = 90;
        if (direction.getX() > 0) {
            if (angle > Math.PI * 1 / 4)
                angle1 = 180;
        } else {
            if (angle > Math.PI * 1 / 4)
                angle1 = 0;
        }
        if (angle > Math.PI * 3 / 4)
            angle1 = 270;
        switch (angle1) {
            case 0:
                direction.setX(0);
                direction.setZ(1);
                break;
            case 90:
                direction.setX(1);
                direction.setZ(0);
                break;
            case 180:
                direction.setX(0);
                direction.setZ(-1);
                break;
            case 270:
                direction.setX(-1);
                direction.setZ(0);
                break;
        }

        HashMap<Location, Block> tree = new HashMap<>();
        HashMap<Location, Block> solid = new HashMap<>();
        HashMap<Location, Block> search = new HashMap<>();
        search.put(breakBlockLocation, block);

        int blockProcessingLimit = this.core.getConfiguration().getBlockProcessingLimit();

        boolean findNext = true;
        int limit = 0;
        while (findNext) {
            findNext = false;
            HashMap<Location, Block> newSearch = new HashMap<>();
            for (Map.Entry<Location, Block> pairs : search.entrySet()) {
                Location l = pairs.getKey();
                Map<Location, Block> near = getNearBlocks(l, 1);
                for (Map.Entry<Location, Block> nearPairs : near.entrySet()) {
                    Location nearLocation = nearPairs.getKey();
                    Block nearBlock = nearPairs.getValue();
                    if (nearBlock.getType() == Material.LOG || nearBlock.getType() == Material.LOG_2) {
                        if (!tree.containsKey(nearLocation)) {
                            boolean put = treeId == 0 && nearBlock.getMetadata("TreeId").isEmpty();
                            if (treeId == 1)
                                put = true;
                            if (treeId != 0) {
                                if (nearBlock.getMetadata("TreeId").iterator().hasNext() && nearBlock.getMetadata("TreeId").iterator().next().asInt() == treeId) {
                                    put = true;
                                }
                            }
                            if (put) {
                                tree.put(nearLocation, nearBlock);
                                newSearch.put(nearLocation, nearBlock);
                                findNext = true;
                            }
                        }
                    }
                    if (nearBlock.getType() != Material.LOG && nearBlock.getType() != Material.LOG_2 && !isLightBlock(nearBlock.getType())) {
                        solid.put(nearLocation, nearBlock);
                    }
                }
            }
            limit++;
            if (limit > blockProcessingLimit) {
                this.core.getLogger().log(Level.INFO, "Tree Logs search reached BlockProcessingLimit.");
                break;
            }
            if (findNext) {
                search.clear();
                search.putAll(newSearch);
            }

        }
        tree.remove(breakBlockLocation);
        solid.remove(breakBlockLocation);
        block.removeMetadata("TreeId", this.core);

        search.clear();
        search.putAll(solid);
        findNext = true;
        limit = 0;

        while (findNext) {
            findNext = false;
            HashMap<Location, Block> newSearch = new HashMap<>();
            for (Map.Entry<Location, Block> pairs : search.entrySet()) {
                Location l = pairs.getKey();
                Map<Location, Block> near = getNearBlocks(l, 1);
                for (Map.Entry<Location, Block> nearPairs : near.entrySet()) {
                    Location nearLocation = nearPairs.getKey();
                    Block nearBlock = nearPairs.getValue();
                    if (nearBlock.getType() == Material.LOG || nearBlock.getType() == Material.LOG_2) {
                        if (tree.containsKey(nearLocation)) {
                            tree.remove(nearLocation);
                            newSearch.put(nearLocation, nearBlock);
                            findNext = true;
                        }
                    }
                }
            }
            limit++;
            if (limit > blockProcessingLimit) {
                this.core.getLogger().log(Level.INFO, "Solid Blocks connections search reached BlockProcessingLimit.");
                break;
            }
            if (findNext) {
                search.clear();
                search.putAll(newSearch);
                newSearch.clear();
            }

        }

        int fallingDistance;
        for (fallingDistance = 1; fallingDistance < 50; fallingDistance++) {
            Block newBlock = world.getBlockAt(breakBlockLocation.getBlockX(), breakBlockLocation.getBlockY() - fallingDistance, breakBlockLocation.getBlockZ());
            Material newBlockType = newBlock.getType();
            if (!isLightBlock(newBlockType))
                break;
        }

        HashMap<Location, Integer> clearWay = new HashMap<>();

        for (Map.Entry<Location, Block> logPairs : tree.entrySet()) {
            Location newBlockLocation = logPairs.getKey();
            Block logBlock = logPairs.getValue();
            Material logBlockType = logBlock.getType();
            MaterialData logBlockData = logBlock.getState().getData();
            logBlock.setType(Material.AIR);
            logBlock.removeMetadata("TreeId", this.core);

            int horisontalDistance = newBlockLocation.getBlockY() - breakBlockLocation.getBlockY() - 1;
            if (horisontalDistance < 0)
                horisontalDistance = 0;
            int verticalDistance = horisontalDistance + fallingDistance;
            int horisontalOffset = (int) Math.floor((horisontalDistance) / 1.5);
            float horisontalSpeed = calcSpeed(horisontalDistance, verticalDistance, horisontalOffset);
            if (fallingDistance == 1) {
                switch (horisontalDistance) {
                    case 1:
                        horisontalOffset = 1;
                        horisontalSpeed = 0;
                        break;
                    case 2:
                        horisontalOffset = 1;
                        horisontalSpeed = 0.1191f;
                        break;
                    case 3:
                        horisontalOffset = 1;
                        horisontalSpeed = 0.185f;
                        break;
                    case 4:
                        horisontalOffset = 2;
                        horisontalSpeed = 0.17f;
                        break;
                    case 5:
                        horisontalOffset = 2;
                        horisontalSpeed = 0.22f;
                        break;
                    case 6:
                        horisontalOffset = 3;
                        horisontalSpeed = 0.21f;
                        break;
                    case 7:
                        horisontalOffset = 3;
                        horisontalSpeed = 0.26f;
                        break;
                    case 8:
                        horisontalOffset = 4;
                        horisontalSpeed = 0.241f;
                        break;
                    case 9:
                        horisontalOffset = 4;
                        horisontalSpeed = 0.28f;
                        break;

                }
            }
            if (fallingDistance == 2) {
                switch (horisontalDistance) {
                    case 1:
                        horisontalOffset = 1;
                        horisontalSpeed = 0;
                        break;
                    case 2:
                        horisontalOffset = 1;
                        horisontalSpeed = 0.1f;
                        break;
                    case 5:
                        horisontalOffset = 2;
                        horisontalSpeed = 0.2f;
                        break;

                }
            }
            Vector vOffset = direction.clone().multiply(horisontalOffset);
            newBlockLocation.add(vOffset);
            Block testBlock = world.getBlockAt(newBlockLocation);
            if (isLightBlock(testBlock.getType())) {
                testBlock.breakNaturally();
            } else {
                newBlockLocation.subtract(vOffset);
                horisontalSpeed = calcSpeed(horisontalDistance, verticalDistance, 0);
            }
            byte face;
            if (Math.abs(direction.getZ()) > Math.abs(direction.getX())) {
                face = 0x8;
            } else {
                face = 0x4;
            }
            FallingBlock blockFalling = world.spawnFallingBlock(newBlockLocation, logBlockType, (byte) ((3 & logBlockData.getData()) | face));
            blockFalling.setVelocity(direction.clone().multiply(horisontalSpeed));

            // calc clear falling way
            int minClearVertical = newBlockLocation.getBlockY() - verticalDistance;
            for (int clearY = newBlockLocation.getBlockY(); clearY >= minClearVertical; clearY--) {
                int horisontalClearDistance = (int) Math.ceil(Math.sqrt(horisontalDistance * horisontalDistance - (clearY - minClearVertical) * (clearY - minClearVertical)));
                Location l = new Location(world, newBlockLocation.getBlockX(), clearY, newBlockLocation.getBlockZ());
                if (clearWay.containsKey(l)) {
                    if (clearWay.get(l) < horisontalClearDistance) {
                        clearWay.put(l, horisontalClearDistance);
                    }
                } else {
                    clearWay.put(l, horisontalClearDistance);
                }
            }
        }

        // clear falling way
        for (Map.Entry<Location, Integer> clearPairs : clearWay.entrySet()) {
            Location clearBlockLocation = clearPairs.getKey();
            int clearDistance = clearPairs.getValue();
            for (int c = 0; c <= clearDistance; c++) {
                Location tempClearLoc = clearBlockLocation.clone().add(direction.clone().multiply(c));
                Block clearBlock = world.getBlockAt(tempClearLoc);
                if (isLightBlock(clearBlock.getType())) {
                    clearBlock.breakNaturally();
                    clearBlock.removeMetadata("TreeId", this.core);
                }
            }
        }

        if (!this.core.getConfiguration().isFallingLeaves())
            return;

        // get blocks around tree to find leaves
        HashMap<Location, Block> leaves = new HashMap<>();
        for (Map.Entry<Location, Block> logPairs : tree.entrySet()) {
            leaves.putAll(getNearBlocks(logPairs.getKey(), 3));
        }

        if (tree.isEmpty()) {
            if (breakBlockType == Material.LOG || breakBlockType == Material.LOG_2) {
                leaves.putAll(getNearBlocks(breakBlockLocation, 3));
            } else {
                for (int i = 1; i <= 5; i++) {
                    Location tempLocation = breakBlockLocation.clone().add(0, i, 0);
                    leaves.put(tempLocation, world.getBlockAt(tempLocation));
                }
            }
        }

        leaves.remove(breakBlockLocation);

        // falling leaves
        for (Map.Entry<Location, Block> leavesPairs : leaves.entrySet()) {
            Location leavesLocation = leavesPairs.getKey();
            Block leavesBlock = leavesPairs.getValue();
            Material leavesMaterial = leavesBlock.getType();
            // byte leavesBlockData = leavesBlock.getData();
            MaterialData leavesBlockData = leavesBlock.getState().getData();
            if (leavesMaterial != Material.LEAVES && leavesMaterial != Material.LEAVES_2)
                continue;

            if (treeId == 0 && !leavesBlock.getMetadata("TreeId").isEmpty())
                continue;
            float horisontalSpeed;
            int horisontalDistance = leavesLocation.getBlockY() - breakBlockLocation.getBlockY() - 1;
            if (horisontalDistance < 0)
                horisontalDistance = 0;
            int verticalDistance = horisontalDistance + fallingDistance;
            horisontalSpeed = calcSpeed(horisontalDistance, verticalDistance, 0);

            if (tree.size() < 2)
                horisontalSpeed = 0;
            if (horisontalSpeed == 0) {
                Location tempLocation = leavesLocation.clone().add(0, -1, 0);
                if (tempLocation.getBlockX() != breakBlockLocation.getBlockX() || tempLocation.getBlockY() != breakBlockLocation.getBlockY() || tempLocation.getBlockZ() != breakBlockLocation.getBlockZ()) {
                    Block tempBlock = world.getBlockAt(tempLocation);
                    if (tempBlock.getType() != Material.AIR && tempBlock.getType() != Material.WATER && tempBlock.getType() != Material.STATIONARY_WATER) {
                        leavesBlock.removeMetadata("TreeId", this.core);
                        continue;
                    }
                }
            }
            leavesBlock.setType(Material.AIR);
            leavesBlock.removeMetadata("TreeId", this.core);
            FallingBlock blockFalling = world.spawnFallingBlock(leavesLocation, Material.LEAVES, (byte) ((0x3 & leavesBlockData.getData()) | 0x8));
            blockFalling.setVelocity(direction.clone().multiply(horisontalSpeed));
        }
    }

    private Map<Location, Block> getNearBlocks(Location l, int radius) {
        int lx = l.getBlockX();
        int ly = l.getBlockY();
        int lz = l.getBlockZ();
        World w = l.getWorld();
        HashMap<Location, Block> m = new HashMap<>();

        for (int z = lz - radius; z <= lz + radius; z++) {
            for (int x = lx - radius; x <= lx + radius; x++) {
                for (int y = ly - radius; y <= ly + radius; y++) {
                    if (x != lx || y != ly || z != lz) {
                        Location tl = new Location(w, x, y, z);
                        m.put(tl, w.getBlockAt(tl));
                    }
                }
            }
        }
        return m;
    }

    public float calcSpeed(float horizontalDistance, float verticalDistance, int horizontalOffset) {
        float speed = 0;
        if (verticalDistance > 0) {
            speed = (horizontalDistance - horizontalOffset) / (float) Math.sqrt(2 * verticalDistance / 0.064814);
        }
        return speed;
    }

    private boolean isLightBlock(Material m) {
        return this.lightMaterials.contains(m);
    }
}
