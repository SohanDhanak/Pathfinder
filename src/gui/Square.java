package gui;

public class Square {
	
	public static final int LENGTH = 20;
	public final int x;
	public final int y;
	private SquareType squareType;
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		squareType = SquareType.UNVISITED;
	}
	
	public Square(int x, int y, SquareType type) {
		this.x = x;
		this.y = y;
		squareType = type;
	}
	
	public SquareType getType() {
		return squareType;
	}
	
	public void setType(SquareType s) {
		squareType = s;
	}
	
	
}
