package com.mariolopezjr.tetris.score;

import java.util.Map;

public interface ScoreObserver {

	void scoreUpdate(Map<String, Integer> scoreInfo);
}
