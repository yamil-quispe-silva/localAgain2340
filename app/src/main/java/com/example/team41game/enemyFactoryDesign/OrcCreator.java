package com.example.team41game.enemyFactoryDesign;


public class OrcCreator extends EnemyCreator {
    public Enemy createEnemy(int x, int y) {
        return new Orc(x, y);
    }
}
