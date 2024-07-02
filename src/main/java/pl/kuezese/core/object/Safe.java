package pl.kuezese.core.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.ItemHelper;

@Getter
@Setter
@RequiredArgsConstructor
public class Safe {

    private final int id;
    private ItemStack[] items;

    public void open(Player p) {
        if (items == null) {
            items = new ItemStack[54];
        }
        Inventory inv = Bukkit.createInventory(p, 54, ChatHelper.color("&8>> &7Sejf &8(&a" + id + "&8) &8<<"));
        inv.setContents(items);
        p.openInventory(inv);
    }

    public String serialize() {
        if (items == null)
            return "";

        return ItemHelper.serialize(items);
    }

    public void deserialize(String data) {
        items = ItemHelper.deserialize(data);
    }
}
