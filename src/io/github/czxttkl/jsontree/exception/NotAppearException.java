package io.github.czxttkl.jsontree.exception;

public class NotAppearException extends Exception{
	
	public int noIndex;
	
	public NotAppearException(int noIndex) {
		this.noIndex = noIndex;
	}
}
