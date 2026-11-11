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
	    	if(event instanceof PointerEvent _) {
	    		game(context, width, height);
	    	}
	    }
	}
	
	public static void game(ApplicationContext context, int width, int height) {
	    double angle = 0;
	    double ballPos = 0;
	    double ballVelocity = 0;
	    
	    long gameStartTime = System.currentTimeMillis();
	    
	    while(true) {
	    	long currentMillis = System.currentTimeMillis();
	        long time = (currentMillis - gameStartTime) / 1000;
            
            ballVelocity += Math.sin(angle) * 0.5;
            ballVelocity *= 0.9;
            ballPos += ballVelocity;
            
            if(ballPos < 0) {
            	angle -= 0.0005;
            } else {
            	angle += 0.0005;
            }
            
            if(ballPos > 160 || ballPos < -160) {
            	Graphics.gameLose(context, width, height, time);
            	var event = context.pollOrWaitEvent(16);
        		if(event != null) {
        			context.dispose();
        			return;
        		}
            }
            
	    	Graphics.drawGame1(context, width, height, angle, ballPos, time);
	    	Event event = context.pollOrWaitEvent(16);
		    if(event instanceof KeyboardEvent ke ) {
		    	KeyboardEvent.Key touche = ke.key();
		    	
		    	if(touche == KeyboardEvent.Key.LEFT) {
		    		angle -= 0.05;
		    	} else if(touche == KeyboardEvent.Key.RIGHT) {
		    		angle += 0.05;
		    	}
		    } else if (event instanceof PointerEvent pe) {
		        PointerEvent.Action action = pe.action();
		        
		        if (action == PointerEvent.Action.POINTER_DOWN) {
		            var location = pe.location();
		            double x = location.x();
		            double y = location.y();
		            
		            context.dispose();
		    		return;
		        }
		    }
	    }
	}
	
    public static void main(String[] args) {
    	Application.run(new Color(203, 203, 203), Main::menu);
    }
}
