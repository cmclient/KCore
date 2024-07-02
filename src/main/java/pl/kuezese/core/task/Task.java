package pl.kuezese.core.task;

import org.bukkit.scheduler.BukkitRunnable;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.manager.impl.UserManager;

public abstract class Task extends BukkitRunnable {

    public Task(long delay, long period, boolean async) {
        if (async) this.runTaskTimerAsynchronously(this.core, delay, period);
        else this.runTaskTimer(this.core, delay, period);
    }

    protected final CorePlugin core = CorePlugin.getInstance();
    protected final UserManager userManager = this.core.getUserManager();

    @Override
    public abstract void run();
}
