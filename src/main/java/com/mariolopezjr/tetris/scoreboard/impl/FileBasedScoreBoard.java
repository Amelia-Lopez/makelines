package com.mariolopezjr.tetris.scoreboard.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.configuration.XMLConfiguration;

import com.mariolopezjr.tetris.scoreboard.Score;
import com.mariolopezjr.tetris.scoreboard.ScoreBoard;

public class FileBasedScoreBoard implements ScoreBoard {

	
	@Inject
	private XMLConfiguration config;
	
	private ScoreBoardFile scoreBoardFile;
	
	private String fileName;

	public boolean addScore(String name, int score, int rows) {
		loadCurrentConfig();
		loadScoreBoardFile();
		
		List<Score> scores = scoreBoardFile.getScores();
		
		// determine if this is a new high score
		int rank = -1;
		for (int position = 0; position < scores.size(); position++) {
			
		}
		
		
		// TODO Auto-generated method stub
		return false;
	}

	public List<Score> getScores() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void reset() {
		
	}

	private void loadCurrentConfig() {
		this.fileName = null;
	}
	
	private void loadScoreBoardFile() {
		
	}
	
	private static class ScoreBoardFile implements Serializable {
		private List<Score> scores;
		
		public ScoreBoardFile() {
			scores = new ArrayList<Score>();
		}
		
		public List<Score> getScores() {
			return scores;
		}
	}
}
