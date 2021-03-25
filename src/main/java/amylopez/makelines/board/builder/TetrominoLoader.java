package amylopez.makelines.board.builder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;

import amylopez.makelines.board.Position;
import amylopez.makelines.board.Block;
import amylopez.makelines.board.Tetromino;
import amylopez.makelines.util.ImageUtil;
import org.apache.commons.configuration.Configuration;

/**
 * Loads Tetromino and texture configuration
 */
public class TetrominoLoader {

    @Inject
    private Configuration config;

    @Inject
    private ImageUtil imageUtil;


    /**
     * Constructor
     */
    public TetrominoLoader() {
        // do nothing before field injection occurs
    }

    /**
     * Load the configured tetrominos and return them
     * @return List<Tetromino>
     */
    public ArrayList<Tetromino> loadTetrominos() {
        ArrayList<Tetromino> tetrominos = new ArrayList<>();
        Map<String, BufferedImage> textures = loadTextures();

        // load the tetrominos from config
        int numOfTetrominos = config.getInt("tetrominos/@size");
        for (int tetrominoNum = 1; tetrominoNum <= numOfTetrominos; tetrominoNum++) {
            String configPath = "tetrominos/tetromino[" + tetrominoNum + "]/";
            Tetromino tetromino = new Tetromino();

            // add name
            tetromino.setName(config.getString(configPath + "@name"));

            // add positions of blocks in the tetromino (which by definition have 4)
            LinkedList<Position> positions = new LinkedList<>();
            for (int posNum = 1; posNum <= 4; posNum++) {
                int x = config.getInt(configPath + "point[" + posNum + "]/@x");
                int y = config.getInt(configPath + "point[" + posNum + "]/@y");
                positions.add(new Position(x, y));
            }
            tetromino.setPositions(positions);

            // set length (used for rotating)
            tetromino.setLength(config.getInt(configPath + "@length"));

            // set the block texture
            String color = config.getString(configPath + "@color");
            tetromino.setBlock(new Block(textures.get(color)));

            tetrominos.add(tetromino);
        }

        return tetrominos;
    }

    /**
     * Load the textures
     * @return Map<String, BufferedImage> color -> image
     */
    private Map<String, BufferedImage> loadTextures() {
        Map<String, BufferedImage> textures = new HashMap<>();

        String defaultBlockTextureType = config.getString("textures/blocks/@default");
        String textureConfigPath = "textures/blocks/" + defaultBlockTextureType + "/";
        String baseImagePath = config.getString(textureConfigPath + "@path");
        int numOfColors = config.getInt(textureConfigPath + "@size");

        for (int colorNum = 1; colorNum <= numOfColors; colorNum++) {
            String blockConfigPath = textureConfigPath + "block[" + colorNum + "]/";
            String color = config.getString(blockConfigPath + "@color");
            String fileName = config.getString(blockConfigPath + "@file");

            BufferedImage texture = imageUtil.getImageFromFile(baseImagePath + fileName);
            textures.put(color, texture);
        }

        return textures;
    }
}
