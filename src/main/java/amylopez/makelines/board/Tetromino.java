package amylopez.makelines.board;

import java.util.LinkedList;

public class Tetromino {

    private String name;

    private LinkedList<Position> positions;

    private Block block;

    private int length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Position> getPositions() {
        return positions;
    }

    public void setPositions(LinkedList<Position> positions) {
        this.positions = positions;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
