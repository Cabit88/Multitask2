package multitask2.view.games;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Objects;

import multitask2.model.games.Circle;

public final class CircleView implements GameView {
  private final Circle circle;

  public CircleView(Circle circle) {
    Objects.requireNonNull(circle);
    this.circle = circle;
  }

  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, circle, area);
    var cx = area.x + area.width / 2;
    var cy = area.y + area.height / 2;
    var radius = Math.min(area.width, area.height) / 2 - 8;

    graphics.setColor(new Color(50, 50, 70));
    graphics.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

    var opening = Math.toRadians(circle.openingAngle());
    graphics.setColor(Color.CYAN);
    graphics.fillArc(cx - radius, cy - radius, radius * 2, radius * 2,
        (int) Math.toDegrees(-opening) - 15, 30);

    graphics.setColor(Color.ORANGE);
    for (var triangle : circle.triangles()) {
      var angle = Math.toRadians(triangle.angle());
      var distance = triangle.radius() / Circle.SIZE * radius * 2;
      var tx = cx + (int) (Math.cos(angle) * distance);
      var ty = cy - (int) (Math.sin(angle) * distance);
      graphics.fillPolygon(triangle(tx, ty));
    }
  }

  private Polygon triangle(int x, int y) {
    return new Polygon(new int[] {x, x - 5, x + 5}, new int[] {y - 6, y + 5, y + 5}, 3);
  }
}
