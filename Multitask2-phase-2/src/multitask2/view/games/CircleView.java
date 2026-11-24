package multitask2.view.games;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

import multitask2.model.games.Circle;

public final class CircleView implements GameView {
  private final Circle circle;

  public CircleView(Circle circle) {
    Objects.requireNonNull(circle);
    this.circle = circle;
    super();
  }

  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, circle, area);
    var cx = area.x + area.width / 2;
    var cy = area.y + area.height / 2;
    var radius = Math.min(area.width, area.height) / 5;
    
    // background
    graphics.setColor(new Color(245, 240, 195));
    graphics.fillRect(area.x, area.y, area.width, area.height);
    
    // dessine le cercle
    graphics.setColor(new Color(128, 128, 0));
    var startAngle = (int) Math.round(circle.openingAngle() + 45);
    graphics.drawArc(cx - radius, cy - radius, radius * 2, radius * 2, startAngle, 270);
    
    // la distance max des triangle par rapport au centre
    var maxDist = Math.min(area.width, area.height) / 2.0 - 10;
    graphics.setColor(Color.BLACK);
    
    // dessine les triangle
    for (var triangle : circle.triangles()) {
      var angle = Math.toRadians(triangle.angle());
      
      var ratio = Math.max(0, Math.min(1, (triangle.radius() - 12) / 36.0));
      var dist = radius + ratio * (maxDist - radius);
      
      // coordonnée du triangle
      var tx = cx + (int) (Math.cos(angle) * dist);
      var ty = cy - (int) (Math.sin(angle) * dist);
      graphics.fillPolygon(new int[] {tx, tx - 5, tx + 5}, new int[] {ty - 6, ty + 5, ty + 5}, 3);
    }
  }
}
