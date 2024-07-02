package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class ElitaCommand extends Command {

	public ElitaCommand() {
		super("elita", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &9E&8L&7I&6T&5A &8) &8&m------------&8 "));
		sender.sendMessage(ChatHelper.color(" &8>> &a&lPrzywileje:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aTo samo co nizsze rangi, oraz:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aSpecjalny kit &9E&8L&7I&6T&5A: &7/kit."));
		sender.sendMessage(ChatHelper.color(" &8>> &aKit TNT: &7/kit&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &aKit CUBOID: &7/kit."));
		sender.sendMessage(ChatHelper.color(" &8>> &aKit PREMIUMCASE: &7/kit."));
		sender.sendMessage(ChatHelper.color(" &8>> &aKit PIEROZEK: &7/kit."));
		sender.sendMessage(ChatHelper.color(" &8>> &a3 sejfy w &7/sejf&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/near &8- &7Pokazuje graczy blisko ciebie"));
		sender.sendMessage(ChatHelper.color(" &8>> &a/fly &8- &7Dostep do latania na spawnie"));
		sender.sendMessage(ChatHelper.color(" &8>> &aZwiekszony drop o 20%&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &aZmniejszona ilosc wymaganych itemow na gildie o 25%&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &aZmniejszony czas na teleportacje /baza o 50%&7."));
		sender.sendMessage("");
		sender.sendMessage(ChatHelper.color(" &8>> &7Zakup ta usluge w naszym sklepie:"));
		sender.sendMessage(ChatHelper.color(" &8>> &2&lcoremax.pl."));
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &9E&8L&7I&6T&5A &8) &8&m------------&8 "));
		return true;
	}
}
