package pl.kuezese.core.listener.impl;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.guild.Guild;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import panda.std.Option;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.DateHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.object.User;

import java.util.*;

public class PlayerChatListener extends Listener {

    private final FunnyGuilds funnyGuilds;
    private final Set<String> restrictedPhrases;

    public PlayerChatListener() {
        this.funnyGuilds = FunnyGuilds.getInstance();
        this.restrictedPhrases = new HashSet<>(Arrays.asList(".pl", ".eu", ",eu", "tasrv", ",ench", ".ench", ",pl", "\uff50", "\u24df", ",p l", ", p l", ".p l", ". p l"));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!this.core.getChatManager().isEnabled() && !p.hasPermission("cm.chat")) {
            if (this.core.getChatManager().isOnlyVip()) {
                if (!p.hasPermission("cm.chat.vip")) {
                    e.setCancelled(true);
                    p.sendMessage(ChatHelper.color(" &8>> &7Chat jest obecnie &c&nwylaczony&r"));
                    p.sendMessage(ChatHelper.color(" &8>> &7Aby pisac przy wylaczonym chacie zakup range &b&lSPONSOR&r"));
                    return;
                }
            } else {
                e.setCancelled(true);
                p.sendMessage(ChatHelper.color(" &8>> &7Chat jest obecnie &c&nwylaczony&r"));
                return;
            }
        }

        User user = this.userManager.get(p.getName());

        if (!p.hasPermission("cm.chat.admin") && !this.isGuildChat(p, e.getMessage())) {
//            if (this.core.getChatManager().isRequireLvl() && user.getLvl() < 2) {
//                p.sendMessage(ChatHelper.color("&8&m---------------------------------------"));
//                p.sendMessage(ChatHelper.color(" &8>> &7&oMusisz posiadac 2 poziom kopania aby moc pisac."));
//                p.sendMessage("");
//                p.sendMessage(ChatHelper.color(" &8• &7&oDbamy, aby czat pozbawiony byl &cniechcianych wiadomosci&7."));
//                p.sendMessage(ChatHelper.color(" &8• &7&oTwoj aktualny poziom kopania: &c" + user.getLvl()));
//                p.sendMessage(ChatHelper.color("&8&m---------------------------------------"));
//                e.setCancelled(true);
//                return;
//            }

            long muteTime = user.getMuteTime();
            if (muteTime > System.currentTimeMillis() || muteTime == -1) {
                p.sendMessage(ChatHelper.color(" &8>> &7Nie możesz pisać na chacie ponieważ jesteś &c&nwyciszony&7."));
                p.sendMessage(ChatHelper.color(" &8>> &7Pozostały czas wyciszenia &a" + (muteTime == -1 ? "na zawsze" : DateHelper.formatDateDiff(muteTime))));
                e.setCancelled(true);
                return;
            }

            if (user.getLastChat() > System.currentTimeMillis()) {
                e.setCancelled(true);
                p.sendMessage(ChatHelper.color(" &8>> &7Kolejną wiadomość będziesz mógł wysłać za &a" + (user.getLastChat() - System.currentTimeMillis()) / 1000 + "s&7."));
                return;
            } else {
                user.setLastChat(System.currentTimeMillis() + 5000L);
            }
        }

        String msg = e.getMessage();

        if (p.hasPermission("cm.chat.color")) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }

        net.luckperms.api.model.user.User permissionUser = this.core.getApi().getUserManager().getUser(p.getUniqueId());

        if (permissionUser != null) {
            String prefix = permissionUser.getCachedData().getMetaData().getPrefix();
            String format = (p.hasPermission("cm.helpop") ? this.core.getConfiguration().getAdminChatFormat() : this.core.getConfiguration().getGlobalChatFormat())
                    .replace("{PREFIX}", prefix == null ? "" : prefix)
                    .replace("{PLAYER}", "%1$s")
                    .replace("{MESSAGE}", "%2$s")
                    .replace("{CMPACK}", user.isUsingClient() ? "&a\u2713 " : "&8\u2716 ");

            e.setFormat(ChatHelper.color(format));
        }

        if (!p.hasPermission("cm.noslowmode")) {
            String lowerCaseMessage = msg.toLowerCase(Locale.ROOT);
            if (this.restrictedPhrases.stream().anyMatch(lowerCaseMessage::contains)) {
                e.setCancelled(true);
                p.sendMessage(e.getFormat());
                this.core.getServer().broadcast(ChatHelper.color(" &8>> &7Gracz &a" + p.getName() + " &7probowal zareklamowac serwer."), "cm.helpop");
                this.core.getServer().broadcast(ChatHelper.color(" &8>> &7Wiadomosc: &a" + e.getMessage() + "&7."), "cm.helpop");
            }
        }
    }

    private boolean isGuildChat(Player player, String message) {
        Option<net.dzikoysk.funnyguilds.user.User> userOption = this.funnyGuilds.getUserManager().findByPlayer(player);
        if (userOption.isEmpty())
            return false;

        net.dzikoysk.funnyguilds.user.User user = userOption.get();

        Option<Guild> guildOption = user.getGuild();
        if (guildOption.isEmpty())
            return false;

        if (this.sendMessageToAllGuilds(message)) {
            return true;
        }

        if (this.sendMessageToGuildAllies(message)) {
            return true;
        }

        return this.sendMessageToGuildMembers(message);
    }

    private boolean sendMessageToGuildMembers(String message) {
        return this.sendMessageToGuilds(funnyGuilds.getPluginConfiguration().chatPriv, message);
    }

    private boolean sendMessageToGuildAllies(String message) {
        return this.sendMessageToGuilds(funnyGuilds.getPluginConfiguration().chatAlly, message);
    }

    private boolean sendMessageToAllGuilds(String message) {
        return this.sendMessageToGuilds(funnyGuilds.getPluginConfiguration().chatGlobal, message);
    }

    private boolean sendMessageToGuilds(String prefix, String message) {
        int prefixLength = prefix.length();
        return message.length() > prefixLength && message.substring(0, prefixLength).equalsIgnoreCase(prefix);
    }
}