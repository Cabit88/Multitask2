package fr.uge.multitask2;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import java.awt.*;

public class Main {
	public static void menu(ApplicationContext context) {
	    var screenInfo = context.getScreenInfo();
	    var width = screenInfo.width();
	    var height = screenInfo.height();

	    var logo = Graphics.loadImage("/data/logo.jpeg"); 
	    
	    while(true) {
	    	Graphics.drawMenu(context, width, height, logo);
	    	Event event = context.pollOrWaitEvent(16);
	    	if (event instanceof PointerEvent pe) {
		        PointerEvent.Action action = pe.action();
		        
		        if (action == PointerEvent.Action.POINTER_DOWN) {
		            var location = pe.location();
		            var x = location.x();
		            var y = location.y();
		            
		            if(Graphics.isClickOnPlay(x, y, width, height)) {
		            	game(context, width, height);
		            } else if(Graphics.isClickOnSetting(x, y, width, height)) {
		            	IO.println("HELLOOOOOOOOOOOOOOOOO");
		            } else if(Graphics.isClickOnLeave(x, y, width, height)) {
		            	context.dispose();
		            	return;
		            }
		        }
	    	}
	    
	    }
	}
	
	public static void game(ApplicationContext context, int width, int height) {
		var game = new BalanceGame();
		while(true) {
            game.update();
            
            if(game.isGameOver()) {
                Graphics.gameLose(context, width, height, game.getScore());
                while(true) {
                    var event = context.pollOrWaitEvent(16);
                    if(event != null) {
                        return;
                    }
                }
            }
            
            game.draw(context, 0, 0, width, height);
            
            Event event = context.pollOrWaitEvent(16);
            if(event instanceof KeyboardEvent ke) {
                game.handleKeyPress(ke.key());
            } else if (event instanceof PointerEvent pe) {
                if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
                    return;
                }
            }
        }
	}
	
    public static void main(String[] args) {
    	Application.run(new Color(203, 203, 203), Main::menu);
    }
}
