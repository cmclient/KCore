package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class LegendaCommand extends Command {

	public LegendaCommand() {
		super("legenda", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &1&lL&2&lE&3&lG&4&lE&5&lN&a&lD&7&lA &8) &8&m------------&8 "));
		sender.sendMessage(ChatHelper.color(" &8>> &a&lPrzywileje:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aTo samo co nizsze rangi, oraz:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aSpecjalny kit &1L&2E&3G&4E&5N&aD&7A: &7/kit."));
		sender.sendMessage(ChatHelper.color(" &8>> &aKit GILDIA: &7/kit."));
		sender.sendMessage(ChatHelper.color(" &8>> &aDostep do wszystkich kitow: &7/kit."));
		sender.sendMessage(ChatHelper.color(" &8>> &aAutomatyczna wymiana przedmiotow na coinsy: &7/panel."));
		sender.sendMessage(ChatHelper.color(" &8>> &aBrak spadku glodu."));
		sender.sendMessage(ChatHelper.color(" &8>> &aMożliwosc ustawienia &1t&2e&3c&4z&5o&7w&7e&8g&9o &7nicku w &a/nick&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &a5 sejfow w &7/sejf&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/panel &8- &7Automatyczne sprzedawanie"));
		sender.sendMessage(ChatHelper.color(" &8>> &aWięcej coins za &azabójstwo&7."));
		sender.sendMessage("");
		sender.sendMessage(ChatHelper.color(" &8>> &7Zakup ta usluge w naszym sklepie:"));
		sender.sendMessage(ChatHelper.color(" &8>> &2&lcoremax.pl."));
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &1&lL&2&lE&3&lG&4&lE&5&lN&a&lD&7&lA &8) &8&m------------&8 "));
		return true;
	}
}
