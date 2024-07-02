package pl.kuezese.core.object;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public @Getter class StoneGenerator {

    private final Location loc;

    public StoneGenerator(CorePlugin core, Location loc) {
        this.loc = loc;
        this.insert(core);
    }

    public StoneGenerator(ResultSet rs) throws SQLException {
        this.loc = new Location(
                Bukkit.getWorld(rs.getString("world")),
                rs.getInt("x"),
                rs.getInt("y"),
                rs.getInt("z"));
    }

    public void insert(CorePlugin core) {
        String s =
                "INSERT INTO `stone_generator` " +
                        "(`world`, " +
                        "`x`, " +
                        "`y`, " +
                        "`z`) " +
                        "VALUES " +
                        "('" + this.loc.getWorld().getName() + "'," +
                        "'" + this.loc.getBlockX() + "'," +
                        "'" + this.loc.getBlockY() + "'," +
                        "'" + this.loc.getBlockZ()+ "');";
        core.getSql().update(s);
    }

    public void destroy(CorePlugin core, Player player) {
        loc.getBlock().setType(Material.AIR);

        if (player != null && player.getGameMode() == GameMode.SURVIVAL) {
//            loc.getWorld().dropItem(loc, new ItemMaker(Material.STONE).setName(ChatHelper.color("&aGenerator Stone")).make());
            player.getInventory().addItem(new ItemMaker(Material.STONE).setName(ChatHelper.color("&aGenerator Stone")).make()).values()
                    .forEach(is -> loc.getWorld().dropItem(loc, is));
        }

        String s =
                "DELETE FROM `stone_generator` WHERE `world`='" + loc.getWorld().getName()
                        + "' AND `x`='" + this.loc.getBlockX()
                        + "' AND `y`='" + this.loc.getBlockY()
                        + "' AND `z`='" + this.loc.getBlockZ() + "'";

        core.getSql().update(s);
    }

    public void regen(CorePlugin core) {
        core.getServer().getScheduler().runTaskLater(core, () -> this.loc.getBlock().setType(Material.STONE), 25L);
    }

    public void regen() {
        if (this.loc.getBlock().getType() == Material.AIR) {
            this.loc.getBlock().setType(Material.STONE);
        }
    }
}
