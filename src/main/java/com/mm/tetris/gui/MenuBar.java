package com.mm.tetris.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.inject.Inject;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mm.tetris.action.AbstractAction;
import com.mm.tetris.util.ReflectionUtil;
import com.mm.tetris.util.ConfigUtil;

public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = -7002677070179778997L;
	
	private static Logger log = LoggerFactory.getLogger(MenuBar.class);

	@Inject
	private Configuration config;
	
	@Inject
	private ReflectionUtil reflectionUtil;
	
	@Inject
	private ConfigUtil configUtil;
	
	/**
	 * Constructor
	 */
	public MenuBar() {
		// do nothing
	}
	
	/**
	 * Creates the menu bar
	 */
	public void setupMenu() {
		// load config values
		String configSection = "gui/menubar/";
		String colorName = config.getString(configSection + "@color");
		int width = config.getInt(configSection + "dimensions/@width");
		int height = config.getInt(configSection + "dimensions/@height");
		
		setOpaque(true);
        setBackground(reflectionUtil.getColor(colorName));
        setPreferredSize(new Dimension(width, height));
		
        populateMenuFromConfig();
	}
	
	/**
	 * Adds configured menus and items
	 */
	private void populateMenuFromConfig() {
		log.debug("Entering method populateMenuFromConfig()");
		int numberOfMenus = configUtil.getNumberOfElements("menus");
		for (int menuPosition = 1; menuPosition <= numberOfMenus; menuPosition++) {
			// load config for menu
			String menuConfigPath = "menus/menu[" + menuPosition + "]";
			String name = config.getString(menuConfigPath + "/@name");
			String mnemonic = config.getString(menuConfigPath + "/@mnemonic");
			log.debug("Adding menu: " + name);
			
			JMenu menu = new JMenu(name);
			menu.setMnemonic(reflectionUtil.getKeyEvent(mnemonic));
			
			int numberOfItems = configUtil.getNumberOfElements(menuConfigPath);
			for (int itemPosition = 1; itemPosition <= numberOfItems; itemPosition++) {
				// load config for menu item
				String itemConfigPath = menuConfigPath + "/item[" + itemPosition + "]";
				String itemName = config.getString(itemConfigPath + "/@name");
				String itemMnemonic = config.getString(itemConfigPath + "/@mnemonic");
				String itemType = config.getString(itemConfigPath + "/@type");
				String itemAction = config.getString(itemConfigPath + "/@action");
				log.debug("Adding menu item: " + itemName);
				
				AbstractAction action = reflectionUtil.<AbstractAction>newInstance(itemAction);
				int itemKeyEvent = reflectionUtil.getKeyEvent(itemMnemonic);
				action.setName(itemName);
				action.setMnemonic(itemKeyEvent);
				
				switch (itemType) {
					case "JMenuItem":
						JMenuItem menuItem = new JMenuItem(action);
						menuItem.setAccelerator(
								KeyStroke.getKeyStroke(
										itemKeyEvent,
										Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
						menu.add(menuItem);
						break;
						
					case "JCheckBoxMenuItem":
						JCheckBoxMenuItem menuCheckBox = new JCheckBoxMenuItem(action);
						menuCheckBox.setAccelerator(
								KeyStroke.getKeyStroke(
										itemKeyEvent,
										Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
						menu.add(menuCheckBox);
						break;
						
					default:
						log.warn("Invalid configuration. Type not supported: " + itemType);
				}
			}
			
			add(menu);
		}
	}
}
