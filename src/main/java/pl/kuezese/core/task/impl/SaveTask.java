package pl.kuezese.core.task.impl;

import pl.kuezese.core.object.User;
import pl.kuezese.core.task.Task;

public class SaveTask extends Task {

    public SaveTask() {
        super(18000L, 18000L, true);
    }

    @Override
    public void run() {
        this.core.getServer().getOnlinePlayers().stream().map(p -> this.userManager.get(p.getName())).forEach(User::update);
    }
}
