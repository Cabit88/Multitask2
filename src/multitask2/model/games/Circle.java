package multitask2.model.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import multitask2.controller.Input;
import multitask2.controller.KeyBindings;
import multitask2.model.Game;

/** Oriente un cercle percé pour capturer les triangles arrivants. */
public final class Circle implements Game {
  public static final int SIZE = 100;

  public record Triangle(double angle, double radius) {}

  private double openingAngle;
  private final List<Triangle> triangles;
  private double spawnTimer;
  private final Random random;

  public Circle() {
    openingAngle = 0;
    triangles = new ArrayList<>();
    spawnTimer = 1;
    random = new Random();
  }

  @Override
  public boolean update(double dt, Input input, double difficulty) {
    Game.checkInput(dt, input, difficulty);
    rotate(dt, input);
    updateTriangles(dt, difficulty);
    return !missed();
  }

  private void rotate(double dt, Input input) {
    // tourne a gauche ou a droite
    var direction = (input.isKeyDown(KeyBindings.circleRight()) ? 1 : 0)
        - (input.isKeyDown(KeyBindings.circleLeft()) ? 1 : 0);
    openingAngle = (openingAngle + direction * 110 * dt + 360) % 360;
  }

  private void updateTriangles(double dt, double difficulty) {
    // cree les triangles puis les rapproche du centre
    spawnTimer -= dt;
    if (spawnTimer <= 0) {
      triangles.add(new Triangle(random.nextInt(360), 48));
      spawnTimer = Math.max(1.1, 2.4 - difficulty * 0.07);
    }
    triangles.replaceAll(triangle -> new Triangle(triangle.angle(),
        triangle.radius() - (14 + difficulty * 3) * dt));
  }

  private boolean missed() {
    // perd si un triangle arrive hors de l ouverture
    var iterator = triangles.iterator();
    while (iterator.hasNext()) {
      var triangle = iterator.next();
      if (triangle.radius() <= 12) {
        var difference = Math.abs(normalize(triangle.angle() - openingAngle));
        if (Math.min(difference, 360 - difference) > 45) return true;
        iterator.remove();
      }
    }
    return false;
  }

  private double normalize(double angle) { return (angle + 360) % 360; }

  public double openingAngle() { return openingAngle; }
  public List<Triangle> triangles() { return Collections.unmodifiableList(triangles); }
}
