package multitask2.view.games;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

import multitask2.model.games.Jump;

public final class JumpView implements GameView {
  private final Jump jump;

  public JumpView(Jump jump) {
    Objects.requireNonNull(jump);
    this.jump = jump;
    super();
  }

  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, jump, area);
    
    var sx = area.width / (double) Jump.WIDTH;
    var sy = area.height / (double) Jump.HEIGHT;
    
    // background
    graphics.setColor(new Color(100, 170, 100));
    graphics.fillRect(area.x, area.y, area.width, area.height);
    
    // bande sol
    graphics.setColor(new Color(70, 45, 25));
    graphics.fillRect(area.x, area.y + area.height - 20, area.width, 20);
    
    // Affiche les trous dans le sol
    graphics.setColor(new Color(24, 26, 32));
    for (var hole : jump.holes()) {
      var hx = area.x + (int) (hole.x() * sx);
      var hw = (int) (hole.width() * sx);
      graphics.fillRect(hx, area.y + area.height - 20, hw, 20);
    }
    
    // Affiche le joueur
    graphics.setColor(Color.ORANGE);
    var py = area.y + area.height - 20 - (int) (jump.height() * sy);
    graphics.fillRect(area.x + (int) (30 * sx) - 7, py - 12, 14, 12);
  }
}
