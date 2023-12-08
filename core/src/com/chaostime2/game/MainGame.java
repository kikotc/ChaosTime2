package com.chaostime2.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Texture playerImg;
	private Rectangle player;

	float playerVelocityX = 0;
	float playerVelocityY = 0;
	int playerVelocity = 100;
	@Override
	public void create () {
		batch = new SpriteBatch();
		playerImg = new Texture("player.png");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);

		player = new Rectangle();
		player.x = 960 - (player.width / 2);
		player.y = 540 - (player.height / 2);
		player.width = 100;
		player.height = 100;
	}

	@Override
	public void render () {
		ScreenUtils.clear(255, 255, 255, 1);

		camera.update();
		batch.begin();
		batch.draw(playerImg, player.x, player.y);
		batch.end();

		boolean movingX = false, movingY = false;

		if(Gdx.input.isKeyPressed(Keys.D)) {
			playerVelocityX = playerVelocity;
			movingX = true;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			playerVelocityX = -playerVelocity;
			movingX = true;
		}
		if(Gdx.input.isKeyPressed(Keys.W)) {
			playerVelocityY = playerVelocity;
			movingY = true;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			playerVelocityY = -playerVelocity;
			movingY = true;
		}

		if(player.x < 0) player.x = 0;
		if(player.x > 1920 - player.width) player.x = 1920 - player.width;
		if(player.y < 0) player.y = 0;
		if(player.y > 1080 - player.height) player.y = 1080 - player.height;

		float delta = Gdx.graphics.getDeltaTime();

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
