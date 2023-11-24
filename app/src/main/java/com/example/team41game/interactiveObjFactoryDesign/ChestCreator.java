package com.example.team41game.interactiveObjFactoryDesign;


public class ChestCreator extends InteractiveObjCreator {
    public InteractiveObj createInteractiveObj(int x, int y, String type) {
        return new Chest(x, y, type);
    }
}