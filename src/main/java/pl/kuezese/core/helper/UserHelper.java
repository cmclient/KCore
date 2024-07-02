package pl.kuezese.core.helper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.object.User;
import pl.kuezese.core.type.ShopType;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public final class UserHelper {

    public static String serializeKits(User user) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> entry : user.getKitCooldowns().entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey()).append(':').append(entry.getValue());
        }
        return sb.toString();
    }

    public static Map<String, Long> deserializeKits(String data) {
        Map<String, Long> map = new ConcurrentHashMap<>();
        if (data.isEmpty()) return map;

        for (String s : data.split(",")) {
            String[] split1 = s.split(":");
            map.put(split1[0], Long.parseLong(split1[1]));
        }

        return map;
    }

    public static String serializeLimits(User user) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : user.getLimits().entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey()).append(':').append(entry.getValue());
        }
        return sb.toString();
    }

    public static Map<Integer, Integer> deserializeLimits(String data) {
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        if (data.isEmpty()) return map;

        for (String s : data.split(",")) {
            String[] split1 = s.split(":");
            map.put(Integer.parseInt(split1[0]), Integer.parseInt(split1[1]));
        }

        return map;
    }

    public static String serializeSafes(User user) {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(user.getSafes()).forEach(safe -> {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(safe.getId()).append(':').append(safe.serialize());
        });

        return sb.toString();
    }

    public static void deserializeSafes(User user, String data) {
        if (data.isEmpty()) return;

        for (String s : data.split(",")) {
            String[] split = s.split(":");

            if (split.length == 2) {
                int safeId = Integer.parseInt(split[0]) - 1;
                user.getSafes()[safeId].deserialize(split[1]);
            }
        }
    }

    public static void checkForLevelUp(Player p, User user) {
        if (user.getXp() > (user.getLvl() + 1) * (user.getLvl() * 5000)) {
            user.setLvl(user.getLvl() + 1);

            if (user.getLvl() % 5 == 0) {
                Bukkit.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + user.getName() + " &7awansowal na &a" + user.getLvl() + " &7poziom kopania!"));
            }

//            if (user.getLvl() % 10 == 0) {
//                Bukkit.getOnlinePlayers().forEach(player -> ChatHelper.title(player, "&8• &2&lGORNICTWO &8•", "&7Gracz &a" + p.getName() + " &7awansowal na &a" + user.getLvl() + " &7poziom kopania.", 10, 30, 10));
//            }

            float coins = Math.min(500.F, user.getLvl() * 2.5F);
            user.addCoins(coins);

            ChatHelper.title(p, "&8>> &7Awans na &a" + user.getLvl() + " poziom", "&a+" + coins + "$ &7coins", 20, 30,40);
            FireworkHelper.spawn(p.getLocation());
        }
    }

    public static void sellAll(CorePlugin core, Player p, User user) {
        AtomicReference<Float> coins = new AtomicReference<>(0.0F);

        core.getShopManager().get(ShopType.SELL).forEach(shopItem -> {
            int playerAmount = ItemHelper.getAmount(p, shopItem.getItem(), shopItem.getItemStack().getDurability(), null);
            if (playerAmount != 0) {
                ItemHelper.remove(p, shopItem.getItem(), shopItem.getItemStack().getDurability(), null, playerAmount);
                int sellAmount = shopItem.getAmount();
                double sellPrice = shopItem.getPrice();
                double ratio = (double) playerAmount / sellAmount;
                double actualSellPrice = sellPrice * ratio;
                coins.updateAndGet(v -> (float) (v + actualSellPrice));
            }
        });

        float added = coins.get();

        if (added == 0.0F) {
            ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Nie masz czego sprzedac.", 10, 30, 10);
        } else {
            user.addCoins(added);
            ChatHelper.title(p, "&8• &2&lSKLEP &8•", "&7Sprzedales wszystkie przedmioty. (&a+" + user.formatCoins(added) + "$&7)", 10, 30, 10);
        }
    }
}
