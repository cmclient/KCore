package pl.kuezese.core.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.manager.impl.WarpManager;
import pl.kuezese.core.object.ItemMaker;

import java.util.List;

public class WarpMenu {

    private final CorePlugin core;

    public WarpMenu(CorePlugin core) {
        this.core = core;
    }

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 27, ChatHelper.color("&8>> &aWarpy &8<<"));
        int[] allowed = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        List<WarpManager.Warp> get = this.core.getWarpManager().getWarps();
        for (int i = 0; i < get.size(); i++) {
            WarpManager.Warp warp = get.get(i);
            ItemMaker builder = new ItemMaker(warp.getItem()).setName(ChatHelper.color("&8>> &a" + warp.getName()));
            builder.addLore(ChatHelper.color(" &8>> &aKliknij, aby przeteleportowac"));
            inv.setItem(allowed[i], builder.make());
        }

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
