package pl.kuezese.core.menu.drop;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.comparator.CoinsComparator;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DropTopCoinsMenu {

    private final CorePlugin core;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 9, ChatHelper.color("&8>> &aTop coins &8<<"));
        List<User> users = new ArrayList<>(this.core.getUserManager().get().values());
        users.sort(new CoinsComparator());

        for (int i = 1; i <= 9; i++) {
            if (i <= users.size()) {
                User user = users.get(users.size() - i);
                ItemStack head = new ItemStack(Material.SKULL_ITEM, i, (short) 3);
                SkullMeta skullmeta = (SkullMeta) head.getItemMeta();
                skullmeta.setOwner(user.getName());
                head.setItemMeta(skullmeta);
                ItemMeta im = head.getItemMeta();
                im.setDisplayName(ChatHelper.color("&8>> &2" + user.getName()));
                im.setLore(Collections.singletonList(ChatHelper.color("&8>> &7Coins: &a" + user.getCoinsFormatted() + "$")));
                head.setItemMeta(im);
                inv.setItem(i - 1, head);
            }
        }

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
