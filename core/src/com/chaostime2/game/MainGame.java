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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

public class MainGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	private final long startTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime());

	//player variables
	private Texture playerImg;
	private Rectangle player;
	private float playerVelocityX = 0;
	private float playerVelocityY = 0;
	private int playerVelocity = 100;

	//enemy variables
	private Texture enemyImg;
	private Array<Rectangle> enemies;
	private long lastEnemyTime;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		viewport = new FitViewport(1920, 1080, camera);
		batch = new SpriteBatch();

		playerImg = new Texture("player.png");
		player = new Rectangle();
		player.width = 80;
		player.height = 80;
		player.x = 960 - (player.width / 2);
		player.y = 540 - (player.height / 2);

		enemyImg = new Texture("enemy.png");
		enemies = new Array<Rectangle>();
		spawnEnemy();
	}

	@Override
	public void render () {
		ScreenUtils.clear(255, 255, 255, 1);

		//draws the player
		camera.update();
		batch.begin();
		batch.draw(playerImg, player.x, player.y);
		for(Rectangle enemy: enemies) {
			batch.draw(enemyImg, enemy.x, enemy.y);
		}
		batch.end();

		float delta = Gdx.graphics.getDeltaTime();
		long time = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
		boolean movingX = false, movingY = false;
		System.out.println(time - startTime);

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
			if (player.x > 1920 - player.width) player.x = 1920 - player.width;
			if (player.y < 0) player.y = 0;
			if (player.y > 1080 - player.height) player.y = 1080 - player.height;


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

		if(time - lastEnemyTime > 2000000000 - TimeUtils.timeSinceMillis(startTime) / 100) spawnEnemy();

	}

	private void spawnEnemy() {
		Rectangle enemy = new Rectangle();
		enemy.width = 60;
		enemy.height = 60;
		enemy.x = MathUtils.random(0, (1980 - enemy.width));
		enemy.y = MathUtils.random(0, (1080 - enemy.height));
		enemies.add(enemy);
		lastEnemyTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
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
