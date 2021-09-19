package gui;

import java.awt.*;

public enum SquareType {
	UNVISITED (Color.GREEN),
	OPEN (Color.YELLOW),
	VISITED (Color.WHITE),
	OBSTACLE (Color.GRAY),
	PATH (Color.MAGENTA),
	START (Color.BLUE),
	DESTINATION (Color.RED);
	
	public final Color colour;
	
	SquareType(Color c){
		this.colour = c;
	}
}
