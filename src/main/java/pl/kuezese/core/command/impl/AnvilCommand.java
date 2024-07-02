package pl.kuezese.core.command.impl;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.object.AnvilContainer;

public class AnvilCommand extends Command {

    public AnvilCommand() {
        super("anvil", "cm.anvil", "kowadlo");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        this.openAnvil(p);
        return true;
    }

    public void openAnvil(Player player) {
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        AnvilContainer container = new AnvilContainer(p);
        int c = p.nextContainerCounter();
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing"), 0));
        p.activeContainer = container;
        p.activeContainer.windowId = c;
        p.activeContainer.addSlotListener(p);
    }
}
