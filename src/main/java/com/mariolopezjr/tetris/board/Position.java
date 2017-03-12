package com.mariolopezjr.tetris.board;

public class Position {

	private int x;
	
	private int y;
	
	public Position() {
		// do nothing
	}
	
	public Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

    public String toString() {
        return "[x:" + x + ", y:" + y + "]";
    }
}
