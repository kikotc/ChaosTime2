package com.chaostime2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.tools.javac.Main;

public class Bullet {
    private static Texture texture;
    float x, y ;
    public float bX, bY;
    private Vector2 position = new Vector2();
    private Vector2 direction = new Vector2();
    public static final int WIDTH = 2;
    public static final int HEIGHT = 2;
    public boolean remove = false;
    Collision rect;

    public Bullet(float x, float y, float mX, float mY) {
        position.x = x;
        position.y = y;
        this.bX = position.x;
        this.bY = position.y;
        direction.x = mX;
        direction.y = mY;
        this.rect = new Collision(x,y,WIDTH, HEIGHT);
        texture = new Texture("bullet.png");
    }

    public void update () {
        position.x += direction.x * 250 * Gdx.graphics.getDeltaTime();
        position.y += direction.y * 250 * Gdx.graphics.getDeltaTime();
        bX = position.x;
        bY = position.y;
        if (position.x > 1920 || position.x < 0 || position.y > 1080 || position.y < 0) {
            remove = true;
            rect.move(x, y);
        }
    }
    public Collision getCollision () {
        return rect;
    }

    /*
    public void mouse ( float mX,  float mY, float px, float py){
        mouseX = mX;
        mouseY = mY;
        playerX = px;
        playerY = py;
    }*/

    public void render(SpriteBatch batch){
        batch.draw(texture, position.x, position.y);
    }
    public float getbX(){
        return bX;
    }
    public float getbY(){
        return bY;
    }
}
