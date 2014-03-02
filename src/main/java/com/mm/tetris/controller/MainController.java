package com.mm.tetris.controller;

import javax.inject.Inject;
import javax.inject.Named;

import com.mm.tetris.board.BlockBoard;
import com.mm.tetris.gui.Paintable;
import com.mm.tetris.gui.ScoreBoardView;
import com.mm.tetris.score.ScoreKeeper;

public class MainController implements Controller {
	
	@Inject
	private BlockBoard blockBoard;
	
	@Inject @Named("All")
	private Paintable entireWindow;
	
	@Inject @Named("Board")
	private Paintable boardPanel;
	
	@Inject
	private ScoreKeeper scoreKeeper;
	
	@Inject
	private ScoreBoardView scoreBoardView;
	
	@Inject
	private Ticker ticker;
	
	private boolean skipATick = false;
	
	
	/**
	 * Constructor
	 */
	public MainController() {
		// do nothing
	}

	@Override
	public void newGame() {
		// reset
		scoreKeeper.reset();
		scoreBoardView.setVisible(false);
		
		// start the game
		blockBoard.start();
		ticker.setTickListener(this).setInterval(300).start();
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
