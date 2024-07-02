package pl.kuezese.core.menu.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;

public class ShopMenu {

    private final CorePlugin core;

    private final ItemStack coinsShop = new ItemMaker(Material.NETHER_STAR)
            .setName(ChatHelper.color("&8>> &7Sklep za &acoins"))
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby przejsc do menu kupna."))
            .make();
    private final ItemStack emeraldShop = new ItemMaker(Material.EMERALD)
            .setName(ChatHelper.color("&8>> &7Sklep za &aemeraldy"))
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby przejsc do menu kupna."))
            .make();
    private final ItemStack levelShop = new ItemMaker(Material.EXP_BOTTLE)
            .setName(ChatHelper.color("&8>> &7Sklep za &2level"))
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby przejsc do menu kupna."))
            .make();
    private final ItemStack villager = new ItemMaker(Material.MONSTER_EGG, 1, (short) 120)
            .setName(ChatHelper.color("&8>> &7Przenosni &avillagerzy"))
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby przejsc do menu kupna."))
            .make();
    private final ItemStack sell = new ItemMaker(Material.WOOL, 1, (short) 14)
            .setName(ChatHelper.color("&8>> &7Sprzedawanie"))
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby przejsc do menu sprzedazy."))
            .make();
    private final ItemStack info = new ItemMaker(Material.BOOK)
            .setName(ChatHelper.color("&8>> &7Przelew coinsow innemu graczowi:"))
            .addLore(ChatHelper.color(" &8>> &7Mozesz przesylac coinsy innym graczom"))
            .addLore(ChatHelper.color(" &8>> &7poprzez komende: &a/pay <nick> <ilosc>&7."))
            .make();

    public ShopMenu(CorePlugin core) {
        this.core = core;
    }

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 27, ChatHelper.color("&8>> &aSklep &8<<"));

        inv.setItem(10, coinsShop);
        inv.setItem(11, emeraldShop);
        inv.setItem(12, levelShop);
        inv.setItem(13, villager);
        inv.setItem(14, sell);
        inv.setItem(16, info);

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
