package com.example.team41game;

public class MoveUp implements MovePattern {
    public void move(Position position) {
        position.setY(position.getY() - 1);
    }
}
