package com.example.team41game.viewModels;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import androidx.lifecycle.ViewModel;

import com.example.team41game.R;
import com.example.team41game.enemyFactoryDesign.Enemy;
import com.example.team41game.interactiveObjFactoryDesign.InteractiveObj;
import com.example.team41game.itemFactoryDesign.Item;
import com.example.team41game.models.GameConfig;
import com.example.team41game.models.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.team41game.Subscriber;
import com.example.team41game.MovePattern;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.example.team41game.models.Room;
import com.example.team41game.Position;

public class GameScreenViewModel extends ViewModel {
    private GameConfig gameConfig;
    private Player player;
    private Room room;
    private List<Subscriber> subscribersActivities = new ArrayList<>();
    private List<Subscriber> subscribersEnemies = new ArrayList<>();

    public GameScreenViewModel() {
        gameConfig = GameConfig.getGameConfig();
        player = Player.getPlayer();
    }

    public void updateScore() {
        int newScore = player.getScore() - 1;

        // Ensure the player's score doesn't go below 0
        if (newScore < 0) {
            player.setScore(0);
        } else {
            player.setScore(newScore);
        }
    }

    public void initPlayerAttempt() {
        player.setScore(100);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String startTime = dateFormat.format(date);
        player.setStartTime(startTime);
    }

    public String getPlayerName() {
        return "Name: " + player.getName();
    }

    public String getHealth() {
        return "Health: " + player.getHealth();
    }

    public int getPlayerAvatar() {
        return player.getAvatarId();
    }

    public String getDifficulty() {
        switch (gameConfig.getDifficulty()) {
        case 0:
            return "Difficulty: Easy";
        case 1:
            return "Difficulty: Medium";
        case 2:
            return "Difficulty: Hard";
        default:
            return "Difficulty: ...";
        }
    }

    public String getScore() {
        return String.format("Score: %d", player.getScore());
    }
    public Position getPlayerPosition() {
        return player.getPosition();
    }

    public MovePattern getPlayerMovePattern() {
        return player.getMovePattern();
    }

    public int getPlayerX() {
        return player.getPosition().getX();
    }

    public int getPlayerY() {
        return player.getPosition().getY();
    }

    // set player's starting position in new screen
    public void initPlayerPosition(int x, int y) {
        player.setPosition(new Position(x, y));
    }

    // set floor layout for current room
    public void initRoom(int[][] layout) {
        room = new Room(layout);
    }

    public Room getRoom() {
        return room;
    }

    public void subscribeActivity(Subscriber subscriber) {
        subscribersActivities.add(subscriber);
    }

    public void unsubscribeActivity(Subscriber subscriber) {
        subscribersActivities.remove(subscriber);
    }

    public void subscribeEnemy(Subscriber subscriber) {
        subscribersEnemies.add(subscriber);
    }

    public void unsubscribeEnemy(Subscriber subscriber) {
        subscribersEnemies.remove(subscriber);
    }

    public void notifyActivitySubscribers() {
        for (Subscriber s: subscribersActivities) {
            s.update(this);
        }
    }

    public void notifyEnemySubscribers() {
        for (Subscriber s: subscribersEnemies) {
            s.update(this);
        }
    }

    public void setPlayerMovePattern(MovePattern movePattern) {
        player.setMovePattern(movePattern);
    }

    public void setPlayerWinStatus(boolean winStatus) {
        player.setWinStatus(winStatus);
    }

    public void movePlayer() {
        player.doMove();
        notifyEnemySubscribers();
        notifyActivitySubscribers();
    }

    public void drawEnemies(HashMap<String, List<Enemy>> enemiesMap, Canvas canvas,
                            int tileWidth, int tileHeight) {
        for (String enemyType: enemiesMap.keySet()) {
            for (Enemy enemy : enemiesMap.get(enemyType)) {
                enemy.render(canvas, tileWidth, tileHeight);
            }
        }
    }

    public void drawInteractiveObjs(HashMap<String, List<InteractiveObj>> interactiveObjsMap,
                                    Canvas canvas, int tileWidth, int tileHeight) {
        for (String interactiveObjType: interactiveObjsMap.keySet()) {
            for (InteractiveObj interactiveObj : interactiveObjsMap.get(interactiveObjType)) {
                interactiveObj.render(canvas, tileWidth, tileHeight);
            }
        }
    }

    public void drawItems(HashMap<String, List<Item>> itemsMap,
                          Canvas canvas, int tileWidth, int tileHeight) {
        for (String itemType: itemsMap.keySet()) {
            if (itemsMap.get(itemType) != null) {
                for (Item item : itemsMap.get(itemType)) {
                    if (item.justDiscovered()) {
                        item.render(canvas, tileWidth, tileHeight);
                    }
                }
            }
        }

    }

    public void moveEnemies(HashMap<String, List<Enemy>> enemiesMap) {
        for (String enemyType: enemiesMap.keySet()) {
            for (Enemy enemy : enemiesMap.get(enemyType)) {
                if (enemy.isAlive()) {
                    enemy.move(getRoom());
                }
            }
        }
        notifyEnemySubscribers();
    }

    public void initPlayerBitmap(Resources res) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, getPlayerAvatar());
        player.setBitmap(bitmap);
    }

    public Bitmap getPlayerBitmap() {
        return player.getBitmap();
    }

    public Bitmap[] initRoomTiles(Resources res) {
        Bitmap[] roomTiles = new Bitmap[6];
        roomTiles[0] = BitmapFactory.decodeResource(res, R.drawable.black);
        roomTiles[1] = BitmapFactory.decodeResource(res, R.drawable.wall_front);
        roomTiles[2] = BitmapFactory.decodeResource(res, R.drawable.floor_light);
        roomTiles[3] = BitmapFactory.decodeResource(res, R.drawable.wall_flag_blue);
        roomTiles[4] = BitmapFactory.decodeResource(res, R.drawable.wall_flag_green);
        roomTiles[5] = BitmapFactory.decodeResource(res, R.drawable.wall_flag_red);
        return roomTiles;
    }

    public void reducePlayerHealth(int damage) {
        switch (gameConfig.getDifficulty()) {
        case 0:
            player.setHealth(player.getHealth() - damage < 0 ? 0 : player.getHealth() - damage);
            break;
        case 1:
            int mDamage = damage + 1;
            player.setHealth(player.getHealth() - mDamage < 0 ? 0 : player.getHealth() - mDamage);
            break;
        case 2:
            int hDamage = damage + 2;
            player.setHealth(player.getHealth() - hDamage < 0 ? 0 : player.getHealth() - hDamage);
            break;
        default:
            player.setHealth(player.getHealth());
        }
    }

    public boolean isPlayerDead() {
        return player.getHealth() == 0;
    }

    public void clearPlayerInventory() {
        HashMap<String, Item> inventory = player.getInventory();
        if (inventory != null) {
            // sets the value of each item type to null in player's inventory
            inventory.replaceAll((t, v) -> null);
        }
    }

    public void clearPlayerScore() {
        player.setScore(0);
    }
}
