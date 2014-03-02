package com.mm.tetris.score.impl;

import java.util.ArrayList;
import java.util.List;

import com.mm.tetris.score.ScoreKeeper;
import com.mm.tetris.score.ScoreObserver;

public class ScoreKeeperImpl implements ScoreKeeper {
	
	List<ScoreObserver> observers = new ArrayList<>();

	@Override
	public void addObserver(ScoreObserver observer) {
		observers.add(observer);
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public void clearedRows(int numberOfRows) {
		// to do
	}

	@Override
	public void fastDrop(int dropHeight) {
		// to do
	}
	
	@Override
	public void reset() {
		// to do
	}
}
