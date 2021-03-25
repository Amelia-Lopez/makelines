package amylopez.makelines.gui;

import java.awt.Dimension;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JFrame;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MainWindow extends JFrame implements Paintable {

    private static final long serialVersionUID = 16467159141850531L;

    private static Logger log = LoggerFactory.getLogger(MainWindow.class);

    @Inject
    private Configuration config;

    @Inject
    private MenuBar menuBar;

    @Inject
    private MainPanel mainPanel;

    /**
     * Constructor
     */
    public MainWindow() {
        super("Make Lines");
    }

    /**
     * Set up the GUI
     */
    public void setupGUI() {
        // load config values
        String configSection = "gui/window/dimensions/";
        int width = config.getInt(configSection + "@width");
        int height = config.getInt(configSection + "@height");
        int widthPadding = config.getInt(configSection + "@width_padding");
        int heightPadding = config.getInt(configSection + "@height_padding");

        // set up the main window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(config.getBoolean("gui/window/@resizable"));
        setLayout(null);
        setSize(new Dimension(width + widthPadding, height + heightPadding));

        // set up menu bar
        menuBar.setupMenu();
        setJMenuBar(menuBar);

        // set up the main panel that contains all of the components
        mainPanel.setupPanel();
        add(mainPanel);

        setVisible(true);
    }
}
