package pl.kuezese.core.menu.effects;

import lombok.RequiredArgsConstructor;
import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.guild.Guild;
import net.dzikoysk.funnyguilds.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;

@RequiredArgsConstructor
public class EffectsGuildMenu {

    private final CorePlugin core;

    private final ItemStack strength2 = new ItemMaker(Material.DIAMOND_SWORD)
            .setName(ChatHelper.color("&cSila 2"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &7Koszt: &a256 &7bloki emeraldow"))
            .addLore(ChatHelper.color(" &8>> &7Czas trwania: &a5 minut"))
            .make();
    private final ItemStack speed2 = new ItemMaker(Material.SUGAR)
            .setName(ChatHelper.color("&fSzybkosc 2"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &7Koszt: &a256 &7bloki emeraldow"))
            .addLore(ChatHelper.color(" &8>> &7Czas trwania: &a5 minut"))
            .addLore(" ")
            .make();
    private final ItemStack haste3 = new ItemMaker(Material.DIAMOND_PICKAXE)
            .setName(ChatHelper.color("&2Haste 3"))
            .addLore(" ")
            .addLore(ChatHelper.color(" &8>> &7Koszt: &a256 &7bloki emeraldow"))
            .addLore(ChatHelper.color(" &8>> &7Czas trwania: &a5 minut"))
            .make();

    public void open(Player p) {
        FunnyGuilds funnyGuilds = FunnyGuilds.getInstance();
        User user = funnyGuilds.getUserManager().findByUuid(p.getUniqueId()).get();
        Guild guild = user.getGuild().orNull();

        if (guild == null) {
            p.sendMessage(ChatHelper.color(" &8>> &cNie posiadasz gildii."));
            p.closeInventory();
            return;
        }

        Inventory inv = this.core.getServer().createInventory(p, 27, ChatHelper.color("&8>> &aEfekty dla &agildii &8<<"));

        inv.setItem(12, strength2);
        inv.setItem(13, speed2);
        inv.setItem(14, haste3);

        InventoryHelper.backgroundEmpty(inv);

        p.openInventory(inv);
    }
}
