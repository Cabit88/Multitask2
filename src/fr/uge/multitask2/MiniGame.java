package fr.uge.multitask2;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;

public interface MiniGame {
    void update();                                                  // Met à jour la physique (gravité, balance)
    void draw(ApplicationContext context, int startX, int startY, int width, int height); // Affiche le jeu dans sa zone
    void handleKeyPress(KeyboardEvent.Key key);                     // Gère les touches clavier
    boolean isGameOver();                                           // Vérifie si le joueur a perdu
}
