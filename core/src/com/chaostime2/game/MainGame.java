package com.chaostime2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.atan2;

public class MainGame implements Screen, InputProcessor {
	final ChaosTime game;

	public SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Viewport extendViewport;
	private Texture foregroundImg;
	private Texture backgroundImg;
	private float angle;
	//subtract 1 so variable time will never be 0 (edge case)
	private final long startTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - 1;
	private long time = 0;

	//player variables
	private Texture playerImg;
	private Circle player;
	private Vector2 playerDirection = new Vector2();
	private Vector2 playerVelocity = new Vector2();
	private int playerSpeed = 220;

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
	private int enemySpeed = 240;
	private int enemyDamage = 0;

	//gun variables
	private Texture gunImg;
	private ArrayList<Bullet> bullets;
	private int shootTime = 0;
	private Vector3 mouseVector = new Vector3();
	private Vector2 relativeMouse = new Vector2();

	public MainGame(final ChaosTime game) {
		Gdx.input.setInputProcessor(this);
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		viewport = new FitViewport(1920, 1080, camera);
		extendViewport = new ExtendViewport(1920, 1080, camera);
		batch = new SpriteBatch();
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

		gunImg = new Texture("gun.png");
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
		batch.draw(gunImg, player.x + 40, player.y + 32, 0, 8, 76,16,1,1, angle, 0,0,76,16, false, false);
		batch.draw(playerImg, player.x, player.y);
		for(Circle enemy: enemies) {
			batch.draw(enemyImg, enemy.x, enemy.y);
		}
		batch.draw(foregroundImg, -600, -600);
		batch.draw(healthBackgroundImg, 20, 1000);
		batch.draw(healthImg, 30, 1010, 400 - 2 * playerHealth,0,410,40);
		batch.draw(healthFrameImg, 20, 1000);
		batch.end();

		float deltaTime = Gdx.graphics.getDeltaTime();
		time = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - startTime;
		boolean movingX = false, movingY = false;

		relativeMouse.x = mouseVector.x - (player.x + player.radius);
		relativeMouse.y = mouseVector.y - (player.y + player.radius);
		relativeMouse.nor();
		angle = (float) Math.toDegrees(atan2(relativeMouse.y, relativeMouse.x));

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (playerHealth <= 0) {
			Gdx.app.exit();
		}

		//shooting
		if(Gdx.input.isTouched() && time - shootTime > 500){
			bullets.add(new Bullet(player.x + 35, player.y + 35, relativeMouse.x, relativeMouse.y));
			shootTime = (int)time;
		}

		for (Iterator<Bullet> bulletIter = bullets.iterator();bulletIter.hasNext();) {
			Bullet bulletI = bulletIter.next();
			bulletI.update();
			for (Iterator<Circle> enemyIter = enemies.iterator();enemyIter.hasNext();) {
				Circle enemyI = enemyIter.next();
				if (bulletI.hitbox.overlaps(enemyI)) {
					bulletI.remove = true;
					enemyIter.remove();
				}
			}
			if(bulletI.remove){
				bulletIter.remove();
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

			//player border
			if (player.x < 0) player.x = 0;
			if (player.x > 1920 - player.radius * 2) player.x = 1920 - player.radius * 2;
			if (player.y < 0) player.y = 0;
			if (player.y > 1080 - player.radius * 2) player.y = 1080 - player.radius * 2;
		}

		//enemy
		{
			if (time - lastEnemyTime > 20000000 / time + 200) spawnEnemy();
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
				if (enemyI.y < 0) enemyI.y = 0;
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

	private void spawnEnemy() {
		Circle enemy = new Circle();
		enemy.radius = 30;
		enemy.x = MathUtils.random(0, (1980 - enemy.radius * 2));
		enemy.y = MathUtils.random(0, (1080 - enemy.radius * 2));
		enemies.add(enemy);
		lastEnemyTime = time;
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
		backgroundImg.dispose();
		healthBackgroundImg.dispose();
		healthImg.dispose();
		healthFrameImg.dispose();
		teleportImg.dispose();
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


