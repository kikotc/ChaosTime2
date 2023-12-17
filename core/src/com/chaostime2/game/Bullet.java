package com.chaostime2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public static Vector2 BulletSpeed = new Vector2(100,100);

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
       BulletSpeed.nor();
       x += BulletSpeed.x * 1 * deltaTime * 250;
       y += BulletSpeed.y * 1 * deltaTime;

        if(y > 1080 || y<0){
            remove = true;
        }

        if(x > 1920 || x <0){
            remove = true;
        }
    }
    public void render(SpriteBatch batch){
        batch.draw(texture,x,y);
    }
}
