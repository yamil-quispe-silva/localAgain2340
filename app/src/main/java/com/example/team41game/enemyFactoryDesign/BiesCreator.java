package com.example.team41game.enemyFactoryDesign;


public class BiesCreator extends EnemyCreator {
    public Enemy createEnemy(int x, int y) {
        return new Bies(x, y);
    }
}

