package pl.kuezese.core.manager.impl;

import pl.kuezese.core.manager.Manager;
import pl.kuezese.core.object.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class UserManager extends Manager {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public Map<String, User> get() {
        return users;
    }

    public void add(String name) {
        users.put(name, new User(name));
    }

    public void load() {
        this.core.getSql().query("SELECT * FROM `core`", rs -> {
            try {
                while (rs.next()) {
                    User user = new User(rs);
                    users.put(user.getName(), user);
                }
            } catch (Throwable throwable) {
                this.core.getLogger().log(Level.WARNING, "Error while loading users from database!", throwable);
            }
            this.core.getLogger().info("Loaded " + users.size() + " players");
        });
    }

    public User get(String s) {
        return users.get(s);
    }
}
