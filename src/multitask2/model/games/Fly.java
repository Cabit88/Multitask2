package multitask2.model.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.github.forax.zen.KeyboardEvent.Key;

import multitask2.controller.Input;
import multitask2.controller.KeyBindings;
import multitask2.model.Game;

/** Maintient une sphère en vol avec l'espace tout en évitant les obstacles. */
public final class Fly implements Game {
  public static final int WIDTH = 100;
  public static final int HEIGHT = 100;

  public record Pillar(double x, double gapY, double gapHeight) {}

  private double y;
  private double velocity;
  private final List<Pillar> pillars;
  private double spawnTimer;
  private final Random random;

  public Fly() {
    y = 50;
    velocity = 0;
    pillars = new ArrayList<>();
    spawnTimer = 1.5;
    random = new Random();
  }

  @Override
  public boolean update(double dt, Input input, double difficulty) {
    Game.checkInput(dt, input, difficulty);
    updateHeight(dt, input);
    updatePillars(dt, difficulty);
    return y >= 0 && y <= HEIGHT && !collision();
  }

  private void updateHeight(double dt, Input input) {
    velocity += (input.isKeyDown(KeyBindings.fly()) ? -38 : 24) * dt;
    velocity = Game.dontExceed(velocity, 34);
    y += velocity * dt;
  }

  private void updatePillars(double dt, double difficulty) {
    // cree des obstacles puis les fait aller vers la gauche
    spawnTimer -= dt;
    if (spawnTimer <= 0) {
      var gap = Math.max(24, 38 - difficulty);
      var gapY = 15 + random.nextInt(71);
      pillars.add(new Pillar(WIDTH, gapY, gap));
      spawnTimer = Math.max(1.0, 2.0 - difficulty * 0.05);
    }
    pillars.replaceAll(pillar -> new Pillar(pillar.x() - (28 + difficulty * 5) * dt,
        pillar.gapY(), pillar.gapHeight()));
    pillars.removeIf(pillar -> pillar.x() < -10);
  }

  private boolean collision() {
    // perd si on touche un obstacle hors du trou
    for (var pillar : pillars) {
      if (pillar.x() < 12 && pillar.x() > -12
          && (y < pillar.gapY() - pillar.gapHeight() / 2
          || y > pillar.gapY() + pillar.gapHeight() / 2)) {
        return true;
      }
    }
    return false;
  }

  public double y() { return y; }
  public List<Pillar> pillars() { return Collections.unmodifiableList(pillars); }
}
