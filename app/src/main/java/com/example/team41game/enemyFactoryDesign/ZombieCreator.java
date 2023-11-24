package com.example.team41game.enemyFactoryDesign;


public class ZombieCreator extends EnemyCreator {

    public Enemy createEnemy(int x, int y) {
        return new Zombie(x, y);
    }
}
