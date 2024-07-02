package pl.kuezese.core.manager.impl;

import lombok.Getter;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.manager.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class BanManager extends Manager {

    private final @Getter Map<String, Ban> bans = new ConcurrentHashMap<>();

    public void load() {
        this.core.getSql().query("SELECT * FROM `bans`", rs -> {
            try {
                while (rs.next()) {
                    Ban ban = new Ban(rs);
                    if (ban.getTime() != 0L && ban.getTime() <= System.currentTimeMillis()) {
                        this.core.getSql().update("DELETE FROM `bans` WHERE `name`='" + ban.getName() + "'");
                        continue;
                    }
                    bans.put(ban.getName(), ban);
                }
            } catch (SQLException ex) {
                this.core.getLogger().log(Level.SEVERE, "Failed to load bans!", ex);
            }
            this.core.getLogger().info("Loaded " + bans.size() + " banned players");
        });
    }

    public Ban get(String name) {
        return bans.get(name);
    }

    public static @Getter class Ban {

        private final String name;
        private final String admin;
        private final long time;
        private final String reason;

        public Ban(String name, String admin, long time, String reason) {
            this.name = name;
            this.admin = admin;
            this.time = time;
            this.reason = reason;
            this.insert();
        }

        public Ban(ResultSet rs) throws SQLException {
            this.name = rs.getString("name");
            this.admin = rs.getString("admin");
            this.time = rs.getLong("time");
            this.reason = rs.getString("reason");
        }

        private void insert() {
            CorePlugin.getInstance().getSql().update("INSERT INTO `bans` (`name`, `admin`, `time`, `reason`) VALUES ('" + this.name + "','" + this.admin + "','" + this.time + "','" + this.reason  + "');");
        }
    }
}