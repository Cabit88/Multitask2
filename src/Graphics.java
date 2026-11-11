package fr.uge.multitask2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.github.forax.zen.ApplicationContext;

public record Graphics(int x, int y, int width, int height) {
	public static void drawGame1(ApplicationContext context, int width, int height, double angle, double ballPos, long time) {
        context.renderFrame(graphics -> {
            graphics.setColor(new Color(52, 38, 28));
            graphics.fillRect(0, 0, width, height);
            
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(5.0f));
            
            graphics.setFont(new Font("Arial", Font.BOLD, 24));
            graphics.drawString("" + time, 10, 80);
            
            int centerX = width/2;
            int centerY = height/2;
            double length = 300;
            
            int x1 = (int) (centerX - (length/2) * Math.cos(angle));
            int y1 = (int) (centerY - (length/2) * Math.sin(angle));
            int x2 = (int) (centerX + (length/2) * Math.cos(angle));
            int y2 = (int) (centerY + (length/2) * Math.sin(angle));
            
            graphics.drawLine(x1, y1, x2, y2);
            
            int ballX = (int) (centerX + ballPos * Math.cos(angle));
            int ballY = (int) (centerY + ballPos * Math.sin(angle));
            
            graphics.setColor(Color.RED);
            graphics.fillOval(ballX - 20, ballY - 40, 40, 40);
            
            graphics.setColor(Color.DARK_GRAY);
            graphics.setFont(new Font("Arial", Font.BOLD, 28));
            graphics.drawString("Touche balance : ← gauche, droite → ", 10, 40);
        });
    }
	
	public static void gameLose(ApplicationContext context, int width, int height, long score) {
		context.renderFrame(graphics -> {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(width/4, height/3, width/2, height/3);
			
			graphics.setColor(Color.DARK_GRAY);
            graphics.setFont(new Font("Arial", Font.BOLD, 32));
            graphics.drawString("PERDU !!", width/2 - 50, height/2);
            graphics.drawString("Score : " + score, width/2 - 50, height/2 + 50);

		});
	}
	
	public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Graphics.class.getResourceAsStream(path));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Impossible de charger l'image : " + path);
            return null;
        }
    }

    public static void drawMenu(ApplicationContext context, int width, int height, BufferedImage logo) {
        context.renderFrame(graphics -> {
            graphics.setColor(new Color(203, 203, 203));
            graphics.fillRect(0, 0, width, height);

            if (logo != null) {
                graphics.drawImage(logo, width / 2 - 200, 50, 400, 150, null);
            }
        });
    }
	
}
