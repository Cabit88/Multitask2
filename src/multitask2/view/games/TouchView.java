package multitask2.view.games;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

import multitask2.model.games.Touch;

public final class TouchView implements GameView {
  private final Touch touch;

  public TouchView(Touch touch) {
    Objects.requireNonNull(touch);
    this.touch = touch;
  }

  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, touch, area);
    var sx = area.width / (double) Touch.WIDTH;
    var sy = area.height / (double) Touch.HEIGHT;
    graphics.setColor(new Color(70, 70, 90));
    graphics.fillRect(area.x, area.y, area.width, area.height);

    for (var target : touch.targets()) {
      graphics.setColor(Color.YELLOW);
      var tx = area.x + (int) (target.x() * sx);
      var ty = area.y + (int) (target.y() * sy);
      graphics.fillRect(tx - 6, ty - 6, 12, 12);
    }

    graphics.setColor(new Color(90, 220, 255));
    var px = area.x + (int) (touch.playerX() * sx);
    var py = area.y + (int) (touch.playerY() * sy);
    graphics.fillRect(px - 7, py - 7, 14, 14);
  }
}
