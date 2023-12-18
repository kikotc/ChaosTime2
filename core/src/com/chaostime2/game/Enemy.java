package com.chaostime2.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

public class Enemy {
    public Circle hitbox = new Circle();

    public Enemy() {
        Circle enemy = new Circle();
        enemy.radius = 30;
        enemy.x = MathUtils.random(0, (1980 - enemy.radius * 2));
        enemy.y = MathUtils.random(0, (1080 - enemy.radius * 2));
    }

}
