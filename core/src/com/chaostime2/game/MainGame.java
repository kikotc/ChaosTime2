package com.chaostime2.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;

public class MainGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture playerImg;
	private Rectangle player;
	float playerVelocityX = 0;
	float playerVelocityY = 0;
	@Override
	public void create () {
		batch = new SpriteBatch();
		playerImg = new Texture("player.png");

		player = new Rectangle();
		player.x = 910;
		player.y = 490;
		player.width = 100;
		player.height = 100;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		batch.begin();
		batch.draw(playerImg, player.x, player.y);
		batch.end();

		float delta = Gdx.graphics.getDeltaTime();
		boolean movingX = false, movingY = false;

		//if D key is pressed
		if(Gdx.input.isKeyPressed(Keys.D)) {
			playerVelocityX = 100;
			movingX = true;
		}
		//if A key is pressed
		if(Gdx.input.isKeyPressed(Keys.A)) {
			playerVelocityX = -100;
			movingX = true;
		}
		//if W key is pressed
		if(Gdx.input.isKeyPressed(Keys.W)) {
			playerVelocityY = 100;
			movingY = true;
		}
		//if S key is pressed
		if(Gdx.input.isKeyPressed(Keys.S)) {
			playerVelocityY = -100;
			movingY = true;
		}

		player.x += playerVelocityX * delta;
		player.y += playerVelocityY * delta;

		//returns the velocity to zero slowly if no inputs
		//Math.min is used to make sure the velocity will return to 0 instead of "overshooting"
		if (playerVelocityX > 0 && !movingX) {
			playerVelocityX -= 400 * delta;
			if (playerVelocityX < 0) playerVelocityX = 0;
		}
		if (playerVelocityX < 0 && !movingX) {
			playerVelocityX += 400 * delta;
			if (playerVelocityX > 0) playerVelocityX = 0;
		}
		if (playerVelocityY > 0 && !movingY) {
			playerVelocityY -= 400 * delta;
			if (playerVelocityY < 0) playerVelocityY = 0;
		}
		if (playerVelocityY < 0 && !movingY) {
			playerVelocityY += 400 * delta;
			if (playerVelocityY > 0) playerVelocityY = 0;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerImg.dispose();
	}
}
