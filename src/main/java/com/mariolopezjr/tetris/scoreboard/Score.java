package com.mariolopezjr.tetris.scoreboard;

public class Score {

    private String name;
    private int score;
    private int rows;

    public Score() {
        this.name = "---";
        this.score = 0;
        this.rows = 0;
    }

    public Score(final String name, final int score, final int rows) {
        this.name = name;
        this.score = score;
        this.rows = rows;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
}
