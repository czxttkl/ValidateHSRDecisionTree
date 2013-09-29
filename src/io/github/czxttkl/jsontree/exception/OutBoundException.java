package io.github.czxttkl.jsontree.exception;

public class OutBoundException extends Exception{

public int outIndex;
	
	public OutBoundException(int outIndex) {
		this.outIndex = outIndex;
	}
}
