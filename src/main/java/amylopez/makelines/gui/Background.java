package amylopez.makelines.gui;

import amylopez.makelines.score.ScoreKeeper;
import amylopez.makelines.score.ScoreObserver;
import amylopez.makelines.util.ImageUtil;
import org.apache.commons.configuration.Configuration;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

public class Background extends JComponent implements ScoreObserver {

    @Inject
    private Configuration config;

    @Inject
    private ImageUtil imageUtil;

    @Inject
    private ScoreKeeper scoreKeeper;

    /**
     * Map of background textures
     */
    private LinkedHashMap<Integer, BufferedImage> backgrounds;

    /**
     * Current background tile
     */
    private BufferedImage currentBackground;

    /**
     * Initialize the background class by loading configuration
     */
    public void init() {
        int width = config.getInt("gui/background/dimensions/@width");
        int height = config.getInt("gui/background/dimensions/@height");
        setSize(new Dimension(width, height));
        setOpaque(true);

        // load backgrounds
        backgrounds = new LinkedHashMap<>();
        String backgroundSection = config.getString("textures/backgrounds/@default");
        String backgroundConfigPath = "textures/backgrounds/" + backgroundSection + "/";
        String basePath = config.getString(backgroundConfigPath + "@path");
        int backgroundsSize = config.getInt(backgroundConfigPath + "@size");
        for (int backgroundNum = 1; backgroundNum <= backgroundsSize; backgroundNum++) {
            String backgroundImageConfigPath = backgroundConfigPath + "background[" + backgroundNum + "]/";
            String path = basePath + config.getString(backgroundImageConfigPath + "@file");
            backgrounds.put(
                    config.getInt(backgroundImageConfigPath + "@level"),
                    imageUtil.getImageFromFile(path, width, height));
        }

        currentBackground = backgrounds.get(1);
        scoreKeeper.addObserver(this);
    }

    /**
     * Paints the background texture based on the current level
     * @param g Graphics
     */
    public void paint(Graphics g) {
        g.drawImage(currentBackground, 0, 0, null);
    }

    @Override
    public void scoreUpdate(Map<String, Integer> scoreInfo) {
        int level = scoreInfo.get("Level");

        if (level > backgrounds.size())
            level = backgrounds.size();

        currentBackground = backgrounds.get(level);
    }
}
