package com.example.team41game.models;

public class GameConfig {
    private Player player;
    private int difficulty;
    private static volatile GameConfig gameConfig;
    private GameConfig() { }

    public static GameConfig getGameConfig() {
        if (gameConfig == null) {
            synchronized (GameConfig.class) {
                if (gameConfig == null) {
                    gameConfig = new GameConfig();
                }
            }
        }
        return gameConfig;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return this.difficulty;
    }
}
