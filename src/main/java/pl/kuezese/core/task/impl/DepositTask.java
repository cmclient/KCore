package pl.kuezese.core.task.impl;

import net.md_5.bungee.api.ChatColor;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.ItemHelper;
import pl.kuezese.core.helper.StringHelper;
import pl.kuezese.core.object.User;
import pl.kuezese.core.task.Task;

public class DepositTask extends Task {

    public DepositTask() {
        super(  100L, 100L, true);
    }

    public void run() {
        this.core.getServer().getOnlinePlayers().forEach(p -> {
            User user = this.userManager.get(p.getName());

            this.core.getLimitManager().getLimits().stream().filter(limit -> limit.getLimit() != 0).forEach(limit -> {
                int amount = ItemHelper.getAmount(p, limit.getItem(), limit.getData(), limit.getRequiredName());

                if (amount > limit.getLimit()) {
                    int remove = amount - limit.getLimit();
                    int val = user.getLimits().getOrDefault(limit.getId(), 0);
                    user.getLimits().put(limit.getId(), val + remove);
                    ItemHelper.remove(p, limit.getItem(), limit.getData(), limit.getRequiredName(), remove);
                    p.updateInventory();
                    p.sendMessage(ChatHelper.color(" &8>> &7Posiadasz przy sobie wiecej niz &a" + limit.getLimit() + " " + StringHelper.stripSpecialChars(ChatColor.stripColor(limit.getDisplayName())) + "&7. Nadmiar &a" + remove + " &7zostaje przeniesiony do &a/depozyt&7."));
                }
            });
        });
    }
}
