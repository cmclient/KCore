package pl.kuezese.core.command.impl;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Locale;

@SuppressWarnings("deprecation")
public class GameModeCommand extends Command {

	public GameModeCommand() {
		super("gamemode","cm.gamemode", "gm");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

        if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/gm <tryb>"));
        	return true;
        }

        GameMode mode = GameMode.getByValue(Integer.parseInt(args[0]));

        if (mode == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany tryb &anie istnieje&7."));
			return true;
		}

        p.setGameMode(mode);

		ChatHelper.title(p, "&8• &2&lGAMEMODE &8•", "&7Zmieniono tryb gry na &a" + mode.name().toLowerCase(Locale.ROOT), 10,40, 10);
		return true;
    }
}