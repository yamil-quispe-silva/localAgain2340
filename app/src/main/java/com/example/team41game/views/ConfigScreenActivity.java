package com.example.team41game.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.team41game.R;
import com.example.team41game.viewModels.ConfigScreenViewModel;


public class ConfigScreenActivity extends AppCompatActivity {

    private ImageView avatarImage;
    private EditText playerNameField;
    private Button leftArrowBtn;
    private Button rightArrowBtn;
    private Button continueBtn;
    private RadioGroup difficultyRadioGroup;
    private int[] avatarResources = {R.drawable.npc_dwarf_2, R.drawable.npc_elf,
                                     R.drawable.npc_barbarian};
    private int currentAvatarIndex = 0;
    private ConfigScreenViewModel configScreenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_screen);

        avatarImage = findViewById(R.id.avatarLabel);
        playerNameField = findViewById(R.id.playerNameField);
        leftArrowBtn = findViewById(R.id.leftArrowButton);
        rightArrowBtn = findViewById(R.id.rightArrowButton);
        continueBtn = findViewById(R.id.continueBtn);
        difficultyRadioGroup = findViewById(R.id.difficultyRadioGroup);

        avatarImage.setImageResource(avatarResources[currentAvatarIndex]);

        configScreenViewModel = new ViewModelProvider(this).get(ConfigScreenViewModel.class);

        continueBtn.setOnClickListener(v -> {
            String playerName = playerNameField.getText().toString();
            if (!configScreenViewModel.validateName(playerName)) {
                return;
            }
            int difficultyId = difficultyRadioGroup.getCheckedRadioButtonId();
            configScreenViewModel.setDifficulty(difficultyId, R.id.radioEasy, R.id.radioMedium);
            configScreenViewModel.setModelAttributes(avatarResources[currentAvatarIndex]);

            Intent game = new Intent(ConfigScreenActivity.this, GameScreen1Activity.class);
            startActivity(game);
        });

        leftArrowBtn.setOnClickListener(v -> {
            currentAvatarIndex = (currentAvatarIndex - 1 + avatarResources.length)
                    % avatarResources.length;
            avatarImage.setImageResource(avatarResources[currentAvatarIndex]);
        });

        rightArrowBtn.setOnClickListener(v -> {
            currentAvatarIndex = (currentAvatarIndex + 1) % avatarResources.length;
            avatarImage.setImageResource(avatarResources[currentAvatarIndex]);
        });
    }
}
