package pl.kuezese.core.task.impl;

import pl.kuezese.core.task.Task;

public class RedstoneTask extends Task {

	public RedstoneTask() {
		super(40L, 40L, true);
	}

	@Override
	public void run() {
		this.core.getRedstoneOps().set(0);
		this.core.getVehicleMoves().set(0);
	}
}
