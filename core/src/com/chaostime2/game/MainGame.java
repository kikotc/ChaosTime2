package com.chaostime2.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.math.Rectangle;

public class MainGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture playerImg;
	private Rectangle player;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		playerImg = new Texture("player.png");
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		batch.draw(playerImg, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerImg.dispose();
	}
}
