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
    super();
  }

  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, fly, area);
    
    var sx = area.width / (double) Fly.WIDTH;
    var sy = area.height / (double) Fly.HEIGHT;
    
    // background
    graphics.setColor(new Color(95, 150, 210));
    graphics.fillRect(area.x, area.y, area.width, area.height);
    
    // dessine les barres
    graphics.setColor(new Color(50, 90, 55));
    for (var pillar : fly.pillars()) {
      var px = area.x + (int) (pillar.x() * sx);
      var ph = (int) (pillar.height() * sy);
      var py = area.y + (int) (pillar.y() * sy) - ph / 2;
      graphics.fillRect(px - 4, py, 8, ph);
    }
    
    // dessine la balle du joueur
    graphics.setColor(Color.WHITE);
    var ballY = area.y + (int) (fly.y() * sy);
    graphics.fillOval(area.x + 20, ballY - 14, 14, 14);
  }
}
