package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class SponsorCommand extends Command {

	public SponsorCommand() {
		super("sponsor", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &b&lSPONSOR &8) &8&m------------&8 "));
		sender.sendMessage(ChatHelper.color(" &8>> &a&lPrzywileje:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aTo samo co SVIP oraz:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aPisanie na &1k&2o&3l&4o&5r&ao&7w&8o&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &aPisanie na wylaczonym chacie."));
		sender.sendMessage(ChatHelper.color(" &8>> &a2 sejfy w &7/sejf&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/nick &8- &7Zmiana koloru nicku&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/kit sponsor&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/kit design&7."));
		sender.sendMessage("");
		sender.sendMessage(ChatHelper.color(" &8>> &7Zakup ta usluge w naszym sklepie:"));
		sender.sendMessage(ChatHelper.color(" &8>> &2&lcoremax.pl."));
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &b&lSPONSOR &8) &8&m------------&8 "));
		return true;
	}
}
