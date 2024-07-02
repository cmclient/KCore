package pl.kuezese.core.command.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class SpawnCommand extends Command {

	public SpawnCommand() {
		super("spawn", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (p.hasPermission("cm.spawn.admin")) {
			PaperLib.teleportAsync(p, this.core.getConfiguration().getSpawnLoc())
					.thenAccept(result -> ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostales &aprzeteleportowany&7.", 10, 40, 10));
            return true;
		}

		User user = this.userManager.get(p.getName());

		if (user.isChecked()) {
			ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&cNie mozesz przeteleportowac sie podczas sprawdzania.", 10, 20, 10);
			return true;
		}

		if (this.core.getTeleportManager().getTeleports().containsKey(p.getName())) {
			this.core.getTeleportManager().getTeleports().remove(p.getName()).cancel();
		}

		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 160, 0, true));

		this.core.getTeleportManager().getTeleports().put(p.getName(), new BukkitRunnable() {

			private int timer = p.hasPermission("cm.fasterteleport") ? 5 : 6;

			@Override
			public void run() {
				timer--;
				ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostaniesz &aprzeteleportowany &7za &a" + timer + " &7sekund.", 0, 30, 0);
				if (timer == 0) {
					p.removePotionEffect(PotionEffectType.CONFUSION);
					PaperLib.teleportAsync(p, core.getConfiguration().getSpawnLoc())
							.thenAccept(result -> ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostales &aprzeteleportowany&7.", 10, 40, 10));
					(core.getTeleportManager().getTeleports().remove(p.getName())).cancel();
				}
			}
		}.runTaskTimer(this.core, 0L, 20L));
		return true;
	}
}
