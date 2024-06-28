package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private String venue;
    private String date;
    private int numOvers;
    private Team team1;
    private Team team2;
    private final Innings innings1;
    private final Innings innings2;
    private final List<Ball> balls;

    public Match(String venue, String date, int numOvers, Team team1, Team team2) {
        this.venue = venue;
        this.date = date;
        this.numOvers = numOvers;
        this.team1 = team1;
        this.team2 = team2;
        this.innings1 = new Innings(team1);
        this.innings2 = new Innings(team2);
        this.balls = new ArrayList<>();
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumOvers() {
        return numOvers;
    }

    public void setNumOvers(int numOvers) {
        this.numOvers = numOvers;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Innings getInnings1() {
        return innings1;
    }

    public Innings getInnings2() {
        return innings2;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void addBall(Ball ball) {
        balls.add(ball);
    }

    public boolean isCompleted() {
        return innings1.isCompleted(numOvers) && innings2.isCompleted(numOvers);
    }

    @Override
    public String toString() {
        return "Venue: " + venue + ", Date: " + date + ", Overs: " + numOvers + ", Team1: " + team1.getName() + ", Team2: " + team2.getName();
    }
}
