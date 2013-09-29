package io.github.czxttkl.jsontree;
import java.util.ArrayList;

import com.google.gson.internal.LinkedTreeMap;


public class Root implements Node {
	
	Object[] decision_tree = new Object[3];
	
	public Root(int n) {
		decision_tree[0] = n;
	}
	
	public void setLeftNode(Object[] l) {
		decision_tree[1] = l;
	}
	
	public void setRightNode(Object[] r) {
		decision_tree[2] = r;
	}
	
	public void setN(int n) {
		decision_tree[0] = n;
	}
	
	public void setLeftNode(Leaf l) {
		decision_tree[1] = l;
	}
	
	public void setRightNode(Leaf r) {
		decision_tree[2] = r;
	}
	
	
	public Object getLeftNode() {
		return decision_tree[1];
	}
	
	public Object getRightNode() {
		return decision_tree[2];
	}
	
	public int getN() {
		if(decision_tree[0] instanceof Double)
			return ((Double)decision_tree[0]).intValue();
		else
			return (Integer)decision_tree[0];
	}
	

}
