package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Collection;
import java.util.Locale;

public class IsCommand extends Command {

    public IsCommand() {
        super("is", "cm.itemshop");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie: &a/is <nick> <usluga>"));
            return true;
        }

        String nick = args[0];
        String service = args[1];

        Collection<? extends Player> players = this.core.getServer().getOnlinePlayers();
        switch (service.toLowerCase(Locale.ROOT)) {
            case "unban": {
                this.exec("unban {NICK}", nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &7Unban"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &7Unban", 10, 30, 10));
                break;
            }
            case "x16":
            case "x64":
            case "x128":
            case "x256":
            case "x512": {
                int amount = Integer.parseInt(service.substring(1));
                this.exec("pcase {NICK} " + amount, nick);
                this.exec("ppandora {NICK} " + amount, nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &7Pakiet x" + amount));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &7Pakiet x" + amount, 10, 30, 10));
                break;
            }
            case "vip": {
                this.exec("lp user {NICK} group set vip", nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &7SVip"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &aVip", 10, 30, 10));
                break;
            }
            case "svip": {
                this.exec("lp user {NICK} group set svip", nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &7SVip"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &5SVip", 10, 30, 10));
                break;
            }
            case "sponsor": {
                this.exec("lp user {NICK} group set sponsor", nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &bSponsor"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &3Sponsor", 10, 30, 10));
                break;
            }
            case "legenda": {
                this.exec("lp user {NICK} group set legenda", nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &1&lL&2&lE&3&lG&4&lE&5&lN&a&lD&7&lA"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &1&lL&2&lE&3&lG&4&lE&5&lN&a&lD&7&lA", 10, 30, 10));
                break;
            }
            case "elita": {
                this.exec("lp user {NICK} group set elita", nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &a&lE&b&lL&c&lI&d&lT&e&lA"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &a&lE&b&lL&c&lI&d&lT&e&lA", 10, 30, 10));
                break;
            }
            case "ovip": {
                this.exec("lp user {NICK} group set ovip", nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &a&lO&b&lV&c&lI&d&lP"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &a&lO&b&lV&c&lI&d&lP", 10, 30, 10));
                break;
            }
            case "1000-coins": {
                this.exec("addcoins {NICK} 1000.0", nick);
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + nick + "&r &7zakupil usluge &71000.0$"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Z sklepu &ahttps://coremax.pl"));
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &f&nDziekujemy za zakup"));
                this.core.getServer().broadcastMessage("");
                this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                players.forEach(p -> ChatHelper.title(p, "&8• &2&lITEMSHOP &8•", "&7Gracz &f&n" + nick + "&r &7zakupil usluge &71000.0$", 10, 30, 10));
                break;
            }
            default: {
                sender.sendMessage(ChatHelper.color(" &8>> &cPodales nieistniejaca usluge."));
                break;
            }
        }

        return true;
    }

    private void exec(String cmd, String nick) {
        this.core.getServer().dispatchCommand(this.core.getServer().getConsoleSender(), cmd.replace("{NICK}", nick));
    }
}
