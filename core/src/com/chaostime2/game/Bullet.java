package com.chaostime2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
    public void update (float deltaTime){
        y+=Speed * deltaTime;
        if(y> 1080 || y<0){
            remove =true;
        }
        if(x>1920|| x <0){
            remove= true;
        }

    }
    public void render(SpriteBatch batch){
        batch.draw(texture,x,y);
    }
}
