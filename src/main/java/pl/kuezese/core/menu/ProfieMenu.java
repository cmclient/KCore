package pl.kuezese.core.menu;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;

public class ProfieMenu {

    private final CorePlugin core;
    private final FunnyGuilds funnyGuilds;
    private final Player player;

    public ProfieMenu(CorePlugin core, Player player) {
        this.core = core;
        this.player = player;
        this.funnyGuilds = FunnyGuilds.getInstance();
    }

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8Profil " + this.player.getName()));

        User user = this.core.getUserManager().get(this.player.getName());
        ItemStack head = new ItemMaker(Material.SKULL_ITEM, 1, (short) 3)
                .setName(ChatHelper.color("&aAktualna ranga:"))
                .addLore(ChatHelper.color("&7" + this.core.getApi().getUserManager().getUser(this.player.getName()).getPrimaryGroup())).make();
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(this.player.getName());
        head.setItemMeta(meta);

        int input = this.player.getStatistic(Statistic.PLAY_ONE_TICK) / 20;

        int numberOfDays = input / 86400;
        int numberOfHours = (input % 86400) / 3600;
        int numberOfMinutes = ((input % 86400) % 3600) / 60;
        int numberOfSeconds = ((input % 86400) % 3600) % 60;

        String playTime = numberOfDays + "d" + numberOfHours + "h" + numberOfMinutes + "m" + numberOfSeconds + "s";

        ItemStack time = new ItemMaker(Material.WATCH)
                .setName(ChatHelper.color("&aCzas na serwerze:"))
                .addLore(ChatHelper.color("&7Online: &a" + playTime)).make();

        net.dzikoysk.funnyguilds.user.User guildUser = this.funnyGuilds.getUserManager().findByUuid(this.player.getUniqueId()).get();

        ItemStack guild = new ItemMaker(Material.STONE_SWORD)
                .setName(ChatHelper.color("&aGildia:"))
                .addLore(ChatHelper.color("&7" + (guildUser.getGuild().isEmpty() ? "brak gildii" : guildUser.getGuild().get().getTag()))).make();

        ItemStack coins = new ItemMaker(Material.GOLD_NUGGET)
                .setName(ChatHelper.color("&aCoins:"))
                .addLore(ChatHelper.color("&7" + user.getCoinsFormatted() + "$")).make();

        ItemStack poins = new ItemMaker(Material.POTION)
                .setName(ChatHelper.color("&aPunkty:"))
                .addLore(ChatHelper.color("&7" + guildUser.getRank().getPoints())).make();

        ItemStack kills = new ItemMaker(Material.IRON_HELMET)
                .setName(ChatHelper.color("&aZabojstwa:"))
                .addLore(ChatHelper.color("&7" + guildUser.getRank().getKills())).make();

        ItemStack deaths = new ItemMaker(Material.SKULL_ITEM)
                .setName(ChatHelper.color("&aSmierci:"))
                .addLore(ChatHelper.color("&7" + guildUser.getRank().getDeaths())).make();

        inv.setItem(9, head);
        inv.setItem(11, time);
        inv.setItem(13, guild);
        inv.setItem(15, coins);
        inv.setItem(17, poins);
        inv.setItem(30, kills);
        inv.setItem(32, deaths);

        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
