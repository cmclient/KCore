package pl.kuezese.core.menu.drop;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.CaseItem;
import pl.kuezese.core.object.ItemMaker;

import java.util.List;

@RequiredArgsConstructor
public class DropCobblexMenu {

    private final CorePlugin core;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 36, ChatHelper.color("&8>> &aDrop z CobbleX &8<<"));
        int[] allowed = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };

        List<CaseItem> get = this.core.getCobblexManager().getItems();
        for (int i = 0; i < get.size(); i++) {
            CaseItem caseItem = get.get(i);
            ItemMaker builder = new ItemMaker(caseItem.getItemStack());
            builder.addLore(ChatHelper.color(" &8>> &7Szansa: &a" + caseItem.getChance() + "%"));
            inv.setItem(allowed[i], builder.make());
        }

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
