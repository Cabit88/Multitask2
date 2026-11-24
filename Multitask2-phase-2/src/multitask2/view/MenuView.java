package multitask2.view;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.forax.zen.ApplicationContext;

public final class MenuView {
	public static final int MARGE = 130; // Espacement entre les boutons
	public static final int BUTTON_HEIGHT = 50; // Hauteur de la bande du bouton
	
	public static BufferedImage loadImage(String path) {
        try {
        	return ImageIO.read(MenuView.class.getResourceAsStream(path));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Impossible de charger l'image : " + path);
            return null;
        }
    }
	
	public static int buttonMenu(int width, int height, double mouseX, double mouseY) {
		if (mouseX >= width/4 && mouseY >= height/2 && mouseX <= 3 * width/4 && mouseY <= height/2 + BUTTON_HEIGHT) {
			return 1; // Play
		} else if (mouseX >= width/4 && mouseY >= height/2 + MARGE && mouseX <= 3 * width/4 && mouseY <= height/2 + MARGE + BUTTON_HEIGHT) {
			return 2; // Settings
		} else if(mouseX >= width/4 && mouseY >= height/2 + 2*MARGE && mouseX <= 3 * width/4 && mouseY <= height/2 + 2*MARGE + BUTTON_HEIGHT) {
			return 3; // Leave
		}
		return -1; // error
	}
	
	public static void drawMenu(ApplicationContext context, int width, int height, BufferedImage logo, int position) {
        int buttonY1 = height/2;
        int buttonY2 = height/2 + MARGE;
        int buttonY3 = height/2 + 2*MARGE;

        context.renderFrame(graphics -> {
            graphics.setColor(new Color(203, 203, 203));
            graphics.fillRect(0, 0, width, height);
            
            graphics.setColor(Color.DARK_GRAY);
            graphics.setFont(new Font("Arial", Font.BOLD, 16));
            graphics.drawString("By MASSIP-SOUECHRE A. and PLUVINET-VIAENE A.", 10, height - 15);

            if (logo != null) {
                graphics.drawImage(logo, width / 2 - 300, 30, 600, 300, null);
            }
            
            // Bonton Jouer
            graphics.setColor(new Color(0, 0, 0, 100));
            graphics.fillRect(width/4, buttonY1, width/2, BUTTON_HEIGHT);
            if(position == 1) {
            	graphics.setColor(Color.RED);
            	graphics.drawRect(width/4, buttonY1, width/2, BUTTON_HEIGHT);
            }
            
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Arial", Font.BOLD, 28));
            graphics.drawString("JOUER", width/2 - 45, buttonY1 + 35);
            
            
            // Bouton Paramètres
            graphics.setColor(new Color(0, 0, 0, 100));
            graphics.fillRect(width/4, buttonY2, width/2, BUTTON_HEIGHT);
            if(position == 2) {
            	graphics.setColor(Color.RED);
            	graphics.drawRect(width/4, buttonY2, width/2, BUTTON_HEIGHT);
            }
            
            graphics.setColor(Color.WHITE);
            graphics.drawString("AIDE", width/2 - 35, buttonY2 + 35);
            
            // Bouton Quitter
            graphics.setColor(new Color(0, 0, 0, 100));
            graphics.fillRect(width/4, buttonY3, width/2, BUTTON_HEIGHT);
            if(position == 3) {
            	graphics.setColor(Color.RED);
            	graphics.drawRect(width/4, buttonY3, width/2, BUTTON_HEIGHT);
            }
            
            graphics.setColor(Color.WHITE);
            graphics.drawString("QUITTER", width/2 - 55, buttonY3 + 35);
        });
    }
	
	public static void drawGameLose(ApplicationContext context, int width, int height, long score, int position) {
		context.renderFrame(graphics -> {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(width/4, 100, width/2, 150);
			
			graphics.setColor(Color.DARK_GRAY);
            graphics.setFont(new Font("Arial", Font.BOLD, 32));
            graphics.drawString("Ton score est", width/2 - 100, 150);
            graphics.drawString("" + score, width/2 - 10, 210);
            
            String[] choice = {"Rejouer", "Retour au Menu", "Quitter"};
            var startY = 200;
            for(int i = 0; i < choice.length; i++) {
            	startY += MARGE;
            	
            	graphics.setColor(Color.WHITE);
            	graphics.fillRect(width/2 - 250, startY, width/2 - 250, BUTTON_HEIGHT);
            	
            	graphics.setColor(Color.BLACK);
            	graphics.drawString(choice[i], width/2 - 50, startY + 35);
            	if(position == i+1) {
                	graphics.setColor(Color.RED);
                	graphics.drawRect(width/2 - 250, startY, width/2 - 250, BUTTON_HEIGHT);
                }
            }
		});
	}
	
	public static int buttonLose(int width, int height, double mouseX, double mouseY) {
	    if (mouseX >= width/2 - 250 && mouseY >= 200 + MARGE && mouseX <= 2 * (width/2 - 250) && mouseY <= 200 + MARGE + BUTTON_HEIGHT) {
	      return 1; // Rejouer
	    } else if (mouseX >= width/2 - 250 && mouseY >= 200 + 2*MARGE && mouseX <= 2 * (width/2 - 250) && mouseY <= 200 + 2*MARGE + BUTTON_HEIGHT) {
	      return 2; // Retour au Menu
	    } else if (mouseX >= width/2 - 250 && mouseY >= 200 + 3*MARGE && mouseX <= 2 * (width/2 - 250) && mouseY <= 200 + 3*MARGE + BUTTON_HEIGHT) {
	      return 3; // Quitter
	    }
	    return -1; // error
	}
	
	public static void drawHelp(Graphics2D graphics, int width, int height) {
	    graphics.setColor(GeneralView.BACKGROUND);
	    graphics.fillRect(0, 0, width, height);
	    graphics.setColor(Color.WHITE);
	    graphics.setFont(new Font("Arial", Font.BOLD, 24));
	    var lines = new String[] {
	    		"AIDE :",
	    		"Balance : ← →, Garder une bille sur une planche que l’on incline.",
	    		"Avoid : ↑ ↓, Esquiver des missiles sur l’une des cinq lignes.",
	    		"Touch : ZQSD (AZERTY), Déplacer un carré pour toucher des blocs avant la fin de leur compte à rebours.",
	    		"Fly : Espace, Maintenir la touche pour monter, la relˆacher pour descendre, et éviter les obstacles.",
	    		"Jump : Maj, Sauter au bon moment par-dessus des trous dans le sol.",
	    		"Circle : Q/E, Faire tourner un cercle percé autour de son centre pour capturer des triangles qui arrivent de toutes les directions.",
	    		"Test : W/X/C/V/B/N pour activer chaque jeu",
	        	"Echap ou Espace : revenir au menu"};
	    for (var i = 0; i < lines.length; i++) {
	      if(i == lines.length-1) {
	    	  graphics.setColor(Color.RED);
	      }
	      graphics.drawString(lines[i], 50, 50 + i * 100);
	    }
	  }
	
}
