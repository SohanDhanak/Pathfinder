package a.star;

import java.util.*;

public class Node<T> implements Comparable<Node<T>> {
	private List<Node<T>> neighbours;
	private Node<T> parent;
	public final T data;
	
	private int fCost;
	private int gCost;
	private int hCost;
	
	public Node(T data) {
		this.data = data;
		neighbours = new ArrayList<Node<T>>();
		gCost = -1;
	}
	
	public void addNeighbour(Node<T> n) {
		neighbours.add(n);
	}
	
	public List<Node<T>> getNeighbours() {
		return new ArrayList<Node<T>>(neighbours);
	}

	public int getfCost() {
		return fCost;
	}
	
	public void updateFCost() {
		fCost = gCost + hCost;
	}

	public int gethCost() {
		return hCost;
	}

	public void sethCost(int hCost) {
		this.hCost = hCost;
	}

	public int getgCost() {
		return gCost;
	}

	public void setgCost(int gCost) {
		this.gCost = gCost;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	@Override
	public int compareTo(Node<T> node) {
		int nodeFCost = node.getfCost();
		
		//Compare fCosts
		if(fCost < nodeFCost) {
			return -1;
		} else if(fCost > nodeFCost) {
			return 1;
		} else {
			//Compare hCosts if fCosts are equal
			int nodeHCost = node.gethCost();
			if(hCost < nodeHCost) {
				return -1;
			} else if(hCost > nodeHCost) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
}
