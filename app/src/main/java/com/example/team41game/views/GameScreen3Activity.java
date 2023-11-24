package com.example.team41game.views;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.team41game.R;
import com.example.team41game.enemyFactoryDesign.BiesCreator;
import com.example.team41game.enemyFactoryDesign.Enemy;
import com.example.team41game.enemyFactoryDesign.EnemyCreator;
import com.example.team41game.enemyFactoryDesign.GooCreator;
import com.example.team41game.enemyFactoryDesign.OrcCreator;
import com.example.team41game.enemyFactoryDesign.ZombieCreator;
import com.example.team41game.interactiveObjFactoryDesign.BoxCreator;
import com.example.team41game.interactiveObjFactoryDesign.Chest;
import com.example.team41game.interactiveObjFactoryDesign.ChestCreator;
import com.example.team41game.interactiveObjFactoryDesign.DoorCreator;
import com.example.team41game.interactiveObjFactoryDesign.InteractiveObj;
import com.example.team41game.interactiveObjFactoryDesign.InteractiveObjCreator;
import com.example.team41game.itemFactoryDesign.Item;
import com.example.team41game.itemFactoryDesign.ItemCreator;
import com.example.team41game.itemFactoryDesign.KeyCreator;
import com.example.team41game.viewModels.CollisionViewModel;
import com.example.team41game.viewModels.GameScreenViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.view.KeyEvent;
import com.example.team41game.Subscriber;
import com.example.team41game.MoveLeft;
import com.example.team41game.MoveRight;
import com.example.team41game.MoveUp;
import com.example.team41game.MoveDown;

public class GameScreen3Activity extends AppCompatActivity implements Subscriber {
    private Timer scoreTimer;
    private Timer gameLoopTimer;
    private Timer enemyMoveTimer;
    private Handler handler;
    private ImageView gameImage;
    private TextView nameField;
    private TextView healthField;
    private TextView difficultyField;
    private TextView scoreDisplay;
    private GameScreenViewModel gameScreenViewModel;
    private CollisionViewModel collisionViewModel;
    private int[][] gameWorld = {
            {1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 5, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 1, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 2, 1, 2, 1, 1, 2, 2, 2, 1},
            {1, 2, 2, 1, 1, 2, 1, 2, 2, 1, 2, 1, 1, 1},
            {1, 2, 2, 1, 2, 2, 1, 1, 2, 1, 2, 2, 2, 1},
            {1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 2, 2, 2, 1},
            {1, 2, 2, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2},
            {1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1}
    };
    private Bitmap gameBitmap;
    private Canvas canvas;
    private final int tileWidth = 42;
    private final int tileHeight = 42;
    private Resources res;
    private HashMap<String, List<Enemy>> enemiesMap = new HashMap<>();
    private HashMap<String, List<InteractiveObj>> interactiveObjsMap = new HashMap<>();
    private HashMap<String, List<Item>> itemsMap = new HashMap<>();
    private Bitmap[] roomTiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        gameScreenViewModel = new ViewModelProvider(this).get(GameScreenViewModel.class);
        gameScreenViewModel.subscribeActivity(this);
        collisionViewModel = new ViewModelProvider(this).get(CollisionViewModel.class);
        scoreTimer = new Timer();
        gameLoopTimer = new Timer();
        enemyMoveTimer = new Timer();
        handler = new Handler();

        gameImage = findViewById(R.id.imageView);
        nameField = findViewById(R.id.nameField);
        healthField = findViewById(R.id.healthField);
        difficultyField = findViewById(R.id.difficultyField);
        scoreDisplay = findViewById(R.id.scoreDisplay);

        gameBitmap = Bitmap.createBitmap(
                gameWorld[0].length * tileWidth,
                gameWorld.length * tileHeight,
                Bitmap.Config.ARGB_8888
        );
        canvas = new Canvas(gameBitmap);

        res = getResources();
        gameScreenViewModel.initPlayerPosition(7, 12);
        gameScreenViewModel.initRoom(gameWorld);
        roomTiles = gameScreenViewModel.initRoomTiles(res);
        initEnemies();
        initInteractiveObjs();
        initItems(); // initItems must be ran after initInteractiveObjs
        displayGameSettings();
        drawGame();

        collisionViewModel.setInteractiveObjsMap(interactiveObjsMap);
        collisionViewModel.setRoom(gameScreenViewModel.getRoom());

        // Timer to call displayNewScore every second
        scoreTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> displayNewScore());
            }
        }, 10, 1000);

        // Timer to call drawGame every 50ms
        gameLoopTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> drawGame());
            }
        }, 10, 50);

        // Timer to call moveEnemies every second
        enemyMoveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    gameScreenViewModel.moveEnemies(enemiesMap);
                    displayGameSettings();
                    checkForLoss();
                });
            }
        }, 10, 1000);
    }

    private void drawGame() {
        drawGameWorld();
        gameScreenViewModel.drawEnemies(enemiesMap, canvas, tileWidth, tileHeight);
        gameScreenViewModel.drawInteractiveObjs(interactiveObjsMap, canvas, tileWidth, tileHeight);
        gameScreenViewModel.drawItems(itemsMap, canvas, tileWidth, tileHeight);
        drawPlayer();
    }

    private void initEnemies() {
        List<Enemy> zombies = new ArrayList<>();
        EnemyCreator zombieCreator = new ZombieCreator();
        zombies.add(zombieCreator.createEnemy(12, 10));
        enemiesMap.put("zombies", zombies);

        List<Enemy> bies = new ArrayList<>();
        EnemyCreator biesCreator = new BiesCreator();
        bies.add(biesCreator.createEnemy(3, 10));
        enemiesMap.put("bies", bies);

        List<Enemy> orcs = new ArrayList<>();
        EnemyCreator orcCreator = new OrcCreator();
        orcs.add(orcCreator.createEnemy(12, 3));
        enemiesMap.put("orcs", orcs);

        List<Enemy> goos = new ArrayList<>();
        EnemyCreator gooCreator = new GooCreator();
        goos.add(gooCreator.createEnemy(2, 5));
        enemiesMap.put("goos", goos);

        for (String enemyType: enemiesMap.keySet()) {
            for (Enemy enemy : enemiesMap.get(enemyType)) {
                enemy.setBitmap(res);
                gameScreenViewModel.subscribeEnemy(enemy);
            }
        }
    }

    private void initInteractiveObjs() {
        List<InteractiveObj> chests = new ArrayList<>();
        InteractiveObjCreator chestCreator = new ChestCreator();
        chests.add(chestCreator.createInteractiveObj(10, 7, "gold"));
        chests.add(chestCreator.createInteractiveObj(4, 7, "gold"));
        chests.add(chestCreator.createInteractiveObj(1, 1, "silver"));
        interactiveObjsMap.put("chests", chests);

        List<InteractiveObj> boxes = new ArrayList<>();
        InteractiveObjCreator boxCreator = new BoxCreator();
        boxes.add(boxCreator.createInteractiveObj(4, 3, "unstacked"));
        interactiveObjsMap.put("boxes", boxes);

        List<InteractiveObj> doors = new ArrayList<>();
        InteractiveObjCreator doorCreator = new DoorCreator();
        doors.add(doorCreator.createInteractiveObj(12, 2, "notExit"));
        doors.add(doorCreator.createInteractiveObj(13, 11, "exit"));
        interactiveObjsMap.put("doors", doors);

        for (String interactiveObjType: interactiveObjsMap.keySet()) {
            for (InteractiveObj interactiveObj : interactiveObjsMap.get(interactiveObjType)) {
                interactiveObj.setSpriteAndBitmap(res);
            }
        }
    }

    private void initItems() {
        List<Item> keys = new ArrayList<>();
        ItemCreator keyCreator = new KeyCreator();

        for (InteractiveObj interactiveObj : interactiveObjsMap.get("chests")) {
            Chest chest = (Chest) interactiveObj;
            if (chest.getType().equals("gold")) {
                Item key = keyCreator.createItem();
                key.setSpriteAndBitmap(res);
                chest.setContents(key);
                key.setContainer(chest);
                keys.add(key);
            }
        }
        itemsMap.put("keys", keys);
    }

    private void drawPlayer() {
        canvas.drawBitmap(gameScreenViewModel.getPlayerBitmap(),
                gameScreenViewModel.getPlayerX() * tileWidth,
                gameScreenViewModel.getPlayerY() * tileHeight,
                null);
    }

    private void drawGameWorld() {
        for (int i = 0; i < gameWorld.length; i++) {
            for (int j = 0; j < gameWorld[0].length; j++) {
                int tileID = gameWorld[i][j];
                Bitmap tileBitmap = roomTiles[tileID];
                canvas.drawBitmap(tileBitmap, j * tileWidth, i * tileHeight, null);
            }
        }
        gameImage.setImageBitmap(gameBitmap);
    }

    public void displayGameSettings() {
        nameField.setText(gameScreenViewModel.getPlayerName());
        healthField.setText(gameScreenViewModel.getHealth());
        difficultyField.setText(gameScreenViewModel.getDifficulty());
    }

    private void displayNewScore() {
        gameScreenViewModel.updateScore();
        scoreDisplay.setText(gameScreenViewModel.getScore());
    }

    public void update(GameScreenViewModel subject) {
        drawPlayer();
    }

    //if player reaches door on this screen, set player's win status to true (win)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_A:
            gameScreenViewModel.setPlayerMovePattern(new MoveLeft());
            collisionViewModel.handleBoxCollision();
            if (collisionViewModel.isValidMove()) {
                gameScreenViewModel.movePlayer();
                displayGameSettings();
                checkForLoss();
                checkForExitCollision();
            }
            return true;
        case KeyEvent.KEYCODE_D:
            gameScreenViewModel.setPlayerMovePattern(new MoveRight());
            collisionViewModel.handleBoxCollision();
            if (collisionViewModel.isValidMove()) {
                gameScreenViewModel.movePlayer();
                displayGameSettings();
                checkForLoss();
                checkForExitCollision();
            }
            return true;
        case KeyEvent.KEYCODE_W:
            gameScreenViewModel.setPlayerMovePattern(new MoveUp());
            collisionViewModel.handleBoxCollision();
            if (collisionViewModel.isValidMove()) {
                gameScreenViewModel.movePlayer();
                displayGameSettings();
                checkForLoss();
                checkForExitCollision();
            }
            return true;
        case KeyEvent.KEYCODE_S:
            gameScreenViewModel.setPlayerMovePattern(new MoveDown());
            collisionViewModel.handleBoxCollision();
            if (collisionViewModel.isValidMove()) {
                gameScreenViewModel.movePlayer();
                displayGameSettings();
                checkForLoss();
                checkForExitCollision();
            }
            return true;
        case KeyEvent.KEYCODE_E:
            InteractiveObj chest = collisionViewModel.handleChestInteraction();
            if (chest != null) {
                chest.setSpriteAndBitmap(res);
            }
            InteractiveObj door = collisionViewModel.handleDoorInteraction();
            if (door != null) {
                door.setSpriteAndBitmap(res);
            }
            return true;
        case KeyEvent.KEYCODE_SPACE:
            collisionViewModel.handleItemAcquisition();
            Enemy attackedEnemy = collisionViewModel.handlePlayerAttack(enemiesMap);
            if (attackedEnemy != null) {
                // we update the enemy's sprite now that it's been attacked
                attackedEnemy.setBitmap(res);
                // we must unsubscribe the enemy so that it can't collide with the player
                gameScreenViewModel.unsubscribeEnemy(attackedEnemy);
            }
            return true;
        default:
            return super.onKeyDown(keyCode, event);
        }
    }

    public void cancelTimers() {
        scoreTimer.cancel();
        gameLoopTimer.cancel();
        enemyMoveTimer.cancel();
    }

    public void checkForExitCollision() {
        if (collisionViewModel.isExitCollision()) {
            gameScreenViewModel.setPlayerWinStatus(true);
            cancelTimers();
            Intent endGame = new Intent(GameScreen3Activity.this, EndScreenActivity.class);
            startActivity(endGame);
        }
    }

    public void checkForLoss() {
        if (gameScreenViewModel.isPlayerDead()) {
            gameScreenViewModel.clearPlayerInventory();
            gameScreenViewModel.clearPlayerScore();
            gameScreenViewModel.setPlayerWinStatus(false);
            cancelTimers();
            Intent endGame = new Intent(GameScreen3Activity.this, EndScreenActivity.class);
            startActivity(endGame);
        }
    }
}


