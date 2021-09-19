package a.star;

import java.util.*;

/**
 * This class defines functions to find a path of some given graph.
 * createNodes() should be used to initialise a list of search nodes.
 * search() may then be used to find a path from A to B.
 * @author Sohan Dhanak
 *
 * @param <T> The type of data that the graph is composed of.
 */
public abstract class Search<T> {
	
	/**
	 * Creates a list of nodes from the given graph.
	 * @param graph The graph (in list form) to be traversed.
	 * @return The list of search node with relevant metadata for searching.
	 */
	public abstract List<Node<T>> createNodes(List<T> graph);
	
	/**
	 * Adds the relevant neighbour nodes from the graph to the given node.
	 * @param node The current node.
	 * @param graph The search nodes to be traversed.
	 */
	protected abstract void addNodeNeighbours(Node<T> node, List<Node<T>> graph);
	
	/**
	 * Calculates the g cost from this node to its neighbour.
	 * @param currentNode The node whose g cost is to be calculated.
	 * @param neighbour The neighbour of the current node.
	 * @return The calculated g cost for the current node.
	 */
	protected abstract int calculateGCost(Node<T> currentNode, Node<T> neighbour);
	
	/**
	 * Calculates a heuristic distance from the current node to the destination node.
	 * @param currentNode The node whose h cost is to be calculated.
	 * @param destNode The destination node.
	 * @return The heuristic distance from the current node to some other node.
	 */
	protected abstract int calculateHCost(Node<T> currentNode, Node<T> destNode);
	
	/**
	 * The behaviour for the algorithm to obey when a node becomes visited.
	 * @param node The node which was visited.
	 */
	protected abstract void onNodeVisit(Node<T> node);
	
	/**
	 * The behaviour for the algorithm to obey when a node becomes open.
	 * @param node The node which was added to the open list.
	 */
	protected abstract void onNodeOpen(Node<T> node);
	
	/**
	 * Determines whether a node may be visited at any point.
	 * @param node The node in question.
	 * @return True: if and only if the node data permits the node in question to be visited. 
	 */
	protected abstract boolean isNodeTraversable(Node<T> node);
	
	/**
	 * Determines a path (possibly shortest) from the start node to the destination node.
	 * NB. This method may not be overridden and any additional functionality should be defined through:
	 *     - onNodeVisit()
	 *     - onNodeOpen()
	 *     - isNodeTraversable()
	 *     - calculate[G/H]Cost()
	 * @param graph The graph to be traversed.
	 * @param start The starting node.
	 * @param dest The destination node.
	 * @return A list of nodes that define a path from the start node to the destination node.
	 */
	public final List<Node<T>> search(List<Node<T>> graph, Node<T> start, Node<T> dest) {
		//Init open list
		List<Node<T>> open = new ArrayList<Node<T>>();
		boolean pathFound = false;
		
		//Set up start node as first on open list
		start.setgCost(0);
		start.sethCost(calculateHCost(start, dest));
		start.updateFCost();
		
		open.add(start);
		
		while(open.size() > 0 && !pathFound) {
			//Pop node with lowest fCost
			Node<T> current = Collections.min(open);
			open.remove(current);
			List<Node<T>> neighbours = current.getNeighbours();
			
			//Call function for when a node is visited
			onNodeVisit(current);
			
			//for all neighbours of the current node
			for(Node<T> child : neighbours) {
				if(child.getParent() == null)
					child.setParent(current);
				
				//End the algorithm if the destination was found
				if(child == dest) {
					pathFound = true;
				} else {
					//Find g cost of the child
					int childGCost = current.getgCost() + calculateGCost(current, child);
					
					if(isNodeTraversable(child)) {
						//Adjust g cost if a shorter path to this child was found
						if((childGCost < child.getgCost()) || child.getgCost() == -1) {
							child.setgCost(childGCost);
							child.setParent(current);
						}
						
						child.sethCost(calculateHCost(child, dest));
						child.updateFCost();
						
						//Add children to open list and perform desired functionality on newly opened node
						if(!open.contains(child)) {
							onNodeOpen(child);
							open.add(child);
						}
					}
				}
			}
		}
		
		//Return a path if one was found (empty list otherwise)
		List<Node<T>> path = new ArrayList<Node<T>>();
		if(pathFound) {
			Node<T> pathNode = dest;
			
			while(pathNode != start) {
				path.add(pathNode);
				pathNode = pathNode.getParent();
			}
			
			path.add(start);
			Collections.reverse(path);
		}
		return path;
	}
}