package pl.kuezese.core.menu.shop;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.manager.impl.ShopManager;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.type.ShopType;

import java.util.List;

@RequiredArgsConstructor
public class ShopBuyCoinsMenu {

    private final CorePlugin core;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 36, ChatHelper.color("&8>> &aSklep (Kupno) &8<<"));

        List<ShopManager.ShopItem> items = this.core.getShopManager().get(ShopType.BUY);

        items.forEach(item -> {
            ItemStack is = new ItemMaker(item.getItem(), item.getAmount(), item.getData())
                    .setName(item.getName().isEmpty() ? null : ChatHelper.color(item.getName()))
                    .addLore(" ")
                    .addLore(ChatHelper.color(" &8>> &7Cena: &f" + item.getPrice() + "$"))
                    .make();
            item.getEnchants().stream().map(enchant -> enchant.split(":")).forEach(split -> is.addUnsafeEnchantment(Enchantment.getByName(split[0]), Integer.parseInt(split[1])));
            inv.addItem(is);
        });

        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
