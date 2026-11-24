package multitask2;

import java.util.function.Function;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Action;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;

import multitask2.controller.Controller;
import multitask2.controller.KeyBindings;
import multitask2.model.Game;
import multitask2.model.Model;
import multitask2.model.games.Avoid;
import multitask2.model.games.Balance;
import multitask2.model.games.Circle;
import multitask2.model.games.Fly;
import multitask2.model.games.Jump;
import multitask2.model.games.Touch;
import multitask2.view.GeneralView;
import multitask2.view.MenuView;
import multitask2.view.Viewers;
import multitask2.view.games.AvoidView;
import multitask2.view.games.BalanceView;
import multitask2.view.games.CircleView;
import multitask2.view.games.FlyView;
import multitask2.view.games.GameView;
import multitask2.view.games.JumpView;
import multitask2.view.games.TouchView;

/**
 * Point d'entrée de l'application de mini-jeux multitâches.
 *
 * <p>
 * Cette classe crée l'interface graphique, le modèle partagé par les mini-jeux
 * et le contrôleur qui orchestre les mises à jour et le rendu. Elle enregistre
 * les mini-jeux avec leur vue respective.
 * </p>
 */
public final class Main {

  /**
   * Constructeur privé afin d'empêcher l'instanciation de cette classe
   * utilitaire.
   */
  private Main() {
	  super();
  }

  /**
   * Lance la boucle de l'application graphique.
   *
   * @param args arguments de la ligne de commande ; ils ne sont pas utilisés
   *             actuellement
   */
  public static void main(String[] args) {
    Application.run(GeneralView.BACKGROUND, Main::menu);
  }
  
  /* Met a jour l'option sélectionné du menu */
  private static int updateMenuPosition(Event event, int width, int height, int pos) {
    if (event instanceof KeyboardEvent ke && ke.action() == Action.KEY_PRESSED) {
      if (ke.key() == KeyBindings.touchUp() || ke.key() == KeyBindings.up()) {
        return pos <= 1 ? 3 : pos - 1;
      }
      if (ke.key() == KeyBindings.touchDown() || ke.key() == KeyBindings.down()) {
        return pos >= 3 ? 1 : pos + 1;
      }
    } else if (event instanceof PointerEvent pe) {
      int hovered = MenuView.buttonMenu(width, height, pe.location().x(), pe.location().y());
      return hovered != -1 ? hovered : pos;
    }
    return pos;
  }
  
  /* Vérifie si le joueur valide le choix pris */
  private static boolean isMenuValidated(Event event, int pos) {
    if (event instanceof KeyboardEvent ke && ke.action() == Action.KEY_PRESSED) {
      return ke.key() == Key.SPACE;
    }
    return event instanceof PointerEvent pe && pe.action() == PointerEvent.Action.POINTER_DOWN && pos != -1;
  }
  
  /* Execute l'action du choix validé au menu */
  private static boolean handleMenuChoice(ApplicationContext context, int width, int height, int pos) {
    switch (pos) {
      case 1 -> game(context);
      case 2 -> showHelp(context);
      case 3 -> {
        context.dispose();
        return true;
      }
    }
    return false;
  }
  
  /* Affiche l'écran d'aide */
  private static void showHelp(ApplicationContext context) {
    var screen = context.getScreenInfo();
    var width = screen.width();
    var height = screen.height();
    while (true) {
      context.renderFrame(graphics -> MenuView.drawHelp(graphics, width, height));
      var event = context.pollOrWaitEvent(16);
      if (event instanceof KeyboardEvent ke && ke.action() == Action.KEY_PRESSED
          && (ke.key() == Key.ESCAPE || ke.key() == Key.SPACE)) return;
    }
  }
  
  /* Affiche le menu + gere les interactions desssus */
  private static void menu(ApplicationContext context) {
    var screen = context.getScreenInfo();
    var width = screen.width();
    var height = screen.height();
    
    // pour charger le logo du jeu
    var logo = MenuView.loadImage("/multitask2/view/data/logo.jpeg");
    int pos = 1;
    
    while(true) {
      MenuView.drawMenu(context, width, height, logo, pos);
      var event = context.pollOrWaitEvent(16);
      
      pos = updateMenuPosition(event, width, height, pos);
      if (isMenuValidated(event, pos)) {
        if (handleMenuChoice(context, width, height, pos)) {
          return;
        }
      }
    }
  }
  
  /* Met a jour le choix selectionné du menu de l'écran défaite */
  private static int updateLosePosition(Event event, int width, int height, int pos) {
    if (event instanceof KeyboardEvent ke && ke.action() == Action.KEY_PRESSED) {
      if (ke.key() == KeyBindings.touchUp() || ke.key() == KeyBindings.up()) {
        return pos <= 1 ? 3 : pos - 1;
      }
      if (ke.key() == KeyBindings.touchDown() || ke.key() == KeyBindings.down()) {
        return pos >= 3 ? 1 : pos + 1;
      }
    } else if (event instanceof PointerEvent pe) {
      int hovered = MenuView.buttonLose(width, height, pe.location().x(), pe.location().y());
      return hovered != -1 ? hovered : pos;
    }
    return pos;
  }

  /* Affiche l'écran de fin de partie */
  private static void showLoseScreen(ApplicationContext context, int width, int height, long score) {
    int pos = 1;
    while (true) {
      MenuView.drawGameLose(context, width, height, score, pos);
      var event = context.pollOrWaitEvent(16);
      
      pos = updateLosePosition(event, width, height, pos);
      if (isMenuValidated(event, pos)) {
        if (pos == 1) { // Relance la partie
          game(context);
          return;
        } else if (pos == 2) { // Revient au menu
          return; 
        } else if (pos == 3) { // Quitte le jeu
          context.dispose();
          System.exit(0);
        }
      }
    }
  }

  /**
   * Construit les composants de la partie et exécute le contrôleur jusqu'à la fin
   * du jeu. <br>
   * Le contexte graphique est libéré même si l'exécution du contrôleur se termine
   * à la suite d'une exception.
   *
   * @param context contexte graphique fourni par la bibliothèque Zen
   */
  private static void game(ApplicationContext context) {
    var model = new Model();
    var viewers = new Viewers();
    // ajout des differents mini jeu
    addGame(model, viewers, new Balance(), 0, BalanceView::new);
    addGame(model, viewers, new Avoid(), 15, AvoidView::new);
    addGame(model, viewers, new Touch(), 25, TouchView::new);
    addGame(model, viewers, new Jump(), 35, JumpView::new);
    addGame(model, viewers, new Fly(), 50, FlyView::new);
    addGame(model, viewers, new Circle(), 65, CircleView::new);

    var view = new GeneralView(context, viewers);
    var controller = new Controller(model, view, context);

    try {
      controller.run();
    } finally {
      var screen = context.getScreenInfo();
      showLoseScreen(context, screen.width(), screen.height(), model.time());
    }
  }

  /**
   * Enregistre un mini-jeu dans le modèle et associe à cette instance sa vue
   * graphique.
   *
   * @param <G>     type concret du mini-jeu
   * @param model   modèle auquel ajouter le mini-jeu
   * @param viewers registre dans lequel enregistrer sa vue
   * @param game    instance du mini-jeu à enregistrer
   * @param time    temps, en secondes depuis le début de la partie, auquel
   *                activer le mini-jeu
   * @param factory fonction construisant une vue à partir de l'instance du
   *                mini-jeu
   */
  private static <G extends Game> void addGame(Model model, Viewers viewers, G game, int time,
      Function<G, GameView> factory) {
    model.addGame(game, time);
    viewers.addGame(game, factory.apply(game));
  }
}
