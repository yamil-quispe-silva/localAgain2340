package com.example.team41game.models;

import android.graphics.Bitmap;

import com.example.team41game.MovePattern;
import com.example.team41game.Position;
import com.example.team41game.itemFactoryDesign.Item;

import java.util.HashMap;

public class Player {
    private String name;
    private int health;
    private int avatarId;
    private Bitmap bitmap;
    private static volatile Player player;
    private int score;
    private String startTime;
    private Position position;
    private MovePattern movePattern;
    private boolean winStatus;
    private HashMap<String, Item> inventory = new HashMap<>();

    private Player() { }

    public static Player getPlayer() {
        if (player == null) {
            synchronized (Player.class) {
                if (player == null) {
                    player = new Player();
                }
            }
        }
        return player;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public int getAvatarId() {
        return this.avatarId;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public MovePattern getMovePattern() {
        return this.movePattern;
    }

    public void setMovePattern(MovePattern movePattern) {
        this.movePattern = movePattern;
    }

    public void doMove() {
        movePattern.move(this.position);
    }

    public boolean getWinStatus() {
        return winStatus;
    }

    public void setWinStatus(boolean winStatus) {
        this.winStatus = winStatus;
    }

    public HashMap<String, Item> getInventory() {
        return this.inventory;
    }

    public void addToInventory(String key, Item item) {
        this.inventory.put(key, item);
    }
}
