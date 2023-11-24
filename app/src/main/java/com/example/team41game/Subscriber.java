package com.example.team41game;

import com.example.team41game.viewModels.GameScreenViewModel;

public interface Subscriber {
    void update(GameScreenViewModel subject);
}
