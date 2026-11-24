package multitask2.view.games;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

import multitask2.model.games.Touch;


public final class TouchView implements GameView {
  private final Touch touch;
  private static final int RADIUS = 15; // rayon des cibles

  public TouchView(Touch touch) {
    Objects.requireNonNull(touch);
    this.touch = touch;
    super();
  }

  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, touch, area);
    
    var sx = area.width / (double) Touch.WIDTH;
    var sy = area.height / (double) Touch.HEIGHT;
    
    graphics.setColor(new Color(70, 70, 90));
    graphics.fillRect(area.x, area.y, area.width, area.height);

    for (var target : touch.targets()) {
      var cx = area.x + (int) (target.x() * sx);
  	  var cy = area.y + (int) (target.y() * sy);
  	  var diameter = RADIUS * 2;
  	  
  	  graphics.setColor(Color.BLACK);
  	  graphics.drawOval(cx - RADIUS, cy - RADIUS, diameter, diameter);
  	  
  	  // Affiche le temps dans les cercles
  	  int cooldown = (int) Math.max(0, target.remaining());
      graphics.setFont(new Font("Arial", Font.BOLD, 12));
      graphics.drawString("" + cooldown, cx - 4, cy + 4);
    }
    
    // Affiche le rectangle du joueur
    graphics.setColor(new Color(90, 220, 255));
    var px = area.x + (int) (touch.playerX() * sx);
    var py = area.y + (int) (touch.playerY() * sy);
    graphics.fillRect(px - 7, py - 7, 14, 14);
  }
}
