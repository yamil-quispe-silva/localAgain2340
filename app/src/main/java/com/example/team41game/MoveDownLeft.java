package com.example.team41game;

public class MoveDownLeft implements MovePattern {
    public void move(Position position) {
        position.setX(position.getX() - 1);
        position.setY(position.getY() + 1);
    }
}
