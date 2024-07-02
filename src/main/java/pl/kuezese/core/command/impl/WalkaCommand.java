package pl.kuezese.core.command.impl;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.guild.Guild;
import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Map;
import java.util.WeakHashMap;

public class WalkaCommand extends Command {

    private final FunnyGuilds funnyGuilds;

    public WalkaCommand() {
        super("walka", "");
        this.funnyGuilds = FunnyGuilds.getInstance();
    }

    private final Map<String, Long> times = new WeakHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player player = sender instanceof Player ? ((Player) sender) : null;

        if (player == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        User user = this.funnyGuilds.getUserManager().findByUuid(player.getUniqueId()).get();
        Guild guild = user.getGuild().orNull();

        if (guild == null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz &agildii&7."));
            return true;
        }

        if (guild.getOwner() != user) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Nie jestes &aliderem&7."));
            return true;
        }

        if (times.containsKey(sender.getName()) && times.get(sender.getName()) > System.currentTimeMillis()){
            long time = times.get(sender.getName());
            sender.sendMessage(ChatHelper.color(" &8>> &7Kolejny raz komende &c/walka &7bedziesz mogl uzyc za: &c" + (time - System.currentTimeMillis()) / 1000 + "s&7."));
            return true;
        }

        times.put(sender.getName(), System.currentTimeMillis() + 30000L);

        this.core.getServer().broadcastMessage(ChatHelper.color("&8&m-----&r &2&lWALKA &8&m-----&r"));
        this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gildia &8[&2" + guild.getTag() + "&8] &8- &2" + guild.getName() + " &7zaprasza na &awalke&7."));
        this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Koordynaty gildii: X: &a" + guild.getRegion().get().getCenter().getBlockX() + "&7, Z: &a" + guild.getRegion().get().getCenter().getBlockZ() + "&a."));
        this.core.getServer().broadcastMessage(ChatHelper.color("&8&m-----&r &2&lWALKA &8&m-----&r"));
        return true;
    }
}
