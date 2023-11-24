package com.example.team41game.models;

public class Room {
    private int[][] floorLayout;
    //add door, chests, etc. later

    public Room(int[][] floorLayout) {
        this.floorLayout = floorLayout;
    }

    //use this to access floor plan in view model bounds checks
    public int[][] getFloorLayout() {
        return this.floorLayout;
    }
}
