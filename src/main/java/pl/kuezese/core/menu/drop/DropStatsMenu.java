package pl.kuezese.core.menu.drop;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;

@RequiredArgsConstructor
public class DropStatsMenu {

    private final CorePlugin core;

    public void show(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 9, ChatHelper.color("&8>> &aStatystyki &8<<"));
        User user = this.core.getUserManager().get(p.getName());
        inv.setItem(3, new ItemMaker(Material.DIAMOND_PICKAXE).setName(ChatHelper.color("&8>> &7Poziom: &a" + user.getLvl())).make());
        inv.setItem(5, new ItemMaker(Material.EXP_BOTTLE).setName(ChatHelper.color("&8>> &7XP: &a" + user.getXp() + "&7/&a" + (user.getLvl() + 1) * (user.getLvl() * 2500))).make());
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
