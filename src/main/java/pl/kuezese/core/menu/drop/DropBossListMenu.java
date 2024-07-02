package pl.kuezese.core.menu.drop;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class DropBossListMenu {

    private final CorePlugin core;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 27, ChatHelper.color("&8>> &aDrop z Bossow &8<<"));
        int[] allowed = {10, 11, 12, 13, 14, 15, 16};

        Collection<Boss> bosses = this.core.getBossManager().getBosses().values();
        AtomicInteger ai = new AtomicInteger();

        for (Boss boss : bosses) {
            ItemMaker builder = new ItemMaker(Material.SKULL_ITEM, 1, (short) 1).setName(ChatHelper.color(" &8>> " + boss.getName()));
            inv.setItem(allowed[ai.getAndIncrement()], builder.make());
        }

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
