package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class AntiGriefCommand extends Command {

    public AntiGriefCommand() {
        super("antigrief", "cm.antigrief");
    }

    @Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/antigrief <info/remove>"));
			return true;
		}

		if (args[0].equalsIgnoreCase("info")) {
			sender.sendMessage(ChatHelper.color(" &8>> &aAktualnie jest " + this.core.getAntiGriefManager().getBlocks().size() + " blokow czekajacych na usuniecie."));
			return true;
		}

        if (args[0].equalsIgnoreCase("remove")) {
            long start = System.currentTimeMillis();
            int size = this.core.getAntiGriefManager().getBlocks().size();
            this.core.getAntiGriefManager().getBlocks().forEach((data, time) -> data.getBlock().setType(data.getPrevious()));
            this.core.getAntiGriefManager().getBlocks().clear();
            long end = System.currentTimeMillis();
            sender.sendMessage(ChatHelper.color(" &8>> &aUsunieto " + size + " blokow w " + (end - start) + "ms."));
        }
        return true;
	}
}
