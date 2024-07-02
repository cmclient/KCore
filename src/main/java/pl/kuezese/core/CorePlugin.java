package pl.kuezese.core;

import com.keenant.bossy.Bossy;
import lombok.Getter;
import lombok.Setter;
import net.luckperms.api.LuckPerms;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import pl.kuezese.region.SimpleRegions;
import pl.kuezese.region.manager.RegionManager;
import pl.kuezese.core.command.impl.DepositCommand;
import pl.kuezese.core.config.Configuration;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.ClassHelper;
import pl.kuezese.core.listener.impl.InventoryClickListener;
import pl.kuezese.core.manager.impl.*;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.StoneGenerator;
import pl.kuezese.core.packet.AuthPacket;
import pl.kuezese.core.sql.SQL;
import pl.kuezese.core.task.impl.AbyssTask;
import pl.kuezese.core.task.impl.DepositTask;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public @Getter class CorePlugin extends JavaPlugin {

    private @Getter static CorePlugin instance;

    private boolean loaded;
    private ChatManager chatManager;
    private Configuration configuration;
    private SQL sql;
    private UserManager userManager;
    private BanManager banManager;
    private DropManager dropManager;
    private TellManager tellManager;
    private TeleportManager teleportManager;
    private WarpManager warpManager;
    private ShopManager shopManager;
    private CaseManager caseManager;
    private PandoraManager pandoraManager;
    private CobblexManager cobblexManager;
    private KitManager kitManager;
    private VillagerManager villagerManager;
    private LimitManager limitManager;
    private BossManager bossManager;
    private GeneratorManager generatorManager;
    private RegionManager regionManager;
    private AntiGriefManager antiGriefManager;
    private @Setter AbyssTask abyss;
    private LuckPerms api;
    private RealChop realChop;
    private Bossy bossy;
    private AtomicInteger redstoneOps;
    private AtomicInteger vehicleMoves;

    public CorePlugin() {
        CorePlugin.instance = this;
    }

    @Override
    public void onEnable() {
        {
            this.getLogger().info("Initializing dependencies...");
            // simpleregions
            SimpleRegions simpleRegions = new SimpleRegions(this);
            this.regionManager = simpleRegions.getRegionManager();
            simpleRegions.onEnable();
            // realchlop
            this.realChop = new RealChop(this);
            // bossyapi
            this.bossy = new Bossy(this);
            this.getLogger().info("Completed, enabling plugin.");
        }

        this.chatManager = new ChatManager();
        this.configuration = new Configuration(this);
        this.sql = new SQL(this);
        this.userManager = new UserManager();
        this.banManager = new BanManager();
        this.dropManager = new DropManager();
        this.tellManager = new TellManager();
        this.warpManager = new WarpManager();
        this.teleportManager = new TeleportManager();
        this.shopManager = new ShopManager();
        this.caseManager = new CaseManager();
        this.pandoraManager = new PandoraManager();
        this.cobblexManager = new CobblexManager();
        this.kitManager = new KitManager();
        this.villagerManager = new VillagerManager();
        this.limitManager = new LimitManager();
        this.bossManager = new BossManager();
        this.generatorManager = new GeneratorManager();
        this.antiGriefManager = new AntiGriefManager();
        this.api = this.getServer().getServicesManager().getRegistration(LuckPerms.class).getProvider();
        this.redstoneOps = new AtomicInteger();
        this.vehicleMoves = new AtomicInteger();

        if (this.sql.connect(this)) {
            this.sql.update("CREATE TABLE IF NOT EXISTS `core` (`name` varchar(16) PRIMARY KEY NOT NULL, `coins` DECIMAL(9,2) NOT NULL, `xp` int NOT NULL, `lvl` int NOT NULL, `cobble` int(1) NOT NULL, `drop` int(1) NOT NULL, `dropOnFullInventory` int(1) NOT NULL, `dropCoins` int(1) NOT NULL, `kits` text NOT NULL, `limits` text NOT NULL, `home` text NOT NULL, `safes` LONGTEXT NOT NULL);");
            this.sql.update("CREATE TABLE IF NOT EXISTS `bans` (`name` varchar(16) PRIMARY KEY NOT NULL, `admin` text NOT NULL, `time` bigint(22) NOT NULL, `reason` text NOT NULL);");
            this.sql.update("CREATE TABLE IF NOT EXISTS `stone_generator` (`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT, `world` varchar(16) NOT NULL, `x` int NOT NULL, `y` int NOT NULL, `z` int NOT NULL);");
            this.userManager.load();
            this.banManager.load();
            this.generatorManager.load();
        } else {
            this.getServer().shutdown();
        }

        this.getLogger().info("Loading listeners...");
        this.loadClasses(InventoryClickListener.class);
        this.getLogger().info("Loading commands...");
        this.loadClasses(DepositCommand.class);
        this.getLogger().info("Loading tasks...");
        this.loadClasses(DepositTask.class);
        this.getLogger().info("Loading recipes...");
        this.loadRecipes();
        this.getLogger().info("Loading packet messenger...");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "cp:auth", new AuthPacket(this));

        this.loaded = true;
        this.getLogger().info("Plugin " + getDescription().getFullName() + " by " + getDescription().getAuthors().get(0) + " has been loaded successfully.");
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);

        if (!this.loaded) {
            this.getLogger().warning("Plugin " + getDescription().getFullName() + " by " + getDescription().getAuthors().get(0) + " failed to load!");
            this.getServer().shutdown();
            return;
        }

        this.userManager.get().values().forEach(user -> {
            user.setDamager(null);
            user.setAttackTime(0L);
            user.update();
        });

        this.generatorManager.getGenerators().values().forEach(StoneGenerator::regen);
        this.antiGriefManager.getBlocks().forEach((data, time) -> data.getBlock().setType(data.getPrevious()));
        this.sql.executor.shutdown();
        try {
            if (!this.sql.executor.awaitTermination(10, TimeUnit.SECONDS)) {
                CorePlugin.getInstance().getLogger().log(Level.SEVERE, "Database shutdown timed out!");
            }
        } catch (InterruptedException ex) {
            CorePlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to shutdown database!", ex);
        }
        this.getLogger().info("Plugin " + getDescription().getFullName() + " by " + getDescription().getAuthors().get(0) + " has been disabled successfully.");
    }

    private void loadRecipes() {
        this.getServer().addRecipe(new ShapedRecipe(new ItemMaker(Material.STONE).setName(ChatHelper.color("&aGenerator Stone")).make())
                .shape("121", "232", "141")
                .setIngredient('1', Material.REDSTONE)
                .setIngredient('2', Material.IRON_INGOT)
                .setIngredient('3', Material.STONE)
                .setIngredient('4', Material.PISTON_BASE));

        this.getServer().addRecipe(new ShapedRecipe(new ItemMaker(Material.ENDER_PORTAL_FRAME).setName(ChatHelper.color("&8>> &7Boy Farmer")).make())
                .shape("121", "212", "121")
                .setIngredient('1', Material.OBSIDIAN)
                .setIngredient('2', Material.EMERALD_BLOCK));

        this.getServer().addRecipe(new ShapedRecipe(new ItemMaker(Material.SANDSTONE).setName(ChatHelper.color("&8>> &7Sand Farmer")).make())
                .shape("111", "121", "111")
                .setIngredient('1', Material.EMERALD_BLOCK)
                .setIngredient('2', Material.SAND));

        this.getServer().addRecipe(new ShapedRecipe(new ItemMaker(Material.STONE).setName(ChatHelper.color("&8>> &7Kopacz Fosy")).make())
                .shape("111", "121", "111")
                .setIngredient('1', Material.EMERALD_BLOCK)
                .setIngredient('2', Material.DIAMOND_PICKAXE));

        this.getServer().addRecipe(new ShapedRecipe(new ItemStack(Material.ENDER_CHEST))
                .shape("111", "121", "111")
                .setIngredient('1', Material.OBSIDIAN)
                .setIngredient('2', Material.ENDER_PEARL));

        this.getServer().addRecipe(new ShapedRecipe(new ItemMaker(Material.TNT).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&8>> &7Rzucane TNT")).make())
                .shape("131", "323", "131")
                .setIngredient('1', Material.TNT)
                .setIngredient('2', Material.ENDER_PEARL)
                .setIngredient('3', Material.GOLDEN_APPLE));

        this.getServer().addRecipe(new ShapedRecipe(new ItemMaker(Material.MILK_BUCKET).setName(ChatHelper.color("&8>> &aAnty-Nogi")).make())
                .shape("131", "323", "131")
                .setIngredient('1', Material.EMERALD_BLOCK)
                .setIngredient('2', Material.ENDER_PEARL)
                .setIngredient('3', Material.GOLDEN_APPLE));
    }

    private void loadClasses(Class<?> clazz) {
        List<Class<?>> classes = ClassHelper.find(clazz);
        this.getLogger().info("Found " + classes.size() + " classes.");
        classes.stream().filter(aClass -> !aClass.isAnonymousClass()).forEach(aClass -> {
            try {
                aClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                this.getLogger().log(Level.SEVERE, "Failed to register class " + clazz.getSimpleName() + ".", ex);
            }
        });
    }
}