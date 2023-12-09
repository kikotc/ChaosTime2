package com.chaostime2.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

public class MainGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	//subtract 1 so variable time will never be 0 (edge case)
	private final long startTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - 1;
	private long time = 0;

	//player variables
	private Texture playerImg;
	private Circle player;
	private float playerVelocityX = 0;
	private float playerVelocityY = 0;
	private int playerVelocity = 100;

	//enemy variables
	private Texture enemyImg;
	private Array<Circle> enemies;
	private long lastEnemyTime;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		viewport = new FitViewport(1920, 1080, camera);
		batch = new SpriteBatch();

		playerImg = new Texture("player.png");
		player = new Circle();
		player.radius = 40;
		player.x = 960 - player.radius;
		player.y = 540 - player.radius;

		enemyImg = new Texture("enemy.png");
		enemies = new Array<Circle>();
		spawnEnemy();
	}

	@Override
	public void render () {
		ScreenUtils.clear(255, 255, 255, 1);

		//draws the player
		camera.update();
		batch.begin();
		batch.draw(playerImg, player.x, player.y);
		for(Circle enemy: enemies) {
			batch.draw(enemyImg, enemy.x, enemy.y);
		}
		batch.end();

		float delta = Gdx.graphics.getDeltaTime();
		time = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - startTime;
		boolean movingX = false, movingY = false;

		//player
		{
			//controls
			if (Gdx.input.isKeyPressed(Keys.D)) {
				playerVelocityX = playerVelocity;
				movingX = true;
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				playerVelocityX = -playerVelocity;
				movingX = true;
			}
			if (Gdx.input.isKeyPressed(Keys.W)) {
				playerVelocityY = playerVelocity;
				movingY = true;
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				playerVelocityY = -playerVelocity;
				movingY = true;
			}


			//map border
			if (player.x < 0) player.x = 0;
			if (player.x > 1920 - player.radius * 2) player.x = 1920 - player.radius * 2;
			if (player.y < 0) player.y = 0;
			if (player.y > 1080 - player.radius * 2) player.y = 1080 - player.radius * 2;


			//moving the player (position = velocity * time)
			player.x += playerVelocityX * delta;
			player.y += playerVelocityY * delta;

			//returns the velocity to zero slowly if no inputs
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

		if(time - lastEnemyTime > 20000000 / time + 500) spawnEnemy();

	}

	private void spawnEnemy() {
		Circle enemy = new Circle();
		enemy.radius = 30;
		enemy.x = MathUtils.random(0, (1980 - enemy.radius * 2));
		enemy.y = MathUtils.random(0, (1080 - enemy.radius * 2));
		enemies.add(enemy);
		lastEnemyTime = time;
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		playerImg.dispose();
	}
}
