package pl.kuezese.core.command.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

import java.util.Locale;

public class CheckCommand extends Command {

	public CheckCommand() {
		super("check", "cm.check", "sprawdz");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player player = sender instanceof Player ? ((Player) sender) : null;

		if (player == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie:"));
			sender.sendMessage(ChatHelper.color(" &8>> &a/check sprawdz <nick>"));
			sender.sendMessage(ChatHelper.color(" &8>> &a/check czysty <nick>"));
			sender.sendMessage(ChatHelper.color(" &8>> &a/check set spawn"));
			return true;
		}

		Player other = this.core.getServer().getPlayer(args[1]);
		switch (args[0].toLowerCase(Locale.ROOT)) {
			case "sprawdz":
				if (other == null) {
					sender.sendMessage(ChatHelper.color(" &8\u00bb &7Podany gracz jest &coffline&7."));
					return true;
				}

				User user = this.userManager.get(other.getName());

				if (user.isUsingClient()) {
					sender.sendMessage(ChatHelper.color(" &8\u00bb &7Gracz &a" + other.getName() + " &7posiada &aweryfikacje&7."));
					return true;
				}

				player.setHealth(player.getMaxHealth());
				player.setFireTicks(0);

				other.setHealth(player.getMaxHealth());
				other.setFireTicks(0);

				user.setChecked(true);

				PaperLib.teleportAsync(player, this.core.getConfiguration().getCheckLoc());
				PaperLib.teleportAsync(other, this.core.getConfiguration().getCheckLoc());

				this.core.getServer().broadcastMessage("");
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Administrator &c" + sender.getName() + " &7sprawdza gracza &c" + other.getName()));
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &c" + other.getName() + "&7, masz 2 minuty na podanie AnyDeska na /helpop."));
				this.core.getServer().broadcastMessage("");
				break;
			case "czysty":
				if (other == null) {
					sender.sendMessage(ChatHelper.color(" &8\u00bb &7Podany gracz jest &coffline&7."));
					return true;
				}

				User user1 = this.userManager.get(other.getName());
				user1.setChecked(false);

				this.core.getServer().broadcastMessage("");
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8\u00bb &7Gracz &c" + other.getName() + " &7jest czysty."));
				this.core.getServer().broadcastMessage("");
				PaperLib.teleportAsync(player, this.core.getConfiguration().getSpawnLoc());
				PaperLib.teleportAsync(other, this.core.getConfiguration().getSpawnLoc());
				break;
			case "set":
				this.core.getConfiguration().setCheckLoc(player.getLocation());
				this.core.getConfiguration().save(this.core);
				sender.sendMessage(ChatHelper.color(" &8>> &aUstawiono nowa lokalizacje sprawdzania."));
				break;
			default:
				sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie:"));
				sender.sendMessage(ChatHelper.color(" &8>> &a/check sprawdz <nick>"));
				sender.sendMessage(ChatHelper.color(" &8>> &a/check czysty <nick>"));
				sender.sendMessage(ChatHelper.color(" &8>> &a/check set spawn"));
				break;
		}
		return true;
	}
}

