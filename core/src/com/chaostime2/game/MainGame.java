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
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;

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
	private int playerVelocity = 90;
	private Texture teleportImg;
	private Circle teleport;
	boolean teleportPlaced = false;

	//enemy variables
	private Texture enemyImg;
	private Array<Circle> enemies;
	private long lastEnemyTime;
	private int enemyVelocity = 120;

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
		teleportImg = new Texture("teleport.png");
		teleport = new Circle();
		teleport.radius = player.radius;

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
		if (teleportPlaced) batch.draw(teleportImg, teleport.x, teleport.y);
		batch.draw(playerImg, player.x, player.y);
		for(Circle enemy: enemies) {
			batch.draw(enemyImg, enemy.x, enemy.y);
		}
		batch.end();

		Vector2 playerDirection = new Vector2(0,0);
		float delta = Gdx.graphics.getDeltaTime();
		time = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - startTime;
		boolean movingX = false, movingY = false;

		//player behavior
		{
			//controls
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				if (teleportPlaced) {
					player.x = teleport.x;
					player.y = teleport.y;
				} else {
					teleport.x = player.x;
					teleport.y = player.y;
				}
				teleportPlaced = !teleportPlaced;
				System.out.println(teleportPlaced);
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				playerDirection.x += 1;
				movingX = true;
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				playerDirection.x -= 1;
				movingX = true;
			}
			if (Gdx.input.isKeyPressed(Keys.W)) {
				playerDirection.y += 1;
				movingY = true;
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				playerDirection.y -= 1;
				movingY = true;
			}

			//moving the player (position = velocity * percent in direction * time)
			playerDirection.nor();
			player.x += playerVelocity * playerDirection.x * delta;
			player.y += playerVelocity * playerDirection.y * delta;

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

		//enemy
		if(time - lastEnemyTime > 20000000 / time + 500) spawnEnemy();
		for (Iterator<Circle> iter = enemies.iterator();iter.hasNext();) {
			Circle enemyI = iter.next();
			Vector2 direction = new Vector2();
			direction.x = (player.x + player.radius) - (enemyI.x + enemyI.radius);
			direction.y = (player.y + player.radius) - (enemyI.y + enemyI.radius);
			direction.nor();
			enemyI.x += direction.x * enemyVelocity * delta;
			enemyI.y += direction.y * enemyVelocity * delta;
		}

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
		enemyImg.dispose();
	}
}
