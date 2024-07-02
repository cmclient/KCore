package pl.kuezese.core.helper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.object.ItemMaker;

import java.util.stream.IntStream;

public final class InventoryHelper {

    private static final ItemStack GLASS = new ItemMaker(Material.STAINED_GLASS_PANE, 1, (short) 15)
            .setName(" ")
            .make();

    public static void backgroundEmpty(Inventory inv) {
        ItemStack[] contents = inv.getContents();
        IntStream.range(0, inv.getSize()).filter(i -> contents[i] == null).forEach(i -> inv.setItem(i, GLASS));
    }

    public static void showKit(Player p, String name, boolean modifiable, ItemStack... items) {
        Inventory inv = Bukkit.createInventory(modifiable ? p : null, 36, name);
        inv.addItem(items);
        p.openInventory(inv);
    }
}
