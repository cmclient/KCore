package pl.kuezese.core.helper;

import com.google.common.base.Strings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import pl.kuezese.core.CorePlugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

public final class ItemHelper {

    public static String serialize(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException ex) {
            CorePlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to serialize items!", ex);
            return "";
        }
    }

    public static ItemStack[] deserialize(String data) {
        if (data.isEmpty()) return null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return items;
        } catch (IOException | ClassNotFoundException ex) {
            CorePlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to deserialize items!", ex);
            return null;
        }
    }

    public static int getAmount(Player p, Material material, short data, String name) {
        return Arrays.stream(p.getInventory().getContents()).filter(is -> is != null && is.getType() == material && is.getDurability() == data).filter(is -> Strings.isNullOrEmpty(name) || (is.getItemMeta().getDisplayName() != null && is.getItemMeta().getDisplayName().equals(name))).mapToInt(ItemStack::getAmount).sum();
    }

    public static void remove(Player p, Material material, short data, String name, int amount) {
        ItemStack[] contents = p.getInventory().getContents();
        for (int i = contents.length - 1; i >= 0; i--) {
            ItemStack is = contents[i];
            if (is != null && is.getType() == material && is.getDurability() == data) {
                if (!Strings.isNullOrEmpty(name) && (is.getItemMeta().getDisplayName() == null || !is.getItemMeta().getDisplayName().equals(name))) {
                    continue;
                }

                int remove = Math.min(is.getMaxStackSize(), amount);

                if (is.getAmount() > remove) {
                    is.setAmount(is.getAmount() - remove);
                    amount -= remove;
                    continue;
                }

                p.getInventory().clear(i);
                amount -= is.getAmount();
            }
        }
    }
}
