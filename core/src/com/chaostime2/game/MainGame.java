package com.chaostime2.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.RecursiveAction;

public class MainGame implements Screen, InputProcessor {
	final ChaosTime game;

	//private SpriteBatch batch;
	public SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Viewport extendViewport;
	private Texture foregroundImg;
	private Texture backgroundImg;
	public Vector3 mouseVector = new Vector3();
	public Vector2 relativeMouse = new Vector2();
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
	private int HEIGHT = 30;
	private int WIDTH = 30;
	private long lastEnemyTime;
	private long lastDamageTime = 0;
	private int enemySpeed = 160;
	private int enemyDamage = 0;

	//bullet variable
	ArrayList<Bullet> bullets;
	public int direction;


	//Collision
	Collision rect;
	//utilities
	private BitmapFont font;
	private int Timer = 60;
	private int shootTime = 0;

	public MainGame(final ChaosTime game) {
		Gdx.input.setInputProcessor(this);
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		viewport = new FitViewport(1920, 1080, camera);
		extendViewport = new ExtendViewport(1920, 1080, camera);
		batch = new SpriteBatch();
		font = new BitmapFont();
		foregroundImg = new Texture("foreground.png");
		backgroundImg = new Texture("background.png");

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
		ScreenUtils.clear(0.639f, 0.647f, 0.659f, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(backgroundImg, -400, -400);
		if (teleportPlaced) batch.draw(teleportImg, teleport.x, teleport.y);
		for(Bullet bullet: bullets){
			bullet.render(batch);
		}
		batch.draw(playerImg, player.x, player.y);
		for(Circle enemy: enemies) {
			batch.draw(enemyImg, enemy.x, enemy.y);
		}
		batch.draw(foregroundImg, -600, -600);
		font.draw(batch, Integer.toString(Timer), 1800, 1000);
		batch.draw(healthBackgroundImg, 20, 1000);
		batch.draw(healthImg, 30, 1010, 400 - 2 * playerHealth,0,410,40);
		batch.draw(healthFrameImg, 20, 1000);
		batch.end();

		float deltaTime = Gdx.graphics.getDeltaTime();
		time = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - startTime;
		boolean movingX = false, movingY = false;

		System.out.println(mouseVector);
		relativeMouse.x = mouseVector.x - player.x;
		relativeMouse.y = mouseVector.y - player.y;
		relativeMouse.nor();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (playerHealth <= 0) {
			Gdx.app.exit();
		}

		//shooting
		if(Gdx.input.isKeyPressed(Keys.F) && time - shootTime > 250 ){
			bullets.add(new Bullet(player.x, player.y, relativeMouse.x, relativeMouse.y));
			shootTime = (int)time;
		}

		//delete out of bounds bullets
		for (Iterator<Bullet> bulletIter = bullets.iterator();bulletIter.hasNext();) {
			Bullet bulletI = bulletIter.next();
			bulletI.update();
			if(bulletI.remove){
				bulletIter.remove();
			}
		}
		Bullet bulletInstance = new Bullet(12f,12f, 12f,12f);
		float bPosX = bulletInstance.getbX();
		float bPosY = bulletInstance.getbY();
		for (Iterator<Bullet> bulletIter = bullets.iterator();bulletIter.hasNext();) {
			Bullet bulletI = bulletIter.next();
			bulletI.update();
			for (Iterator<Circle> EnemyIter = enemies.iterator();EnemyIter.hasNext();){
				Circle enemyI = EnemyIter.next();
				if (bPosX== enemyI.x && bPosY == enemyI.y){
					bulletIter.remove();
					EnemyIter.remove();
				}
			}

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
		{
			if (time - lastEnemyTime > 20000000 / time + 500) spawnEnemy();
            for (Circle enemyI : enemies) {
                //individual enemy movement
                Vector2 direction = new Vector2();
                direction.x = (player.x + player.radius) - (enemyI.x + enemyI.radius);
                direction.y = (player.y + player.radius) - (enemyI.y + enemyI.radius);
                direction.nor();
                enemyI.x += direction.x * enemySpeed * deltaTime;
                enemyI.y += direction.y * enemySpeed * deltaTime;
                if (enemyI.x < 0) enemyI.x = 0;
                if (enemyI.x > 1920 - enemyI.radius * 2) enemyI.x = 1920 - enemyI.radius * 2;
                if (enemyI.y < 0) {enemyI.y = 0;}

                if (enemyI.y > 1080 - enemyI.radius * 2) enemyI.y = 1080 - enemyI.radius * 2;

                //damage
                if (enemyI.overlaps(player) && (time - lastDamageTime > 250)) {
                    for (int i = 0; i < enemies.size; i++) {
                        if (enemies.get(i).overlaps(player)) {
                            enemyDamage++;
                        }
                    }
                    lastDamageTime = time;
                    if (enemyDamage > 0) {
                        playerHealth -= enemyDamage;
                        enemyDamage = 0;
                    }
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
		this.rect = new Collision( enemy.x, enemy.y, WIDTH, HEIGHT);
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		camera.unproject(mouseVector.set(screenX, screenY, 0));
		return false;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		extendViewport.update(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		playerImg.dispose();
		enemyImg.dispose();
		foregroundImg.dispose();
		healthBackgroundImg.dispose();
		healthImg.dispose();
		healthFrameImg.dispose();
		teleportImg.dispose();
		font.dispose();
	}

	@Override
	public void show() {}
	@Override
	public void hide() {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public boolean keyDown(int i) {
		return false;
	}
	@Override
	public boolean keyUp(int i) {
		return false;
	}
	@Override
	public boolean keyTyped(char c) {
		return false;
	}
	@Override
	public boolean touchDown(int i, int i1, int i2, int i3) {
		return false;
	}
	@Override
	public boolean touchUp(int i, int i1, int i2, int i3) {
		return false;
	}
	@Override
	public boolean touchCancelled(int i, int i1, int i2, int i3) {
		return false;
	}
	@Override
	public boolean touchDragged(int i, int i1, int i2) {
		return false;
	}
	@Override
	public boolean scrolled(float v, float v1) {
		return false;
	}
}


