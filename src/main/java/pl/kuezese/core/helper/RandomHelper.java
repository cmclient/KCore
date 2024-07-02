package pl.kuezese.core.helper;

import io.papermc.lib.PaperLib;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomHelper {

	private static final List<ChatColor> colors = new ArrayList<>(Arrays.asList(ChatColor.values()));

	static {
		colors.remove(ChatColor.MAGIC);
		colors.remove(ChatColor.BOLD);
		colors.remove(ChatColor.STRIKETHROUGH);
		colors.remove(ChatColor.UNDERLINE);
		colors.remove(ChatColor.ITALIC);
		colors.remove(ChatColor.RESET);
	}

	public static boolean chance(double chance) {
		return chance >= ThreadLocalRandom.current().nextDouble() * 100.0D;
	}

	public static int nextInt(int bound)  {
		return ThreadLocalRandom.current().nextInt(bound);
	}

	public static int nextInt(int min, int max)  {
		return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
	}

	public static double nextDouble(double min, double max)  {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	public static boolean nextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	public static Location randomLoc(World world, int radius) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		Location location = new Location(world, random.nextInt(-radius, radius), 0, random.nextInt(-radius, radius));
		location.setY(location.getWorld().getHighestBlockAt(location).getLocation().getY());
		return location;
	}

	public static Location getRandomLocationAround(Location center, double radius) {
		double angle = Math.random() * Math.PI * 2; // Random angle in radians
		double x = center.getX() + radius * Math.cos(angle);
		double z = center.getZ() + radius * Math.sin(angle);

		Location location = new Location(center.getWorld(), x, 0, z);
		location.setY(location.getWorld().getHighestBlockAt(location).getLocation().getY());
		return location;
	}

	public static void teleport(Player p, Location loc) {
		PaperLib.teleportAsync(p, loc)
				.thenAccept(result -> ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostales przeteleportowany na &alosowe koordynaty&7.", 10, 40, 10));
	}

	public static String random(String s) {
		char[] chars = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : chars) {
			sb.append(colors.get(ThreadLocalRandom.current().nextInt(colors.size()))).append(c);
		}
		return sb.toString();
	}
}
