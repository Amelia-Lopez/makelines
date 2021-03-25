package amylopez.makelines.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.inject.Inject;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import amylopez.makelines.controller.input.action.ImageMessager;
import amylopez.makelines.util.ImageUtil;
import amylopez.makelines.util.ReflectionUtil;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amylopez.makelines.controller.input.action.AbstractAction;

public class MenuBar extends JMenuBar {

    private static final long serialVersionUID = -7002677070179778997L;

    private static Logger log = LoggerFactory.getLogger(MenuBar.class);

    @Inject
    private Configuration config;

    @Inject
    private ReflectionUtil reflectionUtil;

    @Inject
    private ImageUtil imageUtil;

    private Color backgroundColor;

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
        setBackground(backgroundColor = reflectionUtil.getColor(colorName));
        setPreferredSize(new Dimension(width, height));

        populateMenuFromConfig();
    }

    /**
     * Adds configured menus and items
     */
    private void populateMenuFromConfig() {
        log.trace("Entering method populateMenuFromConfig()");
        int numberOfMenus = config.getInt("menus/@size");
        for (int menuPosition = 1; menuPosition <= numberOfMenus; menuPosition++) {
            // load config for menu
            String menuConfigPath = "menus/menu[" + menuPosition + "]/";
            String name = config.getString(menuConfigPath + "@name");
            String mnemonic = config.getString(menuConfigPath + "@mnemonic");
            log.debug("Adding menu: " + name);

            JMenu menu = new JMenu(name);
            menu.setMnemonic(reflectionUtil.getKeyEvent(mnemonic));
            menu.setOpaque(true);
            menu.setBackground(backgroundColor);

            int numberOfItems = config.getInt(menuConfigPath + "@size");
            for (int itemPosition = 1; itemPosition <= numberOfItems; itemPosition++) {
                // load config for menu item
                String itemConfigPath = menuConfigPath + "item[" + itemPosition + "]/";
                String itemName = config.getString(itemConfigPath + "@name");
                String itemMnemonic = config.getString(itemConfigPath + "@mnemonic");
                String itemType = config.getString(itemConfigPath + "@type");
                String itemAction = config.getString(itemConfigPath + "@action");
                log.debug("Adding menu item: " + itemName);

                AbstractAction action = reflectionUtil.<AbstractAction>newInstance(itemAction);
                int itemKeyEvent = reflectionUtil.getKeyEvent(itemMnemonic);
                action.setName(itemName);
                action.setMnemonic(itemKeyEvent);
                action.init();

                switch (itemType) {
                    case "JMenuItem":
                        JMenuItem menuItem = new JMenuItem(action);
                        menuItem.setOpaque(true);
                        menuItem.setAccelerator(
                                KeyStroke.getKeyStroke(
                                        itemKeyEvent,
                                        Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
                        menu.add(menuItem);
                        break;

                    case "JCheckBoxMenuItem":
                        JCheckBoxMenuItem menuCheckBox = new JCheckBoxMenuItem(action);
                        menuCheckBox.setOpaque(true);
                        menuCheckBox.setAccelerator(
                                KeyStroke.getKeyStroke(
                                        itemKeyEvent,
                                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
                        menu.add(menuCheckBox);
                        break;

                    default:
                        log.warn("Invalid configuration. Type not supported: " + itemType);
                }

                // set message if one is configured
                String messageType = config.getString(itemConfigPath + "message/@type");
                if (messageType != null && !messageType.isEmpty()) {
                    switch (messageType) {
                        // todo: add support for a string messager
                        /*case "string":
                            stringMessager.setMessageString(
                                    config.getString(itemConfigPath + "message/string/@value"));
                            break;*/
                        case "image":
                            ImageMessager imageMessager = (ImageMessager) action;
                            imageMessager.setMessageImage(
                                    imageUtil.getImageFromFile(
                                            config.getString(itemConfigPath + "message/image/@file")));
                            break;
                    }
                }
            }

            add(menu);
        }
    }
}
