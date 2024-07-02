package pl.kuezese.core.menu.drop;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.manager.impl.DropManager;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;

import java.util.List;

@RequiredArgsConstructor
public class DropStoneMenu {

    private final CorePlugin core;

    public void show(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 54, ChatHelper.color("&8>> &aDrop z kamienia &8<<"));
        User user = this.core.getUserManager().get(p.getName());

        int[] allowed = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };

        List<DropManager.Drop> get = this.core.getDropManager().getDrops();
        int i;
        for (i = 0; i < get.size(); i++) {
            DropManager.Drop drop = get.get(i);
            ItemMaker builder = new ItemMaker(drop.getItem(), 1, drop.getData());
            builder.addLore(ChatHelper.color(" &8>> &7Szansa: &a" + drop.getChance() + "%"));
            builder.addLore(ChatHelper.color(" &8>> &7Aktywny: &" + (drop.isEnabled(p.getName()) ? "a✓" : "c✘")));
            builder.addLore(ChatHelper.color(" &8>> &7Fortune: &a✓"));
            builder.addLore(ChatHelper.color(" &8>> &7EXP&7: &a" + drop.getXp()));
            if (user.isUsingClient()) {
                builder.addLore(" ");
                builder.addLore(ChatHelper.color(" &a✓ &7Posiadasz clienta &acmclient.pl"));
                builder.addLore(ChatHelper.color(" &a✓ &7Twoj drop zostal zwiekszony o &a20%&7!"));
            } else if (p.hasPermission("cm.drop.vip")) {
                builder.addLore(" ");
                builder.addLore(ChatHelper.color(" &a✓ &7Posiadasz range &a&lE&b&lL&c&lI&d&lT&e&lA"));
                builder.addLore(ChatHelper.color(" &a✓ &7Twoj drop zostal zwiekszony o &a20%&7!"));
            }
            builder.addLore(" ");
            builder.addLore(ChatHelper.color(" &8>> &aKliknij, aby zmienic status dropu."));
            inv.setItem(allowed[i], builder.make());
        }

        ItemMaker builder = new ItemMaker(Material.GOLD_NUGGET);
        builder.setName(ChatHelper.color("&8>> &aCoins &8(&a0.01-0.05$&8)"));
        builder.addLore(ChatHelper.color(" &8>> &7Szansa: &a15.0%"));
        builder.addLore(ChatHelper.color(" &8>> &7Aktywny: &" + (user.isDropCoins() ? "a✓" : "c✘")));
        builder.addLore(ChatHelper.color(" &8>> &7Fortune: &c✘"));
        builder.addLore(ChatHelper.color(" &8>> &7EXP&7: &a25"));

        if (user.isUsingClient()) {
            builder.addLore(" ");
            builder.addLore(ChatHelper.color(" &a✓ &7Posiadasz clienta &acmclient.pl"));
            builder.addLore(ChatHelper.color(" &a✓ &7Twoj drop zostal zwiekszony o &a20%&7!"));
            builder.addLore(" ");
        } else if (p.hasPermission("cm.drop.vip")) {
            builder.addLore(" ");
            builder.addLore(ChatHelper.color(" &a✓ &7Posiadasz clienta &acmclient.pl"));
            builder.addLore(ChatHelper.color(" &a✓ &7Twoj drop zostal zwiekszony o &a20%&7!"));
            builder.addLore(" ");
        }

        inv.setItem(allowed[i], builder.make());

        ItemStack enableAll = new ItemMaker(Material.STAINED_GLASS_PANE, 1, (short) 13).setName(ChatHelper.color(" &8>> &aWlacz wszystkie dropy")).make();
        ItemStack guildOnly = new ItemMaker(Material.STAINED_GLASS_PANE, 1, (short) 4).setName(ChatHelper.color(" &8>> &eWlacz dropy tylko na gildie")).make();
        ItemStack disableAll = new ItemMaker(Material.STAINED_GLASS_PANE, 1, (short) 14).setName(ChatHelper.color(" &8>> &cWylacz wszystkie dropy")).make();
        inv.setItem(41, enableAll);
        inv.setItem(42, guildOnly);
        inv.setItem(43, disableAll);

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
