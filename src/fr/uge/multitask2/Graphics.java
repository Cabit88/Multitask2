package fr.uge.multitask2;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import com.github.forax.zen.ApplicationContext;

public class Graphics {
	private static final int MARGE = 130;
	private static final int BUTTON_HEIGHT = 50;
	
	public static void drawHeader(java.awt.Graphics2D graphics,
			int width, long time) {
		Objects.requireNonNull(graphics, "graphics");
		graphics.setColor(new Color(66, 66, 66));
		graphics.fillRect(0, 0, width, 50);
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, 24));
		graphics.drawString("Score : " + time, 10, 30);
		drawName(graphics, width, time);
	}

	private static void drawName(java.awt.Graphics2D graphics,
			int width, long time) {
		var rand = new Random(time);
		String nameGame = "MULTITASK 2";
		for (int i = 0; i < nameGame.length(); i++) {
			var x = rand.nextInt(0, 256);
			var y = rand.nextInt(0, 256);
			var z = rand.nextInt(0, 256);
			graphics.setColor(new Color(x, y, z));
			graphics.drawString("" + nameGame.charAt(i),
					width / 2 - 100 + i * 20, 30);
		}
	}
	
	public static void drawGameLose(ApplicationContext context,
			int width, int height, long score) {
		Objects.requireNonNull(context, "context");
		context.renderFrame(graphics -> {
			drawGameLosePanel(graphics, width, height, score);
		});
	}

	private static void drawGameLosePanel(java.awt.Graphics2D graphics,
			int width, int height, long score) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(width / 4, height / 3, width / 2, height / 3);
		graphics.setColor(Color.DARK_GRAY);
		graphics.setFont(new Font("Arial", Font.BOLD, 32));
		graphics.drawString("PERDU !!", width / 2 - 50, height / 2);
		graphics.drawString("Score : " + score,
				width / 2 - 50, height / 2 + 50);
		drawEndButton(graphics, width / 2 - 180, height / 2 + 80, "REJOUER");
		drawEndButton(graphics, width / 2 + 30, height / 2 + 80, "MENU");
	}

	private static void drawEndButton(java.awt.Graphics2D graphics,
			int x, int y, String text) {
		Objects.requireNonNull(graphics, "graphics");
		Objects.requireNonNull(text, "text");
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(x, y, 150, 45);
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, 18));
		graphics.drawString(text, x + 20, y + 30);
	}

	public static boolean isClickOnReplay(int mouseX, int mouseY,
			int width, int height) {
		return mouseX >= width / 2 - 180 && mouseX <= width / 2 - 30
				&& mouseY >= height / 2 + 80 && mouseY <= height / 2 + 125;
	}

	public static boolean isClickOnMenu(int mouseX, int mouseY,
			int width, int height) {
		return mouseX >= width / 2 + 30 && mouseX <= width / 2 + 180
				&& mouseY >= height / 2 + 80 && mouseY <= height / 2 + 125;
	}
	
	public static BufferedImage loadImage(String path) {
		Objects.requireNonNull(path, "path");
        try {
            return ImageIO.read(Graphics.class.getResourceAsStream(path));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Impossible de charger l'image : " + path);
            return null;
        }
    }
	
	public static int buttonMenu(int width, int height, double mouseX, double mouseY) {
		if (mouseX >= width/4 && mouseY >= height/2 && mouseX <= 3 * width/4 && mouseY <= height/2 + BUTTON_HEIGHT) {
			return 1; // Play
		} else if (mouseX >= width/4 && mouseY >= height/2 + MARGE && mouseX <= 3 * width/4 && mouseY <= height/2 + MARGE + BUTTON_HEIGHT) {
			return 2; // Settings
		} else if(mouseX >= width/4 && mouseY >= height/2 + 2*MARGE && mouseX <= 3 * width/4 && mouseY <= height/2 + 2*MARGE + BUTTON_HEIGHT) {
			return 3; // Leave
		}
		return -1; // error
	}

	public static void drawMenu(ApplicationContext context, int width,
			int height, BufferedImage logo, int position) {
		Objects.requireNonNull(context, "context");
		context.renderFrame(graphics -> {
			drawMenuBackground(graphics, width, height, logo);
			drawMenuButton(graphics, width, height / 2,
					"JOUER", position == 1);
			drawMenuButton(graphics, width, height / 2 + MARGE,
					"PARAMÈTRES", position == 2);
			drawMenuButton(graphics, width, height / 2 + 2 * MARGE,
					"QUITTER", position == 3);
		});
	}

	private static void drawMenuBackground(java.awt.Graphics2D graphics,
			int width, int height, BufferedImage logo) {
		graphics.setColor(new Color(203, 203, 203));
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(Color.DARK_GRAY);
		graphics.setFont(new Font("Arial", Font.BOLD, 16));
		graphics.drawString("By MASSIP-SOUECHRE A. and PLUVINET-VIAENE A.",
				10, height - 15);
		if (logo != null) {
			graphics.drawImage(logo, width / 2 - 300, 30, 600, 300, null);
		}
	}

	private static void drawMenuButton(java.awt.Graphics2D graphics,
			int width, int y, String text, boolean selected) {
		Objects.requireNonNull(graphics, "graphics");
		Objects.requireNonNull(text, "text");
		graphics.setColor(new Color(0, 0, 0, 100));
		graphics.fillRect(width / 4, y, width / 2, BUTTON_HEIGHT);
		if (selected) {
			graphics.setColor(Color.RED);
			graphics.drawRect(width / 4, y, width / 2, BUTTON_HEIGHT);
		}
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, 28));
		graphics.drawString(text, width / 2 - text.length() * 9, y + 35);
	}
	
}
