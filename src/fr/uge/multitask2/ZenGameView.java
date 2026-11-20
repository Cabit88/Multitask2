package fr.uge.multitask2;

import com.github.forax.zen.ApplicationContext;
import java.awt.Color;
import java.util.Objects;

public class ZenGameView {
	private ZenGameView() {
	}

	public static void draw(ApplicationContext context, Model model,
			int width, int height) {
		Objects.requireNonNull(context, "context");
		Objects.requireNonNull(model, "model");
		context.renderFrame(graphics -> {
			graphics.setColor(Color.DARK_GRAY);
			graphics.fillRect(0, 0, width, height);
			Graphics.drawHeader(graphics, width, model.getScore());
			drawSlots(graphics, model, width, height);
		});
	}

	private static void drawSlots(java.awt.Graphics2D graphics, Model model,
			int width, int height) {
		Objects.requireNonNull(graphics, "graphics");
		Objects.requireNonNull(model, "model");
		int columns = 2;
		int rows = 3;
		int gap = 8;
		int top = 50;
		int slotWidth = (width - gap * (columns + 1)) / columns;
		int slotHeight = (height - top - gap * (rows + 1)) / rows;
		drawActiveSlots(graphics, model, columns, gap, top,
				slotWidth, slotHeight);
	}

	private static void drawActiveSlots(java.awt.Graphics2D graphics,
			Model model, int columns, int gap, int top,
			int slotWidth, int slotHeight) {
		for (int i = 0; i < model.getSlots().size() && i < 6; i++) {
			int column = i % columns;
			int row = i / columns;
			int x = gap + column * (slotWidth + gap);
			int y = top + gap + row * (slotHeight + gap);
			Slot slot = model.getSlots().get(i);
			slot.getGame().draw(graphics, x, y, slotWidth, slotHeight);
			graphics.setColor(Color.WHITE);
			graphics.drawRect(x, y, slotWidth, slotHeight);
		}
	}
}
