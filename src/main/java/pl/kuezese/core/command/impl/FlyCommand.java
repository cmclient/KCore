package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.region.object.Region;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class FlyCommand extends Command {

	private final Region spawn;

	public FlyCommand() {
		super("fly", "cm.fly");
		this.spawn = this.core.getRegionManager().find("spawn", this.core.getServer().getWorlds().get(0));
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (!p.hasPermission("cm.helpop") && this.core.getRegionManager().find(p.getLocation()) != this.spawn) {
			p.sendMessage(ChatHelper.color(" &8>> &7Fly mozesz uzywac tylko na &a/spawn&7."));
//			ChatHelper.title(p, "&8• &2&lFLY &8•", "&cFly mozesz uzywac tylko na /spawn", 10,40, 10);
			return true;
		}

		if (args.length == 0) {
			p.setAllowFlight(!p.getAllowFlight());
			ChatHelper.title(p, "&8• &2&lFLY &8•", "&7Zmieniono tryb &alatania &7na &" + (p.getAllowFlight() ? "aWLACZONY" : "cWYLACZONY"), 10,40, 10);
			return true;
		}

        if (!p.hasPermission("cm.admin")) {
            p.sendMessage(ChatHelper.color(" &8>> &7Nie mozesz zmieniac trybu latania innym &agraczom&7."));
            return true;
        }

        Player other = this.core.getServer().getPlayer(args[0]);

        if (other == null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
            return true;
        }

        other.setAllowFlight(!other.getAllowFlight());
        ChatHelper.title(p, "&8• &2&lFLY &8•", "&7Zmieniono tryb &alatania &7gracza &a" + other.getName() + " &7na &" + (other.getAllowFlight() ? "aWLACZONY" : "cWYLACZONY"), 10,40, 10);
        ChatHelper.title(other, "&8• &2&lFLY &8•", "&7Zmieniono tryb &alatania &7na &" + (p.getAllowFlight() ? "aWLACZONY" : "cWYLACZONY") + " &7przez &7" + p.getName(), 10,40, 10);
        return true;
	}
}