package pl.kuezese.core.listener.impl;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.helper.UserHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.manager.impl.ShopManager;
import pl.kuezese.core.object.StoneGenerator;
import pl.kuezese.core.object.User;
import pl.kuezese.core.type.ShopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BlockBreakListener extends Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBreakGenerator(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        if (block.getType() == Material.STONE) {
            Location loc = block.getLocation();
            StoneGenerator generator = this.generatorManager.get(loc);

            if (generator != null && this.regionManager.find(loc) != null) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        User user = this.userManager.get(player.getName());
        List<ItemStack> items = new ArrayList<>();

        event.setCancelled(true);
        this.core.getAntiGriefManager().get(block).ifPresent(data -> this.core.getAntiGriefManager().getBlocks().remove(data));

        if (block.getType() == Material.STONE) {
            Location loc = block.getLocation();
            StoneGenerator generator = this.generatorManager.get(loc);

            if (generator != null) {
                if (player.getItemInHand().getType() == Material.GOLD_PICKAXE) {
//                    e.setCancelled(true);
                    if (this.regionManager.find(loc) == null || player.isOp()) {
                        this.generatorManager.remove(loc, player);
                        this.recalculateDurability(player, player.getItemInHand());
                        player.sendMessage(ChatHelper.color(" &8>> &7Usunales &agenerator &7stone."));
                    }
                    return;
                }
                generator.regen(this.core);
            }

            if (player.getGameMode() == GameMode.SURVIVAL) {
                if (user.isCobble()) {
                    items.addAll(block.getDrops(player.getItemInHand()));
                }

                AtomicReference<Float> soldPriceReference = new AtomicReference<>(Float.NaN);

                if (user.isDrop()) {
                    this.core.getDropManager().getDrops().stream().filter(drop -> drop.isEnabled(player.getName()) && drop.chance(player, user)).forEach(drop -> {
                        items.add(new ItemStack(drop.getItem(), this.getFortuneBonus(player.getItemInHand()), drop.getData()));
                        player.giveExp(drop.getExp());
                        user.addXp(drop.getXp());
                    });

                    if (user.isAutoSell()) {
                        List<ItemStack> soldItems = new ArrayList<>();
                        List<ShopManager.ShopItem> shopItems = this.core.getShopManager().get(ShopType.SELL);

                        items.stream()
                                .filter(item -> shopItems.stream()
                                        .anyMatch(shopItem -> item.getType() == shopItem.getItem()))
                                .forEach(item -> {
                                    ShopManager.ShopItem matchingShopItem = shopItems.stream()
                                            .filter(shopItem -> item.getType() == shopItem.getItem())
                                            .findFirst()
                                            .orElse(null);

                                    if (matchingShopItem != null) {
                                        int playerAmount = item.getAmount();
                                        int sellAmount = matchingShopItem.getAmount();
                                        double sellPrice = matchingShopItem.getPrice();
                                        double ratio = (double) playerAmount / sellAmount;
                                        double actualSellPrice = sellPrice * ratio;
                                        if (soldPriceReference.get().isNaN()) {
                                            soldPriceReference.set(0.0F);
                                        }
                                        soldPriceReference.updateAndGet(v -> v + (float) actualSellPrice);
                                        user.addCoins((float) actualSellPrice);
                                        soldItems.add(item);
                                    }
                                });

                        items.removeAll(soldItems);
                    }
                }

                Float soldPrice = soldPriceReference.get();
                Float dropCoins = Float.NaN;

                if (user.isDropCoins()) {
                    double chance = (user.isUsingClient() || player.hasPermission("cm.drop.vip")) ? 15.0D * 1.20D : 15.0D;

                    if (RandomHelper.chance(chance)) {
                        dropCoins = (float) RandomHelper.nextDouble(0.01D, 0.05D);
                        player.giveExp(50);
                        user.addCoins(dropCoins);
                        user.addXp(20);
//                        ChatHelper.actionBar(player, "&8>> &7Wykopales &a" + user.formatCoins(dropCoins) + "$&7. Twoj stan portfela: &a" + user.getCoinsFormatted() + "&7$");
                    }
                }

                if (!soldPrice.isNaN() || !dropCoins.isNaN()) {
                    StringBuilder builder = new StringBuilder("&8>> ");

                    if (!dropCoins.isNaN()) {
                        builder.append("&7Wykopales &a").append(user.formatCoins(dropCoins)).append("$&7.");
                    }

                    if (!soldPrice.isNaN()) {
                        if (!dropCoins.isNaN()) {
                            builder.append(" &8| ");
                        }
                        builder.append("&7Sprzedano wszystko za &a").append(user.formatCoins(soldPrice)).append("$&7.");
                    }

                    builder.append(" Stan portfela: &a").append(user.getCoinsFormatted()).append("&7$");
                    ChatHelper.actionBar(player, builder.toString());
                }

                UserHelper.checkForLevelUp(player, user);
            }
        } else {
            items.addAll(block.getDrops(player.getItemInHand()));
        }

        if (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2) {
            this.core.getRealChop().blockBreak(block, player.getLocation());
        }

        if (player.getGameMode() == GameMode.SURVIVAL) {
            HashMap<Integer, ItemStack> map = player.getInventory().addItem(items.toArray(new ItemStack[0]));

            if (user.isDropOnFullInventory()) {
                map.values().forEach(is -> block.getLocation().getWorld().dropItem(block.getLocation(), is));
            }

            this.recalculateDurability(player, player.getItemInHand());
        }

        block.setType(Material.AIR);
    }

    private int getFortuneBonus(ItemStack item) {
//        return item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) ? RandomHelper.nextInt(1, item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1) : 1;
        int fortune = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        if (fortune == 0)
            return 1;

        int i = RandomHelper.nextInt(fortune + 2) - 1;

        if (i < 0) {
            i = 0;
        }

        return (i + 1);
    }

    private void recalculateDurability(Player player, ItemStack item) {
        if (item.getType().getMaxDurability() == 0) {
            return;
        }
        int enchantLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
        short d = item.getDurability();
        if (enchantLevel > 0) {
            if (100 / (enchantLevel + 1) > RandomHelper.nextInt(0, 100)) {
                if (d == item.getType().getMaxDurability()) {
                    player.getInventory().clear(player.getInventory().getHeldItemSlot());
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                } else {
                    item.setDurability((short) (d + 1));
                }
            }
        } else if (d == item.getType().getMaxDurability()) {
            player.getInventory().clear(player.getInventory().getHeldItemSlot());
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
        } else {
            item.setDurability((short) (d + 1));
        }
    }
}