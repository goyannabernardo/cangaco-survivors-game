package com.unicamp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unicamp.view.scenes.MainScene;

public class GameplayScreen implements Screen {

    private static final float GAME_OVER_DELAY = 1.0f;

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Runnable onGameOver;

    private float gameOverTimer = 0f;
    private boolean gameOverTriggered = false;

    private Scene scene;

    float spriteSize = 64f;

    public GameplayScreen(SpriteBatch batch, Runnable onGameOver) {
        if (batch == null || onGameOver == null) {
            throw new IllegalArgumentException("Dependências vitais da tela não podem ser nulas.");
        }

        this.batch = batch;
        this.onGameOver = onGameOver;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1024f, 768f);

        this.scene = new MainScene(batch, camera);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        scene.updateState(delta);
        camera.update();
		scene.render(batch, delta);
        
        batch.end();

        // Quando morre espera um momento e ai coloca a tela de morte
        if (scene.isGameOver() && !gameOverTriggered) {
            gameOverTimer += delta;
            if (gameOverTimer >= GAME_OVER_DELAY) {
                gameOverTriggered = true;
                onGameOver.run();
            }
        }
    }

    @Override
    public void show() {
        // Chamado quando esta tela se torna a tela ativa do jogo
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 1024f;
        camera.viewportHeight = 1024f * height / width;
        camera.update();
        
        if (scene != null) {
            scene.resize(width, height);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        scene.dispose();
    }
}