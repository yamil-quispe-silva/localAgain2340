package com.example.team41game.interactiveObjFactoryDesign;


public class BoxCreator extends InteractiveObjCreator {
    public InteractiveObj createInteractiveObj(int x, int y, String type) {
        return new Box(x, y, type);
    }
}