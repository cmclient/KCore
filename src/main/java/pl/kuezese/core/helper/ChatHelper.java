package pl.kuezese.core.helper;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class ChatHelper {

	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s)
				.replace(">>", "»")
				.replace("<<", "«")
				.replace("\\n", "\n");
	}

	public static List<String> color(List<String> s) {
		return s.stream().map(ChatHelper::color).collect(Collectors.toList());
	}

	public static void title(Player p, String up, String down, int i, int j, int k) {
		IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + color(up) + "\"}");
		IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + color(down) + "\"}");
		PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(i, j, k);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(subtitle);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(length);
	}

	public static void actionBar(Player p, String text) {
		IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + color(text) + "\"}");
		PacketPlayOutChat bar = new PacketPlayOutChat(bc, (byte)2);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(bar);
	}
}
