package multitask2.model.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import multitask2.controller.Input;
import multitask2.controller.KeyBindings;
import multitask2.model.Game;

/** Déplace un carré avec WASD/ZQSD pour toucher les blocs temporisés. */
public final class Touch implements Game {
  public static final int WIDTH = 100;
  public static final int HEIGHT = 100;

  public record Target(int x, int y, double remaining) {
    public Target move(double dt, double difficulty) {
      return new Target(x, y, remaining - dt);
    }
  }

  private double playerX;
  private double playerY;
  private final List<Target> targets;
  private double spawnTimer;
  private final Random random;

  public Touch() {
    playerX = 50;
    playerY = 50;
    targets = new ArrayList<>();
    spawnTimer = 0;
    random = new Random();
  }

  @Override
  public boolean update(double dt, Input input, double difficulty) {
    Game.checkInput(dt, input, difficulty);
    movePlayer(dt, input);
    return updateTargets(dt, difficulty);
  }

  private void movePlayer(double dt, Input input) {
    // calcule gauche droite puis haut bas
    var dx = (input.isKeyDown(KeyBindings.touchRight()) ? 1 : 0)
        - (input.isKeyDown(KeyBindings.touchLeft()) ? 1 : 0);
    var dy = (input.isKeyDown(KeyBindings.touchDown()) ? 1 : 0)
        - (input.isKeyDown(KeyBindings.touchUp()) ? 1 : 0);
    playerX = Game.dontExceed(playerX + dx * 45 * dt - 50, 50) + 50;
    playerY = Game.dontExceed(playerY + dy * 45 * dt - 50, 50) + 50;
  }

  private boolean updateTargets(double dt, double difficulty) {
    // cree une cible puis retire celles touchees ou expirees
    spawnTimer -= dt;
    if (spawnTimer <= 0) {
      var lifetime = Math.max(1.2, 3.2 - difficulty * 0.12);
      targets.add(new Target(10 + random.nextInt(81), 10 + random.nextInt(81), lifetime));
      spawnTimer = Math.max(0.7, 1.8 - difficulty * 0.08);
    }
    targets.replaceAll(target -> target.move(dt, difficulty));
    if (targets.removeIf(target -> target.remaining() <= 0)) return false;
    targets.removeIf(this::hit);
    return true;
  }

  private boolean hit(Target target) {
    return Math.abs(playerX - target.x()) < 8 && Math.abs(playerY - target.y()) < 8;
  }

  public double playerX() { return playerX; }
  public double playerY() { return playerY; }
  public List<Target> targets() { return Collections.unmodifiableList(targets); }
}
