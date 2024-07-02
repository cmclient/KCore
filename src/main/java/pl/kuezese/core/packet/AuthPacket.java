package pl.kuezese.core.packet;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class AuthPacket implements PluginMessageListener {

    private final CorePlugin core;
    private final String apiVersion;

    public AuthPacket(CorePlugin core) {
        this.core = core;
        this.apiVersion = "1.1.1";
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (bytes.length > 0) {
            String[] split = new String(bytes).split("\u0000");
            if (isAuthorized(split, player)) {
                handleAuthorizedPlayer(player);
                return;
            }
        }
        player.kickPlayer("[CMCLIENT] Authorization error!");
    }

    private boolean isAuthorized(String[] split, Player player) {
        return split.length >= 2 &&
                split[0].equalsIgnoreCase(player.getName()) &&
                split[1].equalsIgnoreCase(this.apiVersion);
    }

    private void handleAuthorizedPlayer(Player player) {
        User user = core.getUserManager().get(player.getName());
        user.setUsingClient(true);
        core.getServer().getScheduler().runTaskLaterAsynchronously(core,
                () -> ChatHelper.title(player, "&aTwoj drop zostal zwiekszony",
                        "&a✓ Posiadasz clienta CMClient.pl", 10, 20, 30), 40L);
    }
}
