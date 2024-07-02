package pl.kuezese.core.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;

@RequiredArgsConstructor
public class SafeMenu {

    private final CorePlugin core;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 27, ChatHelper.color("&8>> &aSejfy &8<<"));

        ItemStack safe1 = new ItemMaker(Material.CHEST)
                .setName(ChatHelper.color(" &8>> &7Sejf #1"))
                .addLore("")
                .addLore(ChatHelper.color(" &8>> &7Posiadasz uprawnienia: &" + (p.hasPermission("cm.safe.1") ? "a✓" : "c✘")))
                .make();

        ItemStack safe2 = new ItemMaker(Material.CHEST)
                .setName(ChatHelper.color(" &8>> &7Sejf #2"))
                .addLore("")
                .addLore(ChatHelper.color(" &8>> &7Posiadasz uprawnienia: &" + (p.hasPermission("cm.safe.2") ? "a✓" : "c✘")))
                .make();

        ItemStack safe3 = new ItemMaker(Material.CHEST)
                .setName(ChatHelper.color(" &8>> &7Sejf #3"))
                .addLore("")
                .addLore(ChatHelper.color(" &8>> &7Posiadasz uprawnienia: &" + (p.hasPermission("cm.safe.3") ? "a✓" : "c✘")))
                .make();

        ItemStack safe4 = new ItemMaker(Material.CHEST)
                .setName(ChatHelper.color(" &8>> &7Sejf #4"))
                .addLore("")
                .addLore(ChatHelper.color(" &8>> &7Posiadasz uprawnienia: &" + (p.hasPermission("cm.safe.4") ? "a✓" : "c✘")))
                .make();

        ItemStack safe5 = new ItemMaker(Material.ENDER_CHEST)
                .setName(ChatHelper.color(" &8>> &5EnderChest"))
                .addLore("")
                .addLore(ChatHelper.color(" &8>> &7Posiadasz uprawnienia: &" + (p.hasPermission("cm.enderchest") ? "a✓" : "c✘")))
                .make();

        inv.setItem(11, safe1);
        inv.setItem(12, safe2);
        inv.setItem(13, safe3);
        inv.setItem(14, safe4);
        inv.setItem(15, safe5);

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
