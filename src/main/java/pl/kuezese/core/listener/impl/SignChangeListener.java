package pl.kuezese.core.listener.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.listener.Listener;

public class SignChangeListener extends Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onChange(SignChangeEvent e) {
		if (e.getPlayer().hasPermission("cm.sign")) {
			String[] lines = e.getLines();
			for (int i = 0; i < lines.length; i++) {
				e.setLine(i, ChatHelper.color(lines[i]));
			}
		}
	}
}
