package multitask2.model.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import multitask2.controller.Input;
import multitask2.controller.KeyBindings;
import multitask2.model.Game;

/** Fait sauter un personnage avec Maj pour franchir les trous du sol. */
public final class Jump implements Game {
  public static final int WIDTH = 100;
  public static final int HEIGHT = 100;

  public record Hole(double x, double width) {}

  private static final double PLAYER_X = 30;

  private double height;
  private double velocity;
  private boolean onGround;
  private final List<Hole> holes;
  private double spawnTimer;
  private final Random random;

  public Jump() {
    height = 0;
    velocity = 0;
    onGround = true;
    holes = new ArrayList<>();
    spawnTimer = 2;
    random = new Random();
  }

  @Override
  public boolean update(double dt, Input input, double difficulty) {
    Game.checkInput(dt, input, difficulty);
    updateJump(dt, input);
    updateHoles(dt, difficulty);
    return onGround && overHole() ? false : true;
  }

  private void updateJump(double dt, Input input) {
    // saute puis applique gravite jusqu au sol
    if (onGround && input.isKeyJustPressed(KeyBindings.jump())) {
      velocity = 88;
      height = 5;
      onGround = false;
    }
    if (!onGround) {
      velocity -= 125 * dt;
      height += velocity * dt;
      if (height <= 0) {
        height = 0;
        velocity = 0;
        onGround = true;
      }
    }
  }

  private void updateHoles(double dt, double difficulty) {
    // fait arriver les trous vers le personnage
    spawnTimer -= dt;
    if (spawnTimer <= 0) {
      holes.add(new Hole(WIDTH, 6 + random.nextInt(5)));
      spawnTimer = Math.max(1.8, 3.2 - difficulty * 0.08);
    }
    holes.replaceAll(hole -> new Hole(hole.x() - (22 + difficulty * 3) * dt, hole.width()));
    holes.removeIf(hole -> hole.x() + hole.width() < 0);
  }

  private boolean overHole() {
    // regarde si le personnage est exactement sur un trou
    for (var hole : holes) {
      var holeRight = hole.x() + hole.width();
      if (hole.x() <= PLAYER_X && PLAYER_X <= holeRight) return true;
    }
    return false;
  }

  public double height() { return height; }
  public List<Hole> holes() { return Collections.unmodifiableList(holes); }
}
