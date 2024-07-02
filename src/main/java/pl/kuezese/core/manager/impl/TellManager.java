package pl.kuezese.core.manager.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TellManager {

    private final Map<String, String> players = new ConcurrentHashMap<>();

    public void add(String player, String other) {
        players.put(player, other);
        players.put(other, player);
    }

    public String get(String player) {
        return players.get(player);
    }
}
