package pl.kuezese.core.task.impl;

import org.bukkit.Sound;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;
import pl.kuezese.core.task.Task;

public class CheckTask extends Task {

    public CheckTask() {
        super(20L, 20L, true);
    }

    @Override
    public void run() {
        this.core.getServer().getOnlinePlayers().forEach(player -> {
            User user = this.userManager.get(player.getName());
            if (user.isChecked()) {
                ChatHelper.title(player, "&8• &2&lSPRAWDZANIE &8•", "&cJestes sprawdzany, podaj AnyDeska na /helpop", 5, 10, 5);
                ChatHelper.actionBar(player, "&cJestes sprawdzany, masz 2 minuty na podanie AnyDeska na /helpop");
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
            }
        });
    }
}

