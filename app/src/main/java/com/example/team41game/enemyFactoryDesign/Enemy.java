package com.example.team41game.enemyFactoryDesign;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.team41game.Position;
import com.example.team41game.models.Room;
import com.example.team41game.Subscriber;
import com.example.team41game.viewModels.GameScreenViewModel;

public interface Enemy extends Subscriber {
    public void setBitmap(Resources res);
    void render(Canvas canvas, int tileWidth, int tileHeight);
    void move(Room room);
    void update(GameScreenViewModel subject);
    Position getPosition();
    void die();
    boolean isAlive();
}
