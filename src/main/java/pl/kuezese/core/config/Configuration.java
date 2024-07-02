package pl.kuezese.core.config;

import com.google.common.io.Files;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.kuezese.core.CorePlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;

@Getter
public class Configuration {

    private final CorePlugin core;

    private String databaseType, mysqlHost, mysqlDatabase, mysqlUser, mysqlPassword;
    private int mysqlPort;

    private List<String> autoMsg;

    private int borderSize;

    private String globalChatFormat, adminChatFormat;

    private @Setter Location spawnLoc, checkLoc;

    private List<String> cmds;
    private List<String> ops;

    private int blockProcessingLimit;
    private boolean fallingLeaves;

    public Configuration(CorePlugin core) {
        this.core = core;
        this.reload();
    }

    public void reload() {
        this.core.saveDefaultConfig();
        this.core.reloadConfig();

        FileConfiguration config = new YamlConfiguration();
        File file = new File(this.core.getDataFolder() + File.separator + "config.yml");
        try {
            config.loadFromString(Files.toString(file, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            this.core.getLogger().log(Level.SEVERE, "Failed to load configuration!", ex);
        }


        World world = this.core.getServer().getWorlds().get(0);

        this.databaseType = config.getString("mysql.type");
        this.mysqlHost = config.getString("mysql.host");
        this.mysqlPort = config.getInt("mysql.port");
        this.mysqlDatabase = config.getString("mysql.database");
        this.mysqlUser = config.getString("mysql.user");
        this.mysqlPassword = config.getString("mysql.password");

        this.autoMsg = config.getStringList("automsg.messages");

        this.borderSize = config.getInt("border.size");

        this.core.getChatManager().setEnabled(config.getBoolean("chat.enabled"));
        this.core.getChatManager().setOnlyVip(config.getBoolean("chat.vip"));
        this.core.getChatManager().setRequireLvl(config.getBoolean("chat.lvl"));

        this.globalChatFormat = config.getString("chat.format.global");
        this.adminChatFormat = config.getString("chat.format.admin");

        this.spawnLoc = new Location(world, config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"), (float) config.getDouble("spawn.yaw"), (float) config.getDouble("spawn.pitch"));
        this.checkLoc = new Location(world, config.getDouble("check.x"), config.getDouble("check.y"), config.getDouble("check.z"), (float) config.getDouble("check.yaw"), (float) config.getDouble("check.pitch"));

        this.cmds = config.getStringList("antigrief.commands");
        this.ops = config.getStringList("antigrief.ops");

        this.blockProcessingLimit = config.getInt("trees.blockProcessingLimit");
        this.fallingLeaves = config.getBoolean("trees.fallingLeaves");
    }

    public void save(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        config.set("spawn.x", spawnLoc.getX());
        config.set("spawn.y", spawnLoc.getY());
        config.set("spawn.z", spawnLoc.getZ());
        config.set("spawn.yaw", spawnLoc.getYaw());
        config.set("spawn.pitch", spawnLoc.getPitch());

        config.set("chat.enabled", this.core.getChatManager().isEnabled());
        config.set("chat.vip", this.core.getChatManager().isOnlyVip());
        config.set("chat.lvl", this.core.getChatManager().isRequireLvl());

        config.set("check.x", checkLoc.getX());
        config.set("check.y", checkLoc.getY());
        config.set("check.z", checkLoc.getZ());
        config.set("check.yaw", checkLoc.getYaw());
        config.set("check.pitch", checkLoc.getPitch());

        plugin.saveConfig();
        plugin.reloadConfig();
    }
}
