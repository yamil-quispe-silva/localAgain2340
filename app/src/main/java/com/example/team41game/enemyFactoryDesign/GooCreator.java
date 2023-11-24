package com.example.team41game.enemyFactoryDesign;


public class GooCreator extends EnemyCreator {
    public Enemy createEnemy(int x, int y) {
        return new Goo(x, y);
    }
}
