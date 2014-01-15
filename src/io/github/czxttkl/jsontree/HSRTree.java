package io.github.czxttkl.jsontree;
import java.util.ArrayList;

import com.google.gson.internal.LinkedTreeMap;


public class HSRTree implements Node {
	
	/** The current rung where a jar is dropped*/
	private int rung;
	/** The subtree after the jar breaks at current rung*/
	private Node breakNode;
	/** The subtree after the jar doesn't break at current rung*/
	private Node surviveNode;
	
	public HSRTree() {
		
	}
	public HSRTree(int n) {
		rung = n;
	}
	
	public void setLeftNode(Node leftNode) {
		breakNode = leftNode;
	}
	
	public void setRightNode(Node rightNode) {
		surviveNode = rightNode;
	}
	
	public void setN(int n) {
		rung = n;
	}
	
	
	public Node getLeftNode() {
		return breakNode;
	}
	
	public Node getRightNode() {
		return surviveNode;
	}
	
	/** Return the rung the current node represents*/
	public int getRung() {
		return rung;
	}
	

}
