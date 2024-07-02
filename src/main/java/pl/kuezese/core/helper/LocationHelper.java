package pl.kuezese.core.helper;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class LocationHelper {

    public static Location fromString(String s) {
        if (StringHelper.isEmpty(s)) return null;
        String[] split = s.split(":");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    public static String toString(Location loc) {
        if (loc == null) return "";
        return loc.getWorld().getName() + ':' + loc.getX() + ':' + loc.getY() + ':' + loc.getZ();
    }
}
