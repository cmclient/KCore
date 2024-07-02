package pl.kuezese.core.manager.impl;

import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportManager {

    public @Getter Map<String, BukkitTask> teleports = new ConcurrentHashMap<>();
    private final Map<String, String> requests = new ConcurrentHashMap<>();

    public void add(String player, String other) {
        requests.put(other, player);
    }

    public void remove(String player) {
        requests.remove(player);
    }

    public String get(String player) {
        return requests.get(player);
    }
}
