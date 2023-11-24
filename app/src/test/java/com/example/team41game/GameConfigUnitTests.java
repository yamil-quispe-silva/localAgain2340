package com.example.team41game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.example.team41game.models.GameConfig;
import com.example.team41game.models.Player;
import com.example.team41game.viewModels.ConfigScreenViewModel;

import org.junit.Before;
import org.junit.Test;

public class GameConfigUnitTests {
    private ConfigScreenViewModel configScreenViewModel;
    private GameConfig gameConfig;
    private int easyButtonId;
    private int mediumButtonId;
    private Player player;

    @Before
    public void setup() {
        player = Player.getPlayer();
        gameConfig = GameConfig.getGameConfig();
        configScreenViewModel = new ConfigScreenViewModel();
        easyButtonId = 0;
        mediumButtonId = 1;
    }


    /**
     * Test for detection of white-space only, null, and empty names (Sprint 2)
     */
    @Test
    public void testValidateName() {
        boolean isValid;
        isValid = configScreenViewModel.validateName("");
        assertFalse(isValid);

        isValid = configScreenViewModel.validateName("   ");
        assertFalse(isValid);

        isValid = configScreenViewModel.validateName(null);
        assertFalse(isValid);

        isValid = configScreenViewModel.validateName("new");
        assertTrue(isValid);
    }

    /**
     * Test that validateName updates player's name only if passed a valid name (Sprint 2)
     */
    @Test
    public void testValidateNameUpdatesPlayerName() {
        player.setName("test");

        configScreenViewModel.validateName("   ");
        assertSame("test", player.getName());

        configScreenViewModel.validateName(null);
        assertSame("test", player.getName());

        configScreenViewModel.validateName("new");
        assertSame("new", player.getName());
    }

    /**
     * Test that player's initial health depends on difficulty level (Sprint 2)
     */
    @Test
    public void testSetModelAttributesHealth() {
        int easyHealth = 30;
        gameConfig.setDifficulty(0);
        configScreenViewModel.setModelAttributes(-1); // not testing avatar ID assignment
        assertEquals(easyHealth, player.getHealth());

        int mediumHealth = 20;
        gameConfig.setDifficulty(1);
        configScreenViewModel.setModelAttributes(-1); // not testing avatar ID assignment
        assertEquals(mediumHealth, player.getHealth());

        int hardHealth = 10;
        gameConfig.setDifficulty(2);
        configScreenViewModel.setModelAttributes(-1); // not testing avatar ID assignment
        assertEquals(hardHealth, player.getHealth());
    }


    /**
     * Test that avatarResourceId is set properly by setModelAttributes (Sprint 2)
     */
    @Test
    public void testSetModelAttributesAvatarId() {
        configScreenViewModel.setModelAttributes(0);
        assertEquals(0, player.getAvatarId());
    }


    /**
     * Test that setDifficulty correctly modifies the gameConfig's difficulty (Sprint 2)
     */
    @Test
    public void testSetDifficulty() {
        // Test for Easy Difficulty
        configScreenViewModel.setDifficulty(0, easyButtonId, mediumButtonId);
        assertEquals(0, gameConfig.getDifficulty());

        // Test for Medium Difficulty
        configScreenViewModel.setDifficulty(1, easyButtonId, mediumButtonId);
        assertEquals(1, gameConfig.getDifficulty());

        // Test for Hard Difficulty
        configScreenViewModel.setDifficulty(2, easyButtonId, mediumButtonId);
        assertEquals(2, gameConfig.getDifficulty());
    }
}
