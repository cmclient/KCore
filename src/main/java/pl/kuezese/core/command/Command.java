package pl.kuezese.core.command;

import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.manager.impl.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Level;

public abstract class Command extends org.bukkit.command.Command {

    private final String name;
    private final String permission;

    public Command(String name, String permission, String... aliases) {
        super(name, "", "", Arrays.asList(aliases));
        this.name = name;
        this.permission = permission;
        try {
            Field field = this.core.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(this.core.getServer());
            commandMap.register(this.name, this);
        } catch (Exception ex) {
            this.core.getLogger().log(Level.SEVERE, "Failed to register command " + this.name + "!", ex);
        }
    }

    protected CorePlugin core = CorePlugin.getInstance();
    protected UserManager userManager = this.core.getUserManager();
    protected BanManager banManager = this.core.getBanManager();
    protected TellManager tellManager = this.core.getTellManager();
    protected TeleportManager teleportManager = this.core.getTeleportManager();
    protected ChatManager chatManager = this.core.getChatManager();

    public abstract boolean onCommand(CommandSender sender, String s, String[] args);

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!this.permission.isEmpty() && !sender.hasPermission(this.permission)) {
            sender.sendMessage(ChatHelper.color(" &8>> &cNie posiadasz dostepu do tej komendy! &8(&7" + this.permission + "&8)"));
            return true;
        }
        return this.onCommand(sender, s, args);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }
}
