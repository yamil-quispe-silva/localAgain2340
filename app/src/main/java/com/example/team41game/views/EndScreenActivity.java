package com.example.team41game.views;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.team41game.viewModels.EndScreenViewModel;
import androidx.appcompat.app.AppCompatActivity;

import com.example.team41game.R;

public class EndScreenActivity extends AppCompatActivity {

    private EndScreenViewModel endScreenViewModel;

    private String[] leaderboardNames;
    private int[] leaderboardScores;
    private String[] leaderboardStartTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);

        endScreenViewModel = new ViewModelProvider(this).get(EndScreenViewModel.class);
        String newestName = endScreenViewModel.getName();
        int newestScore = endScreenViewModel.getScore();
        String newestStartTime = endScreenViewModel.getStartTime();

        TextView newName = findViewById(R.id.nameNew);
        TextView newScore = findViewById(R.id.newScoreNum);
        TextView newStartTime = findViewById(R.id.timeNew);
        newName.setText(newestName);
        newScore.setText(String.valueOf(newestScore));
        newStartTime.setText(newestStartTime);
        TextView winLose = findViewById(R.id.winLabel);

        winLose.setText(endScreenViewModel.getWinStatus());

        endScreenViewModel.updateLeaderboard(newestName, newestScore, newestStartTime);

        leaderboardNames = endScreenViewModel.getTopFiveNames();
        leaderboardScores = endScreenViewModel.getTopFiveScores();
        leaderboardStartTimes = endScreenViewModel.getTopFiveStartTimes();
        
        TextView name1 = findViewById(R.id.name1);
        TextView score1 = findViewById(R.id.highScore1);
        TextView startTime1 = findViewById(R.id.time1);
        name1.setText(leaderboardNames[0]);
        score1.setText(String.valueOf(leaderboardScores[0]));
        startTime1.setText(leaderboardStartTimes[0]);

        TextView name2 = findViewById(R.id.name2);
        TextView score2 = findViewById(R.id.highScore2);
        TextView startTime2 = findViewById(R.id.time2);
        name2.setText(leaderboardNames[1]);
        score2.setText(String.valueOf(leaderboardScores[1]));
        startTime2.setText(leaderboardStartTimes[1]);

        TextView name3 = findViewById(R.id.name3);
        TextView score3 = findViewById(R.id.highScore3);
        TextView startTime3 = findViewById(R.id.time3);
        name3.setText(leaderboardNames[2]);
        score3.setText(String.valueOf(leaderboardScores[2]));
        startTime3.setText(leaderboardStartTimes[2]);

        TextView name4 = findViewById(R.id.name4);
        TextView score4 = findViewById(R.id.highScore4);
        TextView startTime4 = findViewById(R.id.time4);
        name4.setText(leaderboardNames[3]);
        score4.setText(String.valueOf(leaderboardScores[3]));
        startTime4.setText(leaderboardStartTimes[3]);

        TextView name5 = findViewById(R.id.name5);
        TextView score5 = findViewById(R.id.highScore5);
        TextView startTime5 = findViewById(R.id.time5);
        name5.setText(leaderboardNames[4]);
        score5.setText(String.valueOf(leaderboardScores[4]));
        startTime5.setText(leaderboardStartTimes[4]);

        Button restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(view -> {
            Intent restartGame = new Intent(this, MainActivity.class);
            startActivity(restartGame);
        });
    }
}
