package pl.kuezese.core.command.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.StringHelper;

public class TpAcceptCommand extends Command {

	public TpAcceptCommand() {
		super("tpaccept", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		String name = this.teleportManager.get(sender.getName());

		if (name == null || this.core.getServer().getPlayerExact(name) == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Nikt nie chce sie do Ciebie &aprzeteleportowac&7."));
			return true;
		}

		Player other = this.core.getServer().getPlayerExact(name);

		other.sendMessage(ChatHelper.color(" &8>> &7Gracz &a" + p.getName() + " &7zaakceptowal twoja prosbe o teleportacje."));
		p.sendMessage(ChatHelper.color(" &8>> &7Zaakceptowales prosbe o teleportacje gracza &a" + other.getName() + "&7."));

		this.teleportManager.remove(p.getName());

		if (this.core.getTeleportManager().getTeleports().containsKey(other.getName())) this.core.getTeleportManager().getTeleports().remove(other.getName()).cancel();

		other.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 160, 0, true));

		this.core.getTeleportManager().getTeleports().put(other.getName(), new BukkitRunnable() {

			private int timer = p.hasPermission("cm.fasterteleport") ? 5 : 6;

			@Override
			public void run() {
				timer--;
				ChatHelper.title(other, "&8• &2&lTELEPORTACJA &8•", "&7Zostaniesz przeteleportowany za &a" + timer + " &7" + StringHelper.pluralizedTimeText(timer) + ".", 0, 30, 0);
				ChatHelper.actionBar(p, "&8>> &7Teleportacja nastapi za &a" + timer + " &7" + StringHelper.pluralizedTimeText(timer) + ".");
				if (timer == 0) {
					other.removePotionEffect(PotionEffectType.CONFUSION);
					PaperLib.teleportAsync(other, p.getLocation()).thenAccept(result -> {
						ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostales przeteleportowany.", 10, 40, 10);
						ChatHelper.title(other, "&8• &2&lTELEPORTACJA &8•", "&7Przeteleportowano.", 10, 40, 10);
					});
					(core.getTeleportManager().getTeleports().remove(other.getName())).cancel();
				}
			}
		}.runTaskTimer(this.core, 0L, 20L));

		return true;
	}
}
