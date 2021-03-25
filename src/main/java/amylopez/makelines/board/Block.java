package amylopez.makelines.board;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Block {

    private BufferedImage texture;

    // by default all blocks are falling when they are created
    private boolean isFalling = true;

    public Block(BufferedImage texture) {
        this.texture = texture;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public Block stopFalling() {
        isFalling = false;
        return this;  // convenience
    }

    public Block clone() {
        return new Block(texture);
    }

    public void paint(Graphics2D g2) {
        g2.drawImage(texture, 0, 0, null);
    }
}
