package pl.kuezese.core.menu.drop;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;

@RequiredArgsConstructor
public class DropMenu {
    
    private final CorePlugin core;
    private final ItemStack green = new ItemMaker(Material.STAINED_GLASS_PANE, 1, (short) 13).setName(" ").make();
    private final ItemStack stone = new ItemMaker(Material.STONE).setName(ChatHelper.color("&8>> &aDrop z kamienia &8<<")).addLore(ChatHelper.color("&8>> &7Kliknij, aby wyswietlic drop z kamienia")).make();
    private final ItemStack stats = new ItemMaker(Material.EYE_OF_ENDER).setName(ChatHelper.color("&8>> &aStatystyki &8<<")).addLore(ChatHelper.color("&8>> &7Kliknij, aby wyswietlic statystyki")).make();
    private final ItemStack topminers = new ItemMaker(Material.DIAMOND_PICKAXE).setName(ChatHelper.color("&8>> &aTop gornicy &8<<")).addLore(ChatHelper.color("&8>> &7Kliknij, aby wyswietlic najlepszych gornikow")).make();
    private final ItemStack pcase = new ItemMaker(Material.CHEST).setName(ChatHelper.color("&8>> &aDrop z PremiumCase &8<<")).addLore(ChatHelper.color("&8>> &7Kliknij, aby wyswietlic drop z PremiumCase")).make();
    private final ItemStack pandora = new ItemMaker(Material.DRAGON_EGG).setName(ChatHelper.color("&8>> &aDrop z Pandory &8<<")).addLore(ChatHelper.color("&8>> &7Kliknij, aby wyswietlic drop z Pandory")).make();
    private final ItemStack boss = new ItemMaker(Material.SKULL_ITEM, 1, (short) 1).setName(ChatHelper.color("&8>> &aDrop z Bossow &8<<")).addLore(ChatHelper.color("&8>> &7Kliknij, aby wyswietlic drop z Bossow")).make();
    private final ItemStack cobblex = new ItemMaker(Material.MOSSY_COBBLESTONE).setName(ChatHelper.color("&8>> &aDrop z CobbleX &8<<")).addLore(ChatHelper.color("&8>> &7Kliknij, aby wyswietlic drop z CobbleX")).make();
    private final ItemStack topcoins = new ItemMaker(Material.NETHER_STAR).setName(ChatHelper.color("&8>> &aTop coins &8<<")).addLore(ChatHelper.color("&8>> &7Kliknij, aby wyswietlic topke coinsow")).make();

    public void show(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 54, ChatHelper.color("&8>> &aDrop &8<<"));
        User user = this.core.getUserManager().get(p.getName());

        int[] green = {0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53};
        for (int i : green) inv.setItem(i, this.green);

        inv.setItem(12, this.topminers);
        inv.setItem(14, this.topcoins);

        inv.setItem(20, this.pcase);
        inv.setItem(22, this.stone);
        inv.setItem(24, this.pandora);
        inv.setItem(30, this.stats);
        inv.setItem(31, this.boss);
        inv.setItem(32, this.cobblex);

        inv.setItem(38, new ItemMaker(Material.COBBLESTONE).setName(ChatHelper.color("&8>> &aWypadanie Cobblestone &8<<")).addLore(ChatHelper.color("&8>> &7Aktywny: &" + (user.isCobble() ? "a✓" : "c✘"))).addLore(ChatHelper.color("&8>> &7Kliknij, aby zmienic")).make());
        inv.setItem(40, new ItemMaker(Material.HOPPER).setName(ChatHelper.color("&8>> &aWypadanie przy pelnym ekwipunku &8<<")).addLore(ChatHelper.color("&8>> &7Aktywny: &" + (user.isDropOnFullInventory() ? "a✓" : "c✘"))).addLore(ChatHelper.color("&8>> &7Kliknij, aby zmienic")).make());
        inv.setItem(42, new ItemMaker(Material.GOLD_SPADE).setName(ChatHelper.color("&8>> &aDrop z kamienia &8<<")).addLore(ChatHelper.color("&8>> &7Aktywny: &" + (user.isDrop() ? "a✓" : "c✘"))).addLore(ChatHelper.color("&8>> &7Kliknij, aby zmienic")).make());

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
