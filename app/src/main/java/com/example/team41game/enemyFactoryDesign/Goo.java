package com.example.team41game.enemyFactoryDesign;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.team41game.MoveDownRight;
import com.example.team41game.MovePattern;
import com.example.team41game.MoveUpLeft;
import com.example.team41game.Position;
import com.example.team41game.R;
import com.example.team41game.models.Room;
import com.example.team41game.viewModels.GameScreenViewModel;

public class Goo implements Enemy {
    private Position position;
    private int sprite;
    private Bitmap bitmap;
    private MovePattern movePattern;
    private int damage = 2;
    private boolean isAlive;

    public Goo(int x, int y) {
        this.position = new Position(x, y);
        this.movePattern = new MoveDownRight();
        this.sprite = R.drawable.monster_elemental_goo;
        this.isAlive = true;
    }

    public void setBitmap(Resources res) {
        this.bitmap = BitmapFactory.decodeResource(res, this.sprite);
    }

    public void move(Room room) {
        if (isWallCollision(room)) {
            // switch enemy movement direction if it collides with a wall
            if (movePattern instanceof MoveDownRight) {
                movePattern = new MoveUpLeft();
            } else {
                movePattern = new MoveDownRight();
            }
        }
        movePattern.move(this.position);
    }

    public boolean isWallCollision(Room room) {
        int nextX;
        int nextY;
        if (this.movePattern instanceof MoveDownRight) {
            nextX = this.position.getX() + 1;
            nextY = this.position.getY() + 1;
        } else {
            nextX = this.position.getX() - 1;
            nextY = this.position.getY() - 1;
        }
        int nextTile = room.getFloorLayout()[nextY][nextX];
        return (nextTile == 1) || (nextTile == 3) || (nextTile == 4) || (nextTile == 5);
    }

    public void render(Canvas canvas, int tileWidth, int tileHeight) {
        canvas.drawBitmap(bitmap, this.position.getX() * tileWidth,
                this.position.getY() * tileHeight, null);
    }

    public Position getPosition() {
        return this.position;
    }

    public void update(GameScreenViewModel subject) {
        if (subject.getPlayerX() == this.position.getX()
                && subject.getPlayerY() == this.position.getY()) {
            subject.reducePlayerHealth(damage);
        }
    }

    public void die() {
        this.sprite = R.drawable.skull;
        this.isAlive = false;
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}
