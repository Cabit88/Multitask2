package fr.uge.multitask2;

import com.github.forax.zen.KeyboardEvent;
import java.awt.Graphics2D;

public interface MiniGame {
	void update();
	void draw(Graphics2D graphics, int startX, int startY, int width, int height);
	void handleKeyPress(KeyboardEvent.Key key);
	boolean isGameOver();
}
