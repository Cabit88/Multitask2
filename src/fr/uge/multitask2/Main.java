package fr.uge.multitask2;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import java.awt.*;

public class Main {
	public static final long startTime = System.currentTimeMillis();
	
	public static long getScore() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
	
	private static boolean handleMenuChoice(ApplicationContext context, int width, int height, int position) {
	    switch(position) {
	        case 1 -> game(context, width, height);
	        case 2 -> IO.println("HELLLOOOOOOOOOOOOOOOOO");
	        case 3 -> {
	            context.dispose();
	            return true;
	        }
	    }
	    return false;
	}
	
	public static void menu(ApplicationContext context) {
	    var screenInfo = context.getScreenInfo();
	    var width = screenInfo.width();
	    var height = screenInfo.height();

	    var logo = Graphics.loadImage("/data/logo.jpeg");
	    var position = 1; // position du rectangle rouge
	    
	    while(true) {
	    	Graphics.drawMenu(context, width, height, logo, position);
	    	Event event = context.pollOrWaitEvent(16);
	    	if(event instanceof KeyboardEvent ke) {
	    		if(ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
	    			if (ke.key() == KeyboardEvent.Key.Z || ke.key() == KeyboardEvent.Key.UP) {
		    			position--;
		    			if(position < 1) {
		    				position = 3;
		    			}
		    		} else if (ke.key() == KeyboardEvent.Key.S || ke.key() == KeyboardEvent.Key.DOWN) {
		    			position++;
		    			if(position > 3) {
		    				position = 1;
		    			}
		    		} else if(ke.key() == KeyboardEvent.Key.SPACE) {
		    			if (handleMenuChoice(context, width, height, position)){
		    				return;
		    			}
		    		}
	    		}
            } else if (event instanceof PointerEvent pe) {
		        PointerEvent.Action action = pe.action();
		        var location = pe.location();
	            var x = location.x();
	            var y = location.y();
	            position = Graphics.buttonMenu(width, height, x, y);
	            
		        if (action == PointerEvent.Action.POINTER_DOWN) {
		        	if (position != -1 && handleMenuChoice(context, width, height, position)) {
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
                Graphics.drawGameLose(context, width, height, getScore());
                while(true) {
                    var event = context.pollOrWaitEvent(16);
                    if(event != null) {
                        return;
                    }
                }
            }
            
            long time = (System.currentTimeMillis() - startTime) / 1000;
            Graphics.drawHeader(context, width, height, time);
            game.draw(context, 0, 50, width, height - 50);
            
            Event event = context.pollOrWaitEvent(16);
            if(event instanceof KeyboardEvent ke) {
            	if (ke.action() == KeyboardEvent.Action.KEY_PRESSED) {
                    game.handleKeyPress(ke.key());
                }
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
