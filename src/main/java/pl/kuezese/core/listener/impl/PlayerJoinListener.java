package pl.kuezese.core.listener.impl;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.DateHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.manager.impl.BanManager;
import pl.kuezese.core.manager.impl.KitManager;
import pl.kuezese.core.object.User;

public class PlayerJoinListener extends Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(ChatHelper.color(" &8[&a+&8] &7Gracz &a" + player.getName() + " &7dolaczyl na serwer."));

        User user = this.userManager.get(player.getName());
        if (user == null) this.userManager.add(player.getName());

        player.setGameMode(GameMode.SURVIVAL);

        if ((player.isOp() || player.hasPermission("*")) && !core.getConfiguration().getOps().contains(player.getName())) {
            this.core.getServer().dispatchCommand(this.core.getServer().getConsoleSender(), "lp user " + player.getName() + " group set default");
            player.setOp(false);
            player.kickPlayer(ChatHelper.color(" &8>> &7Przykro mi, lecz nie mozesz posiadac opa wiec zostales automatycznie zbanowany."));
            this.banManager.getBans().put(player.getName(), new BanManager.Ban(player.getName(), "CONSOLE", 0L, "Proba zniszczenia serwera [Auto-Ban] [OP]"));
            return;
        }

        for (int i = 0; i < 12; ++i) player.sendMessage("");

        player.sendMessage(ChatHelper.color("&8&m-++-------------&r &2&lCORE&f&lMAX &8&m-------------++-&r"));
        player.sendMessage(ChatHelper.color("&8&l>> &7Witaj &f" + player.getName() + " &7na serwerze &2&lCORE&f&lMAX.PL &7na trybie &fMegaDrop&7."));
        player.sendMessage(ChatHelper.color("&8&l>> &fZyczymy Ci milej gry&7."));
        player.sendMessage("");
        player.sendMessage(ChatHelper.color("&a&l\u00bb &7Strona WWW: &awww.coremax.pl"));
        player.sendMessage(ChatHelper.color("&a&l\u00bb &7Discord: &9dc.coremax.pl"));
        player.sendMessage(ChatHelper.color("&8&m-++-------------&r &2&lCORE&f&lMAX &8&m-------------++-&r"));

        ChatHelper.title(player, "&8• &2&lCORE&f&lMAX.PL &8•", "&7Witamy na serwerze &cMegaDrop&7!", 10, 20, 10);

        if (!player.hasPlayedBefore()) {
            KitManager.Kit kit = core.getKitManager().getKits().get(0);
            if (kit != null) {
                ItemStack[] items = kit.getItems().stream().map(KitManager.Item::getItemStack).toArray(ItemStack[]::new);
                player.getInventory().addItem(items);
            }
//            RandomHelper.teleport(player, RandomHelper.randomLoc(player.getWorld()));
            Bukkit.broadcastMessage(ChatHelper.color(" &8>> &7Powitajmy nowego gracza: &a" + player.getName() + "&7!"));
        }

        String primaryGroup = this.core.getApi().getUserManager().getUser(player.getName()).getPrimaryGroup();
        if (primaryGroup.equals("default")) {
            this.core.getServer().dispatchCommand(this.core.getServer().getConsoleSender(),
                    "lp user " + player.getName() + " group set svip");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(ChatHelper.color(" &8[&c-&8] &7Gracz &a" + player.getName() + " &7wyszedl z serwera."));

        this.quit(player);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        this.quit(player);
    }

    private void quit(Player player) {
        User user = this.userManager.get(player.getName());
        user.setUsingClient(false);
        user.update();

        if (user.isChecked()) {
            user.setChecked(false);
            if (this.banManager.get(player.getName()) == null) {
                this.banManager.getBans().put(player.getName(), new BanManager.Ban(player.getName(), "AntiLogout", 0L, "Wylogowanie sie podczas sprawdzania [Auto-Ban]"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8\u00bb &7Gracz &c" + player.getName() + " &7zostal zbanowany za &cwylogowanie sie podczas sprawdzania&7."));
                this.core.getServer().broadcastMessage("");
            }
            return;
        }

        if (user.isCombat()) {
            player.setHealth(0.0D);

            if (user.getDamager() != null && user.getDamager() != player) {
                User damagerUser = this.userManager.get(user.getDamager().getName());
                damagerUser.setAttackTime(0L);
                damagerUser.setDamager(null);
            }

            user.setAttackTime(0L);
            user.setDamager(null);

            this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + player.getName() + " &7wylogowal sie podczas walki."));
        }
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        BanManager.Ban ban = this.banManager.get(e.getName());
        if (ban != null) {
            if (ban.getTime() != 0L && ban.getTime() <= System.currentTimeMillis()) {
                this.banManager.getBans().remove(ban.getName());
                this.core.getSql().update("DELETE FROM `bans` WHERE `name`='" + ban.getName() + "'");
                return;
            }
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatHelper.color("&8>> &7Zostales zbanowany\n&8>> &7Przez admina: &c" + ban.getAdmin() + "\n&8>> &7Czas: &c" + ((ban.getTime() == 0L) ? "Na zawsze" : DateHelper.formatDateDiff(ban.getTime())) + "\n&8>> &7Powod: &c" + ban.getReason() + "&7.\n\n&8>> &7Unbana mozesz zakupic na: &awww.coremax.pl"));
        }
    }
}
