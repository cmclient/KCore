package pl.kuezese.core.command.impl;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class UnlagCommand extends Command {

    public UnlagCommand() {
        super("unlag", "cm.unlag", "clearlag");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        AtomicInteger ai = new AtomicInteger();

        this.core.getServer().getWorlds()
                .stream()
                .map(World::getEntities)
                .flatMap(Collection::stream)
                .filter(entity -> !(entity instanceof Player))
                .filter(entity -> !(entity instanceof ArmorStand))
                .forEach(entity -> {
                    entity.remove();
                    ai.getAndIncrement();
                });

        sender.sendMessage(ChatHelper.color(" &8>> &7Usunieto &a" + ai.get() + " entity &7z &a" + this.core.getServer().getWorlds().size() + " swiatow&7."));
        return true;
    }
}
