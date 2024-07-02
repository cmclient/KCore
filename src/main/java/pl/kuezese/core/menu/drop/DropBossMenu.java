package pl.kuezese.core.menu.drop;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class DropBossMenu {

    private final CorePlugin core;
    private final Boss boss;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 54, ChatHelper.color("&8>> &aDrop z Bossa " + this.boss.getName() + " &8<<"));
        int[] allowed = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43, 46, 47, 48, 49, 50, 51, 52 };
        AtomicInteger i = new AtomicInteger();

        this.boss.getDrops().forEach((is, chance) -> {
            ItemMaker builder = new ItemMaker(is).addLore(ChatHelper.color(" &8>> &7Szansa: &a" + chance + "%"));
            inv.setItem(allowed[i.getAndIncrement()], builder.make());
        });

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
