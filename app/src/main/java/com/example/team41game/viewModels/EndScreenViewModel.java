package com.example.team41game.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.team41game.models.Leaderboard;
import com.example.team41game.models.Player;

public class EndScreenViewModel extends ViewModel {
    private Leaderboard leaderboard;
    private Player player;

    public EndScreenViewModel() {
        leaderboard = Leaderboard.getLeaderboard();
        player = Player.getPlayer();
    }

    public int getScore() {
        return player.getScore();
    }

    public String getName() {
        return player.getName();
    }

    public String getStartTime() {
        return player.getStartTime();
    }

    public String[] getTopFiveNames() {
        return leaderboard.getTopFiveNames();
    }

    public int[] getTopFiveScores() {
        return leaderboard.getTopFiveScores();
    }

    public String[] getTopFiveStartTimes() {
        return leaderboard.getTopFiveStartTimes();
    }

    public void updateLeaderboard(String newestName, int newestScore, String newestStartTime) {
        String[] leaderboardNames = leaderboard.getTopFiveNames();
        int[] leaderboardScores = leaderboard.getTopFiveScores();
        String[] leaderboardStartTimes = leaderboard.getTopFiveStartTimes();

        if (leaderboardNames[0] == null) { // newest score must be the best
            leaderboardNames[0] = newestName;
            leaderboardScores[0] = newestScore;
            leaderboardStartTimes[0] = newestStartTime;
        } else {
            int i = 0;
            boolean isPlaced = false;
            // continue until we find a place for the new score in the top five
            // we won't place the score if it isn't in the top five
            while (i < leaderboardScores.length && !isPlaced) {
                if (newestScore > leaderboardScores[i]) {
                    shiftElementsRightByOne(i,
                            leaderboardNames,
                            leaderboardScores,
                            leaderboardStartTimes);
                    leaderboardScores[i] = newestScore;
                    leaderboardNames[i] = newestName;
                    leaderboardStartTimes[i] = newestStartTime;
                    isPlaced = true;
                }
                i++;
            }
        }
    }

    public void shiftElementsRightByOne(int fromIndex,
                                        String[] leaderboardNames,
                                        int[] leaderboardScores,
                                        String[] leaderboardStartTimes) {
        // shift elements to the right starting from the end of the arrays
        for (int i = leaderboardNames.length - 1; i >= fromIndex; i--) {
            // only shift elements that exist and aren't the last one
            if (leaderboardNames[i] != null && i != leaderboardScores.length - 1) {
                leaderboardScores[i + 1] = leaderboardScores[i];
                leaderboardNames[i + 1] = leaderboardNames[i];
                leaderboardStartTimes[i + 1] = leaderboardStartTimes[i];
            }
        }
    }

    //string to be displayed
    public String getWinStatus() {
        if (player.getWinStatus()) {
            return "YOU WIN!!!";
        } else {
            return "YOU LOSE!!!";
        }
    }
}
