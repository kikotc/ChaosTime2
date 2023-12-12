package com.chaostime2.game;

import com.badlogic.gdx.graphics.Texture;

public class Bullet {
    public static final int Speed = 100;
    private static Texture texture;

    float x, y;

    public boolean remove = false;
    public Bullet(float x, float y){
        this.x =x;
        this.y =y;
        if(texture==null){
            texture = new Texture("bullet.png");
        }
    }
}
