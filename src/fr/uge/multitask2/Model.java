package fr.uge.multitask2;

import java.util.ArrayList;
import java.util.List;

public class Model {
	private final List<Slot> slots = new ArrayList<>();
	private final long startTime = System.currentTimeMillis();

	public Model() {
		slots.add(new Slot(new BalanceGame()));
		slots.add(new Slot(new AvoidGame()));
	}

	public List<Slot> getSlots() {
		return slots;
	}

	public void update() {
		for (Slot slot : slots) {
			slot.getGame().update();
		}
	}

	public boolean isGameOver() {
		for (Slot slot : slots) {
			if (slot.getGame().isGameOver()) {
				return true;
			}
		}
		return false;
	}

	public long getScore() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}
}
