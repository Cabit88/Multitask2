package fr.uge.multitask2;

import com.github.forax.zen.KeyboardEvent;
import java.util.Objects;

public final class Controls {
	private Controls() {
	}

	public static boolean left(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return key == KeyboardEvent.Key.LEFT;
	}

	public static boolean right(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return key == KeyboardEvent.Key.RIGHT;
	}

	public static boolean up(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return key == KeyboardEvent.Key.UP;
	}

	public static boolean down(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return key == KeyboardEvent.Key.DOWN;
	}

	public static boolean menuUp(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return up(key) || key == KeyboardEvent.Key.Z;
	}

	public static boolean menuDown(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return down(key) || key == KeyboardEvent.Key.S;
	}

	public static boolean activate(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return key == KeyboardEvent.Key.SPACE;
	}

	public static boolean replay(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return key == KeyboardEvent.Key.R;
	}

	public static boolean escape(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		return key == KeyboardEvent.Key.ESCAPE;
	}
}
