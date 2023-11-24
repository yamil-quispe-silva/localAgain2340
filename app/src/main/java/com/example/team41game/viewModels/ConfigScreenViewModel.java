package com.example.team41game.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.team41game.models.GameConfig;
import com.example.team41game.models.Player;

public class ConfigScreenViewModel extends ViewModel {
    private Player player;
    private GameConfig gameConfig;

    public ConfigScreenViewModel() {
        player = Player.getPlayer();
        gameConfig = GameConfig.getGameConfig();
    }

    public boolean validateName(String name) {
        if (name != null && !name.replace(" ", "").isEmpty()) {
            player.setName(name);
            return true;
        } else {
            return false;
        }
    }

    public void setDifficulty(int difficultyId, int easyButtonId, int mediumButtonId) {
        int difficulty = -1;
        if (difficultyId == easyButtonId) {
            difficulty = 0;
        } else if (difficultyId == mediumButtonId) {
            difficulty = 1;
        } else {
            difficulty = 2;
        }
        gameConfig.setDifficulty(difficulty);
    }

    public void setModelAttributes(int selectedAvatarResourceId) {
        player.setHealth(30 - (gameConfig.getDifficulty() * 10));
        player.setAvatarId(selectedAvatarResourceId);
        gameConfig.setPlayer(player);
    }
}
