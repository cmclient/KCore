package pl.kuezese.core.sql;

import lombok.RequiredArgsConstructor;
import pl.kuezese.core.CorePlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@RequiredArgsConstructor
public class SQL {

    private final CorePlugin core;
    public ExecutorService executor = Executors.newScheduledThreadPool(10);
    private Connection connection;

    public boolean connect(CorePlugin core) {
        try {
            core.getLogger().info("Connecting to " + core.getConfiguration().getDatabaseType() + " database...");

            switch (core.getConfiguration().getDatabaseType()) {
                case "mysql":
//                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String connectionString = "jdbc:mysql://" + core.getConfiguration().getMysqlHost() + ":" + core.getConfiguration().getMysqlPort() + "/" + core.getConfiguration().getMysqlDatabase() + "?autoReconnect=true";
                    connection = DriverManager.getConnection(connectionString, core.getConfiguration().getMysqlUser(), core.getConfiguration().getMysqlPassword());

                    // Schedule a task to execute periodically
                    core.getServer().getScheduler().runTaskTimerAsynchronously(core, () -> this.execute("SELECT CURTIME()"), 15000L, 15000L);
                    break;
                case "sqlite":
                    Class.forName("org.sqlite.JDBC");
                    connection = DriverManager.getConnection("jdbc:sqlite:" + core.getDataFolder() + File.separatorChar + "minecraft.db");
                    break;
                default: {
                    core.getLogger().warning("Invalid database type.");
                    return false;
                }
            }

            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            core.getLogger().log(Level.SEVERE,"Failed to connect to database.", ex);
            return false;
        }
    }

    public void execute(String query) {
        try {
            this.connection.createStatement().execute(query);
        } catch (SQLException ex) {
            this.core.getLogger().log(Level.SEVERE, "Failed to execute query!", ex);
        }
    }

    public void update(String update) {
        executor.submit(() -> {
            try {
                connection.createStatement().executeUpdate(update);
            } catch (SQLException ex) {
                this.core.getLogger().log(Level.SEVERE, "Failed to execute update!", ex);
            }
        });
    }

    public void query(String query, QueryCallback callback) {
        executor.submit(() -> {
            try (ResultSet rs = connection.createStatement().executeQuery(query)) {
                callback.received(rs);
            } catch (SQLException ex) {
                this.core.getLogger().log(Level.SEVERE, "Failed to execute query!", ex);
            }
        });
    }

    public interface QueryCallback {
        void received(ResultSet rs);
    }
}