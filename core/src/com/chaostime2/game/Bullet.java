package com.chaostime2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.tools.javac.Main;

public class Bullet {

    private static Texture texture;

    float x, y;
    float mouseX, mouseY;
    float playerX, playerY;




    public boolean remove = false;
    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
        if(texture==null){
            texture = new Texture("bullet.png");
        }

    }

    public void update (float deltaTime){
       int dx = (int)(mouseX - playerX);
       int dy = (int)(mouseY - playerY);

       x += dx * deltaTime;

       y += dy * deltaTime;

        if(y > 1080 || y<0){
            remove = true;
        }

        if(x > 1920 || x <0){
            remove = true;
        }
    }
    public void mouse ( float mX,  float mY, float px, float py){
        mouseX = mX;
        mouseY = mY;
        playerX = px;
        playerY =py;
    }
    public void render(SpriteBatch batch){

        batch.draw(texture, x, y);
        System.out.println(x + "and " + y);

    }

}
