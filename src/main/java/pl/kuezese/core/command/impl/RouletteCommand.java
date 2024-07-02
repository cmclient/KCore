package pl.kuezese.core.command.impl;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.object.User;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

public class RouletteCommand extends Command {

	private final Map<String, Long> times;
	private final Pattern pattern;

	public RouletteCommand() {
		super("roulette", "", "ruletka");
		this.times = new WeakHashMap<>();
		this.pattern = Pattern.compile("(\\d*)\\.(\\d*)");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/ruletka <mnoznik> <ilosc>"));
			return true;
		}

		User user = this.userManager.get(sender.getName());

		if (!pattern.matcher(args[0]).matches()) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Dostepne mnozniki: &a1.25 &7(&a75%&7), &a1.50 &7(&a50%&7), &a1.75 &7(&a15%&7)"));
            return true;
		}

		float multiplier = Float.parseFloat(args[0]);

		if (multiplier != 1.25 && multiplier != 1.50 && multiplier != 1.75) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Dostepne mnozniki: &a1.25 &7(&a75%&7), &a1.50 &7(&a50%&7), &a1.75 &7(&a15%&7), &a2.0 &7(&a5%&7)"));
			return true;
		}

		Float amount;

		try {
			amount = Float.parseFloat(args[1]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podana liczba jest &anieprawidlowa&7."));
			return true;
		}

		if (amount.isInfinite() || amount.isNaN()) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podana liczba jest &anieprawidlowa&7."));
			return true;
		}

		if (user.getCoins() < amount) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Nie masz tylu &acoins&7."));
			return true;
		}

		if (amount < 50.0 || amount > 1000.0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Minimalna ilosc to &a50.0 &7a maksymalna &a500&7."));
			return true;
		}

		if (times.containsKey(p.getName()) && times.get(p.getName()) > System.currentTimeMillis()) {
			long time = times.get(p.getName());
			p.sendMessage(ChatHelper.color(" &8>> &7Kolejny raz bedziesz mogl obstawic na ruletce za: &a" + (time - System.currentTimeMillis()) / 1000 + "s&7."));
			return true;
		} else times.put(p.getName(), System.currentTimeMillis() + 1000L);

		int chance = (multiplier == 1.25) ? 75 : (multiplier == 1.50) ? 50 : (multiplier == 1.75) ? 15 : 5;

		new BukkitRunnable() {

			private int time = 5;

			@Override
			public void run() {
				if (time > 0) {
					p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
					ChatHelper.title(p, "&8• &2&lRULETKA &8•", "&a" + time, 0, 20, 30);
					this.time--;
					return;
				}
                if (RandomHelper.chance(chance)) {
                    p.playSound(p.getLocation(), Sound.EXPLODE, 10, 2);
                    float coins = (amount * multiplier) - amount;
                    user.addCoins(coins);
                    core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + sender.getName() + " &awygral &a" + user.formatCoins(coins) + "$ &7na &a/ruletka&7."));
                    ChatHelper.title(p, "&8• &2&lRULETKA &8•", "&7Wygrales &a" + coins + "$.", 20, 30, 40);
                } else {
                    p.playSound(p.getLocation(), Sound.VILLAGER_DEATH, 1, 1);
                    user.removeCoins(amount);
                    core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + sender.getName() + " &cprzegral &a" + user.formatCoins(amount) + "$ &7na &a/ruletka&7."));
                    ChatHelper.title(p, "&8• &2&lRULETKA &8•", "&7Przegrales &a" + amount + "$.", 20, 30, 40);
                }
                this.cancel();
            }

		}.runTaskTimerAsynchronously(this.core, 0L, 20L);

		return true;
	}
}
