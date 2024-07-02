package pl.kuezese.core.object;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.LocationHelper;
import pl.kuezese.core.helper.UserHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class User {

    private final String name;
    private float coins;
    private final DecimalFormat coinsFormat;
    private int xp;
    private int lvl;
    private boolean cobble;
    private boolean drop;
    private boolean dropOnFullInventory;
    private boolean dropCoins;
    private final Map<String, Long> kitCooldowns;
    private final Map<Integer, Integer> limits;
    private boolean autoSell;
    private Location home;
    private final Safe[] safes;
    //NOT SAVED TO DB
    private long lastChat;
    private boolean checked;
    private boolean vanished;
    private boolean godmode;
    private long muteTime;
    private Player damager;
    private long attackTime;
    private long teleportTime;
    private boolean usingClient;

    public User(String name) {
        this.name = name;
        this.coinsFormat = new DecimalFormat("#.##");
        this.cobble = true;
        this.drop = true;
        this.dropOnFullInventory = true;
        this.dropCoins = true;
        this.kitCooldowns = new ConcurrentHashMap<>();
        this.limits = new ConcurrentHashMap<>();
        this.safes = new Safe[]{new Safe(1), new Safe(2), new Safe(3), new Safe(4)};
        this.insert();
    }

    public User(ResultSet rs) throws SQLException {
        this.name = rs.getString("name");
        this.coins = rs.getFloat("coins");
        this.coinsFormat = new DecimalFormat("#.##");
        this.xp = rs.getInt("xp");
        this.lvl = rs.getInt("lvl");
        this.cobble = rs.getBoolean("cobble");
        this.drop = rs.getBoolean("drop");
        this.dropOnFullInventory = rs.getBoolean("dropOnFullInventory");
        this.dropCoins = rs.getBoolean("dropCoins");
        this.kitCooldowns = UserHelper.deserializeKits(rs.getString("kits"));
        this.limits = UserHelper.deserializeLimits(rs.getString("limits"));
        this.home = LocationHelper.fromString(rs.getString("home"));
        this.safes = new Safe[]{new Safe(1), new Safe(2), new Safe(3), new Safe(4)};
        UserHelper.deserializeSafes(this, rs.getString("safes"));
    }

    public String getCoinsFormatted() {
        return this.formatCoins(this.coins);
    }

    public String formatCoins(float value) {
        return this.coinsFormat.format(value);
    }

    public void addCoins(float value) {
        this.coins += value;
    }

    public void removeCoins(float value) {
        this.coins -= value;
    }

    public void addXp(int xp) {
        this.xp += xp;
    }

    public Long getKitCooldown(String name) {
        return this.kitCooldowns.getOrDefault(name, 0L);
    }

    public void setKitCooldown(String name, long time) {
        this.kitCooldowns.put(name, time);
    }

    public boolean isCombat() {
        return this.damager != null;
    }

    private void insert() {
        String s =
                "INSERT INTO `core` " +
                        "(`name`, " +
                        "`coins`, " +
                        "`xp`, " +
                        "`lvl`, " +
                        "`cobble`, " +
                        "`drop`, " +
                        "`dropOnFullInventory`, " +
                        "`dropCoins`, " +
                        "`kits`, " +
                        "`limits`, " +
                        "`home`, " +
                        "`safes`) " +

                        "VALUES " +

                        "('" + this.name + "'," +
                        "'" + this.coins + "'," +
                        "'" + this.xp + "'," +
                        "'" + this.lvl + "'," +
                        "'" + BooleanUtils.toInteger(this.cobble) + "'," +
                        "'" + BooleanUtils.toInteger(this.drop) + "'," +
                        "'" + BooleanUtils.toInteger(this.dropOnFullInventory) + "'," +
                        "'" + BooleanUtils.toInteger(this.dropCoins) + "'," +
                        "'" + UserHelper.serializeKits(this) + "'," +
                        "'" + UserHelper.serializeLimits(this) + "'," +
                        "'" + LocationHelper.toString(this.home) + "'," +
                        "'" + UserHelper.serializeSafes(this) + "');";
        CorePlugin.getInstance().getSql().update(s);
    }

    public void update() {
        String s =
                "UPDATE `core` SET " +
                        "`coins`='" + this.coins + "'," +
                        "`xp`='" + this.xp + "'," +
                        "`lvl`='" + this.lvl + "'," +
                        "`cobble`='" + BooleanUtils.toInteger(this.cobble) + "'," +
                        "`drop`='" + BooleanUtils.toInteger(this.drop) + "'," +
                        "`dropOnFullInventory`='" + BooleanUtils.toInteger(this.dropOnFullInventory) + "'," +
                        "`dropCoins`='" + BooleanUtils.toInteger(this.dropCoins) + "'," +
                        "`kits`='" + UserHelper.serializeKits(this) + "'," +
                        "`limits`='" + UserHelper.serializeLimits(this) + "'," +
                        "`home`='" + LocationHelper.toString(this.home) + "'," +
                        "`safes`='" + UserHelper.serializeSafes(this) + "' WHERE `name` = '" + this.name + "'";
        CorePlugin.getInstance().getSql().update(s);
    }
}