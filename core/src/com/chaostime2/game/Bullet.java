package com.chaostime2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.tools.javac.Main;

public class Bullet {
    public static Vector2 BulletSpeed = new Vector2(100,100);

    private static Texture texture;

    float x, y;
    float mouseX, mouseY;
    int direction;







    public boolean remove = false;
    public Bullet(float x, float y){
//        MainGame obj = new MainGame();
        this.x =x;
        this.y =y;
        if(texture==null){
            texture = new Texture("bullet.png");
        }

    }
    public void Direction(){

    }
    public void update (float deltaTime){
       BulletSpeed.nor();
       x += 250  * deltaTime * direction * mouseX ;
       y += 250  * deltaTime * direction * mouseY ;

        if(y > 1080 || y<0){
            remove = true;
        }

        if(x > 1920 || x <0){
            remove = true;
        }
    }
    public void mouse (int md, float mX,  float mY){
        direction = md;
        mouseX = mX;
        mouseY = mY;


    }
    public void render(SpriteBatch batch){
        batch.draw(texture,x,y);
    }

}
