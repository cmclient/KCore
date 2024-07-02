package pl.kuezese.core.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.helper.ItemHelper;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;

@RequiredArgsConstructor
public class DepositMenu {

    private final CorePlugin core;

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 54, ChatHelper.color("&8>> &aDepozyt &8<<"));
        User user = this.core.getUserManager().get(p.getName());

        this.core.getLimitManager().getLimits().forEach(limit -> {
            ItemMaker im = new ItemMaker(limit.getItem(), limit.getLimit() == 0 ? 1 : limit.getLimit(), limit.getData())
                    .setName(limit.getDisplayName())
                    .addLore(" ")
                    .addLore(ChatHelper.color(" &8>> &7W depozycie: &a&o" + user.getLimits().getOrDefault(limit.getId(), 0)))
                    .addLore(ChatHelper.color(" &8>> &7W ekwipunku: &a&o" + ItemHelper.getAmount(p, limit.getItem(), limit.getData(), limit.getRequiredName())))
                    .addLore(ChatHelper.color(" &8>> &7Limit: &c&o" + limit.getLimit()))
                    .addLore(" ")
                    .addLore(ChatHelper.color(" &8>> &aKliknij lewym, aby wyplacic."))
                    .addLore(ChatHelper.color(" &8>> &aKliknij prawym, aby wplacic."))
                    .setEnchants(limit.getEnchantments());

            inv.setItem(limit.getDisplaySlot(), im.make());
        });

        ItemStack all = new ItemMaker(Material.HOPPER).setName(ChatHelper.color("&8>> &cWyplac wszystko")).addLore(" ").addLore(ChatHelper.color(" &8>> &fMozesz takze wyplacic wszystko")).addLore(ChatHelper.color(" &8>> &fuzywajac komendy &7/depozyt all&f.")).addLore(" ").make();
        inv.setItem(40, all);

        InventoryHelper.backgroundEmpty(inv);

        p.openInventory(inv);
    }
}
