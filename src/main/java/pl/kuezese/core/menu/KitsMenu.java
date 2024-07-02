package pl.kuezese.core.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.DateHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.manager.impl.KitManager;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class KitsMenu {

    private final CorePlugin core;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aZestawy &8<<"));
        User user = this.core.getUserManager().get(p.getName());
        int[] allowed = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        List<KitManager.Kit> get = this.core.getKitManager().getKits();
        for (int i = 0; i < get.size(); i++) {
            KitManager.Kit kit = get.get(i);
            ItemMaker builder = new ItemMaker(kit.getItem()).setName(kit.getName());
            builder.addLore(ChatHelper.color(" &8>> &7Czas odnowienia: &a" + String.format("%d godzin %d minut %d sekund",
                    TimeUnit.MILLISECONDS.toHours(kit.getCooldown()),
                    TimeUnit.MILLISECONDS.toMinutes(kit.getCooldown()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(kit.getCooldown())),
                    TimeUnit.MILLISECONDS.toSeconds(kit.getCooldown()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(kit.getCooldown()))
            )));
            builder.addLore(ChatHelper.color(" &8>> &7Posiadasz uprawnienia: &" + (p.hasPermission(kit.getPermission()) ? "a✓" : "c✘")));
            long cooldown = user.getKitCooldown(kit.getSimpleName());
            builder.addLore(ChatHelper.color(" &8>> &7Mozesz odebrac: &" + (cooldown == 0L || System.currentTimeMillis() > cooldown ? "a✓" : "eza " + DateHelper.formatDateDiff(cooldown))));
            builder.addLore("");
            builder.addLore(ChatHelper.color(" &8>> &aKliknij LPM, aby odebrac zestaw"));
            builder.addLore(ChatHelper.color(" &8>> &aKliknij PPM, aby wyswietlic zestaw"));
            inv.setItem(allowed[i], builder.make());
        }

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
