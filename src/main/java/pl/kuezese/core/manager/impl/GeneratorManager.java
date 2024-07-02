package pl.kuezese.core.manager.impl;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.kuezese.core.manager.Manager;
import pl.kuezese.core.object.StoneGenerator;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@Getter
public class GeneratorManager extends Manager {

	private final Map<Location, StoneGenerator> generators = new ConcurrentHashMap<>();

	public StoneGenerator add(StoneGenerator generator) {
		return generators.put(generator.getLoc(), generator);
	}

	public void remove(Location loc, Player player) {
		StoneGenerator generator = this.generators.get(loc);

		if (generator != null) {
			generator.destroy(this.core, player);
			this.generators.remove(loc);
		}
	}

	public void load() {
		this.core.getSql().query("SELECT * FROM `stone_generator`", rs -> {
			try {
				while (rs.next()) {
					StoneGenerator generator = new StoneGenerator(rs);
					this.generators.put(generator.getLoc(), generator);
				}
			} catch (SQLException ex) {
				this.core.getLogger().log(Level.SEVERE, "Failed to load generators!", ex);
			}
			this.core.getLogger().info("Loaded " + generators.size() + " stone generators");
		});
	}

	public StoneGenerator get(Location loc) {
		return generators.get(loc);
	}

	public Optional<StoneGenerator> find(Location loc) {
		return Optional.ofNullable(generators.get(loc));
	}
}
