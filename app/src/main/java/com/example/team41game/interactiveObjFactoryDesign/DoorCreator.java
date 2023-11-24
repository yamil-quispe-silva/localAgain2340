package com.example.team41game.interactiveObjFactoryDesign;


public class DoorCreator extends InteractiveObjCreator {
    public InteractiveObj createInteractiveObj(int x, int y, String type) {
        return new Door(x, y, type);
    }
}