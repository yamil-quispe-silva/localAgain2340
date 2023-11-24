package com.example.team41game.interactiveObjFactoryDesign;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.team41game.Position;
import com.example.team41game.R;
import com.example.team41game.itemFactoryDesign.Item;

public class Chest implements InteractiveObj {
    private Position position;
    private String type;
    private Resources res;
    private Boolean isOpen;
    private int sprite;
    private Bitmap bitmap;
    private Item contents;

    public Chest(int x, int y, String type) {
        this.position = new Position(x, y);
        this.type = type;
        this.isOpen = false;
    }

    public Position getPosition() {
        return this.position;
    }

    public String getType() {
        return this.type;
    }

    public Item getContents() {
        return this.contents;
    }

    public void setContents(Item item) {
        this.contents = item;
    }

    public void setSpriteAndBitmap(Resources res) {
        this.res = res;
        if (isOpen) {
            if (this.type.equals("silver")) {
                this.sprite = R.drawable.chest_open_empty;
            } else {
                this.sprite = R.drawable.chest_golden_open_empty;
            }
        } else {
            if (this.type.equals("silver")) {
                this.sprite = R.drawable.chest_closed;
            } else {
                this.sprite = R.drawable.chest_golden_closed;
            }
        }
        this.bitmap = BitmapFactory.decodeResource(this.res, this.sprite);
    }

    public void render(Canvas canvas, int tileWidth, int tileHeight) {
        canvas.drawBitmap(bitmap, this.position.getX() * tileWidth,
                this.position.getY() * tileHeight, null);
    }

    public void open() {
        this.isOpen = true;
    }

    public Boolean isOpen() {
        return this.isOpen;
    }
}
