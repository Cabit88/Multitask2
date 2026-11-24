package multitask2.model.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import multitask2.controller.Input;
import multitask2.controller.KeyBindings;
import multitask2.model.Game;

/** Maintient une sphère en vol avec l'espace tout en évitant les obstacles. */
public final class Fly implements Game {
  public static final int WIDTH = 100;
  public static final int HEIGHT = 100;

  public record Pillar(double x, double y, double height) {}
  
  /* Position de la balle */
  private double y;
  
  /* Vitesse de la balle */
  private double velocity;
  
  /* Liste des piliers */
  private final List<Pillar> pillars;
  
  /* temps restant avant le prochain pilier */
  private double spawnTimer;
  
  private final Random random;

  public Fly() {
    y = 50;
    velocity = 0;
    pillars = new ArrayList<>();
    spawnTimer = 5;
    random = new Random();
    super();
  }

  @Override
  public boolean update(double dt, Input input, double difficulty) {
    Game.checkInput(dt, input, difficulty);
    updateHeight(dt, input);
    updatePillars(dt, difficulty);
    return !collision();
  }

  private void updateHeight(double dt, Input input) {
    velocity += (input.isKeyDown(KeyBindings.fly()) ? -38 : 24) * dt;
    velocity = Game.dontExceed(velocity, 34);
    y += velocity * dt;
    // Le plafond ne fait pas perdre
    if(y < 5) {
      y = 5;
      velocity = 0;
    }
    // Le sol ne fait pas perdre
    if(y > HEIGHT) {
      y = HEIGHT;
      velocity = 0;
    }
  }

  private void updatePillars(double dt, double difficulty) {
    // cree des obstacles puis les fait aller vers la gauche
    spawnTimer -= dt;
    if (spawnTimer <= 0) {
      var barHeight = 20 + random.nextInt(15);
      var barY = 20 + random.nextInt(61);
      pillars.add(new Pillar(WIDTH, barY, barHeight));
      spawnTimer = Math.max(2.0, 5.0 - difficulty * 0.05);
    }
    pillars.replaceAll(pillar -> new Pillar(pillar.x() - (18 + difficulty * 3) * dt, pillar.y(), pillar.height()));
    pillars.removeIf(pillar -> pillar.x() < -10);
  }

  private boolean collision() {
    // Vérifie si la balle touche un pilier
    for (var pillar : pillars) {
      if (pillar.x() < 12 && pillar.x() > -12 && Math.abs(y - pillar.y()) <= pillar.height()/2.0) {
        return true;
      }
    }
    return false;
  }

  public double y() { 
	  return y; 
  }
  
  public List<Pillar> pillars() { 
	  return Collections.unmodifiableList(pillars);
  }
}
