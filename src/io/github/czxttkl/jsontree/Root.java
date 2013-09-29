package io.github.czxttkl.jsontree;
import java.util.ArrayList;

import com.google.gson.internal.LinkedTreeMap;


public class Root implements Node {
	
	Object[] decisiontree = new Object[3];
	
	public Root(int n) {
		decisiontree[0] = n;
	}
	
	public void setLeftNode(Object[] l) {
		decisiontree[1] = l;
	}
	
	public void setRightNode(Object[] r) {
		decisiontree[2] = r;
	}
	
	public void setN(int n) {
		decisiontree[0] = n;
	}
	
	public void setLeftNode(Leaf l) {
		decisiontree[1] = l;
	}
	
	public void setRightNode(Leaf r) {
		decisiontree[2] = r;
	}
	
	
	public Object getLeftNode() {
		return decisiontree[1];
	}
	
	public Object getRightNode() {
		return decisiontree[2];
	}
	
	public int getN() {
		if(decisiontree[0] instanceof Double)
			return ((Double)decisiontree[0]).intValue();
		else
			return (Integer)decisiontree[0];
	}
	

}
