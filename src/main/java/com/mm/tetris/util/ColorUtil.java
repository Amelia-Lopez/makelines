package com.mm.tetris.util;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorUtil {
	
	private static Logger log = LoggerFactory.getLogger(ColorUtil.class);

	/**
	 * Get the Color instance for the color specified in the string
	 * @param colorName String
	 * @return Color
	 */
	public Color getColorForString(final String colorName) {
		Color color = null;
		
		try {
			color = (Color) Color.class.getField(colorName.toUpperCase()).get(null);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			log.error("Invalid configuration.", e);
			System.exit(1);
		}
		
		return color;
	}
}
