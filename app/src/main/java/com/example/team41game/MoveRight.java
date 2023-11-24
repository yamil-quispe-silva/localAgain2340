package com.example.team41game;

public class MoveRight implements MovePattern {
    public void move(Position position) {
        position.setX(position.getX() + 1);
    }
}
