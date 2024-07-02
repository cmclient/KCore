package pl.kuezese.core.manager.impl;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.kuezese.core.manager.Manager;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class WarpManager extends Manager {

    private final File file = new File(this.core.getDataFolder(), "warps.yml");
    private final @Getter List<Warp> warps = new LinkedList<>();

    public WarpManager() {
        this.load();
    }

    private void load() {
        if (!file.exists()) this.core.saveResource("warps.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.getConfigurationSection("warps").getKeys(false).forEach(key -> {
            Location loc = new Location(
                    this.core.getServer().getWorld(cfg.getString("warps." + key + ".location.world")),
                    cfg.getDouble("warps." + key + ".location.x"),
                    cfg.getDouble("warps." + key + ".location.y"),
                    cfg.getDouble("warps." + key + ".location.z"),
                    (float) cfg.getDouble("warps." + key + ".location.yaw"),
                    (float) cfg.getDouble("warps." + key + ".location.pitch"));

            Warp warp = new Warp(
                    key,
                    Material.matchMaterial(cfg.getString("warps." + key + ".item")),
                    loc);

            this.warps.add(warp);
        });
    }

    public void reload() {
        this.warps.clear();
        this.load();
    }

    public Warp get(String name) {
        return this.warps.stream().filter(warp -> warp.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public void save() {
        YamlConfiguration cfg = new YamlConfiguration();

        warps.forEach(warp -> {
            cfg.set("warps." + warp.getName() + ".item", warp.getItem().name());
            cfg.set("warps." + warp.getName() + ".location.world", warp.getLocation().getWorld().getName());
            cfg.set("warps." + warp.getName() + ".location.x", warp.getLocation().getX());
            cfg.set("warps." + warp.getName() + ".location.y", warp.getLocation().getY());
            cfg.set("warps." + warp.getName() + ".location.z", warp.getLocation().getZ());
            cfg.set("warps." + warp.getName() + ".location.yaw", warp.getLocation().getYaw());
            cfg.set("warps." + warp.getName() + ".location.pitch", warp.getLocation().getPitch());
        });

        try {
            cfg.save(this.file);
        } catch (IOException ex) {
            this.core.getLogger().log(Level.SEVERE, "Failed to save warps!", ex);
        }
    }

    public static @Getter class Warp {

        private final String name;
        private final Material item;
        private @Setter Location location;

        public Warp(String name, Material item, Location location) {
            this.name = name;
            this.item = item;
            this.location = location;
        }
    }

}