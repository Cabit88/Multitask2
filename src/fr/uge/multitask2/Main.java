package fr.uge.multitask2;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import java.awt.Color;
import java.util.Objects;

public class Main {
	public static void menu(ApplicationContext context) {
		Objects.requireNonNull(context, "context");
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.width();
		var height = screenInfo.height();
		var logo = Graphics.loadImage("/data/logo.jpeg");
		int position = 1;
		while (true) {
			Graphics.drawMenu(context, width, height, logo, position);
			Event event = context.pollOrWaitEvent(16);
			if (event instanceof KeyboardEvent key) {
				position = handleMenuKey(context, key, position, width, height);
				if (position == -1) return;
			} else if (event instanceof PointerEvent pointer) {
				position = handleMenuPointer(context, pointer, position, width, height);
				if (position == -1) return;
			}
		}
	}

	public static void game(ApplicationContext context, int width, int height) {
		Objects.requireNonNull(context, "context");
		while (true) {
			Model model = new Model();
			if (!runGame(context, model, width, height)) return;
		}
	}

	public static void main(String[] args) {
		Objects.requireNonNull(args, "args");
		Application.run(new Color(203, 203, 203), Main::menu);
	}

	private static int handleMenuKey(ApplicationContext context,
			KeyboardEvent key, int position, int width, int height) {
		Objects.requireNonNull(context, "context");
		Objects.requireNonNull(key, "key");
		if (key.action() != KeyboardEvent.Action.KEY_PRESSED) return position;
		if (Controls.menuUp(key.key())) position = position == 1 ? 3 : position - 1;
		if (Controls.menuDown(key.key())) position = position == 3 ? 1 : position + 1;
		if (Controls.activate(key.key())) {
			if (handleMenuChoice(context, width, height, position)) return -1;
		}
		if (Controls.escape(key.key())) {
			context.dispose();
			return -1;
		}
		return position;
	}

	private static int handleMenuPointer(ApplicationContext context,
			PointerEvent pointer, int position, int width, int height) {
		Objects.requireNonNull(context, "context");
		Objects.requireNonNull(pointer, "pointer");
		if (pointer.action() != PointerEvent.Action.POINTER_DOWN) return position;
		var location = pointer.location();
		int clicked = Graphics.buttonMenu(width, height, location.x(), location.y());
		if (clicked == -1) return position;
		if (handleMenuChoice(context, width, height, clicked)) return -1;
		return clicked;
	}

	private static boolean handleMenuChoice(ApplicationContext context,
			int width, int height, int position) {
		Objects.requireNonNull(context, "context");
		if (position == 1) game(context, width, height);
		if (position == 2) System.out.println("Les paramètres ne sont pas disponibles.");
		if (position == 3) { context.dispose(); return true; }
		return false;
	}

	private static boolean runGame(ApplicationContext context, Model model,
			int width, int height) {
		Objects.requireNonNull(context, "context");
		Objects.requireNonNull(model, "model");
		while (true) {
			model.update();
			if (model.isGameOver()) return gameOver(context, model.getScore(), width, height);
			ZenGameView.draw(context, model, width, height);
			Event event = context.pollOrWaitEvent(16);
			if (event instanceof KeyboardEvent key
					&& key.action() == KeyboardEvent.Action.KEY_PRESSED
					&& Controls.escape(key.key())) return false;
			handleGameKey(model, event);
		}
	}

	private static void handleGameKey(Model model, Event event) {
		Objects.requireNonNull(model, "model");
		if (event == null) return;
		if (event instanceof KeyboardEvent key
				&& key.action() == KeyboardEvent.Action.KEY_PRESSED) {
			for (Slot slot : model.getSlots()) {
				slot.getGame().handleKeyPress(key.key());
			}
		}
	}

	private static boolean gameOver(ApplicationContext context, long score,
			int width, int height) {
		Objects.requireNonNull(context, "context");
		while (true) {
			Graphics.drawGameLose(context, width, height, score);
			Event event = context.pollOrWaitEvent(16);
			if (event instanceof KeyboardEvent key
					&& key.action() == KeyboardEvent.Action.KEY_PRESSED) {
				if (Controls.replay(key.key())) return true;
				if (Controls.escape(key.key())) return false;
			}
			if (isReplayPointer(event, width, height)) return true;
			if (isMenuPointer(event, width, height)) return false;
		}
	}

	private static java.awt.Point getClickPoint(Event event) {
		if (!(event instanceof PointerEvent pointer)) return null;
		if (pointer.action() != PointerEvent.Action.POINTER_DOWN) return null;
		var location = pointer.location();
		return new java.awt.Point(location.x(), location.y());
	}

	private static boolean isReplayPointer(Event event, int width, int height) {
		var point = getClickPoint(event);
		if (point == null) return false;
		return Graphics.isClickOnReplay(point.x, point.y, width, height);
	}

	private static boolean isMenuPointer(Event event, int width, int height) {
		var point = getClickPoint(event);
		if (point == null) return false;
		return Graphics.isClickOnMenu(point.x, point.y, width, height);
	}
}
