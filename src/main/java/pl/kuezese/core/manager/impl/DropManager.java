package pl.kuezese.core.manager.impl;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.manager.Manager;
import pl.kuezese.core.object.User;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class DropManager extends Manager {

    private final List<Drop> drops = new LinkedList<>();

    public DropManager() {
        this.load();
    }

    private void load() {
        File file = new File(this.core.getDataFolder(), "drops.yml");

        if (!file.exists()) this.core.saveResource("drops.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.getConfigurationSection("drops").getKeys(false)
                .stream()
                .map(key -> new Drop(Material.matchMaterial(cfg.getString("drops." + key + ".item")),
                        (short) cfg.getInt("drops." + key + ".data"),
                        cfg.getDouble("drops." + key + ".chance"),
                        cfg.getInt("drops." + key + ".exp"),
                        cfg.getInt("drops." + key + ".xp")))
                .forEach(drops::add);
    }

    public void reload() {
        this.drops.clear();
        this.load();
    }

    public @Getter class Drop {

        private final Material item;
        private final short data;
        private final double chance;
        private final int exp;
        private final int xp;
        private final Set<String> enabled;

        public Drop(Material item, short data, double chance, int exp, int xp) {
            this.item = item;
            this.data = data;
            this.chance = chance;
            this.exp = exp;
            this.xp = xp;
            this.enabled = ConcurrentHashMap.newKeySet();
        }

        public boolean isEnabled(String name) {
            return !enabled.contains(name);
        }

        public void setEnabled(String name, boolean enable) {
            if (enable) enabled.remove(name);
            else enabled.add(name);
        }

        public boolean chance(Player player, User user) {
            double chance = (user.isUsingClient() || player.hasPermission("cm.drop.vip")) ? this.chance * 1.20D : this.chance;
            return RandomHelper.chance(chance);
        }
    }
}
