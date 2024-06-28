package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Innings {
    private final Team battingTeam;
    private final List<Ball> balls;
    private int totalRuns;
    private int totalWickets;
    private Player striker;
    private Player nonStriker;
    private Player currentBowler;
    private Player previousBowler;
    private int currentOver;
    private int currentBall;

    public Innings(Team battingTeam) {
        this.battingTeam = battingTeam;
        this.balls = new ArrayList<>();
        this.totalRuns = 0;
        this.totalWickets = 0;
        this.striker = null;
        this.nonStriker = null;
        this.currentBowler = null;
        this.previousBowler = null;
        this.currentOver = 0;
        this.currentBall = 0;
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void addTotalRuns(int runs) {
        this.totalRuns += runs;
    }

    public int getTotalWickets() {
        return totalWickets;
    }

    public void addWicket() {
        this.totalWickets++;
    }

    public Player getStriker() {
        return striker;
    }

    public void setStriker(Player striker) {
        this.striker = striker;
    }

    public Player getNonStriker() {
        return nonStriker;
    }

    public void setNonStriker(Player nonStriker) {
        this.nonStriker = nonStriker;
    }

    public Player getCurrentBowler() {
        return currentBowler;
    }

    public void setCurrentBowler(Player currentBowler) {
        this.previousBowler = this.currentBowler;
        this.currentBowler = currentBowler;
    }

    public Player getPreviousBowler() {
        return previousBowler;
    }

    public int getCurrentOver() {
        return currentOver;
    }

    public int getCurrentBall() {
        return currentBall;
    }

    public void addBall(Ball ball) {
        balls.add(ball);
        if (!ball.isNoBall() && !ball.isWide()) {
            currentBall++;
            if (currentBall >= 6) {
                currentOver++;
                currentBall = 0;
            }
        }
    }

    public boolean isCompleted(int numOvers) {
        return currentOver >= numOvers || battingTeamIsAllOut();
    }

    private boolean battingTeamIsAllOut() {
        int notOutPlayers = 0;
        for (Player player : battingTeam.getPlayers()) {
            if (!player.isOut()) {
                notOutPlayers++;
            }
        }
        // Check if at least two players are not out
        return notOutPlayers < 2;
    }

    @Override
    public String toString() {
        return "Innings{" +
                "battingTeam=" + battingTeam.getName() +
                ", totalRuns=" + totalRuns +
                ", totalWickets=" + totalWickets +
                ", currentOver=" + currentOver +
                ", currentBall=" + currentBall +
                '}';
    }
}
