package pl.kuezese.core.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;

public class SettingsMenu {

    private final CorePlugin core;

    public SettingsMenu(CorePlugin core) {
        this.core = core;
    }

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 27, ChatHelper.color("&8>> &2Ustawienia &8<<"));
        User user = this.core.getUserManager().get(p.getName());
        ItemStack autoSell = new ItemMaker(Material.DOUBLE_PLANT)
                .setName(ChatHelper.color("&8>> &aAutomatyczna wymiana przedmiotow na coinsy"))
                .addLore(ChatHelper.color("&8>> &7Status: &" + (user.isAutoSell() ? "awlaczony" : "cwylaczony")))
                .make();
        inv.setItem(13, autoSell);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
