package com.example.team41game.itemFactoryDesign;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.team41game.R;
import com.example.team41game.interactiveObjFactoryDesign.InteractiveObj;

public class Key implements Item {
    private Resources res;
    private InteractiveObj container;
    private Boolean justDiscovered;
    private int sprite;
    private Bitmap bitmap;

    public Key() {
        this.justDiscovered = false;
    }

    public void setContainer(InteractiveObj container) {
        this.container = container;
    }

    public InteractiveObj getContainer() {
        return container;
    }

    public void setSpriteAndBitmap(Resources res) {
        this.res = res;
        this.sprite = R.drawable.key;
        this.bitmap = BitmapFactory.decodeResource(this.res, this.sprite);
    }

    public void render(Canvas canvas, int tileWidth, int tileHeight) {
        canvas.drawBitmap(bitmap, getContainer().getPosition().getX() * tileWidth,
                getContainer().getPosition().getY() * tileHeight, null);
    }

    public void discover() {
        this.justDiscovered = true;
    }

    public boolean justDiscovered() {
        return this.justDiscovered;
    }

    public void acquire() {
        this.justDiscovered = false;
    }
}
