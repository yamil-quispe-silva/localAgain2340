package com.example.team41game.itemFactoryDesign;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.team41game.interactiveObjFactoryDesign.InteractiveObj;

public interface Item {
    void render(Canvas canvas, int tileWidth, int tileHeight);
    void setContainer(InteractiveObj container);
    void setSpriteAndBitmap(Resources res);
    void discover();
    boolean justDiscovered();
    void acquire();
}
