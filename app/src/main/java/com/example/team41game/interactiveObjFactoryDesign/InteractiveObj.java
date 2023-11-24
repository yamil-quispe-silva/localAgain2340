package com.example.team41game.interactiveObjFactoryDesign;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.team41game.Position;

public interface InteractiveObj {
    Position getPosition();
    String getType();
    void setSpriteAndBitmap(Resources res);
    void render(Canvas canvas, int tileWidth, int tileHeight);
    void open();
    Boolean isOpen();
}
