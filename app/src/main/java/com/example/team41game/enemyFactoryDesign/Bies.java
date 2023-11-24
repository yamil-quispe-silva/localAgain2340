package com.example.team41game.enemyFactoryDesign;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.team41game.MoveDown;
import com.example.team41game.MovePattern;
import com.example.team41game.MoveUp;
import com.example.team41game.Position;
import com.example.team41game.R;
import com.example.team41game.models.Room;
import com.example.team41game.viewModels.GameScreenViewModel;

public class Bies implements Enemy {
    private Position position;
    private int sprite;
    private Bitmap bitmap;
    private MovePattern movePattern;
    private int damage = 1;
    private boolean isAlive;

    public Bies(int x, int y) {
        this.position = new Position(x, y);
        this.movePattern = new MoveDown();
        this.sprite = R.drawable.monster_bies;
        this.isAlive = true;
    }

    public void setBitmap(Resources res) {
        this.bitmap = BitmapFactory.decodeResource(res, this.sprite);
    }

    public void move(Room room) {
        if (isWallCollision(room)) {
            // switch enemy movement direction if it collides with a wall
            if (movePattern instanceof MoveDown) {
                movePattern = new MoveUp();
            } else {
                movePattern = new MoveDown();
            }
        }
        movePattern.move(this.position);
    }

    public boolean isWallCollision(Room room) {
        int nextY;
        if (this.movePattern instanceof MoveDown) {
            nextY = this.position.getY() + 1;
        } else {
            nextY = this.position.getY() - 1;
        }
        int nextTile = room.getFloorLayout()[nextY][this.position.getX()];
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
