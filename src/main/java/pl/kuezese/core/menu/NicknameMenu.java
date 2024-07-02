package pl.kuezese.core.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;

public class NicknameMenu {

    private final CorePlugin core;
    private final ItemStack black = new ItemMaker(Material.WOOL, 1, (short)15).setName(ChatHelper.color("&8>> &0Czarny")).make();
    private final ItemStack dark_blue = new ItemMaker(Material.WOOL, 1, (short)11).setName(ChatHelper.color("&8>> &1Ciemny niebieski")).make();
    private final ItemStack dark_green = new ItemMaker(Material.WOOL, 1, (short)13).setName(ChatHelper.color("&8>> &2Ciemny zielony")).make();
    private final ItemStack cyan = new ItemMaker(Material.WOOL, 1, (short)9).setName(ChatHelper.color("&8>> &3Ciemny blekit")).make();
    private final ItemStack dark_red = new ItemMaker(Material.WOOL, 1, (short)14).setName(ChatHelper.color("&8>> &4Ciemny czerwony")).make();
    private final ItemStack purple = new ItemMaker(Material.WOOL, 1, (short)10).setName(ChatHelper.color("&8>> &5Fioletowy")).make();
    private final ItemStack orange = new ItemMaker(Material.WOOL, 1, (short)1).setName(ChatHelper.color("&8>> &7Pomaranczowy")).make();
    private final ItemStack light_gray = new ItemMaker(Material.WOOL, 1, (short)8).setName(ChatHelper.color("&8>> &7Szary")).make();
    private final ItemStack dark_gray = new ItemMaker(Material.WOOL, 1, (short)7).setName(ChatHelper.color("&8>> &8Ciemny szary")).make();
    private final ItemStack light_blue = new ItemMaker(Material.WOOL, 1, (short)11).setName(ChatHelper.color("&8>> &9Niebieski")).make();
    private final ItemStack blue = new ItemMaker(Material.WOOL, 1, (short)3).setName(ChatHelper.color("&8>> &bBlekitny")).make();
    private final ItemStack light_red = new ItemMaker(Material.WOOL, 1, (short)14).setName(ChatHelper.color("&8>> &cJasny czerwony")).make();
    private final ItemStack pink = new ItemMaker(Material.WOOL, 1, (short)6).setName(ChatHelper.color("&8>> &dRozowy")).make();
    private final ItemStack yellow = new ItemMaker(Material.WOOL, 1, (short)4).setName(ChatHelper.color("&8>> &aZolty")).make();
    private final ItemStack white = new ItemMaker(Material.WOOL).setName(ChatHelper.color("&8>> &fBialy")).make();
    private final ItemStack rainbow = new ItemMaker(Material.REDSTONE_TORCH_ON).setName(ChatHelper.color("&8>> &1T&2e&3c&4z&5o&7w&7y")).make();
    private final ItemStack off = new ItemMaker(Material.BARRIER).setName(ChatHelper.color("&8>> &fDomyslny")).make();

    public NicknameMenu(CorePlugin core) {
        this.core = core;
    }

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 18, ChatHelper.color("&8>> &aNick &8<<"));
        inv.setItem(0, black);
        inv.setItem(1, dark_blue);
        inv.setItem(2, dark_green);
        inv.setItem(3, cyan);
        inv.setItem(4, dark_red);
        inv.setItem(5, purple);
        inv.setItem(6, orange);
        inv.setItem(7, light_gray);
        inv.setItem(8, dark_gray);
        inv.setItem(9, light_blue);
        inv.setItem(10, blue);
        inv.setItem(11, light_red);
        inv.setItem(12, pink);
        inv.setItem(13, yellow);
        inv.setItem(14, white);
        inv.setItem(15, rainbow);
        inv.setItem(16, off);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
