package pl.kuezese.core.command.impl;

import com.sun.management.OperatingSystemMXBean;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.DateHelper;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;

public class TpsCommand extends Command {

    private final Runtime runtime;
    private final RuntimeMXBean mxBean;
    private final OperatingSystemMXBean osBean;
    private final DecimalFormat tpsFormat;

    public TpsCommand() {
        super("tps", "cm.tps", "gc", "lag");
        this.runtime = Runtime.getRuntime();
        this.mxBean = ManagementFactory.getRuntimeMXBean();
        this.osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        this.tpsFormat = new DecimalFormat("#.##");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        double[] tps = MinecraftServer.getServer().recentTps;

//		double stability = Math.min(((tps[0] / 20.0) * 100.0D), 100.0D);
        double stability = Math.min(((tps[0] + tps[1] + tps[2]) / 60.0D) * 100.0D, 100.0D);
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory());
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();

        sender.sendMessage(ChatHelper.color(" &8>> &7Statystyki serwera:"));
        sender.sendMessage(ChatHelper.color(" &8>> &aTPS z ostatnich 1m, 5m, 15m: &7" + formatTps(tps[0]) + "&a, &7" + formatTps(tps[1]) + "&a, &7" + formatTps(tps[2])));
        sender.sendMessage(ChatHelper.color(" &8>> &aStabilnosc serwera: &7" + this.tpsFormat.format(stability) + "%"));
        sender.sendMessage(ChatHelper.color(" &8>> &aPamiec RAM: &7" + this.humanReadableByteCount(usedMemory) + "/" + this.humanReadableByteCount(totalMemory)));
        sender.sendMessage(ChatHelper.color(" &8>> &aPrzydzielony RAM: &7" + this.humanReadableByteCount(maxMemory)));
        sender.sendMessage(ChatHelper.color(" &8>> &aSerwer dziala od: &7" + DateHelper.formatDateDiff(System.currentTimeMillis() + mxBean.getUptime())));
        sender.sendMessage(ChatHelper.color(" &8>> &aIlosc rdzeni procesora: &7" + osBean.getAvailableProcessors()));
        sender.sendMessage(ChatHelper.color(" &8>> &aUzycie procesora (system)&7: &7" + Math.floor(osBean.getSystemCpuLoad() * 100.0 * 10.0) / 10.0 + "%"));
        sender.sendMessage(ChatHelper.color(" &8>> &aUzycie procesora (jvm)&7: &7" + Math.floor(osBean.getProcessCpuLoad() * 100.0 * 10.0) / 10.0 + "%"));
        sender.sendMessage(ChatHelper.color(" &8>> &aIlosc entity: &7" + this.core.getServer().getWorlds().stream().mapToInt(world -> world.getEntities().size()).sum()));
        sender.sendMessage(ChatHelper.color(" &8>> &aIlosc swiatow: &7" + this.core.getServer().getWorlds().size()));
        return true;
    }

    private String formatTps(double v) {
        return v > 20.00 ? "20.00*" : this.tpsFormat.format(v);
    }

    private String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
