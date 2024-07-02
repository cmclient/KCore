package pl.kuezese.core.task.impl;

import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;
import pl.kuezese.core.task.Task;

public class CombatTask extends Task {

    public CombatTask() {
        super(20L, 20L, true);
    }

    @Override
    public void run() {
        this.core.getServer().getOnlinePlayers().forEach(p -> {
            User user = this.userManager.get(p.getName());
            long time = System.currentTimeMillis();

            if (user.getAttackTime() > time) {
                if (user.getTeleportTime() > time) {
                    ChatHelper.actionBar(p, "&8>> &eKomendy po teleportacji zablokowane na: &7" + (int) ((user.getTeleportTime() - time) / 1000) + "s &8| &4&lANTYLOGOUT &8(&4" + (int) ((user.getAttackTime() - time) / 1000) + "s&8) &8<<");
                } else {
                    ChatHelper.actionBar(p, "&8>> &4&lANTYLOGOUT &8(&4" + (int) ((user.getAttackTime() - time) / 1000) + "s&8) &8<<");
                }
            } else if (user.isCombat()) {
                user.setDamager(null);
                ChatHelper.actionBar(p, "&8>> &7Mozesz sie juz wylogowac &8<<");
            } else if (user.getTeleportTime() > time) {
                ChatHelper.actionBar(p, "&8>> &eKomendy po teleportacji zablokowane na: &7" + (int) ((user.getTeleportTime() - time) / 1000) + "s &8<<");
            }
        });
    }
}
