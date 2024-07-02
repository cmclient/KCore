package pl.kuezese.core.menu.effects;

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
public class EffectsPlayerMenu {

    private final CorePlugin core;
    private final ItemStack remove = new ItemMaker(Material.MILK_BUCKET, 1)
            .setName(ChatHelper.color(" &8>> &7&lUsuwanie efektow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a8 &fblokow emeraldow"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack haste1 = new ItemMaker(Material.STONE_PICKAXE, 1)
            .setName(ChatHelper.color(" &8>> &7&lHaste 1"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a8 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack haste2 = new ItemMaker(Material.IRON_PICKAXE, 2)
            .setName(ChatHelper.color(" &8>> &7&lHaste 2"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a32 &fbloki emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack haste3 = new ItemMaker(Material.DIAMOND_PICKAXE, 3)
            .setName(ChatHelper.color(" &8>> &7&lHaste 3"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a64 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a32 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack speed1 = new ItemMaker(Material.SUGAR, 1)
            .setName(ChatHelper.color(" &8>> &7&lSzybkosc 1"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a8 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack speed2 = new ItemMaker(Material.SUGAR, 2)
            .setName(ChatHelper.color(" &8>> &7&lSzybkosc 2"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a32 &fbloki emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack jump1 = new ItemMaker(Material.FEATHER, 1)
            .setName(ChatHelper.color(" &8>> &7&lWysoki skok 1"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a8 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack jump2 = new ItemMaker(Material.FEATHER, 2)
            .setName(ChatHelper.color(" &8>> &7&lWysoki skok 2"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a32 &fbloki emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack jump3 = new ItemMaker(Material.FEATHER, 3)
            .setName(ChatHelper.color(" &8>> &7&lWysoki skok 3"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a64 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a32 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack strength1 = new ItemMaker(Material.DIAMOND_SWORD, 1)
            .setName(ChatHelper.color(" &8>> &7&lSila 1"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a8 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack strength2 = new ItemMaker(Material.DIAMOND_SWORD, 2)
            .setName(ChatHelper.color(" &8>> &7&lSila 2"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a32 &fbloki emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack nightVision1 = new ItemMaker(Material.EYE_OF_ENDER, 1)
            .setName(ChatHelper.color(" &8>> &7&lWidzenie w ciemnosci 1"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a16 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a8 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack escape = new ItemMaker(Material.DIAMOND_BOOTS, 1)
            .setName(ChatHelper.color(" &8>> &7&lEfekty do ucieczki"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a128 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a64 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &7&nLista efektow:"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &fWysoki skok 3 na &75 &fminut"))
            .addLore(ChatHelper.color(" &8>> &fSzybkosc 2 na &75 &fminut"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack fight = new ItemMaker(Material.DIAMOND_SWORD, 1)
            .setName(ChatHelper.color(" &8>> &7&lEfekty do walki"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a128 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a64 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &7&nLista efektow:"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &fSzybkosc 2 na &75 &fminut"))
            .addLore(ChatHelper.color(" &8>> &fSila 2 na &75 &fminut"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();
    private final ItemStack mining = new ItemMaker(Material.DIAMOND_PICKAXE, 1)
            .setName(ChatHelper.color(" &8>> &7&lEfekty do kopania"))
            .addLore(ChatHelper.color(" &8>> &fKoszt: &a128 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fKoszt dla SVIPa: &a64 &fblokow emeraldow"))
            .addLore(ChatHelper.color(" &8>> &fCzas trwania: &a5 min&f."))
            .addLore(" ")
            .addLore(ChatHelper.color(" &7&nLista efektow:"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &fHaste 3 na &75 &fminut"))
            .addLore(ChatHelper.color(" &8>> &fSzybkosc 2 na &75 &fminut"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby zakupic efekt."))
            .make();

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aEfekty dla &agracza &8<<"));

        inv.setItem(4, remove);

        inv.setItem(10, haste1);
        inv.setItem(19, haste2);
        inv.setItem(28, haste3);

        inv.setItem(12, speed1);
        inv.setItem(21, speed2);

        inv.setItem(14, jump1);
        inv.setItem(23, jump2);
        inv.setItem(32, jump3);

        inv.setItem(16, strength1);
        inv.setItem(25, strength2);
        inv.setItem(34, nightVision1);

        inv.setItem(39, escape);
        inv.setItem(40, fight);
        inv.setItem(41, mining);

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
