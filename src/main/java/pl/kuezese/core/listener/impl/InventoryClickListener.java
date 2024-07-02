package pl.kuezese.core.listener.impl;

import io.papermc.lib.PaperLib;
import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.guild.Guild;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.menu.CraftingsMenu;
import pl.kuezese.core.menu.DepositMenu;
import pl.kuezese.core.menu.SettingsMenu;
import pl.kuezese.core.menu.drop.*;
import pl.kuezese.core.menu.effects.EffectsGuildMenu;
import pl.kuezese.core.menu.effects.EffectsPlayerMenu;
import pl.kuezese.core.menu.shop.*;
import pl.kuezese.core.helper.*;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.manager.impl.KitManager;
import pl.kuezese.core.manager.impl.LimitManager;
import pl.kuezese.core.manager.impl.ShopManager;
import pl.kuezese.core.manager.impl.VillagerManager;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.User;
import pl.kuezese.core.task.impl.AbyssTask;
import pl.kuezese.core.type.ShopType;

import java.util.List;

public class InventoryClickListener extends Listener {

    private final FunnyGuilds funnyGuilds = FunnyGuilds.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof EnchantingInventory && e.getSlot() == 1) {
            e.setCancelled(true);
            return;
        }

        if (e.getClickedInventory() != null && e.getClickedInventory().getType() != InventoryType.CHEST
                && e.getClickedInventory().getType() != InventoryType.HOPPER)
            return;

        Player p = (Player) e.getWhoClicked();

        if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aSklep &8<<"))) {
            e.setCancelled(true);
            switch (e.getSlot()) {
                case 10: {
                    new ShopBuyCoinsMenu(core).open(p);
                    break;
                }
                case 11: {
                    new ShopBuyEmeraldsMenu(core).open(p);
                    break;
                }
                case 12: {
                    new ShopBuyLevelsMenu(core).open(p);
                    break;
                }
                case 13: {
                    new ShopVillagerMenu(core).open(p);
                    break;
                }
                case 14: {
                    new ShopSellMenu(core).open(p);
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aSklep (Kupno) &8<<"))) {
            e.setCancelled(true);
            ItemStack is = e.getCurrentItem();

            if (is != null && is.getType() == Material.BARRIER) {
                new ShopMenu(this.core).open(p);
                return;
            }

            if (is != null && is.getType() == Material.STAINED_GLASS_PANE) {
                return;
            }

            User user = this.userManager.get(p.getName());
            List<ShopManager.ShopItem> items = this.shopManager.get(ShopType.BUY);

            for (ShopManager.ShopItem item : items) {
                if (is != null && is.getType() == item.getItem() && is.getDurability() == item.getData() && is.getAmount() == item.getAmount() && (is.getItemMeta().getDisplayName() == null || is.getItemMeta().getDisplayName().equals(item.getName()))) {
                    if (user.getCoins() >= item.getPrice()) {
                        user.removeCoins((float) item.getPrice());
                        p.getInventory().addItem(new ItemStack(item.getItemStack())).values().forEach(stack -> p.getLocation().getWorld().dropItem(p.getLocation(), stack));
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Zakupiles przedmiot &a" + (item.getName().isEmpty() ? item.getItem().toString() : item.getName()), 10, 30, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Nie posiadasz wystarczajacej ilosci &acoins&7.", 10, 30, 10);
                    }
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aSklep (Kupno za emeraldy) &8<<"))) {
            e.setCancelled(true);
            ItemStack is = e.getCurrentItem();

            if (is != null && is.getType() == Material.BARRIER) {
                new ShopMenu(this.core).open(p);
                return;
            }

            if (is != null && is.getType() == Material.STAINED_GLASS_PANE) {
                return;
            }

            User user = this.userManager.get(p.getName());
            List<ShopManager.ShopItem> items = this.shopManager.get(ShopType.EMERALD);

            for (ShopManager.ShopItem item : items) {
                if (is != null && is.getType() == item.getItem() && is.getDurability() == item.getData() && is.getAmount() == item.getAmount() && (item.getName() == null || is.getItemMeta().getDisplayName() == null || is.getItemMeta().getDisplayName().equals(item.getName()))) {
                    ItemStack emeralds = new ItemStack(Material.EMERALD_BLOCK, (int) item.getPrice());
                    if (p.getInventory().containsAtLeast(emeralds, (int) item.getPrice())) {
                        p.getInventory().removeItem(emeralds);
                        p.getInventory().addItem(new ItemStack(item.getItemStack())).values().forEach(stack -> p.getLocation().getWorld().dropItem(p.getLocation(), stack));
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Zakupiles przedmiot &a" + (item.getName().isEmpty() ? item.getItem().toString() : item.getName()), 10, 30, 10);
                    } else {
                        LimitManager.Limit limit = this.core.getLimitManager().get(Material.EMERALD_BLOCK).orElse(null);

                        if (limit != null) {
                            int emeraldsInDeposit = user.getLimits().getOrDefault(limit.getId(), 0);
                            if (emeraldsInDeposit >= item.getPrice()) {
                                user.getLimits().put(limit.getId(), (int) (emeraldsInDeposit - item.getPrice()));
                                p.getInventory().addItem(new ItemStack(item.getItemStack())).values().forEach(stack -> p.getLocation().getWorld().dropItem(p.getLocation(), stack));
                                ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Zakupiles przedmiot &a" + (item.getName().isEmpty() ? item.getItem().toString() : item.getName()), 10, 30, 10);
                                break;
                            }
                        }

                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aSklep (Kupno za level) &8<<"))) {
            e.setCancelled(true);
            ItemStack is = e.getCurrentItem();

            if (is != null && is.getType() == Material.BARRIER) {
                new ShopMenu(this.core).open(p);
                return;
            }

            if (is != null && is.getType() == Material.STAINED_GLASS_PANE) {
                return;
            }

            List<ShopManager.ShopItem> items = this.shopManager.get(ShopType.LVL);

            for (ShopManager.ShopItem item : items) {
                if (is != null && is.getType() == item.getItem() && is.getDurability() == item.getData() && is.getAmount() == item.getAmount() && (item.getName() == null || is.getItemMeta().getDisplayName() == null || is.getItemMeta().getDisplayName().equals(item.getName()))) {
                    if (p.getLevel() >= item.getPrice()) {
                        p.setLevel((int) (p.getLevel() - item.getPrice()));
                        p.getInventory().addItem(new ItemStack(item.getItemStack())).values().forEach(stack -> p.getLocation().getWorld().dropItem(p.getLocation(), stack));
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Zakupiles przedmiot &a" + (item.getName().isEmpty() ? item.getItem().toString() : item.getName()), 10, 30, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Nie posiadasz wystarczajacej ilosci &alvl&7.", 10, 30, 10);
                    }
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aSklep (Sprzedaz) &8<<"))) {
            e.setCancelled(true);
            ItemStack is = e.getCurrentItem();

            if (is != null && is.getType() == Material.BARRIER) {
                new ShopMenu(this.core).open(p);
                return;
            }

            if (is != null && is.getType() == Material.STAINED_GLASS_PANE) {
                return;
            }

            User user = this.userManager.get(p.getName());

            if (e.getSlot() == 21 || e.getSlot() == 23) {
                return;
            }

            if (e.getSlot() == 31) {
                UserHelper.sellAll(this.core, p, user);
                return;
            }

            List<ShopManager.ShopItem> items = this.shopManager.get(ShopType.SELL);

            for (ShopManager.ShopItem item : items) {
                if (is != null && is.getType() == item.getItem() && is.getDurability() == item.getData() && is.getAmount() == item.getAmount() && (item.getName() == null || is.getItemMeta().getDisplayName() == null || is.getItemMeta().getDisplayName().equals(item.getName()))) {
                    if (p.getInventory().containsAtLeast(item.getItemStack(), item.getAmount())) {
                        user.addCoins((float) item.getPrice());
                        p.getInventory().removeItem(item.getItemStack());
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Sprzedales przedmiot &a" + (item.getName().isEmpty() ? item.getItem().toString() : item.getName()), 10, 30, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Nie posiadasz tego przedmiotu.", 10, 30, 10);
                    }
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aVillagerzy &8<<"))) {
            e.setCancelled(true);
            ItemStack is = e.getCurrentItem();

            if (is != null && is.getType() == Material.BARRIER) {
                new ShopMenu(this.core).open(p);
                return;
            }

            if (is != null && is.getType() == Material.STAINED_GLASS_PANE && is.getDurability() == (short) 15 && is.getItemMeta().getDisplayName() != null && is.getItemMeta().getDisplayName().equals(" ")) {
                return;
            }

            List<VillagerManager.Villager> villagers = this.villagerManager.getVillagers();

            for (VillagerManager.Villager villager : villagers) {
                if (is != null && is.getType() == villager.getItem() && is.getDurability() == villager.getData() && (is.getItemMeta().getDisplayName() == null || is.getItemMeta().getDisplayName().equals(villager.getName()))) {
                    new ShopBuyVillagerMenu(core).open(p, villager);
                    break;
                }
            }
        } else if (e.getInventory().getName().startsWith(ChatHelper.color("&8>> &aVillager "))) {
            e.setCancelled(true);
            ItemStack is = e.getCurrentItem();

            if (is != null && is.getType() == Material.BARRIER) {
                new ShopVillagerMenu(this.core).open(p);
                return;
            }

            if (is != null && is.getType() == Material.STAINED_GLASS_PANE && is.getDurability() == (short) 15 && is.getItemMeta().getDisplayName() != null && is.getItemMeta().getDisplayName().equals(" ")) {
                return;
            }

            String name = e.getInventory().getName().split(ChatHelper.color("&8>> &aVillager "))[1].split(ChatHelper.color(" &8<<"))[0];
            VillagerManager.Villager villager = this.villagerManager.get(name);
            List<VillagerManager.Item> items = villager.getItems();

            for (VillagerManager.Item item : items) {
                if (is != null && is.getType() == item.getItem() && is.getDurability() == item.getData() && is.getAmount() == item.getAmount() && (is.getItemMeta().getDisplayName() == null || is.getItemMeta().getDisplayName().equals(item.getName()))) {
                    ItemStack emeralds = new ItemStack(Material.EMERALD_BLOCK, item.getPrice());
                    if (p.getInventory().containsAtLeast(emeralds, item.getPrice())) {
                        p.getInventory().removeItem(emeralds);
                        p.getInventory().addItem(new ItemStack(item.getItemStack())).values().forEach(is1 -> p.getLocation().getWorld().dropItem(p.getLocation(), is1));
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Zakupiles przedmiot &a" + (item.getName().isEmpty() ? item.getItem().toString() : item.getName()), 10, 30, 10);

                    } else {
                        ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aZestawy &8<<"))) {
            e.setCancelled(true);
            ItemStack is = e.getCurrentItem();

            this.core.getKitManager().getKits().forEach(kit -> {
                if (is != null && is.getType() == kit.getItem() && (is.getItemMeta().getDisplayName() == null || is.getItemMeta().getDisplayName().equals(kit.getName()))) {
                    if (!p.hasPermission(kit.getPermission())) {
                        p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz uprawnien do tego &azestawu&7. &7(&a" + kit.getPermission() + "&7)"));
                        p.sendMessage(ChatHelper.color(" &8>> &7Aby uzyskac dostep do tego &azestawu &7zakup range na &awww.coremax.pl&7."));
                        return;
                    }

                    if (e.isRightClick()) {
                        InventoryHelper.showKit(p, ChatHelper.color("&8>> &7Kit " + kit.getSimpleName()), false, kit.getItems().stream().map(KitManager.Item::getItemStack).toArray(ItemStack[]::new));
                        return;
                    }

                    User user = this.userManager.get(p.getName());
                    long cooldown = user.getKitCooldown(kit.getSimpleName());

                    if (cooldown > System.currentTimeMillis()) {
                        p.sendMessage(ChatHelper.color(" &8>> &7Zestaw &a" + kit.getSimpleName() + " &7bedziesz mogl odebrac za: &a" + DateHelper.formatDateDiff(cooldown) + "&7."));
                        return;
                    }

                    user.setKitCooldown(kit.getSimpleName(), System.currentTimeMillis() + kit.getCooldown());
                    InventoryHelper.showKit(p, ChatHelper.color("&8>> &7Kit " + kit.getSimpleName()), true, kit.getItems().stream().map(KitManager.Item::getItemStack).toArray(ItemStack[]::new));
                }
            });
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aCraftingi &8<<"))) {
            e.setCancelled(true);
            switch (e.getSlot()) {
                case 10: {
                    new CraftingsMenu(core).openStoneGeneratorGui(p);
                    break;
                }
                case 11: {
                    new CraftingsMenu(core).openBoyFarmerGUI(p);
                    break;
                }
                case 12: {
                    new CraftingsMenu(core).openSandFamerGUI(p);
                    break;
                }
                case 13: {
                    new CraftingsMenu(core).openKopaczFosGUI(p);
                    break;
                }
                case 14: {
                    new CraftingsMenu(core).openEnderChestGUI(p);
                    break;
                }
                case 15: {
                    new CraftingsMenu(core).openRzucaneTntGui(p);
                    break;
                }
                case 16: {
                    new CraftingsMenu(core).openAntyNogiGui(p);
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aGenerator Stone &8<<"))) {
            e.setCancelled(true);
            if (e.getSlot() == 25) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.REDSTONE), 4)
                        && p.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 3)
                        && p.getInventory().containsAtLeast(new ItemStack(Material.PISTON_BASE), 1)
                        && p.getInventory().containsAtLeast(new ItemStack(Material.STONE), 1)
                ) {
                    p.getInventory().removeItem(new ItemStack(Material.REDSTONE, 4));
                    p.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 3));
                    p.getInventory().removeItem(new ItemStack(Material.PISTON_BASE, 1));
                    p.getInventory().removeItem(new ItemStack(Material.STONE, 1));
                    p.getInventory().addItem(new ItemMaker(Material.STONE).setName(ChatHelper.color("&aGenerator Stone")).make());
                    p.sendMessage(ChatHelper.color(" &8>> &aStworzyles generator stone."));
                } else {
                    p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz wymaganych przedmiotow na stworzenie &ageneratora stone&7."));
                }
            } else if (e.getSlot() == 44) {
                new CraftingsMenu(core).open(p);
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aBoy farmer &8<<"))) {
            e.setCancelled(true);
            if (e.getSlot() == 25) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.OBSIDIAN), 4) && p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD_BLOCK), 4)) {
                    p.getInventory().removeItem(new ItemStack(Material.OBSIDIAN, 4));
                    p.getInventory().removeItem(new ItemStack(Material.EMERALD_BLOCK, 4));
                    p.getInventory().addItem(new ItemMaker(Material.ENDER_PORTAL_FRAME).setName(ChatHelper.color("&8>> &7Boy Farmer")).make());
                    p.sendMessage(ChatHelper.color(" &8>> &aStworzyles boy farmera."));
                } else {
                    p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz wymaganych przedmiotow na stworzenie &aboy farmera&7."));
                }
            } else if (e.getSlot() == 44) {
                new CraftingsMenu(core).open(p);
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aSand farmer &8<<"))) {
            e.setCancelled(true);
            if (e.getSlot() == 25) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD_BLOCK), 8) && p.getInventory().containsAtLeast(new ItemStack(Material.SAND), 1)) {
                    p.getInventory().removeItem(new ItemStack(Material.EMERALD_BLOCK, 8));
                    p.getInventory().removeItem(new ItemStack(Material.SAND, 1));
                    p.getInventory().addItem(new ItemMaker(Material.SANDSTONE).setName(ChatHelper.color("&8>> &7Sand Farmer")).make());
                    p.sendMessage(ChatHelper.color(" &8>> &aStworzyles sand farmera."));
                } else {
                    p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz wymaganych przedmiotow na stworzenie &asand farmera&7."));
                }
            } else if (e.getSlot() == 44) {
                new CraftingsMenu(core).open(p);
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aKopacz fos &8<<"))) {
            e.setCancelled(true);
            if (e.getSlot() == 25) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD_BLOCK), 8) && p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND_PICKAXE), 1)) {
                    p.getInventory().removeItem(new ItemStack(Material.EMERALD_BLOCK, 8));
                    p.getInventory().removeItem(new ItemStack(Material.DIAMOND_PICKAXE, 1));
                    p.getInventory().addItem(new ItemMaker(Material.STONE).setName(ChatHelper.color("&8>> &7Kopacz Fosy")).make());
                    p.sendMessage(ChatHelper.color(" &8>> &aStworzyles kopacz fos."));
                } else {
                    p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz wymaganych przedmiotow na stworzenie &akopacza fos&7."));
                }
            } else if (e.getSlot() == 44) {
                new CraftingsMenu(core).open(p);
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aEnder Chest &8<<"))) {
            e.setCancelled(true);
            if (e.getSlot() == 25) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.OBSIDIAN), 8) && p.getInventory().containsAtLeast(new ItemStack(Material.ENDER_PEARL), 1)) {
                    p.getInventory().removeItem(new ItemStack(Material.OBSIDIAN, 8));
                    p.getInventory().removeItem(new ItemStack(Material.ENDER_PEARL, 1));
                    p.getInventory().addItem(new ItemStack(Material.ENDER_CHEST));
                    p.sendMessage(ChatHelper.color(" &8>> &aStworzyles ender chesta."));
                } else {
                    p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz wymaganych przedmiotow na stworzenie &aender chesta&7."));
                }
            } else if (e.getSlot() == 44) {
                new CraftingsMenu(core).open(p);
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aRzucane TNT &8<<"))) {
            e.setCancelled(true);
            if (e.getSlot() == 25) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.TNT), 64 * 4)
                        && p.getInventory().containsAtLeast(new ItemStack(Material.GOLDEN_APPLE), 4)
                        && p.getInventory().containsAtLeast(new ItemStack(Material.ENDER_PEARL), 1)
                ) {
                    p.getInventory().removeItem(new ItemStack(Material.TNT, 64 * 4));
                    p.getInventory().removeItem(new ItemStack(Material.GOLDEN_APPLE, 4));
                    p.getInventory().removeItem(new ItemStack(Material.ENDER_PEARL, 1));
                    p.getInventory().addItem(new ItemMaker(Material.TNT).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&8>> &7Rzucane TNT")).make());
                    p.sendMessage(ChatHelper.color(" &8>> &aStworzyles rzucane tnt."));
                } else {
                    p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz wymaganych przedmiotow na stworzenie &arzucanego tnt&7."));
                }
            } else if (e.getSlot() == 44) {
                new CraftingsMenu(core).open(p);
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aAnty Nogi &8<<"))) {
            e.setCancelled(true);
            if (e.getSlot() == 25) {
                if (p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD_BLOCK), 4)
                        && p.getInventory().containsAtLeast(new ItemStack(Material.GOLDEN_APPLE), 4)
                        && p.getInventory().containsAtLeast(new ItemStack(Material.ENDER_PEARL), 1)
                ) {
                    p.getInventory().removeItem(new ItemStack(Material.EMERALD_BLOCK, 4));
                    p.getInventory().removeItem(new ItemStack(Material.GOLDEN_APPLE, 4));
                    p.getInventory().removeItem(new ItemStack(Material.ENDER_PEARL, 1));
                    p.getInventory().addItem(new ItemMaker(Material.MILK_BUCKET).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&8>> &aAnty-Nogi")).make());
                    p.sendMessage(ChatHelper.color(" &8>> &aStworzyles anty nogi."));
                } else {
                    p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz wymaganych przedmiotow na stworzenie &aanty nog&7."));
                }
            } else if (e.getSlot() == 44) {
                new CraftingsMenu(core).open(p);
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aDrop z PremiumCase &8<<")))
            e.setCancelled(true);

        else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aDrop z Pandory &8<<")))
            e.setCancelled(true);

        else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aDrop z Bossow &8<<"))) {
            e.setCancelled(true);

            Boss boss = this.core.getBossManager().get(e.getSlot() - 10);

            if (boss != null) {
                new DropBossMenu(this.core, boss).open(p);
            }
        } else if (e.getInventory().getName().contains(ChatHelper.color("&8>> &aDrop z Bossa")))
            e.setCancelled(true);

        else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aDrop z CobbleX &8<<")))
            e.setCancelled(true);

        else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aTop coins &8<<")))
            e.setCancelled(true);

        else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aMenu efektow &8<<"))) {
            e.setCancelled(true);
            int i = e.getSlot();
            if (i == 11) {
                new EffectsPlayerMenu(core).open(p);
            } else if (i == 15) {
                new EffectsGuildMenu(core).open(p);
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color(ChatHelper.color("&8>> &aEfekty dla &agracza &8<<")))) {
            e.setCancelled(true);
            p.closeInventory();
            switch (e.getSlot()) {
                case 4: {
                    int price = p.hasPermission("cm.effects.svip") ? 8 : 16;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(p::removePotionEffect);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles &ausuniecie efektow", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 10: {
                    int price = p.hasPermission("cm.effects.svip") ? 8 : 16;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 0), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aHaste 1", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 19: {
                    int price = p.hasPermission("cm.effects.svip") ? 16 : 32;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 1), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aHaste 2", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 28: {
                    int price = p.hasPermission("cm.effects.svip") ? 64 : 32;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 2), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aHaste 3", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 12: {
                    int price = p.hasPermission("cm.effects.svip") ? 8 : 16;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 0), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aSzybkosc 1", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 21: {
                    int price = p.hasPermission("cm.effects.svip") ? 16 : 32;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aSzybkosc 2", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 14: {
                    int price = p.hasPermission("cm.effects.svip") ? 8 : 16;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, 0), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aWysoki skok 1", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 23: {
                    int price = p.hasPermission("cm.effects.svip") ? 16 : 32;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, 1), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aWysoki skok 2", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 32: {
                    int price = p.hasPermission("cm.effects.svip") ? 64 : 32;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, 2), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aWysoki skok 3", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 16: {
                    int price = p.hasPermission("cm.effects.svip") ? 8 : 16;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 0), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aSila 1", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 25: {
                    int price = p.hasPermission("cm.effects.svip") ? 16 : 32;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 1), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aSila 2", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 34: {
                    int price = p.hasPermission("cm.effects.svip") ? 8 : 16;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 0), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aWidzenie w ciemnosci 1", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 39: {
                    int price = p.hasPermission("cm.effects.svip") ? 64 : 128;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, 2), true);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekty &ado ucieczki", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 40: {
                    int price = p.hasPermission("cm.effects.svip") ? 64 : 128;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1), true);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 1), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekty &ado walki", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 41: {
                    int price = p.hasPermission("cm.effects.svip") ? 64 : 128;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 2), true);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1), true);
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekty &ado kopania", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aEfekty dla &agildii &8<<"))) {
            e.setCancelled(true);
            p.closeInventory();

            net.dzikoysk.funnyguilds.user.User user = this.funnyGuilds.getUserManager().findByUuid(p.getUniqueId()).get();
            Guild guild = user.getGuild().get();

            switch (e.getSlot()) {
                case 12: {
                    int price = 256;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        guild.getOnlineMembers().forEach(member -> {
                            Player player = this.core.getServer().getPlayerExact(member.getName());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 1), true);
                            if (player != p) {
                                ChatHelper.title(player, "&8• &2&lEFEKTY &8•", "&a" + p.getName() + " &7zakupil efekt &aSila 2 &7dla gildii", 10, 20, 10);
                            }
                        });
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aSila 2 &7dla gildii", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 13: {
                    int price = 256;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        guild.getOnlineMembers().forEach(member -> {
                            Player player = this.core.getServer().getPlayerExact(member.getName());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1), true);
                            if (player != p) {
                                ChatHelper.title(player, "&8• &2&lEFEKTY &8•", "&a" + p.getName() + " &7zakupil efekt &aSzybkosc 2 &7dla gildii", 10, 20, 10);
                            }
                        });
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aSzybkosc 2", 10, 30, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
                case 14: {
                    int price = 256;
                    ItemStack is = new ItemStack(Material.EMERALD_BLOCK, price);
                    if (p.getInventory().containsAtLeast(is, price)) {
                        p.getInventory().removeItem(is);
                        guild.getOnlineMembers().forEach(member -> {
                            Player player = this.core.getServer().getPlayerExact(member.getName());
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000, 2), true);
                            if (player != p) {
                                ChatHelper.title(player, "&8• &2&lEFEKTY &8•", "&a" + p.getName() + " &7zakupil efekt &aHaste 3 &7dla gildii", 10, 20, 10);
                            }
                        });
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Zakupiles efekt &aHaste 3", 10, 20, 10);
                    } else {
                        ChatHelper.title(p, "&8• &2&lEFEKTY &8•", "&7Nie posiadasz wystarczajacej ilosci &aemeraldow&7.", 10, 30, 10);
                    }
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aNick &8<<"))) {
            e.setCancelled(true);
            p.closeInventory();
            switch (e.getSlot()) {
                case 0: {
                    p.setDisplayName("§0" + p.getName());
                    break;
                }
                case 1: {
                    p.setDisplayName("§1" + p.getName());
                    break;
                }
                case 2: {
                    p.setDisplayName("§2" + p.getName());
                    break;
                }
                case 3: {
                    p.setDisplayName("§3" + p.getName());
                    break;
                }
                case 4: {
                    p.setDisplayName("§4" + p.getName());
                    break;
                }
                case 5: {
                    p.setDisplayName("§5" + p.getName());
                    break;
                }
                case 6: {
                    p.setDisplayName("§6" + p.getName());
                    break;
                }
                case 7: {
                    p.setDisplayName("§7" + p.getName());
                    break;
                }
                case 8: {
                    p.setDisplayName("§8" + p.getName());
                    break;
                }
                case 9: {
                    p.setDisplayName("§9" + p.getName());
                    break;
                }
                case 10: {
                    p.setDisplayName("§b" + p.getName());
                    break;
                }
                case 11: {
                    p.setDisplayName("§c" + p.getName());
                    break;
                }
                case 12: {
                    p.setDisplayName("§d" + p.getName());
                    break;
                }
                case 13: {
                    p.setDisplayName("§e" + p.getName());
                    break;
                }
                case 14: {
                    p.setDisplayName("§f" + p.getName());
                    break;
                }
                case 15: {
                    if (!p.hasPermission("cm.nick.rainbow")) {
                        ChatHelper.title(p, "&8• &2&lNICK &8•", "&7Teczowy nick jest tylko dla rangi &bUVIP", 10, 20, 10);
                        break;
                    }
                    p.setDisplayName(RandomHelper.random(p.getName()));
                    break;
                }
                case 16: {
                    p.setDisplayName(p.getName());
                    break;
                }
                default: {
                    return;
                }
            }
            ChatHelper.title(p, "&8• &2&lNICK &8•", "&7Twoj nick od teraz to: " + p.getDisplayName(), 10, 20, 10);
        } else if (e.getInventory().getName().contains("Otchlan")) {
            AbyssTask abyssTask = this.core.getAbyss();
            switch (e.getSlot()) {
                case 34: {
                    e.setCancelled(true);

                    int value = abyssTask.opened.get(p) - 1;
                    if (value < 0) return;

                    abyssTask.opened.put(p, value);
                    p.openInventory(abyssTask.inventories.get(value));
                    break;
                }
                case 35: {
                    e.setCancelled(true);

                    int value = abyssTask.opened.get(p) + 1;
                    if (value >= abyssTask.inventories.size()) return;

                    abyssTask.opened.put(p, value);
                    p.openInventory(abyssTask.inventories.get(value));
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aDepozyt &8<<"))) {
            e.setCancelled(true);

            User user = this.userManager.get(p.getName());

            if (e.getSlot() == 40) {
                this.core.getLimitManager().withdrawAll(p, user);
                p.sendMessage(ChatHelper.color(" &8>> &7Wyplaciles &awszystkie przedmioty&7."));
            } else if (e.isLeftClick()) {
                this.core.getLimitManager().get(e.getSlot()).ifPresent(limit -> {
                    if (this.core.getLimitManager().withdraw(p, user, limit)) {
                        new DepositMenu(this.core).open(p);
                    }
                });
            } else if (e.isRightClick()) {
                this.core.getLimitManager().get(e.getSlot()).ifPresent(limit -> {
                    int deposit = this.core.getLimitManager().deposit(p, user, limit);

                    if (deposit == 0) {
                        p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz tego przedmiotu w ekwipunku."));
                        return;
                    }

                    p.sendMessage(ChatHelper.color(" &8>> &7Wplaciles &a" + deposit + " przedimotow &7do depozytu&7."));
                    new DepositMenu(this.core).open(p);
                });
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aDrop &8<<"))) {
            e.setCancelled(true);
            switch (e.getSlot()) {
                case 12: {
                    new DropTopMinersMenu(core).show(p);
                    break;
                }
                case 14: {
                    new DropTopCoinsMenu(core).open(p);
                    break;
                }
                case 20: {
                    new DropCaseMenu(core).open(p);
                    break;
                }
                case 22: {
                    new DropStoneMenu(core).show(p);
                    break;
                }
                case 24: {
                    new DropPandoraMenu(core).open(p);
                    break;
                }
                case 30: {
                    new DropStatsMenu(core).show(p);
                    break;
                }
                case 31: {
                    new DropBossListMenu(core).open(p);
                    break;
                }
                case 32: {
                    new DropCobblexMenu(core).open(p);
                    break;
                }
                case 38: {
                    User user = this.userManager.get(p.getName());
                    user.setCobble(!user.isCobble());
                    new DropMenu(core).show(p);
                    break;
                }
                case 40: {
                    User user = this.userManager.get(p.getName());
                    user.setDropOnFullInventory(!user.isDropOnFullInventory());
                    new DropMenu(core).show(p);
                    break;
                }
                case 42: {
                    User user = this.userManager.get(p.getName());
                    user.setDrop(!user.isDrop());
                    new DropMenu(core).show(p);
                    break;
                }
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aDrop z kamienia &8<<"))) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null)
                if (e.getCurrentItem().getType() == Material.GOLD_NUGGET) {
                    User user = this.userManager.get(p.getName());
                    user.setDropCoins(!user.isDropCoins());
                    new DropStoneMenu(core).show(p);
                } else if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE && e.getCurrentItem().getDurability() == (short) 13) {
                    this.dropManager.getDrops().forEach(drop -> drop.setEnabled(p.getName(), true));
                    User user = this.userManager.get(p.getName());
                    user.setDropCoins(true);
                    new DropStoneMenu(core).show(p);
                } else if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE && e.getCurrentItem().getDurability() == (short) 4) {
                    List<ItemStack> guildItems = this.funnyGuilds.getPluginConfiguration().createItems;
                    this.dropManager.getDrops().forEach(drop -> drop.setEnabled(p.getName(), guildItems.stream().anyMatch(itemStack -> itemStack.getType() == drop.getItem())));
                    User user = this.userManager.get(p.getName());
                    user.setDropCoins(true);
                    new DropStoneMenu(core).show(p);
                } else if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE && e.getCurrentItem().getDurability() == (short) 14) {
                    this.dropManager.getDrops().forEach(drop -> drop.setEnabled(p.getName(), false));
                    User user = this.userManager.get(p.getName());
                    user.setDropCoins(false);
                    new DropStoneMenu(core).show(p);
                } else if (e.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                    this.dropManager.getDrops().stream().filter(drop -> e.getCurrentItem().getType() == drop.getItem()).forEach(drop -> drop.setEnabled(p.getName(), !drop.isEnabled(p.getName())));
                    new DropStoneMenu(core).show(p);
                }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aStatystyki &8<<"))) {
            e.setCancelled(true);
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aTop gornicy &8<<"))) {
            e.setCancelled(true);
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aSejfy &8<<"))) {
            e.setCancelled(true);

            if (e.getSlot() >= 11 && e.getSlot() <= 14) {
                int id = e.getSlot() - 10;
                if (!p.hasPermission("cm.safe." + id)) {
                    p.closeInventory();
                    ChatHelper.title(p, "&8• &2&lSEJF &8•", "&7Nie posiadasz uprawnien do &asejfu #" + id, 10, 40, 10);
                    return;
                }
                User user = this.userManager.get(p.getName());
                user.getSafes()[id - 1].open(p);
            } else if (e.getSlot() == 15) {
                if (!p.hasPermission("cm.enderchest")) {
                    p.closeInventory();
                    ChatHelper.title(p, "&8• &2&lSEJF &8•", "&7Nie posiadasz uprawnien do &aenderchesta", 10, 40, 10);
                    return;
                }
                p.openInventory(p.getEnderChest());
            }
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &aWarpy &8<<"))) {
            e.setCancelled(true);
            ItemStack is = e.getCurrentItem();

            this.core.getWarpManager().getWarps().forEach(kit -> {
                if (is != null && is.getType() == kit.getItem()) {
                    p.closeInventory();

                    Location loc = kit.getLocation();

                    if (p.hasPermission("cm.spawn.admin")) {
                        PaperLib.teleportAsync(p, loc)
                                .thenAccept(result -> ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostales przeteleportowany.", 10, 40, 10));
                    } else {
                        if (this.core.getTeleportManager().getTeleports().containsKey(p.getName())) {
                            this.core.getTeleportManager().getTeleports().remove(p.getName()).cancel();
                        }

                        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 160, 0, true));

                        this.core.getTeleportManager().getTeleports().put(p.getName(), new BukkitRunnable() {

                            private int timer = p.hasPermission("cm.fasterteleport") ? 5 : 6;

                            @Override
                            public void run() {
                                timer--;
                                ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostaniesz przeteleportowany za &a" + timer + " &7" + StringHelper.pluralizedTimeText(timer) + ".", 0, 30, 0);
                                if (timer == 0) {
                                    p.removePotionEffect(PotionEffectType.CONFUSION);
                                    PaperLib.teleportAsync(p, loc)
                                            .thenAccept(result -> ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostales przeteleportowany.", 10, 40, 10));
                                    (core.getTeleportManager().getTeleports().remove(p.getName())).cancel();
                                }
                            }
                        }.runTaskTimer(this.core, 0L, 20L));
                    }
                }
            });
        } else if (e.getInventory().getName().equals(ChatHelper.color("&8>> &2Ustawienia &8<<"))) {
            e.setCancelled(true);
            if (e.getSlot() == 13) {
                User user = this.userManager.get(p.getName());
                user.setAutoSell(!user.isAutoSell());
                new SettingsMenu(this.core).open(p);
            }
        } else if (e.getInventory().getName().startsWith(ChatHelper.color("&8Profil "))) {
            e.setCancelled(true);
        } else if (e.getInventory().getName().startsWith(ChatHelper.color("&8>> &7Kit")) && e.getInventory().getHolder() == null) {
            e.setCancelled(true);
        }
    }
}