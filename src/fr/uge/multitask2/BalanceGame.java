package fr.uge.multitask2;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import java.awt.*;
import java.util.Objects;

public class BalanceGame implements MiniGame {
    private double angle = 0;
    private double ballPos = 0;
    private double ballVelocity = 0;
    

    @Override
    public void update() {
        ballVelocity += Math.sin(angle) * 0.5;
        ballVelocity *= 0.9;
        ballPos += ballVelocity;
        
        // Balance la balance...
        if (ballPos < 0) {
            angle -= 0.0005;
        } else {
            angle += 0.0005;
        }
        
        // mais la limite a 45°
        double maxAngle = Math.PI/4;
        if (angle > maxAngle) {
            angle = maxAngle;
        } else if (angle < -maxAngle) {
            angle = -maxAngle;
        }
    }

    @Override
    public void draw(Graphics2D graphics, int startX, int startY,
            int width, int height) {
        Objects.requireNonNull(graphics, "graphics");
        graphics.setColor(new Color(217, 102, 227));
        graphics.fillRect(startX, startY, width, height);
        drawBalance(graphics, startX, startY, width, height);
        drawBall(graphics, startX, startY, width, height);
    }

    private void drawBalance(Graphics2D graphics, int startX, int startY,
            int width, int height) {
        int centerX = startX + width / 2;
        int centerY = startY + height / 2;
        int x1 = (int) (centerX - 150 * Math.cos(angle));
        int y1 = (int) (centerY - 150 * Math.sin(angle));
        int x2 = (int) (centerX + 150 * Math.cos(angle));
        int y2 = (int) (centerY + 150 * Math.sin(angle));
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(5.0f));
        graphics.drawLine(x1, y1, x2, y2);
    }

    private void drawBall(Graphics2D graphics, int startX, int startY,
            int width, int height) {
        int centerX = startX + width / 2;
        int centerY = startY + height / 2;
        int ballX = (int) (centerX + ballPos * Math.cos(angle));
        int ballY = (int) (centerY + ballPos * Math.sin(angle));
        graphics.setColor(Color.RED);
        graphics.fillOval(ballX - 20, ballY - 40, 40, 40);
    }

    @Override
    public void handleKeyPress(KeyboardEvent.Key key) {
        Objects.requireNonNull(key, "key");
        if (Controls.left(key)) {
            angle -= 0.05;
        } else if (Controls.right(key)) {
            angle += 0.05;
        }
    }

    @Override
    public boolean isGameOver() {
        return ballPos > 160 || ballPos < -160;
    }
}
