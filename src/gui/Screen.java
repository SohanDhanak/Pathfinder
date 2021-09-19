package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class Screen extends JPanel implements Runnable, MouseListener {
	private static final long serialVersionUID = 1L;
	public static final int SCREEN_WIDTH = 500;
	public static final int SCREEN_HEIGHT = 500;
	public static final long DELAY = 10;

	private JFrame screen;
	
	private BufferedImage buffer;
	private Graphics2D graphics;
	
	private Square[][] squares;
	
	private SquareType clickState;
	
	public Screen() {
		//Initialise graphics components
		init();
		/*-----------------------------*/
		squares = new Square[25][25];
		for(int i = 0; i < 25; i++) {
			for(int j = 0; j < 25; j++) {
				squares[j][i] = new Square(j, i);
			}
		}
		/*-----------------------------*/
		//Start refresh loop
		Thread t = new Thread(this);
		t.start();
		
	}
	
	private void init() {
		//Create JFrame
		screen = new JFrame("Search algorithms");
		screen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		screen.setResizable(false);
		screen.setBackground(Color.BLACK);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Set panel
		screen.setContentPane(this);
		screen.setVisible(true);

		//Reset sizes
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		Insets insets = screen.getInsets();
		screen.setSize(insets.left+insets.right+SCREEN_WIDTH, insets.top+insets.bottom+SCREEN_HEIGHT);

		//Create 'Canvas'
		buffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		graphics = buffer.createGraphics();

		this.addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		Graphics2D window = (Graphics2D) g;
		
		//Lock JPanel
		synchronized(this) {
			//Clear canvas
			graphics.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			
			//Redraw all squares
			for(Square[] row : squares) {
				for(Square s : row) {
					graphics.setColor(s.getType().colour);
					graphics.fillRect(s.x * Square.LENGTH, s.y * Square.LENGTH, Square.LENGTH, Square.LENGTH);
					graphics.setColor(Color.BLACK);
					graphics.drawRect(s.x * Square.LENGTH, s.y * Square.LENGTH, Square.LENGTH, Square.LENGTH);
				}
			}
			
			Insets insets = this.getInsets();
			window.drawImage(buffer, insets.left, insets.top, this);
			
		}
	}
	
	public Square[][] getSquares(){
		return squares;
	}

	@Override
	public void run() {
		try {
			while(true) {
				this.repaint();
				Thread.sleep(DELAY);
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//Figure out which square was clicked
		int squareX = e.getX() / Square.LENGTH;
		int squareY = e.getY() / Square.LENGTH;
		
		//Get the square
		Square clickedSquare = squares[squareX][squareY];
		
		if(clickedSquare.getType() == clickState) {
			clickedSquare.setType(SquareType.UNVISITED);
		} else {
			if(clickState == SquareType.OBSTACLE) {
				clickedSquare.setType(clickState);
			} else {
				boolean exists = false;
				
				for(int i = 0; i < 25 && !exists; i++) {
					for(int j = 0; j < 25 && !exists; j++) {
						if(squares[i][j].getType() == clickState) {
							exists = true;
						}
					}
				}
				
				if(!exists)
					clickedSquare.setType(clickState);
			}
		}
	}
	
	public void setClickState(SquareType state) {
		clickState = state;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
