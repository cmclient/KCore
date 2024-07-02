package pl.kuezese.core.listener;

import pl.kuezese.core.CorePlugin;
import pl.kuezese.region.manager.RegionManager;
import pl.kuezese.core.manager.impl.*;

public class Listener implements org.bukkit.event.Listener {

    public Listener() {
        this.core.getServer().getPluginManager().registerEvents(this, this.core);
    }

    protected final CorePlugin core = CorePlugin.getInstance();
    protected final UserManager userManager = this.core.getUserManager();
    protected final BanManager banManager = this.core.getBanManager();
    protected final DropManager dropManager = this.core.getDropManager();
    protected final ShopManager shopManager = this.core.getShopManager();
    protected final VillagerManager villagerManager = this.core.getVillagerManager();
    protected final GeneratorManager generatorManager = this.core.getGeneratorManager();
    protected final RegionManager regionManager = this.core.getRegionManager();
}
