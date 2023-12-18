package com.chaostime2.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    public Circle hitbox = new Circle();
    Vector2 direction = new Vector2();
    public static final int enemySpeed = 240;


    public Enemy( float dX, float dY) {
        Circle enemy = new Circle();
        hitbox.x = enemy.x;
        hitbox.y = enemy.y;

        enemy.radius = 30;

        enemy.x = MathUtils.random(0, (1980 - enemy.radius * 2));
        enemy.y = MathUtils.random(0, (1080 - enemy.radius * 2));
    }
    public void update(float deltaTime){
        hitbox.x += direction.x * deltaTime *  enemySpeed;
        hitbox.y += direction.y * deltaTime * enemySpeed;
    }

}