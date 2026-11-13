package fr.uge.multitask2;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import java.awt.*;

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
    public void draw(ApplicationContext context, int startX, int startY, int width, int height) {
        context.renderFrame(graphics -> {
            graphics.setColor(new Color(217, 102, 227));
            graphics.fillRect(startX, startY, width, height);

            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(5.0f));

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
}
