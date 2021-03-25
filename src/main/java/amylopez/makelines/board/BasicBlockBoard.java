package amylopez.makelines.board;

/**
 * A basic block board usable by the primary block board where game play takes place and
 * the next piece block board
 */
public class BasicBlockBoard {

    /**
     * Number of blocks
     */
    protected int width;
    protected int height;
    protected int heightPadding;

    /**
     * The board
     */
    protected Block[][] board;

    public void init() {
        board = new Block[width][height + heightPadding];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Block getBlockAt(final int x, final int y) {
        return board[x][y + heightPadding];
    }

    protected void setBlockAt(Block block, int x, int y) {
        board[x][y + heightPadding] = block;
    }
}
