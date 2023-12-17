package com.chaostime2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MainMenu implements Screen {
    final ChaosTime game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture menuImg;

    public MainMenu(final ChaosTime game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        viewport = new FitViewport(1920, 1080, camera);

        menuImg = new Texture("menu.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.439f, 0.443f, 0.447f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(menuImg, -600, -600);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MainGame(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        menuImg.dispose();
    }

    @Override
    public void show() {}
    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
}
