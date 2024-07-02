package pl.kuezese.core.listener.impl;

import com.google.common.base.Strings;
import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.guild.Guild;
import net.dzikoysk.funnyguilds.guild.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import panda.std.Option;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.FireworkHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.manager.impl.AntiGriefManager;
import pl.kuezese.core.object.CaseItem;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.StoneGenerator;
import pl.kuezese.core.object.User;

import java.util.Collections;
import java.util.List;

public class BlockPlaceListener extends Listener {

    private final FunnyGuilds funnyGuilds;

    public BlockPlaceListener() {
        this.funnyGuilds = FunnyGuilds.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block eBlock = e.getBlock();
        Location loc = eBlock.getLocation();

        switch (p.getItemInHand().getType()) {
//            case TNT: {
//                if (e.getBlock().getLocation().getBlockY() > 50) {
//                    p.sendMessage(ChatHelper.color(" &8>> &cTNT mozna stawiac tylko ponizej 50 poziomu Y."));
//                    e.setCancelled(true);
//                }
//                return;
//            }
//            case RAILS:
//            case ACTIVATOR_RAIL:
//            case POWERED_RAIL:
//            case DETECTOR_RAIL: {
//                if (e.getBlock().getLocation().getBlockY() > 50) {
//                    p.sendMessage(ChatHelper.color(" &8>> &cTory mozna stawiac tylko ponizej 50 poziomu Y."));
//                    e.setCancelled(true);
//                }
//                return;
//            }
            case ENDER_PORTAL_FRAME: {
                String name = p.getItemInHand().getItemMeta().getDisplayName();
                if (name != null && name.equals(ChatHelper.color("&8>> &7Boy Farmer"))) {
                    net.dzikoysk.funnyguilds.user.User user = this.funnyGuilds.getUserManager().findByName(p.getName()).get();
                    Guild guild = user.getGuild().orNull();

                    if (guild == null || !guild.getRegion().get().isIn(loc)) {
                        p.sendMessage(ChatHelper.color(" &8>> &aBoy Farmery &7mozna stawiac tylko na terenie swojej &agildii&7."));
                        e.setCancelled(true);
                        return;
                    }

                    eBlock.setType(Material.OBSIDIAN);

                    for (int i = eBlock.getY() - 1; i > 0; --i) {
                        Block block = eBlock.getWorld().getBlockAt(eBlock.getX(), i, eBlock.getZ());

                        if (block.getType() == Material.OBSIDIAN) {
                            continue;
                        }

                        if (block.getType() != Material.AIR) {
                            return;
                        }

                        block.setType(Material.OBSIDIAN);
                    }
                } else e.setCancelled(true);
                return;
            }
            case SANDSTONE: {
                String name = p.getItemInHand().getItemMeta().getDisplayName();
                if (name != null && name.equals(ChatHelper.color("&8>> &7Sand Farmer"))) {
                    net.dzikoysk.funnyguilds.user.User user = this.funnyGuilds.getUserManager().findByName(p.getName()).get();

                    Guild guild = user.getGuild().orNull();

                    if (guild == null || !guild.getRegion().get().isIn(loc)) {
                        p.sendMessage(ChatHelper.color(" &8>> &aSand Farmery &7mozna stawiac tylko na terenie swojej &agildii&7."));
                        e.setCancelled(true);
                        return;
                    }

                    eBlock.setType(Material.SAND);

                    for (int i = eBlock.getY() - 1; i > 0; --i) {
                        Block block = eBlock.getWorld().getBlockAt(eBlock.getX(), i, eBlock.getZ());

                        if (block.getType() == Material.SAND) {
                            continue;
                        }

                        if (block.getType() != Material.AIR) {
                            return;
                        }

                        block.setType(Material.SAND);
                    }
                }
                return;
            }
            case STONE: {
                String name = p.getItemInHand().getItemMeta().getDisplayName();

                if (name != null) {
                    if (name.equals(ChatHelper.color("&aGenerator Stone"))) {
                        StoneGenerator found = this.generatorManager.get(loc);

                        if (found != null) {
                            e.setCancelled(true);
                            if (eBlock.getType() == Material.AIR) {
                                found.regen();
                                return;
                            }
                            p.sendMessage(ChatHelper.color(" &8>> &7W tym miejscu jest juz stworzony &agenerator stone&7."));
                            return;
                        }

                        StoneGenerator generator = new StoneGenerator(this.core, loc);
                        this.generatorManager.add(generator);
                        loc.getBlock().setType(Material.STONE);
                        p.sendMessage(ChatHelper.color(" &8>> &7Stworzyles &agenerator stone&7. Mozesz zniszczyc go &ezlotym kilofem&7."));
                        return;
                    }

                    if (name.equals(ChatHelper.color("&8>> &7Kopacz Fosy"))) {
                        net.dzikoysk.funnyguilds.user.User user = this.funnyGuilds.getUserManager().findByName(p.getName()).get();
                        Guild guild = user.getGuild().orNull();

                        if (guild == null || !guild.getRegion().get().isIn(loc)) {
                            p.sendMessage(ChatHelper.color(" &8>> &aKopacze fosy &7mozna stawiac tylko na terenie swojej &agildii&7."));
                            e.setCancelled(true);
                            return;
                        }

                        eBlock.setType(Material.AIR);
                        for (int i = eBlock.getY() - 1; i > 0; --i) {
                            Block block = eBlock.getWorld().getBlockAt(eBlock.getX(), i, eBlock.getZ());

                            if (block.getType() == Material.STONE && this.generatorManager.get(block.getLocation()) != null) {
                                this.generatorManager.remove(block.getLocation(), null);
                            } else if (block.getType() == Material.BEDROCK || block.getType() == Material.SPONGE) {
                                return;
                            }

                            block.setType(Material.AIR);
                        }
                    }
                    return;
                }
            }
            case CHEST: {
                String name = p.getItemInHand().getItemMeta().getDisplayName();
                if (name != null && name.equals(ChatHelper.color("&8[&9&lPremiumCase&8]"))) {
                    e.setCancelled(true);

                    User user = this.userManager.get(p.getName());

                    if (user.isCombat()) {
                        p.sendMessage(ChatHelper.color(" &8>> &7Nie mozesz otwierac &aPremiumCase &7podczas walki."));
                        return;
                    }

                    if (p.getItemInHand().getAmount() > 1)
                        p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                    else p.setItemInHand(null);


                    StringBuilder builder = new StringBuilder();

                    builder.append(ChatHelper.color("&8&l&m------------------------&r\n"));
                    builder.append(ChatHelper.color(" &8>> &a" + p.getName() + " &7otworzyl &9&lPremium Case &7i wylosowal:\n"));

                    this.core.getCaseManager().getItems().stream().filter(item -> RandomHelper.chance(item.getChance())).forEach(item -> {
                        ItemStack is = new ItemMaker(item.getItemStack()).make();
                        p.getWorld().dropItem(loc, is);
                        String customName = Strings.isNullOrEmpty(item.getName()) ? item.getItem().name() : item.getName()
                                .replace("&8>> ", "")
                                .trim();
                        builder.append(ChatHelper.color(" &7>> &ax" + item.getAmount() + " " + customName + " &8(&a" + user.formatCoins((float) item.getChance()) + "%&8)\n"));
                    });

                    builder.append(ChatHelper.color(" &8>> &9&lPremium Case &7zakupisz na: &7www.CoreMax.pl\n"));
                    builder.append(ChatHelper.color("&8&l&m------------------------"));

                    FireworkHelper.spawn(loc);
                    p.playSound(p.getLocation(), Sound.EXPLODE, 1.0F, 2.0F);
                    ChatHelper.title(p, "&8\u2022 &2PremiumCase &8\u2022", "&7Otworzyles &2Premium Case&7!", 10, 30, 10);

                    this.core.getServer().broadcast(builder.toString(), "i.case");
                }
                return;
            }
            case DRAGON_EGG: {
                e.setCancelled(true);
                String name = p.getItemInHand().getItemMeta().getDisplayName();
                if (name != null && name.equals(ChatHelper.color("&8[&5&lPandora&8]"))) {
                    User user = this.userManager.get(p.getName());

                    if (user.isCombat()) {
                        p.sendMessage(ChatHelper.color("&4Blad: &cNie mozesz otwierac &aPandory &7podczas walki."));
                        return;
                    }

                    if (p.getItemInHand().getAmount() > 1)
                        p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                    else p.setItemInHand(null);

                    StringBuilder builder = new StringBuilder();

                    builder.append(ChatHelper.color("&8&l&m------------------------&r\n"));
                    builder.append(ChatHelper.color(" &8>> &a" + p.getName() + " &7otworzyl &2Pandore &7i wylosowal:\n"));

                    this.core.getPandoraManager().getItems().stream().filter(item -> RandomHelper.chance(item.getChance())).forEach(item -> {
                        ItemStack is = new ItemMaker(item.getItemStack()).make();
                        p.getWorld().dropItem(loc, is);
                        String customName = Strings.isNullOrEmpty(item.getName()) ? item.getItem().name() : item.getName()
                                .replace("&8>> ", "")
                                .trim();
                        builder.append(ChatHelper.color(" &7>> &ax" + item.getAmount() + " " + customName + " &8(&a" + user.formatCoins((float) item.getChance()) + "%&8)\n"));
                    });

                    builder.append(ChatHelper.color(" &8>> &2Pandore &7zakupisz na: &7www.CoreMax.pl\n"));
                    builder.append(ChatHelper.color("&8&l&m------------------------"));

                    FireworkHelper.spawn(loc);
                    this.core.getServer().broadcast(builder.toString(), "i.case");
                }
                return;
            }
            case MOSSY_COBBLESTONE: {
                String name = p.getItemInHand().getItemMeta().getDisplayName();
                if (name != null && name.equals(ChatHelper.color("&8>> &aCobbleX"))) {
                    e.setCancelled(true);

                    User user = this.userManager.get(p.getName());

                    if (user.isCombat()) {
                        p.sendMessage(ChatHelper.color("&4Blad: &cNie mozesz otwierac &aCobbleX &7podczas walki."));
                        return;
                    }

                    if (p.getItemInHand().getAmount() > 1)
                        p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                    else p.setItemInHand(null);


                    List<CaseItem> items = this.core.getCobblexManager().getItems();
                    Collections.shuffle(items);

                    CaseItem caseItem = items
                            .stream()
                            .filter(item -> RandomHelper.chance(item.getChance()))
                            .findAny()
                            .orElse(null);

                    if (caseItem != null) {
                        p.getWorld().dropItem(loc, new ItemMaker(caseItem.getItemStack()).make());
                    }

                    String message = caseItem == null
                            ? ChatHelper.color(" &8>> &a" + p.getName() + " &7otworzyl &2CobbleX &7i nic nie wylosowal :(")
                            : ChatHelper.color(" &8>> &a" + p.getName() + " &7otworzyl &2CobbleX &7i wylosowal: &a" + caseItem.getItemStack().getType().name() + " &8(&a" + user.formatCoins((float) caseItem.getChance()) + "%&8)");

                    FireworkHelper.spawn(loc);
                    this.core.getServer().broadcast(message, "i.case");
                }
                return;
            }
        }

        if (!e.isCancelled() && !p.hasPermission("cm.gamemode")) {
            Option<Region> regionAtLocation = this.funnyGuilds.getRegionManager().findRegionAtLocation(e.getBlockPlaced().getLocation());
            if (regionAtLocation.isEmpty()) {
                this.core.getAntiGriefManager().getBlocks().put(new AntiGriefManager.BlockData(e.getBlockPlaced(), e.getBlockReplacedState().getType()), System.currentTimeMillis());
                ChatHelper.actionBar(p, "&c&lANTIGRIEF &8>> &7Ten blok zniknie za &f1 godzine");
            }
        }
    }
}