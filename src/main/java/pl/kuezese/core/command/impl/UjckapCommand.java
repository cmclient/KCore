package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class UjckapCommand extends Command {

	public UjckapCommand() {
		super("ujckap", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		ChatHelper.title(p, "&8• &2&lE E E &8•", "&a&lUJCKAP", 10, 20, 10);
		ChatHelper.actionBar(p, "&8>> &a&lJP MYSZARD JP DAWID JP AREK JP WOJCIK");
		p.sendMessage(ChatHelper.color(" &8>> &aJak ja lubie tego ujckapa on jest zajebisty!"));
		return true;
	}
}
