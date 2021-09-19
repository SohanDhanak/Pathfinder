package menu;

import gui.*;
import a.star.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Menu implements ActionListener {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 200;
	
	private JFrame frame;
	private ButtonGroup buttonGroup;
	private JPanel panel;
	private JButton begin;
	private JButton clear;
	
	private Screen screen;
	private Search<Square> search;
	
	public Menu(Screen screen) {
		this.screen = screen;
		search = new GridSearch();
		
		initFrame();
	}
	
	private void initFrame() {
		frame = new JFrame("Menu");
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initButtons();
		initPanel();
		frame.setContentPane(panel);
		frame.setVisible(true);
	}
	
	private void initPanel() {
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		Enumeration<AbstractButton> buttons = buttonGroup.getElements();
		
		while(buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			panel.add(button);
		}
		
		panel.add(begin);
		panel.add(clear);
	}
	
	private void initButtons() {
		buttonGroup = new ButtonGroup();
		String[] types = {"Start", "Destination", "Obstacle"};
		boolean[] selection = {true, false, false};
		
		for(int i = 0; i < types.length; i++) {
			JRadioButton button = new JRadioButton(types[i], selection[i]);
			button.addActionListener(this);
			buttonGroup.add(button);
		}
		screen.setClickState(SquareType.START);
		
		begin = new JButton("Start search");
		begin.addActionListener(this);
		
		clear = new JButton("Clear board");
		clear.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JRadioButton) {
			JRadioButton button = (JRadioButton) e.getSource();
			String state = button.getText().toUpperCase();
			screen.setClickState(SquareType.valueOf(state));
		} else {
			if(e.getSource() == begin) {
				Square[][] squares = screen.getSquares();
				List<Square> squareList = new ArrayList<Square>();
				for(int i = 0; i < squares.length; i++) {
					for(int j = 0; j < squares[i].length; j++) {
						squareList.add(squares[i][j]);
					}
				}
				
				List<Node<Square>> nodes = search.createNodes(squareList);
				Node<Square> startNode = null;
				Node<Square> destNode = null;
				
				for(int i = 0; i < nodes.size(); i++) {
					Node<Square> current = nodes.get(i);
					
					if(current.data.getType() == SquareType.START)
						startNode = current;
					
					if(current.data.getType() == SquareType.DESTINATION)
						destNode = current;
				}
				
				if(startNode != null && destNode != null) {
					final Node<Square> runStartNode = startNode;
					final Node<Square> runDestNode = destNode;
					
					Runnable anonRun = new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							List<Node<Square>> path = search.search(nodes, runStartNode, runDestNode);
							
							for(Node<Square> p : path) {
								try{Thread.sleep(GridSearch.DELAY);}
								catch(InterruptedException e) {e.printStackTrace();}
								p.data.setType(SquareType.PATH);
							}
						}
					};
					Thread t = new Thread(anonRun);
					t.start();
				}
			} else if(e.getSource() == clear) {
				Square[][] squares = screen.getSquares();
				
				for(int i = 0; i < 25; i++) {
					for(int j = 0; j < 25; j++) {
						squares[i][j].setType(SquareType.UNVISITED);
					}
				}
			}
		}
		
	}
}
