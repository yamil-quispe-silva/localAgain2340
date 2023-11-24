package com.example.team41game.models;

public class Leaderboard {

    private String[] topFiveNames;
    private String[] topFiveStartTimes;
    private int[] topFiveScores;

    private static volatile Leaderboard leaderboard;

    private Leaderboard() {
        topFiveNames = new String[5];
        topFiveStartTimes = new String[5];
        topFiveScores = new int[5];
    }

    public static Leaderboard getLeaderboard() {
        if (leaderboard == null) {
            synchronized (Leaderboard.class) {
                if (leaderboard == null) {
                    leaderboard = new Leaderboard();
                }
            }
        }
        return leaderboard;
    }

    public String[] getTopFiveNames() {
        return this.topFiveNames;
    }

    public void setTopFiveNames(String[] topFiveNames) {
        for (int i = 0; i < 5; i++) {
            this.topFiveNames[i] = topFiveNames[i];
        }
    }

    public String[] getTopFiveStartTimes() {
        return this.topFiveStartTimes;
    }

    public void setTopFiveStartTimes(String[] topFiveStartTimes) {
        for (int i = 0; i < 5; i++) {
            this.topFiveStartTimes[i] = topFiveStartTimes[i];
        }
    }

    public int[] getTopFiveScores() {
        return this.topFiveScores;
    }

    public void setTopFiveScores(int[] topFiveScores) {
        for (int i = 0; i < 5; i++) {
            this.topFiveScores[i] = topFiveScores[i];
        }
    }
}
