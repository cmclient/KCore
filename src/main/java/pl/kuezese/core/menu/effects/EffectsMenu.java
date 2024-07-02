package pl.kuezese.core.menu.effects;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;

@RequiredArgsConstructor
public class EffectsMenu {

    private final CorePlugin core;

    private final ItemStack info = new ItemMaker(Material.BOOK)
            .setName(ChatHelper.color("&8>> &7Informacje"))
            .addLore(ChatHelper.color(" &8>> &7Ranga &8[&5SVIP&8] &7potrzebuje"))
            .addLore(ChatHelper.color(" &8>> &7o &a50% &7mniej emeraldow"))
            .addLore(ChatHelper.color(" &8>> &7na zakup efektow dla gracza."))
            .make();
    ItemStack players = new ItemMaker(Material.SKULL_ITEM, 1, (short) 3)
            .setName(ChatHelper.color("&8>> &7Efekty dla &agracza"))
            .addLore("")
            .addLore(ChatHelper.color(" &7Mozesz takze otworzyc efekty &agracza"))
            .addLore(ChatHelper.color(" &7uzywajac komendy &a/eg"))
            .addLore("")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby przejsc dalej"))
            .make();
    private final ItemStack guild = new ItemMaker(Material.DRAGON_EGG)
            .setName(ChatHelper.color("&8>> &7Efekty dla &agildii"))
            .addLore("")
            .addLore(ChatHelper.color(" &7Mozesz takze otworzyc efekty &agildii"))
            .addLore(ChatHelper.color(" &7uzywajac komendy &a/gefekty"))
            .addLore("")
            .addLore(ChatHelper.color(" &8>> &aKliknij, aby przejsc dalej"))
            .make();

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 27, ChatHelper.color("&8>> &aMenu efektow &8<<"));

        SkullMeta meta = (SkullMeta) players.getItemMeta();
        meta.setOwner(p.getName());
        players.setItemMeta(meta);

        inv.setItem(11, players);
        inv.setItem(13, info);
        inv.setItem(15, guild);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
