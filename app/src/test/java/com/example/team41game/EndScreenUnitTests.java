package com.example.team41game;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.example.team41game.models.Leaderboard;
import com.example.team41game.models.Player;
import com.example.team41game.viewModels.EndScreenViewModel;
public class EndScreenUnitTests {
    private EndScreenViewModel endScreenViewModel;
    private Leaderboard leaderboard;
    private Player player;

    @Before
    public void setup() {
        player = Player.getPlayer();
        endScreenViewModel = new EndScreenViewModel();
        leaderboard = Leaderboard.getLeaderboard();
    }

    /**
     * Test that setTopFiveNames correctly sets the names in the leaderboard (Sprint 2)
     */
    @Test
    public void testSetTopFiveNames() {
        String[] expectedNames = {"Alice", "Bob", "Charlie", "Dave", "Eve"};
        leaderboard.setTopFiveNames(expectedNames);
        assertArrayEquals(expectedNames, leaderboard.getTopFiveNames());
    }


    /**
     * Test that setTopFiveScores correctly sets the scores in the leaderboard (Sprint 2)
     */
    @Test
    public void testSetTopFiveScores() {
        int[] expectedScores = {100, 90, 80, 70, 60};
        leaderboard.setTopFiveScores(expectedScores);
        assertArrayEquals(expectedScores, leaderboard.getTopFiveScores());
    }


    /**
     * Test that updateLeaderboard correctly places a new leaderboard entry (Sprint 2)
     */
    @Test
    public void testUpdateLeaderboard() {
        String[] topFiveNames = new String[]{"Alice", "Bob", "Charlie", "Dave", "Eve"};
        int[] topFiveScores = new int[]{95, 80, 70, 63, 42};
        String[] topFiveStartTimes = new String[]{"2023/10/10 07:00:00", "2023/10/10 06:00:00",
            "2023/10/10 08:00:00", "2023/10/10 09:00:00", "2023/10/10 09:30:00"};
        leaderboard.setTopFiveNames(topFiveNames);
        leaderboard.setTopFiveScores(topFiveScores);
        leaderboard.setTopFiveStartTimes(topFiveStartTimes);

        String[] updatedTopFiveNames = new String[]{"Alice", "Faith", "Bob", "Charlie", "Dave"};
        int[] updatedTopFiveScores = new int[]{95, 85, 80, 70, 63};
        String[] updatedTopFiveStartTimes = new String[]{"2023/10/10 07:00:00",
            "2023/10/10 11:25:00", "2023/10/10 06:00:00", "2023/10/10 08:00:00",
            "2023/10/10 09:00:00"};

        endScreenViewModel.updateLeaderboard("Faith", 85,
                "2023/10/10 11:25:00");
        assertArrayEquals(updatedTopFiveNames, leaderboard.getTopFiveNames());
        assertArrayEquals(updatedTopFiveScores, leaderboard.getTopFiveScores());
        assertArrayEquals(updatedTopFiveStartTimes, leaderboard.getTopFiveStartTimes());
    }

    /**
     * Test that shiftElementsRightByOne correctly shifts the elements of the passed in arrays
     * (Sprint 2)
     */
    @Test
    public void testShiftElementsRightByOne() {
        String[] leaderboardNames = new String[]{"Alice", "Bob", "Charlie", "Dave", "Eve"};
        int[] leaderboardScores = new int[]{95, 80, 70, 63, 42};
        String[] leaderboardStartTimes = new String[]{"2023/10/10 07:00:00", "2023/10/10 06:00:00",
            "2023/10/10 08:00:00", "2023/10/10 09:00:00", "2023/10/10 09:30:00"};

        String[] shiftedLeaderboardNames = new String[]{"Alice", "Alice", "Bob", "Charlie", "Dave"};
        int[] shiftedLeaderboardScores = new int[]{95, 95, 80, 70, 63};
        String[] shiftedLeaderboardStartTimes = new String[]{"2023/10/10 07:00:00",
            "2023/10/10 07:00:00", "2023/10/10 06:00:00", "2023/10/10 08:00:00",
            "2023/10/10 09:00:00"};

        endScreenViewModel.shiftElementsRightByOne(0, leaderboardNames, leaderboardScores,
                leaderboardStartTimes);

        assertArrayEquals(shiftedLeaderboardNames, leaderboardNames);
        assertArrayEquals(shiftedLeaderboardScores, leaderboardScores);
        assertArrayEquals(shiftedLeaderboardStartTimes, leaderboardStartTimes);
    }

    /**
     * Test for verifying the win status message in the EndScreenViewModel class. (Sprint 3)
     */
    @Test
    public void testEndScreenViewModelGetWinStatus() {
        // Test when the player wins
        player.setWinStatus(true);
        assertEquals("YOU WIN!!!", endScreenViewModel.getWinStatus());

        // Test when the player loses
        player.setWinStatus(false);
        assertEquals("YOU LOSE!!!", endScreenViewModel.getWinStatus());
    }

    /**
     * Test for verifying the win status in the Player class. (Sprint 3)
     */
    @Test
    public void testPlayerGetWinStatus() {
        // Test when the player wins
        player.setWinStatus(true);
        assertTrue(player.getWinStatus());

        // Test when the player loses
        player.setWinStatus(false);
        assertFalse(player.getWinStatus());
    }
}
