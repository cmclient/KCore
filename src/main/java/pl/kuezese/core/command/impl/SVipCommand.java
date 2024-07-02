package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;


public class SVipCommand extends Command {

	public SVipCommand() {
		super("svip", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &2&lSVIP &8) &8&m------------&8 "));
		sender.sendMessage(ChatHelper.color(" &8>> &a&lPrzywileje:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aTo samo co VIP oraz:"));
		sender.sendMessage(ChatHelper.color(" &8>> &aSpecjalny kit SVIP."));
		sender.sendMessage(ChatHelper.color(" &8>> &a&lKomendy:"));
		sender.sendMessage(ChatHelper.color(" &8>> &a/workbench &8- &7Przenosny crafting"));
		sender.sendMessage(ChatHelper.color(" &8>> &a/kowadlo &8- &7Przenosne kowadlo"));
		sender.sendMessage(ChatHelper.color(" &8>> &a/hat &8- &7Zakladasz blok na glowe."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/heal &8- &7leczysz sie."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/repair all &8- &7Naprawia wszystkie przedmioty."));
		sender.sendMessage("");
		sender.sendMessage(ChatHelper.color(" &8>> &7Zakup ta usluge w naszym sklepie:"));
		sender.sendMessage(ChatHelper.color(" &8>> &2&lcoremax.pl."));
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &2&lSVIP &8) &8&m------------&8 "));
		return true;
	}
}
