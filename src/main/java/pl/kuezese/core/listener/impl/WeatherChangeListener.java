package pl.kuezese.core.listener.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.weather.WeatherChangeEvent;
import pl.kuezese.core.listener.Listener;

public class WeatherChangeListener extends Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onChange(WeatherChangeEvent e) {
		if (e.toWeatherState()) {
			e.setCancelled(true);
		}
	}
}
