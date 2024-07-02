package pl.kuezese.core.menu.shop;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.manager.impl.VillagerManager;
import pl.kuezese.core.object.ItemMaker;

import java.util.List;

@RequiredArgsConstructor
public class ShopVillagerMenu {

    private final CorePlugin core;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 36, ChatHelper.color("&8>> &aVillagerzy &8<<"));
        int[] allowed = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

        List<VillagerManager.Villager> get = this.core.getVillagerManager().getVillagers();
        for (int i = 0; i < get.size(); i++) {
            VillagerManager.Villager villager = get.get(i);
            inv.setItem(allowed[i], villager.getItemStack());
        }

        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
