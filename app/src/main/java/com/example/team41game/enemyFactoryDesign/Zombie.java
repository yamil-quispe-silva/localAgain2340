package com.example.team41game.enemyFactoryDesign;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.team41game.MoveLeft;
import com.example.team41game.MovePattern;
import com.example.team41game.MoveRight;
import com.example.team41game.Position;
import com.example.team41game.R;
import com.example.team41game.models.Room;
import com.example.team41game.viewModels.GameScreenViewModel;

public class Zombie implements Enemy {
    private Position position;
    private int sprite;
    private Bitmap bitmap;
    private MovePattern movePattern;
    private int damage = 4;
    private boolean isAlive;

    public Zombie(int x, int y) {
        this.position = new Position(x, y);
        this.movePattern = new MoveRight();
        this.sprite = R.drawable.monster_zombie;
        this.isAlive = true;
    }

    public void setBitmap(Resources res) {
        this.bitmap = BitmapFactory.decodeResource(res, this.sprite);
    }

    public void move(Room room) {
        if (isWallCollision(room)) {
            // switch enemy movement direction if it collides with a wall
            if (movePattern instanceof MoveRight) {
                movePattern = new MoveLeft();
            } else {
                movePattern = new MoveRight();
            }
        }
        movePattern.move(this.position);
    }

    public boolean isWallCollision(Room room) {
        int nextX;
        if (this.movePattern instanceof MoveRight) {
            nextX = this.position.getX() + 1;
        } else {
            nextX = this.position.getX() - 1;
        }
        int nextTile = room.getFloorLayout()[this.position.getY()][nextX];
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
