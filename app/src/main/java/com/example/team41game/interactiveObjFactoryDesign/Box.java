package com.example.team41game.interactiveObjFactoryDesign;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.team41game.MovePattern;
import com.example.team41game.Position;
import com.example.team41game.R;

public class Box implements InteractiveObj {
    private Position position;
    private String type;
    private Resources res;
    private Boolean isOpen;
    private int sprite;
    private Bitmap bitmap;
    private int contents;
    private MovePattern movePattern;

    public Box(int x, int y, String type) {
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

    public int getContents() {
        return this.contents;
    }

    public void setContents(int contents) {
        this.contents = contents;
    }
    public void setMovePattern(MovePattern movePattern) {
        this.movePattern = movePattern;
    }

    public void setSpriteAndBitmap(Resources res) {
        this.res = res;
        if (!isOpen) {
            if (this.type.equals("stacked")) {
                this.sprite = R.drawable.boxes_stacked;
            } else {
                this.sprite = R.drawable.box;
            }
        } else {
            this.sprite = R.drawable.floor_light;
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

    public void doMove() {
        movePattern.move(this.position);
    }
}
