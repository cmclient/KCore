package pl.kuezese.core.listener.impl;

import pl.kuezese.core.listener.Listener;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerSneakListener extends Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (p.getSpectatorTarget() != null) {
            p.setGameMode(p.hasPermission("cm.gamemode") ? GameMode.CREATIVE : GameMode.SURVIVAL);
        }
    }
}
