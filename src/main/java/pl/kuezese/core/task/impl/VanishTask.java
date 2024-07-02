package pl.kuezese.core.task.impl;

import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;
import pl.kuezese.core.task.Task;

public class VanishTask extends Task {

	public VanishTask() {
		super(40L, 40L, false);
	}

	@Override
	public void run() {
		this.core.getServer().getOnlinePlayers().forEach(player -> {
			User user = this.userManager.get(player.getName());
			if (user.isVanished()) {
				ChatHelper.actionBar(player, "&8>> &7Jestes &aniewidoczny &7dla innych graczy");
				this.core.getServer().getOnlinePlayers().stream().filter(other -> other != player && !other.hasPermission("cm.helpop") && other.canSee(player)).forEach(other -> other.hidePlayer(player));
			}
		});
	}
}