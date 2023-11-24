package com.example.team41game;

public class MoveLeft implements MovePattern {
    public void move(Position position) {
        position.setX(position.getX() - 1);
    }
}
