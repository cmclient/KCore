package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class VipCommand extends Command {

	public VipCommand() {
		super("vip", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &2&lVIP &8) &8&m------------&8 "));
		sender.sendMessage(ChatHelper.color(" &8>> &7VIPa dostaje sie na start."));
		sender.sendMessage("");
		sender.sendMessage(ChatHelper.color(" &8>> &a&lPrzywileje:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aSpecjalny kit VIP."));
		sender.sendMessage(ChatHelper.color(" &8>> &a&lKomendy:"));
		sender.sendMessage(ChatHelper.color(" &8>> &a/enderchest &8- &7Podreczny enderchest"));
		sender.sendMessage(ChatHelper.color(" &8>> &a/repair &8- &7Darmowa naprawa przedmiotow."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/feed &8- &7Nie jestes juz glodny."));
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &2&lVIP &8) &8&m------------&8 "));
		return true;
	}
}
