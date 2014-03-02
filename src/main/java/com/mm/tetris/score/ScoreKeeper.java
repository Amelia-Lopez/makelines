package com.mm.tetris.score;

import com.google.inject.ImplementedBy;
import com.mm.tetris.score.impl.ScoreKeeperImpl;

@ImplementedBy(ScoreKeeperImpl.class)
public interface ScoreKeeper {

	void addObserver(ScoreObserver observer);
	
	int getLevel();
	
	void clearedRows(int numberOfRows);
	
	void fastDrop(int dropHeight);
	
	void reset();
}
