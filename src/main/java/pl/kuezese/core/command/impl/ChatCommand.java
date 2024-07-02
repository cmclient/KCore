package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Locale;

public class ChatCommand extends Command {

	public ChatCommand() {
		super("chat", "cm.chat");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/chat <cc/on/off/vip>"));
			return true;
		}

		switch (args[0].toLowerCase(Locale.ROOT)) {
			case "clear":
			case "cc": {
				for (int i = 0; i < 100; i++) this.core.getServer().broadcastMessage("");
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Chat serwera zostal: &a&nwyczyszczony&r"));
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Przez: &a" + sender.getName()));
				this.core.getServer().broadcastMessage("");
				break;
			}
			case "on": {
				this.chatManager.setEnabled(true);
				this.chatManager.setOnlyVip(false);
				this.core.getServer().broadcastMessage("");
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Chat serwera zostal: &a&nwlaczony&r"));
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Przez: &a" + sender.getName()));
				this.core.getServer().broadcastMessage("");
				break;
			}
			case "off": {
				this.chatManager.setEnabled(false);
				this.chatManager.setOnlyVip(false);
				this.core.getServer().broadcastMessage("");
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Chat serwera zostal: &a&nwylaczony&r"));
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Przez: &a" + sender.getName()));
				this.core.getServer().broadcastMessage("");
				break;
			}
			case "vip": {
				this.chatManager.setEnabled(false);
				this.chatManager.setOnlyVip(true);
				this.core.getServer().broadcastMessage("");
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Chat serwera dla rangi &aSPONSOR &7zostal: &a&nwlaczony&r"));
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Przez: &7" + sender.getName()));
				this.core.getServer().broadcastMessage("");
				break;
			}
			case "lvl": {
				this.chatManager.setRequireLvl(!this.chatManager.isRequireLvl());
				this.core.getServer().broadcastMessage("");
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Wymagany poziom 2 na pisanie na chat zostal: &" + (this.chatManager.isRequireLvl() ? "a&nwlaczony" : "c&nwylaczony") + "&r"));
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Przez: &a" + sender.getName()));
				this.core.getServer().broadcastMessage("");
				break;
			}
			default: {
				sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/chat <cc/on/off/vip/lvl>"));
			}
		}
		return true;
	}
}