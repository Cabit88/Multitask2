package fr.uge.multitask2;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import java.awt.*;

public class BalanceGame implements MiniGame {
    private double angle = 0;
    private double ballPos = 0;
    private double ballVelocity = 0;
    private final long startTime = System.currentTimeMillis();

    @Override
    public void update() {
        ballVelocity += Math.sin(angle) * 0.5;
        ballVelocity *= 0.9;
        ballPos += ballVelocity;
        
        // Balance la balance
        if (ballPos < 0) {
            angle -= 0.0005;
        } else {
            angle += 0.0005;
        }
    }

    @Override
    public void draw(ApplicationContext context, int startX, int startY, int width, int height) {
        long time = (System.currentTimeMillis() - startTime) / 1000;
        
        context.renderFrame(graphics -> {
            graphics.setColor(new Color(217, 102, 227));
            graphics.fillRect(startX, startY, width, height);

            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(5.0f));

            graphics.setFont(new Font("Arial", Font.BOLD, 24));
            graphics.drawString("Temps : " + time + "s", startX + 10, startY + 80);

            int centerX = startX + width / 2;
            int centerY = startY + height / 2;
            double length = 300;

            int x1 = (int) (centerX - (length / 2) * Math.cos(angle));
            int y1 = (int) (centerY - (length / 2) * Math.sin(angle));
            int x2 = (int) (centerX + (length / 2) * Math.cos(angle));
            int y2 = (int) (centerY + (length / 2) * Math.sin(angle));

            graphics.drawLine(x1, y1, x2, y2);

            int ballX = (int) (centerX + ballPos * Math.cos(angle));
            int ballY = (int) (centerY + ballPos * Math.sin(angle));

            graphics.setColor(Color.RED);
            graphics.fillOval(ballX - 20, ballY - 40, 40, 40);

            graphics.setColor(Color.DARK_GRAY);
            graphics.setFont(new Font("Arial", Font.BOLD, 28));
            graphics.drawString("Touche balance : ← gauche, droite → ", startX + 10, startY + 40);
        });
    }

    @Override
    public void handleKeyPress(KeyboardEvent.Key key) {
        if (key == KeyboardEvent.Key.LEFT) {
            angle -= 0.05;
        } else if (key == KeyboardEvent.Key.RIGHT) {
            angle += 0.05;
        }
    }

    @Override
    public boolean isGameOver() {
        return ballPos > 160 || ballPos < -160;
    }

    public long getScore() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
