package io.github.czxttkl.jsontree.exception;

public class DuplicateException extends Exception{

	public int dupIndex;
	
	public DuplicateException(int dupIndex) {
		this.dupIndex = dupIndex;
	}
}
