package com.chaostime2.game;

import com.badlogic.gdx.math.Circle;


public class Enemy {
    //create an Enemy class with the following properties:
    Circle enemyItem;// = new Circle();
    public int health = 2;


    public Enemy(float enemyX, float enemyY, float enemyR) {
        enemyItem = new Circle();
        enemyItem.x = enemyX;
        enemyItem.y = enemyY;
        enemyItem.radius = enemyR;

    }

}
