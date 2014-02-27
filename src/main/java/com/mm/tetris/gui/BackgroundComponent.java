package com.mm.tetris.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * JComponent with a background and border color
 */
public abstract class BackgroundComponent extends JComponent {

	private static final long serialVersionUID = -4251493494340063365L;

	protected Color backgroundColor = Color.BLACK;
	protected Color borderColor = Color.WHITE;
	
	/**
	 * Draw the background and border
	 */
	@Override
	public final void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		// paint background
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
        
        // draw border
        g2.setColor(borderColor);
        g2.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        
        // hook for class extending this class
        paint(g2);
	}
	
	/**
	 * Require class extending this class to implement its own paint method
	 * @param g2 Graphics2D
	 */
	public abstract void paint(Graphics2D g2);
}
