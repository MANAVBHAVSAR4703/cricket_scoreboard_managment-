package com.example.model;

public class Ball {
    private int runs;
    private String playerName;
    private boolean isNoBall;
    private boolean isWide;
    private boolean isWicket;

    public Ball(int runs, String playerName) {
        this.runs = runs;
        this.playerName = playerName;
        this.isNoBall = false;
        this.isWide = false;
        this.isWicket = false;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isNoBall() {
        return isNoBall;
    }

    public void setNoBall(boolean noBall) {
        isNoBall = noBall;
    }

    public boolean isWide() {
        return isWide;
    }

    public void setWide(boolean wide) {
        isWide = wide;
    }

    public boolean isWicket() {
        return isWicket;
    }

    public void setWicket(boolean wicket) {
        isWicket = wicket;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Runs: ").append(runs).append(", Player: ").append(playerName);
        if (isNoBall) {
            sb.append(" (No Ball)");
        }
        if (isWide) {
            sb.append(" (Wide)");
        }
        if (isWicket) {
            sb.append(" (Wicket)");
        }
        return sb.toString();
    }
}
