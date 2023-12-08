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
	float playerVeloX = 0;
	float playerVeloY = 0;
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
		batch.draw(playerImg, player.x + (playerVeloX * Gdx.graphics.getDeltaTime()), player.y + (playerVeloY * Gdx.graphics.getDeltaTime()));
		batch.end();
		//if D key is pressed
		if(Gdx.input.isKeyPressed(Keys.D)) {
			player.x += 200 * Gdx.graphics.getDeltaTime();
		}
		//if A key is pressed
		if(Gdx.input.isKeyPressed(Keys.A)) {
			player.x -= 200 * Gdx.graphics.getDeltaTime();
		}
		//if W key is pressed
		if(Gdx.input.isKeyPressed(Keys.W)) {
			player.y += 200 * Gdx.graphics.getDeltaTime();
		}
		//if S key is pressed
		if(Gdx.input.isKeyPressed(Keys.S)) {
			player.y -= 200 * Gdx.graphics.getDeltaTime();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerImg.dispose();
	}
}
