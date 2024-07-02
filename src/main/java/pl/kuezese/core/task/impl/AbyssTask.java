package pl.kuezese.core.task.impl;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.task.Task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AbyssTask extends Task {

    public boolean open;
    public int time;

    private final List<ItemStack> items;
    public List<Inventory> inventories;
    public Map<Player, Integer> opened;

    private final ItemStack prev;
    private final ItemStack next;

    public AbyssTask() {
        super(20L, 20L, false);

        this.core.setAbyss(this);

        this.open = false;
        this.time = 240;

        this.items = new LinkedList<>();
        this.inventories = new LinkedList<>();
        this.opened = new ConcurrentHashMap<>();

        this.prev = new ItemMaker(Material.SKULL_ITEM, 1, (short) 3).setName(ChatHelper.color("&cPoprzednia strona")).setOwner("MHF_ArrowLeft").make();
        this.next = new ItemMaker(Material.SKULL_ITEM, 1, (short) 3).setName(ChatHelper.color("&aNastepna strona")).setOwner("MHF_ArrowRight").make();
    }

    @Override
    public void run() {
        this.time--;

        switch (this.time) {
            case 5: {
                int entities = (int) this.core.getServer().getWorlds()
                        .stream()
                        .map(World::getEntities)
                        .flatMap(Collection::stream)
                        .filter(entity -> entity instanceof Item)
                        .count();

                if (entities == 0) {
                    this.time = 120;
                    break;
                }

                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Otchlan zostanie otwarta za &a" + this.parseSeconds(this.time) + "&7."));
                break;
            }
            case 4:
            case 3:
            case 2:
            case 1: {
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Otchlan zostanie otwarta za &a" + this.parseSeconds(this.time) + "&7."));
                break;
            }

            case 0: {
                this.core.getServer().getWorlds()
                        .stream()
                        .map(World::getEntities)
                        .flatMap(Collection::stream)
                        .filter(entity -> entity instanceof Item)
                        .forEach(entity -> {
                            ItemStack is = ((Item) entity).getItemStack();
                            this.items.add(is);
                            entity.remove();
                        });

                int items = this.items.size();

                int inventories = 1;

                for (int size = this.items.size(); size >= 27; size -= 27) inventories++;

                for (int i = 0; i <= inventories - 1; i++) {
                    Inventory inv = this.core.getServer().createInventory(null, 36, ChatHelper.color("&aOtchlan &8>> &7" + (i + 1) + "/" + inventories));
                    inv.setItem(34, this.prev);
                    inv.setItem(35, this.next);
                    for (int i1 = 0; i1 <= 26; i1++) {
                        if (!this.items.isEmpty()) {
                            inv.addItem(this.items.get(0));
                            this.items.remove(0);
                        }
                    }
                    this.inventories.add(inv);
                }

                this.open = true;
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Otchlan z &a" + items + " przedmiotami &7zostala otwarta na &a60 sekund&7."));
                break;
            }

            case -60: {
                this.open = false;
                this.time = 240;

                this.items.clear();
                this.inventories.clear();
                this.opened.clear();

                this.core.getServer().getOnlinePlayers().stream().filter(p -> p.getOpenInventory() != null && p.getOpenInventory().getTitle() != null && p.getOpenInventory().getTitle().contains("Otchlan")).forEach(HumanEntity::closeInventory);
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Otchlan zostala &azamknieta&7."));
                break;
            }
        }
    }

    private String parseSeconds(int time) {
        switch (time) {
            case 4:
            case 3:
            case 2:
                return time + " sekundy";
            case 1:
                return "1 sekunde";
        }
        return time + " sekund";
    }
}
