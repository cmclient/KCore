package pl.kuezese.core.manager.impl;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.kuezese.core.manager.Manager;
import pl.kuezese.core.object.CaseItem;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Getter
public class CaseManager extends Manager {

    private final List<CaseItem> items = new LinkedList<>();

    public CaseManager() {
        this.load();
    }

    private void load() {
        File file = new File(this.core.getDataFolder(), "case.yml");

        if (!file.exists()) this.core.saveResource("case.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.getConfigurationSection("case").getKeys(false)
                .stream()
                .map(key -> new CaseItem(
                        Material.matchMaterial(cfg.getString("case." + key + ".item")),
                        (short) cfg.getInt("case." + key + ".data"),
                        cfg.getInt("case." + key + ".amount"),
                        cfg.getDouble("case." + key + ".chance"),
                        cfg.getString("case." + key + ".name"),
                        cfg.getStringList("case." + key + ".enchants")))
                .forEach(items::add);
    }

    public void reload() {
        this.items.clear();
        this.load();
    }
}