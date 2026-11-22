package multitask2.view.games;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

import multitask2.model.games.Balance;

/**
 * Vue graphique du mini-jeu {@link Balance} : dessine la planche inclinée et la
 * bille.
 */
public final class BalanceView implements GameView {

  /** État de jeu lu pour produire le dessin. */
  private final Balance balance;

  /**
   * Crée une vue associée au mini-jeu fourni.
   *
   * @param balance mini-jeu dont l'état doit être représenté
   * @throws NullPointerException si {@code balance} est {@code null}
   */
  public BalanceView(Balance balance) {
    Objects.requireNonNull(balance);
    this.balance = balance;
  }

  /**
   * Dessine la planche à son angle courant et positionne la bille
   * proportionnellement à sa position sur la piste.
   *
   * @param graphics contexte graphique à utiliser
   * @param area     zone disponible pour le dessin
   * @throws NullPointerException si {@code graphics}, le jeu associé ou
   *                              {@code area} est nul
   */
  @Override
  public void render(Graphics2D graphics, Rectangle area) {
    GameView.checkInput(graphics, balance, area);

    var centerX = area.x + area.width / 2;
    var centerY = area.y + area.height / 2;
    var halfPlank = area.width / 2 - 15;

    var tiltRad = Math.toRadians(balance.tilt());
    var dx = (int) (halfPlank * Math.cos(tiltRad));
    var dy = (int) (halfPlank * Math.sin(tiltRad));

    var leftX = centerX - dx + 100;
    var leftY = centerY - dy;
    var rightX = centerX + dx - 100;
    var rightY = centerY + dy;

    graphics.setColor(new Color(180, 140, 90));
    graphics.setStroke(new BasicStroke(6));
    graphics.drawLine(leftX, leftY, rightX, rightY);

    var t = (balance.position() + Balance.TRACK_HALF_LENGTH) / (2 * Balance.TRACK_HALF_LENGTH);
    var ballX = (int) (leftX + t * (rightX - leftX));
    var ballY = (int) (leftY + t * (rightY - leftY));

    graphics.setColor(Color.WHITE);
    graphics.fillOval(ballX - 7, ballY - 15, 14, 14);
  }
}