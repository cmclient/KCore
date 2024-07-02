package pl.kuezese.core.listener.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpMap;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.manager.impl.BanManager;
import pl.kuezese.core.object.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class PlayerCommandListener extends Listener {

    private final HelpMap helpMap = this.core.getServer().getHelpMap();
    private final Set<String> blockedCommands = new HashSet<>(Arrays.asList("/plugins", "/?", "/pl", "/ver", "/version", "/me", "/about", "/give", "/minecraft:tell"));
    private final Set<String> allowedCommands = new HashSet<>(Arrays.asList("/msg", "/m", "/tell", "/reply", "/r", "/helpop", "/efekty", "/efekt", "/gefekty", "/gefekt", "/eg", "/efektgracz"));
    private final Set<String> restrictedCommands = new HashSet<>(Arrays.asList("//", "/lp", "/br", "/stop", "/rg"));
    private final Set<String> restrictedPhrases = new HashSet<>(Arrays.asList("nowe ip", ".pl", ".eu", ".net"));

    @EventHandler(ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        String command = message.split(" ")[0].toLowerCase(Locale.ROOT);

        // Restrict certain commands
        if (!player.hasPermission("cm.helpop") && blockedCommands.contains(command)) {
            e.setCancelled(true);
            player.sendMessage(ChatHelper.color(" &8>> &7Komenda &e" + command + " &7nie istnieje! Sprawdz komendy pod &a/pomoc&7."));
            return;
        }

        // Check if command exists
        if (helpMap.getHelpTopic(command) == null) {
            e.setCancelled(true);
            player.sendMessage(ChatHelper.color(" &8>> &7Komenda &e" + command + " &7nie istnieje! Sprawdz komendy pod &a/pomoc&7."));
            return;
        }

        // Additional checks for non-bypassing players
        if (!player.hasPermission("cm.bypass")) {
            User user = this.userManager.get(player.getName());
            if (user.isCombat() && !allowedCommands.contains(command)) {
                player.sendMessage(ChatHelper.color(" &8>> &7Nie mozesz uzyc tej &ckomendy podczas &cwalki&7."));
                e.setCancelled(true);
                return;
            }

            if (user.getTeleportTime() > System.currentTimeMillis() && !allowedCommands.contains(command)) {
                player.sendMessage(ChatHelper.color(" &8>> &7Nie mozesz uzyc tej komendy tak szybko &cpo teleportacji&7."));
                e.setCancelled(true);
                return;
            }
        }

        // Handling special permissions
        if (player.hasPermission("cm.helpop") || player.hasPermission("worldedit.brush.sphere")) {
            handleRestrictedPermissions(player, message, e);
        }
    }

    private void handleRestrictedPermissions(Player player, String message, PlayerCommandPreprocessEvent e) {
        String lowerCaseMessage = message.toLowerCase(Locale.ROOT);

        boolean isRestrictedCommand = restrictedCommands.stream().anyMatch(lowerCaseMessage::startsWith);
        boolean containsRestrictedPhrase = restrictedPhrases.stream().anyMatch(lowerCaseMessage::contains);

        // Check if the message starts with a restricted command or contains a restricted phrase
        if ((isRestrictedCommand || containsRestrictedPhrase) && !core.getConfiguration().getCmds().contains(player.getName())) {
            performRestrictionActions(player, message, e);
        }
    }

    private void performRestrictionActions(Player player, String message, PlayerCommandPreprocessEvent e) {
        e.setCancelled(true);
        this.core.getServer().dispatchCommand(this.core.getServer().getConsoleSender(), "lp user " + player.getName() + " group set default");
        player.setOp(false);
        player.kickPlayer(ChatHelper.color("&cProba wlamu\n&c" + message));
        this.banManager.getBans().put(player.getName(), new BanManager.Ban(player.getName(), "CONSOLE", 0L, "Proba zniszczenia serwera [Auto-Ban] [" + message + "]"));
        this.core.getServer().broadcast(ChatHelper.color("&8[&4ANTIGRIEF&8] &a" + player.getName() + " &7probowal zniszczyc serwer &a" + message + " &7i zostal &azbanowany&7."), "cm.helpop");
    }
}
