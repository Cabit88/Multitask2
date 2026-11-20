package fr.uge.multitask2;

import java.util.Objects;

public class Slot {
	private final MiniGame game;

	public Slot(MiniGame game) {
		this.game = Objects.requireNonNull(game, "game");
	}

	public MiniGame getGame() {
		return game;
	}
}
