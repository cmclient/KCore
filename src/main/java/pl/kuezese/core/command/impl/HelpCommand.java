package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class HelpCommand extends Command {

	public HelpCommand() {
		super("help", "", "pomoc");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &2&lPOMOC &8) &8&m------------&8 "));
		sender.sendMessage(ChatHelper.color(" &8>> &a/vip &8- &7informacje o randze &6VIP."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/svip &8- &7informacje o randze &eSVIP."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/sponsor &8- &7informacje o randze &bSPONSOR."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/legenda &8- &7informacje o randze &1L&2E&3G&4E&5N&aD&7A."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/elita &8- &7informacje o randze &9E&8L&7I&aT&5A."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/yt &8- &7informacje o randze &4Y&fT."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/yt+ &8- &7informacje o randze &4Y&fT+."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/tiktok &8- &7informacje o randze &0Tik&fTok."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/sklep &8- &7sklep za coins."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/gildie &8- &7komendy gildii."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/efekty &8- &7menu efektow."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/eg &8- &7efekty gracza."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/gefekty &8- &7efekty gildii."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/gracz <nick> &8- &7statystyki gracza."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/helpop <wiadomosc> &8- &7kontakt z administracja."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/enderchest &8- &7podreczny enderchest."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/kosz &8- &7kosz na niepotrzebne przedmioty."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/craftingi &8- &7dostepne craftingi."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/drop &8- &7drop z kamienia, premium case etc."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/pay &8- &7przelewasz coins do gracza."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/sejf &8- &7sejf."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/ruletka &8- &7ruletka."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/statystyki &8- &7twoje statystyki."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/gamma &8- &7full bright."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/lobby &8- &7powrot na lobby."));
		sender.sendMessage("");
		sender.sendMessage(ChatHelper.color(" &8>> &7Sklep: &awww.coremax.pl"));
		sender.sendMessage(ChatHelper.color(" &8>> &7Discord: &adc.coremax.pl"));
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &2&lPOMOC &8) &8&m------------&8 "));
		return true;
	}
}
