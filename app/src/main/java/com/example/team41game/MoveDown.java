package com.example.team41game;

public class MoveDown implements MovePattern {
    public void move(Position position) {
        position.setY(position.getY() + 1);
    }
}
