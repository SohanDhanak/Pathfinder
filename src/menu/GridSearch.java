package menu;

import java.util.*;
import a.star.*;
import gui.*;

public class GridSearch extends Search<Square> {
	
	public static final int ADJ_DIST = 10;
	public static final int DIAG_DIST = 14;
	public static final long DELAY = 20;

	@Override
	public List<Node<Square>> createNodes(List<Square> graph) {
		List<Node<Square>> nodes = new ArrayList<Node<Square>>();
		for(Square s : graph) {
			nodes.add(new Node<Square>(s));
		}
		
		for(int i = 0; i < nodes.size(); i++) {
			addNodeNeighbours(nodes.get(i), nodes);
		}
		
		return nodes;
	}

	@Override
	protected void addNodeNeighbours(Node<Square> node, List<Node<Square>> graph) {
		for(int i = 0; i < graph.size(); i++) {
			Node<Square> current = graph.get(i);
			if(Math.abs(current.data.x - node.data.x) <= 1 && Math.abs(current.data.y - node.data.y) <= 1 && current != node) {
				node.addNeighbour(current);
			}
		}
	}

	@Override
	protected int calculateGCost(Node<Square> currentNode, Node<Square> neighbour) {
		if(currentNode.getNeighbours().contains(neighbour)) {
			boolean adjacent = (neighbour.data.x == currentNode.data.x) || (neighbour.data.y == currentNode.data.y);
			int gCost = adjacent ? ADJ_DIST : DIAG_DIST;
			return gCost;
		}
		
		return -1;
	}

	@Override
	protected int calculateHCost(Node<Square> currentNode, Node<Square> destNode) {
		int cost = Math.max(Math.abs(currentNode.data.x - destNode.data.x), Math.abs(currentNode.data.y - destNode.data.y));
		return DIAG_DIST * cost;
	}

	@Override
	protected void onNodeVisit(Node<Square> node) {
		try {
			Thread.sleep(DELAY);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		node.data.setType(SquareType.VISITED);
	}

	@Override
	protected void onNodeOpen(Node<Square> node) {
		node.data.setType(SquareType.OPEN);
		
	}

	@Override
	protected boolean isNodeTraversable(Node<Square> node) {
		SquareType type = node.data.getType();
		if(type == SquareType.OPEN || type == SquareType.UNVISITED) {
			return true;
		}
		return false;
	}

}
