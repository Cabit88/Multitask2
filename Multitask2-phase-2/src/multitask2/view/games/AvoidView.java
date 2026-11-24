package multitask2.view.games;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

import multitask2.model.games.Avoid;

/**
 * Vue graphique du mini-jeu {@link Avoid} : dessine la zone agrandie, le joueur
 * et les missiles.
 */
public final class AvoidView implements GameView {

  /** État du mini-jeu d'esquive lu pour produire le dessin. */
  private final Avoid avoider;

  /**
   * Crée une vue associée au mini-jeu fourni.
   *
   * @param avoider mini-jeu dont l'état doit être représenté
   * @throws NullPointerException si {@code avoider} est {@code null}
   */
  public AvoidView(Avoid avoider) {
    Objects.requireNonNull(avoider);
    this.avoider = avoider;
    super();
  }

  /**
   * Dessine la zone de jeu, la position du joueur et les missiles présents.
   * Les coordonnées du modèle sont mises à l'échelle pour tenir dans le
   * rectangle fourni.
   *
   * @param graphics contexte graphique à utiliser
   * @param area     zone disponible pour le dessin
   * @throws NullPointerException si {@code graphics}, le jeu associé ou
   *                              {@code area} est nul
   */
  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, avoider, area);

    var g = (Graphics2D) graphics.create();
    try {
      var scaleX = area.width / Avoid.WIDTH;

      g.setColor(new Color(210, 216, 250));
      g.fillRect(area.x, area.y, area.width, area.height);

      var laneWidth = Math.min(area.width, Math.max(18, area.width / 18));
      var laneHeight = (int) (area.height * 0.65);
      var laneX = area.x + (area.width - laneWidth) / 2;
      var laneY = area.y + (area.height - laneHeight) / 2;
      var cellHeight = laneHeight / (double) Avoid.ROWS;
      var playerCellY = laneY + (int) ((Avoid.ROWS - 1 - avoider.row()) * cellHeight);

      g.setColor(new Color(0, 76, 166));
      g.fillRect(laneX, playerCellY, laneWidth, (int) Math.ceil(cellHeight));
      g.setColor(Color.BLACK);
      g.setStroke(new BasicStroke(3));
      g.drawRect(laneX, laneY, laneWidth, laneHeight);
      for (var i = 1; i < Avoid.ROWS; i++) {
        var y = laneY + (int) (i * cellHeight);
        g.drawLine(laneX, y, laneX + laneWidth, y);
      }

      g.setColor(new Color(230, 90, 90));
      for (Avoid.Missile missile : avoider.missiles()) {
        var missileX = area.x + (int) (missile.x() * scaleX);
        var missileY = laneY + (int) ((Avoid.ROWS - missile.row() - 0.5) * cellHeight);
        g.fillOval(missileX - 7, missileY - 5, 14, 10);
      }
    } finally {
      g.dispose();
    }
  }
}
