package com.chaostime2.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

public class MainGame implements Screen {
	final ChaosTime game;

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	//subtract 1 so variable time will never be 0 (edge case)
	private final long startTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - 1;
	private long time = 0;

	//player variables
	private Texture playerImg;
	private Circle player;
	private Vector2 playerDirection = new Vector2();
	private Vector2 playerVelocity = new Vector2();
	private int playerSpeed = 180;

	//health variables
	private int playerHealth = 200;
	private Texture healthBackgroundImg;
	private Texture healthImg;
	private Texture healthFrameImg;

	//teleport abilities variables
	private Texture teleportImg;
	private Circle teleport;
	boolean teleportPlaced = false;

	//enemy variables
	private Texture enemyImg;
	private Array<Circle> enemies;
	private long lastEnemyTime;
	private long lastDamageTime = 0;
	private int enemySpeed = 160;
	private int enemyDamage = 0;

	//bullet variable
	ArrayList<Bullet> bullets;


	//utilities
	private BitmapFont font;
	private int Timer=60;
	private int shootTime =0;

	public MainGame(final ChaosTime game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		viewport = new FitViewport(1920, 1080, camera);
		batch = new SpriteBatch();

		playerImg = new Texture("player.png");
		player = new Circle();
		player.radius = 40;
		player.x = 960 - player.radius;
		player.y = 540 - player.radius;

		healthBackgroundImg = new Texture("healthBackground.png");
		healthImg = new Texture("health.png");
		healthFrameImg = new Texture("healthFrame.png");

		teleportImg = new Texture("teleport.png");
		teleport = new Circle();

		enemyImg = new Texture("enemy.png");
		enemies = new Array<Circle>();
		spawnEnemy();

		bullets = new ArrayList<Bullet>();
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.422f, 0.326f, 0.252f, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		if (teleportPlaced) batch.draw(teleportImg, teleport.x, teleport.y);
		batch.draw(playerImg, player.x, player.y);
		for(Circle enemy: enemies) {
			batch.draw(enemyImg, enemy.x, enemy.y);
		}
		font = new BitmapFont();
		font.draw(batch, Integer.toString(Timer), 1800, 1000);
		batch.draw(healthBackgroundImg, 20, 1000);
		batch.draw(healthImg, 30, 1010, 400 - 2 * playerHealth,0,410,40);
		batch.draw(healthFrameImg, 20, 1000);
		for(Bullet bullets: bullets){
			bullets.render(batch);
		}
		batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		float deltaTime = Gdx.graphics.getDeltaTime();
		time = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - startTime;

		boolean movingX = false, movingY = false;
		//shooting
		if(Gdx.input.isKeyPressed(Keys.F) && time - shootTime > 250 ){
			bullets.add(new Bullet(player.x + 4, player.y + 4));
			shootTime = (int)time;
		}
		//update bullets
		ArrayList<Bullet> removeBullets= new ArrayList<Bullet>();
		for(Bullet bullet: bullets){
			bullet.update(deltaTime);
			if(bullet.remove){
				//this is what breaks it goes out of bounds im pretty sure 
				//removeBullets.add(bullet);

			}
			bullets.removeAll(removeBullets);
		}
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

			//moving the player (position = speed * vector (percent in each direction) * time)
			if (movingX || movingY) {
				playerDirection.nor();
				playerVelocity.x = playerSpeed * playerDirection.x;
				playerVelocity.y = playerSpeed * playerDirection.y;
			}
			player.x += playerVelocity.x * deltaTime;
			player.y += playerVelocity.y * deltaTime;

			//dampen player movement
			if (playerVelocity.x > 0 && !movingX) {
				playerVelocity.x -= 300 * deltaTime;
				if (playerVelocity.x < 0) playerVelocity.x = 0;
			}
			if (playerVelocity.x < 0 && !movingX) {
				playerVelocity.x += 300 * deltaTime;
				if (playerVelocity.x > 0) playerVelocity.x = 0;
			}
			if (playerVelocity.y > 0 && !movingY) {
				playerVelocity.y -= 300 * deltaTime;
				if (playerVelocity.y < 0) playerVelocity.y = 0;
			}
			if (playerVelocity.y < 0 && !movingY) {
				playerVelocity.y += 300 * deltaTime;
				if (playerVelocity.y > 0) playerVelocity.y = 0;
			}

			if (player.x < 0) player.x = 0;
			if (player.x > 1920 - player.radius * 2) player.x = 1920 - player.radius * 2;
			if (player.y < 0) player.y = 0;
			if (player.y > 1080 - player.radius * 2) player.y = 1080 - player.radius * 2;
		}

		//enemy
		if(time - lastEnemyTime > 20000000 / time + 500) spawnEnemy();
		for (Iterator<Circle> iter = enemies.iterator();iter.hasNext();) {
			Circle enemyI = iter.next();

			//individual enemy movement
			Vector2 direction = new Vector2();
			direction.x = (player.x + player.radius) - (enemyI.x + enemyI.radius);
			direction.y = (player.y + player.radius) - (enemyI.y + enemyI.radius);
			direction.nor();
			enemyI.x += direction.x * enemySpeed * deltaTime;
			enemyI.y += direction.y * enemySpeed * deltaTime;

			//damage
			if (enemyI.overlaps(player) && (time - lastDamageTime > 250)) {
				for (int i = 0; i < enemies.size; i++) {
					if(enemies.get(i).overlaps(player)){
						enemyDamage++;
					}
				}
				System.out.println(enemyDamage);
				lastDamageTime = time;
				if(enemyDamage>0){
					playerHealth-=enemyDamage;
					enemyDamage=0;
					System.out.println(playerHealth);
					enemyDamage=0;
				}
			}
		}

	}
	//WIP OF TIMER
	public void timeTrack(){
		Timer = Timer - (int)(TimeUtils.nanosToMillis(TimeUtils.nanoTime())/1000);
		System.out.println(Timer);
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
		viewport.update(width, height, true);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose () {
		batch.dispose();
		playerImg.dispose();
		enemyImg.dispose();
		healthBackgroundImg.dispose();
		healthImg.dispose();
		healthFrameImg.dispose();
		teleportImg.dispose();
		font.dispose();
	}
}


