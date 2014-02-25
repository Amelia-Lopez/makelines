package com.mm.tetris.action;

import java.awt.event.KeyEvent;

import javax.swing.Action;


public abstract class AbstractAction extends javax.swing.AbstractAction {

	private static final long serialVersionUID = 8606941361173724258L;

	public void setName(final String name) {
		putValue(Action.NAME, name);
	}
	
	public void setMnemonic(final KeyEvent mnemonic) {
		putValue(Action.MNEMONIC_KEY, mnemonic);
	}
}
