package com.mm.tetris.score;

import com.google.inject.ImplementedBy;
import com.mm.tetris.score.impl.ScoreKeeperImpl;

@ImplementedBy(ScoreKeeperImpl.class)
public interface ScoreKeeper {

    void init();

	void addObserver(ScoreObserver observer);
	
	int getLevel();

    int getRows();

    int getScore();
	
	void clearedRows(int numberOfRows);

    void clearedEntireBoard();
	
	void fastDrop(int dropHeight);
	
	void reset();
}
