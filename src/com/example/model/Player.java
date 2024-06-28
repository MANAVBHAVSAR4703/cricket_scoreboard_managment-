package com.example.model;

public class Player {
    private String name;
    private int runs;
    private boolean isOut;

    public Player(String name) {
        this.name = name;
        this.runs = 0;
        this.isOut = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRuns() {
        return runs;
    }

    public void addRuns(int runs) {
        this.runs += runs;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    @Override
    public String toString() {
        return name + " - " + runs + (isOut ? " (out)" : " (not out)");
    }
}
