package pl.kuezese.core.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.InventoryHelper;
import pl.kuezese.core.object.ItemMaker;

@RequiredArgsConstructor
public class CraftingsMenu {
    
    private final CorePlugin core;
    private final ItemStack generator = new ItemMaker(Material.STONE).setName(ChatHelper.color("&aGenerator Stone")).make();
    private final ItemStack boyfarmer = new ItemMaker(Material.ENDER_PORTAL_FRAME).setName(ChatHelper.color("&8>> &7Boy Farmer")).make();
    private final ItemStack sandfarmer = new ItemMaker(Material.SANDSTONE).setName(ChatHelper.color("&8>> &7Sand Farmer")).make();
    private final ItemStack kopacz = new ItemMaker(Material.STONE).setName(ChatHelper.color("&8>> &7Kopacz Fosy")).make();
    private final ItemStack enderchest = new ItemMaker(Material.ENDER_CHEST).setName(ChatHelper.color("&8>> &5Ender Chest")).make();
    private final ItemStack rzucanetnt = new ItemMaker(Material.TNT).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&8>> &7Rzucane TNT")).make();
    private final ItemStack antynogi = new ItemMaker(Material.MILK_BUCKET).setName(ChatHelper.color("&8>> &aAnty-Nogi")).make();
    private final ItemStack create = new ItemMaker(Material.TRIPWIRE_HOOK).setName(ChatHelper.color("&8>> &aKliknij, aby stworzyc")).make();

    private final ItemStack redstone = new ItemStack(Material.REDSTONE);
    private final ItemStack iron_ingot = new ItemStack(Material.IRON_INGOT);
    private final ItemStack stone = new ItemStack(Material.STONE);
    private final ItemStack piston = new ItemStack(Material.PISTON_BASE);
    private final ItemStack diamond_pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
    private final ItemStack obsidian = new ItemStack(Material.OBSIDIAN);
    private final ItemStack emerald_block = new ItemStack(Material.EMERALD_BLOCK);
    private final ItemStack ender_pearl = new ItemStack(Material.ENDER_PEARL);
    private final ItemStack sand = new ItemStack(Material.SAND);
    private final ItemStack tnt = new ItemStack(Material.TNT, 64);
    private final ItemStack golden_apple = new ItemStack(Material.GOLDEN_APPLE);

    public void open(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 27, ChatHelper.color( "&8>> &aCraftingi &8<<"));
        inv.setItem(10, generator);
        inv.setItem(11, boyfarmer);
        inv.setItem(12, sandfarmer);
        inv.setItem(13, kopacz);
        inv.setItem(14, enderchest);
        inv.setItem(15, rzucanetnt);
        inv.setItem(16, antynogi);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }

    public void openStoneGeneratorGui(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aGenerator Stone &8<<"));
        inv.setItem(11, redstone);
        inv.setItem(12, iron_ingot);
        inv.setItem(13, redstone);
        inv.setItem(20, iron_ingot);
        inv.setItem(21, stone);
        inv.setItem(22, iron_ingot);
        inv.setItem(29, redstone);
        inv.setItem(30, piston);
        inv.setItem(31, redstone);
        inv.setItem(24, generator);
        inv.setItem(25, create);
        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }

    public void openBoyFarmerGUI(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aBoy farmer &8<<"));
        inv.setItem(11, obsidian);
        inv.setItem(12, emerald_block);
        inv.setItem(13, obsidian);
        inv.setItem(20, emerald_block);
        inv.setItem(21, obsidian);
        inv.setItem(22, emerald_block);
        inv.setItem(29, obsidian);
        inv.setItem(30, emerald_block);
        inv.setItem(31, obsidian);
        inv.setItem(24, boyfarmer);
        inv.setItem(25, create);
        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }

    public void openSandFamerGUI(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aSand farmer &8<<"));
        inv.setItem(11, emerald_block);
        inv.setItem(12, emerald_block);
        inv.setItem(13, emerald_block);
        inv.setItem(20, emerald_block);
        inv.setItem(21, sand);
        inv.setItem(22, emerald_block);
        inv.setItem(29, emerald_block);
        inv.setItem(30, emerald_block);
        inv.setItem(31, emerald_block);
        inv.setItem(24, sandfarmer);
        inv.setItem(25, create);
        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }

    public void openKopaczFosGUI(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aKopacz fos &8<<"));
        inv.setItem(11, emerald_block);
        inv.setItem(12, emerald_block);
        inv.setItem(13, emerald_block);
        inv.setItem(20, emerald_block);
        inv.setItem(21, diamond_pickaxe);
        inv.setItem(22, emerald_block);
        inv.setItem(29, emerald_block);
        inv.setItem(30, emerald_block);
        inv.setItem(31, emerald_block);
        inv.setItem(24, kopacz);
        inv.setItem(25, create);
        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }

    public void openEnderChestGUI(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aEnder Chest &8<<"));
        inv.setItem(11, obsidian);
        inv.setItem(12, obsidian);
        inv.setItem(13, obsidian);
        inv.setItem(20, obsidian);
        inv.setItem(21, ender_pearl);
        inv.setItem(22, obsidian);
        inv.setItem(29, obsidian);
        inv.setItem(30, obsidian);
        inv.setItem(31, obsidian);
        inv.setItem(24, enderchest);
        inv.setItem(25, create);
        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }

    public void openRzucaneTntGui(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aRzucane TNT &8<<"));
        inv.setItem(11, tnt);
        inv.setItem(12, golden_apple);
        inv.setItem(13, tnt);
        inv.setItem(20, golden_apple);
        inv.setItem(21, ender_pearl);
        inv.setItem(22, golden_apple);
        inv.setItem(29, tnt);
        inv.setItem(30, golden_apple);
        inv.setItem(31, tnt);
        inv.setItem(24, rzucanetnt);
        inv.setItem(25, create);
        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }



    public void openAntyNogiGui(Player p) {
        Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aAnty Nogi &8<<"));
        inv.setItem(11, emerald_block);
        inv.setItem(12, golden_apple);
        inv.setItem(13, emerald_block);
        inv.setItem(20, golden_apple);
        inv.setItem(21, ender_pearl);
        inv.setItem(22, golden_apple);
        inv.setItem(29, emerald_block);
        inv.setItem(30, golden_apple);
        inv.setItem(31, emerald_block);
        inv.setItem(24, antynogi);
        inv.setItem(25, create);
        ItemStack is = new ItemMaker(Material.BARRIER).setName(ChatHelper.color(" &8>> &aKliknij, aby cofnac do poprzedniego menu")).make();
        inv.setItem(inv.getSize() - 1, is);
        InventoryHelper.backgroundEmpty(inv);
        p.openInventory(inv);
    }
}
