package pl.kuezese.core.manager;

import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.manager.impl.BanManager;
import pl.kuezese.core.manager.impl.UserManager;

public class Manager {

    protected final CorePlugin core = CorePlugin.getInstance();
    protected final UserManager userManager = this.core.getUserManager();
}
