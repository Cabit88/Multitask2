package multitask2.model.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import multitask2.controller.Input;
import multitask2.controller.KeyBindings;
import multitask2.model.Game;

/**
 * Mini-jeu d'esquive : le joueur change de rangée pour éviter des missiles qui
 * traversent horizontalement la zone de jeu depuis les deux côtés.
 *
 * <p>
 * La cadence d'apparition et la vitesse des missiles augmentent progressivement.
 * Une collision avec un missile au centre sur la rangée du joueur met fin à la
 * partie.
 * </p>
 */
public final class Avoid implements Game {

  /** Largeur de l'aire de jeu dans les coordonnées du modèle. */
  public static final double WIDTH = 200;
  /** Position centrale du joueur dans les coordonnées du modèle. */
  public static final double PLAYER_X = WIDTH / 2;
  /** Nombre de rangées disponibles pour le joueur et les missiles. */
  public static final int ROWS = 5;

  /** Vitesse de déplacement initiale d'un missile, en unités par seconde. */
  private static final double BASE_MISSILE_SPEED = 16;
  /** Intervalle initial entre deux apparitions de missiles, en secondes. */
  private static final double BASE_SPAWN_INTERVAL = 2.5;

  /**
   * Missile se déplaçant horizontalement sur une rangée fixe.
   *
   * @param x        position horizontale du missile dans les coordonnées du modèle
   * @param row      indice de la rangée occupée par le missile
   * @param fromLeft {@code true} si le missile vient de la gauche
   */
  public record Missile(double x, int row, boolean fromLeft) {

    public Missile(double x, int row) {
      this(x, row, true);
    }

    /**
     * Déplace le missile horizontalement, en conservant sa rangée et son côté.
     *
     * @param dx déplacement horizontal à ajouter à la position courante
     * @return nouvelle valeur immuable représentant le missile déplacé
     */
    private Missile move(double dx) {
      return new Missile(x + (fromLeft ? dx : -dx), row, fromLeft);
    }
  }

  /** Rangée actuellement occupée par le joueur. */
  private int row;
  /** Missiles présents dans l'aire de jeu. */
  private final List<Missile> missiles;
  /** Temps restant avant la prochaine apparition d'un missile, en secondes. */
  private double spawnTimer;
  /** Générateur utilisé pour choisir aléatoirement la rangée des missiles. */
  private final Random random;
  /** Alterne le côté d'apparition des missiles. */
  private boolean nextMissileFromLeft;
  /** Temps passé dans ce mini-jeu, utilisé pour sa difficulté progressive. */
  private double elapsedTime;

  /**
   * Crée le jeu avec le joueur sur la rangée centrale et initialise le compte à
   * rebours.
   */
  public Avoid() {
    row = ROWS / 2;
    missiles = new ArrayList<>();
    spawnTimer = BASE_SPAWN_INTERVAL;
    random = new Random();
    nextMissileFromLeft = true;
    elapsedTime = 0;
    super();
  }

  /**
   * Traite les changements de rangée, fait apparaître et avance les missiles,
   * puis vérifie si l'un d'eux a atteint le joueur.
   *
   * @param dt         durée du pas de simulation, en secondes
   * @param input      état des entrées clavier
   * @param difficulty niveau de difficulté global, validé pour le contrat commun
   * @return {@code true} si aucun missile n'a atteint le joueur au centre,
   *         {@code false} en cas de collision
   * @throws IllegalArgumentException si {@code dt} ou {@code difficulty} est
   *                                  négatif
   * @throws NullPointerException     si {@code input} est {@code null}
   */
  @Override
  public boolean update(double dt, Input input, double difficulty) {
    Game.checkInput(dt, input, difficulty);

    elapsedTime += dt;
    var localDifficulty = elapsedTime / 10;
    readInput(input);
    spawnMissiles(dt, localDifficulty);
    moveMissiles(dt, localDifficulty);
    return checkCollision();
  }

  /**
   * Déplace le joueur d'au plus une rangée en réponse à un nouvel appui sur la
   * flèche haut ou bas. Le déplacement est limité à l'intervalle des rangées
   * disponibles.
   *
   * @param input état des entrées clavier
   */
  private void readInput(Input input) {
    var up = input.isKeyJustPressed(KeyBindings.up());
    var down = input.isKeyJustPressed(KeyBindings.down());
    var dir = (up ? 1 : 0) - (down ? 1 : 0);
    if (dir != 0) {
      row = Game.withinBounds(row + dir, 0, ROWS - 1);
    }
  }

  /**
   * Diminue le compte à rebours et fait apparaître un missile sur une rangée
   * aléatoire lorsque celui-ci devient strictement négatif. L'intervalle diminue
   * avec la difficulté locale, sans passer sous 0,9 seconde.
   *
   * @param dt         durée du pas de simulation, en secondes
   * @param difficulty difficulté locale, calculée depuis l'activation du mini-jeu
   */
  private void spawnMissiles(double dt, double difficulty) {
    var spawnInterval = Math.max(0.9, BASE_SPAWN_INTERVAL - difficulty * 0.2);
    spawnTimer -= dt;
    if (spawnTimer < 0) {
      var x = nextMissileFromLeft ? 0 : WIDTH;
      missiles.add(new Missile(x, random.nextInt(ROWS), nextMissileFromLeft));
      nextMissileFromLeft = !nextMissileFromLeft;
      spawnTimer = spawnInterval;
    }
  }

  /**
   * Déplace chaque missile vers le centre à la vitesse déterminée par la
   * difficulté locale.
   *
   * @param dt         durée du pas de simulation, en secondes
   * @param difficulty difficulté locale, calculée depuis l'activation du mini-jeu
   */
  private void moveMissiles(double dt, double difficulty) {
    var missileSpeed = BASE_MISSILE_SPEED + difficulty * 3;
    missiles.replaceAll(m -> m.move(dt * missileSpeed));
  }

  /**
   * Détecte les collisions au centre et retire les missiles qui l'ont atteint
   * sans toucher le joueur.
   *
   * @return {@code false} si un missile a atteint le joueur ; {@code true} sinon
   */
  private boolean checkCollision() {
    if (missiles.stream().anyMatch(m -> m.row == row
        && (m.fromLeft ? m.x >= PLAYER_X : m.x <= PLAYER_X))) {
      return false;
    }
    missiles.removeIf(m -> m.fromLeft ? m.x >= PLAYER_X : m.x <= PLAYER_X);
    return true;
  }

  /**
   * Retourne la rangée actuellement occupée par le joueur.
   *
   * @return indice de rangée, compris entre {@code 0} et {@code ROWS - 1}
   */
  public int row() {
    return row;
  }

  /**
   * Retourne les missiles actuellement présents.
   *
   * <p>
   * La liste ne peut pas être modifiée par l'appelant, mais elle reflète les
   * modifications effectuées lors des mises à jour suivantes du jeu.
   * </p>
   *
   * @return vue non modifiable de la liste des missiles
   */
  public List<Missile> missiles() {
    return Collections.unmodifiableList(missiles);
  }
}