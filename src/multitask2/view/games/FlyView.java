package multitask2.view.games;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

import multitask2.model.games.Fly;

public final class FlyView implements GameView {
  private final Fly fly;

  public FlyView(Fly fly) {
    Objects.requireNonNull(fly);
    this.fly = fly;
  }

  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, fly, area);
    var sx = area.width / (double) Fly.WIDTH;
    var sy = area.height / (double) Fly.HEIGHT;
    graphics.setColor(new Color(95, 150, 210));
    graphics.fillRect(area.x, area.y, area.width, area.height);

    graphics.setColor(new Color(50, 90, 55));
    for (var pillar : fly.pillars()) {
      var px = area.x + (int) (pillar.x() * sx);
      var center = area.y + (int) (pillar.gapY() * sy);
      var gap = (int) (pillar.gapHeight() * sy);
      graphics.fillRect(px - 5, area.y, 10, center - gap / 2 - area.y);
      graphics.fillRect(px - 5, center + gap / 2, 10, area.y + area.height - center - gap / 2);
    }

    graphics.setColor(Color.WHITE);
    var py = area.y + (int) (fly.y() * sy);
    graphics.fillOval(area.x + 20, py - 7, 14, 14);
  }
}
