package pl.kuezese.core.manager.impl;

import com.google.common.base.Strings;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.ItemHelper;
import pl.kuezese.core.manager.Manager;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;

import java.io.File;
import java.util.*;

public @Getter class LimitManager extends Manager {

    private final List<Limit> limits = new LinkedList<>();

    public LimitManager() {
        this.load();
    }

    private void load() {
        File file = new File(this.core.getDataFolder(), "limits.yml");

        if (!file.exists()) this.core.saveResource("limits.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.getConfigurationSection("limits").getKeys(false)
                .stream()
                .map(key -> new Limit(
                        Integer.parseInt(key),
                        Material.matchMaterial(cfg.getString("limits." + key + ".item")),
                        (short) cfg.getInt("limits." + key + ".data"),
                        cfg.getInt("limits." + key + ".limit"),
                        cfg.getString("limits." + key + ".requiredName"),
                        cfg.getString("limits." + key + ".displayName"),
                        cfg.getInt("limits." + key + ".displaySlot"),
                        cfg.getStringList("limits." + key + ".enchants")))
                .forEach(limits::add);
    }

    public void reload() {
        this.limits.clear();
        this.load();
    }

    public @Getter class Limit {

        private final int id;
        private final Material item;
        private final short data;
        private final int limit;
        private final String requiredName;
        private final String displayName;
        private final int displaySlot;
        private final Map<Enchantment, Integer> enchantments;

        public Limit(int id, Material item, short data, int limit, String requiredName, String displayName, int displaySlot, List<String> enchantments) {
            this.id = id;
            this.item = item;
            this.data = data;
            this.limit = limit;
            this.requiredName = ChatHelper.color(requiredName);
            this.displayName = ChatHelper.color(displayName);
            this.displaySlot = displaySlot;
            this.enchantments = new HashMap<>();
            if (!enchantments.isEmpty()) {
                enchantments.stream().map(s -> s.split(":")).forEach(strings -> this.enchantments.put(Enchantment.getByName(strings[0]), Integer.parseInt(strings[1])));
            }
        }
    }

    public Optional<Limit> get(int slot) {
        return this.limits.stream().filter(limit -> limit.getDisplaySlot() == slot).findAny();
    }

    public Optional<Limit> get(Material item) {
        return this.limits.stream().filter(limit -> limit.getItem() == item).findAny();
    }

    public int deposit(Player player, User user, Limit limit) {
        int inInventory = ItemHelper.getAmount(player, limit.getItem(), limit.getData(), limit.getRequiredName());

        if (inInventory == 0)
            return 0;

        int val = user.getLimits().getOrDefault(limit.getId(), 0);
        user.getLimits().put(limit.getId(), val + inInventory);
        ItemHelper.remove(player, limit.getItem(), limit.getData(), limit.getRequiredName(), inInventory);
        player.updateInventory();
        return inInventory;
    }

    public boolean withdraw(Player player, User user, Limit limit) {
        int inDeposit = user.getLimits().getOrDefault(limit.getId(), 0);

        if (inDeposit == 0) {
            player.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz tego przedmiotu w depozycie."));
            return false;
        }

        int emptySlots = (int) Arrays.stream(player.getInventory().getContents())
                .filter(obj -> Objects.isNull(obj) || (limit.getItem() == obj.getType() && limit.getData() == obj.getDurability() && limit.getLimit() > obj.getAmount()))
                .count();

        if (emptySlots == 0) {
            player.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz zadnego wolnego slotu w ekwipunku."));
            return false;
        }

        if (limit.getLimit() == 0) {
            int add = Math.min(emptySlots * 64, inDeposit);
            user.getLimits().put(limit.getId(), inDeposit - add);
            player.getInventory().addItem(new ItemMaker(limit.getItem(), add, limit.getData()).setName(Strings.isNullOrEmpty(limit.getRequiredName()) ? null : limit.getRequiredName()).make());
            player.sendMessage(ChatHelper.color(" &8>> &7Wyplaciles: &a" + add + " przedmiotow z depozytu&7."));
            player.updateInventory();
            return true;
        }

        int inInventory = ItemHelper.getAmount(player, limit.getItem(), limit.getData(), limit.getRequiredName());

        if (inInventory >= limit.getLimit()) {
            player.sendMessage(ChatHelper.color(" &8>> &7Posiadasz maksymalna ilosc tego przedmiotu w ekwipunku."));
            return false;
        }

        int add = Math.min(emptySlots * 64, Math.min(inDeposit, limit.getLimit() - inInventory));
        user.getLimits().put(limit.getId(), inDeposit - add);
        player.getInventory().addItem(new ItemMaker(limit.getItem(), add, limit.getData()).setName(Strings.isNullOrEmpty(limit.getRequiredName()) ? null : limit.getRequiredName()).setEnchants(limit.getEnchantments()).make());
        player.sendMessage(ChatHelper.color(" &8>> &7Wyplaciles: &a" + add + " przedmiotow z depozytu&7."));
        player.updateInventory();
        return true;
    }

    public void withdrawAll(Player player, User user) {
        this.limits.stream().filter(limit -> limit.getLimit() != 0).forEach(limit -> this.withdraw(player, user, limit));
    }
}