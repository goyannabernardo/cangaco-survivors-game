package com.unicamp.view.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unicamp.model.entity.Player;
import com.unicamp.model.entity.weapon.Whip;
import com.unicamp.model.entity.weapon.Cross;
import com.unicamp.model.valueobject.CreatureHealth;
import com.unicamp.view.EnemySpawner;
import com.unicamp.view.Scene;
import com.unicamp.view.renderer.BackgroundRenderer;

public class MainScene extends Scene {

    private static final float HEALTH_BAR_WIDTH = 260f; // largura da barra na tela (px)
    private static final String PAUSE_FONT_PATH = "fonts/xilosa.ttf";

    private final SpriteBatch batch;
    private final Player player;
    private final OrthographicCamera camera;
    private final BackgroundRenderer backgroundRenderer;

    private final EnemySpawner enemySpawner;
    
    private final Viewport uiViewport;
    private final Texture healthInsideTex;
    private final Texture healthOutlineTex;

    private final BitmapFont pauseFont;
    private final GlyphLayout pauseLayout;

    public MainScene(SpriteBatch batch, OrthographicCamera camera) {
        this.camera = camera;
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.backgroundRenderer = new BackgroundRenderer("background/Floor.png", 4, 3);
        
        this.player = new Player(0, 0);
        player.addWeapon(new Cross());

        this.batch = batch;
        
        this.enemySpawner = new EnemySpawner(entityManager, camera);
        addEntity(player);

        this.uiViewport = new ScreenViewport();
        this.uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.healthInsideTex = new Texture("ui/life_fill.png");
        this.healthOutlineTex = new Texture("ui/life_bar.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(PAUSE_FONT_PATH));
        FreeTypeFontParameter fontParams = new FreeTypeFontParameter();
        fontParams.size = 64;
        
        fontParams.color = Color.valueOf("#f7ead4");

        fontParams.borderWidth = 3;
        fontParams.borderColor = Color.BLACK;

        //tentativa de efeito de borda dupla
        fontParams.shadowOffsetX = 0;
        fontParams.shadowOffsetY = 0;
        fontParams.shadowColor = Color.valueOf("#f7ead4");

        this.pauseFont = generator.generateFont(fontParams);
        generator.dispose();
        this.pauseLayout = new GlyphLayout(pauseFont, "PAUSED");
    }

    @Override
    protected void onUpdate(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            entityManager.togglePause();
        }

        // Pausado ignora o input para o personagem não trocar de pose
        if (!entityManager.getIsPaused()) {
            input(deltaTime);
        }

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        enemySpawner.update(deltaTime);
        backgroundRenderer.render(batch, player, camera.viewportWidth, camera.viewportHeight);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);

        boolean wasDrawing = batch.isDrawing();
        if (wasDrawing) {
            batch.end();
        }

        uiViewport.apply(); 

        batch.setProjectionMatrix(uiViewport.getCamera().combined);
        batch.begin();
        
        drawHealthBar(batch);

        if (entityManager.getIsPaused()) {
            pauseFont.draw(batch, pauseLayout,
                (Gdx.graphics.getWidth() - pauseLayout.width) / 2f,
                (Gdx.graphics.getHeight() + pauseLayout.height) / 2f);
        }
        
        batch.end();

        if (wasDrawing) {
            batch.begin();
        }
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
        // camera.setToOrtho(false, width, height);
    }

    private void drawHealthBar(SpriteBatch batch) {
        CreatureHealth health = player.getHealth();
        float maxHealth = health.maxHealth(); 
        float currentHealth = health.health(); 
        float healthPercentage = currentHealth / maxHealth;
        healthPercentage = Math.max(0, Math.min(1, healthPercentage));

        float margin = 20f;
        float barWidth = HEALTH_BAR_WIDTH;
        float barHeight = barWidth * healthOutlineTex.getHeight() / healthOutlineTex.getWidth();

        int cropWidth = (int) (healthInsideTex.getWidth() * healthPercentage);

        if (cropWidth > 0) {
            batch.draw(
                healthInsideTex,
                margin, margin,
                barWidth * healthPercentage, barHeight,
                0, 0,
                cropWidth, healthInsideTex.getHeight(),
                false, false
            );
        }

        batch.draw(healthOutlineTex, margin, margin, barWidth, barHeight);
    }

    //antes os frames influenciavam a velocidade
    private static final float WALK_SPEED = 300f;

    public void input(float deltaTime) {
        float speedX = Boolean.compare(Gdx.input.isKeyPressed(Keys.D), false) -  Boolean.compare(Gdx.input.isKeyPressed(Keys.A), false);
        float speedY = Boolean.compare(Gdx.input.isKeyPressed(Keys.W), false) -  Boolean.compare(Gdx.input.isKeyPressed(Keys.S), false);
        float magnitude = (float) Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
        
        if (magnitude > 0) {
            speedX /= magnitude;
            speedY /= magnitude;
        }

        player.setxSpeed(speedX * WALK_SPEED * deltaTime);
        player.setySpeed(speedY * WALK_SPEED * deltaTime);
    }

    @Override
    public boolean isGameOver() {
        return !player.isActive();
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundRenderer.dispose();
        healthInsideTex.dispose();
        healthOutlineTex.dispose();
        pauseFont.dispose();
    }
}