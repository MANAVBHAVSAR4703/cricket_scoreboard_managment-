package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Over {
    private final List<Ball> balls;

    public Over() {
        this.balls = new ArrayList<>();
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void addBall(Ball ball) {
        balls.add(ball);
    }

    @Override
    public String toString() {
        return "Over{" +
                "balls=" + balls +
                '}';
    }
}
