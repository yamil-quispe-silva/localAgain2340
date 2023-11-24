package com.example.team41game;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        // a Position is equal to itself
        if (obj == this) {
            return true;
        }

        // a Position can only be equal to another Position
        if (!(obj instanceof Position)) {
            return false;
        }

        // make the compared to object a Position
        Position pos2 = (Position) obj;

        // two positions are equal if they have the same x and y values
        return (this.x == pos2.getX() && this.y == pos2.getY());
    }
}
