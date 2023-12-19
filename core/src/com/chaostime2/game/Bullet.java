package com.chaostime2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private static Texture texture;
    public Circle hitbox = new Circle();
    private Vector2 direction = new Vector2();
    public boolean remove = false;

    public Bullet(float x, float y, float mX, float mY) {
        hitbox.x = x;
        hitbox.y = y;
        direction.x = mX;
        direction.y = mY;
        texture = new Texture("bullet.png");
        hitbox.radius = 5;
    }

    public void update () {
        hitbox.x += direction.x * 500 * Gdx.graphics.getDeltaTime();
        hitbox.y += direction.y * 500 * Gdx.graphics.getDeltaTime();
        if (hitbox.x > 1920 || hitbox.x < 0 || hitbox.y > 1080 || hitbox.y < 0) {
            remove = true;
        }
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, hitbox.x, hitbox.y);
    }
}
