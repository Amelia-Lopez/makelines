package com.mm.tetris.score.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mm.tetris.score.ScoreKeeper;
import com.mm.tetris.score.ScoreObserver;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ScoreKeeperImpl implements ScoreKeeper {

    private static Logger log = LoggerFactory.getLogger(ScoreKeeperImpl.class);

    @Inject
    private Configuration config;

    /**
     * Scores for clearing particular rows at a time
     */
    private Map<Integer, Integer> clearRowScoring;

    /**
     * Scoring details for particular events
     */
    private Map<String, Integer> specialScoring;

    /**
     * List of observers that are interested in scoring changes
     */
	private List<ScoreObserver> observers = new ArrayList<>();

    private int level;
    private int rows;
    private int score;

    /**
     * Initialize the score keeper
     */
    @Override
    public void init() {
        String configPath = "scoring/";

        clearRowScoring = new HashMap<>();
        int clearRowScoringSize = config.getInt(configPath + "cleared_rows/@size");
        for (int numRow = 1; numRow <= clearRowScoringSize; numRow++) {
            String rowConfigPath = configPath + "cleared_rows/rows[" + numRow + "]/";
            clearRowScoring.put(
                    config.getInt(rowConfigPath + "@number"),
                    config.getInt(rowConfigPath + "@score"));
        }

        specialScoring = new HashMap<>();
        int specialScoringSize = config.getInt(configPath + "specials/@size");
        for (int numSpecial = 1; numSpecial <= specialScoringSize; numSpecial++) {
            String specialConfigPath = configPath + "specials/special[" + numSpecial + "]/";
            specialScoring.put(
                    config.getString(specialConfigPath + "@type"),
                    config.getInt(specialConfigPath + "@score"));
        }
    }

    /**
     * Add an observer for the score
     * @param observer ScoreObserver
     */
	@Override
	public void addObserver(ScoreObserver observer) {
		observers.add(observer);
	}

    /**
     * This method should be called whenever the score changes so the observers can be
     * informed
     */
    public void scoreChanged() {
        log.debug("Informing observers of score change");

        Map<String, Integer> scoreInfo = new HashMap<>();

        scoreInfo.put("Level", level);
        scoreInfo.put("Rows", rows);
        scoreInfo.put("Score", score);

        for (ScoreObserver observer : observers) {
            observer.scoreUpdate(scoreInfo);
        }
    }

    /**
     * Return the level the player is currently on
     * @return int
     */
	@Override
	public int getLevel() {
		return level;
	}

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getScore() {
        return score;
    }

    /**
     * Adds the specified number of rows cleared to the score information
     * @param numberOfRows int
     */
	@Override
	public void clearedRows(int numberOfRows) {
		rows += numberOfRows;
        level = (rows / 10) + 1;
        score += clearRowScoring.get(numberOfRows);

        scoreChanged();
    }

    /**
     * Adds points to the score for clearing the entire board
     */
    @Override
    public void clearedEntireBoard() {
        // assuming this gets run before the cleared rows does, so we don't have to inform observers
        score += specialScoring.get("entireBoard");
    }

    /**
     * Adds points to the score for dropping the tetromino down manually
     * @param dropHeight int number of rows dropped from
     */
    @Override
	public void fastDrop(int dropHeight) {
        score += specialScoring.get("dropRow") * dropHeight;

        scoreChanged();
    }

    /**
     * Reset the score for a new game
     */
    @Override
	public void reset() {
		level = 1;
        rows = 0;
        score = 0;

        scoreChanged();
	}
}
