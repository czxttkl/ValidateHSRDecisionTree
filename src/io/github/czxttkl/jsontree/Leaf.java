package io.github.czxttkl.jsontree;

public class Leaf implements Node{
	
	private int h;
	
	public Leaf() {
		
	}
	
	public Leaf(int h) {
		this.h = h;
	}
	
	public int getH() {
		return h;
	}

}
