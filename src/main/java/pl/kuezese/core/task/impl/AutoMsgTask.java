package pl.kuezese.core.task.impl;

import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.task.Task;
import java.util.List;

public class AutoMsgTask extends Task {

	private final List<String> messages = this.core.getConfiguration().getAutoMsg();
	private int currentIndex = 0;

	public AutoMsgTask() {
		super(1200L, 1200L, true);
	}

	@Override
	public void run() {
		if (!messages.isEmpty()) {
			String message = messages.get(currentIndex);
			this.core.getServer().broadcastMessage(ChatHelper.color(message));
			currentIndex = (currentIndex + 1) % messages.size();
		}
	}
}
