package multitask2.model.games;

import module java.base;

import multitask2.controller.Input;
import multitask2.controller.KeyBindings;
import multitask2.model.Game;

public final class Touch implements Game {
	public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    
    public record Target(int x, int y, double remaining) {
        public Target move(double dt, double difficulty) {
          return new Target(x, y, remaining - dt * (1 + difficulty * 0.1));
        }
      }
    
    /* Coordonnées du carré du joueur */
    private double playerX;
    private double playerY;
    
    /* fait apparaitre le timer du cercle */
    private double spawnTimer;
    
    /* Liste des cercle presents sur l'écran */
    private final List<Target> targets;
    
    /* Permet de choisir aleatoirement la ligne/colonne d'un cercle*/
    private final Random random;
    
    public Touch() {
    	playerX = 50;
        playerY = 50;
        spawnTimer = 2;
        targets = new ArrayList<>();
    	random = new Random();
    	super();
    }
    
    @Override
    public boolean update(double dt, Input input, double difficulty) {
      Game.checkInput(dt, input, difficulty);
      movePlayer(dt, input);
      return updateCercle(dt, difficulty);
    }

    private void movePlayer(double dt, Input input) {
      // calcule gauche droite puis haut bas
      var dx = (input.isKeyDown(KeyBindings.touchRight()) ? 1 : 0) - (input.isKeyDown(KeyBindings.touchLeft()) ? 1 : 0);
      var dy = (input.isKeyDown(KeyBindings.touchDown()) ? 1 : 0) - (input.isKeyDown(KeyBindings.touchUp()) ? 1 : 0);
      playerX = Game.dontExceed(playerX + dx * 45 * dt - 50, 50) + 50;
      playerY = Game.dontExceed(playerY + dy * 45 * dt - 50, 50) + 50;
    }
    
    private boolean updateCercle(double dt, double difficulty) {
        // cree une cible puis retire celles touchees ou expirees
        spawnTimer -= dt;
        if (spawnTimer <= 0) {
          var lifetime = Math.max(5, 10 - difficulty * 0.12);
          targets.add(new Target(10 + random.nextInt(81), 10 + random.nextInt(81), lifetime));
          spawnTimer = Math.max(1.5, 3.0 - difficulty * 0.06);
        }
        targets.replaceAll(target -> target.move(dt, difficulty)); // met a jour le temps des cibles
        
        if (targets.removeIf(target -> target.remaining() <= 0)) {
        	return false;
        }
        
        targets.removeIf(this::hit); // supp cible si touche
        return true;
      }

      private boolean hit(Target c) {
        return Math.abs(playerX - c.x()) < 8 && Math.abs(playerY - c.y()) < 8;
      }

      public double playerX() { 
    	  return playerX; 
      }
      
      public double playerY() { 
    	  return playerY; 
      }
      
      public List<Target> targets() {
    	  return Collections.unmodifiableList(targets);
      }
}

