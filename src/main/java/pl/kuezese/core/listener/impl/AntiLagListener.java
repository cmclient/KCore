package pl.kuezese.core.listener.impl;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.listener.Listener;

public class AntiLagListener extends Listener {

    @EventHandler(ignoreCancelled = true)
    public void onRedstone(BlockRedstoneEvent e) {
        if (e.getBlock().getType() == Material.REDSTONE_WIRE && this.core.getRedstoneOps().incrementAndGet() >= 3500) {
            e.getBlock().setType(Material.AIR);
            this.core.getServer().broadcast(ChatHelper.color(
                            "&8[&4AntiLag&8] &7Wykryto zbyt duzo &aREDSTONE &7na: " +
                                    "&aX: " + e.getBlock().getX() + "&7, " +
                                    "&aY: " + e.getBlock().getY() + "&7, " +
                                    "&aZ: " + e.getBlock().getZ() + "&7. "),
                    "cm.helpop");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(VehicleMoveEvent e) {
        if (this.core.getVehicleMoves().incrementAndGet() >= 125) {
            e.getVehicle().remove();
            this.core.getServer().broadcast(ChatHelper.color(
                            "&8[&4AntiLag&8] &7Wykryto zbyt duzo pojazdow &a" + e.getVehicle().getName() + " &7na: " +
                                    "&aX: " + e.getVehicle().getLocation().getBlockX() + "&7, " +
                                    "&aY: " + e.getVehicle().getLocation().getBlockY() + "&7, " +
                                    "&aZ: " + e.getVehicle().getLocation().getBlockZ() + "&7. "),
                    "cm.helpop");
        }
    }
}
