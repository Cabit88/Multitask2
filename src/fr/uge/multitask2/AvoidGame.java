package fr.uge.multitask2;

import com.github.forax.zen.KeyboardEvent;
import java.awt.Color;
import java.awt.Font;
import java.util.Objects;
import java.util.Random;

public class AvoidGame implements MiniGame {
	private int line = 2;
	private int obstacleX = 600;
	private final long startTime = System.currentTimeMillis();
	private final Random random = new Random();
	private int obstacleLine;

	@Override
	public void update() {
		long elapsed = (System.currentTimeMillis() - startTime) / 1000;
		obstacleX -= 3 + elapsed / 10;
		if (obstacleX < -20) {
			resetObstacle();
		}
	}

	public AvoidGame() {
		obstacleLine = random.nextInt(5);
	}

	@Override
	public void draw(java.awt.Graphics2D graphics, int startX, int startY,
			int width, int height) {
		Objects.requireNonNull(graphics, "graphics");
		drawBackground(graphics, startX, startY, width, height);
		drawLines(graphics, startX, startY, width, height);
		drawCharacters(graphics, startX, startY, width, height);
	}

	@Override
	public void handleKeyPress(KeyboardEvent.Key key) {
		Objects.requireNonNull(key, "key");
		if (Controls.up(key) && line > 0) {
			line--;
		} else if (Controls.down(key) && line < 4) {
			line++;
		}
	}

	@Override
	public boolean isGameOver() {
		return obstacleX >= 10 && obstacleX <= 46 && line == obstacleLine;
	}

	private void resetObstacle() {
		obstacleX = 600;
		obstacleLine = random.nextInt(5);
	}

	private void drawBackground(java.awt.Graphics2D graphics,
			int startX, int startY, int width, int height) {
		graphics.setColor(new Color(80, 170, 230));
		graphics.fillRect(startX, startY, width, height);
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, 16));
		graphics.drawString("AVOID", startX + 8, startY + 22);
	}

	private void drawLines(java.awt.Graphics2D graphics,
			int startX, int startY, int width, int height) {
		int top = startY + 34;
		int lineHeight = (height - 45) / 5;
		graphics.setColor(new Color(190, 230, 255));
		for (int i = 1; i < 5; i++) {
			graphics.drawLine(startX, top + i * lineHeight,
					startX + width, top + i * lineHeight);
		}
	}

	private void drawCharacters(java.awt.Graphics2D graphics,
			int startX, int startY, int width, int height) {
		int top = startY + 34;
		int lineHeight = (height - 45) / 5;
		int playerY = top + line * lineHeight + lineHeight / 2;
		int enemyY = top + obstacleLine * lineHeight + lineHeight / 2;
		int playerX = startX + 28 * width / 600;
		int enemyX = startX + obstacleX * width / 600;
		drawPlayer(graphics, playerX, playerY);
		drawObstacle(graphics, enemyX, enemyY);
	}

	private void drawPlayer(java.awt.Graphics2D graphics,
			int playerX, int playerY) {
		graphics.setColor(Color.GREEN);
		graphics.fillOval(playerX - 10, playerY - 10, 20, 20);
	}

	private void drawObstacle(java.awt.Graphics2D graphics,
			int enemyX, int enemyY) {
		graphics.setColor(Color.RED);
		int[] xPoints = {enemyX - 12, enemyX + 12, enemyX};
		int[] yPoints = {enemyY + 12, enemyY + 12, enemyY - 12};
		graphics.fillPolygon(xPoints, yPoints, 3);
	}
}
