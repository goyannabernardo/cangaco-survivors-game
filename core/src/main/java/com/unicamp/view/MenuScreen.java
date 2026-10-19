package com.unicamp.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

// Tela de menu reutilizada tanto pra o principal quanto para o de morte
public class MenuScreen implements Screen {

    // botões
    public static class Option {
        private final String label;
        private final String imagePath;
        private final Runnable action;

        public Option(String label, String imagePath, Runnable action) {
            this.label = label;
            this.imagePath = imagePath;
            this.action = action;
        }
    }

    
    private static final float WORLD_WIDTH = 1280f;
    private static final float WORLD_HEIGHT = 720f;

    private static final float BOTTOM_MARGIN = 60f;
    private static final float BUTTON_SPACING = 20f;
    private static final float IMAGE_BUTTON_WIDTH = 260f; // altura sai da proporção do PNG

    private final Stage stage;
    private final Skin skin;
    private final Option[] options;
    private final List<Texture> textures = new ArrayList<>();


    // A ação só roda no fim do frame
    private Runnable pendingAction;

    public MenuScreen(String backgroundPath, Option... options) {
        this.options = options;
        this.skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        this.stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        Table root = new Table();
        root.setFillParent(true);
        root.bottom().padBottom(BOTTOM_MARGIN);

        Texture background = loadTexture(backgroundPath);
        if (background != null) {
            root.setBackground(new TextureRegionDrawable(new TextureRegion(background)));
        }

        for (Option option : options) {
            Button button = createButton(option);
            Cell<Button> cell = root.add(button).pad(BUTTON_SPACING);
            if (button instanceof TextButton) {
                cell.size(340f, 72f);
            } else {
                cell.size(button.getPrefWidth(), button.getPrefHeight());
            }
        }

        stage.addActor(root);
    }

    // Carrega texturas e devolve null se não existir.
    private Texture loadTexture(String path) {
        FileHandle file = Gdx.files.internal(path);
        
        if (!file.exists()) {
            return null;
        }

        Texture texture = new Texture(file);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        textures.add(texture);
        return texture;
    }

    private Button createButton(Option option) {
        Texture image = loadTexture(option.imagePath);
        Button button;

        if (image != null) {
            float width = IMAGE_BUTTON_WIDTH;
            float height = width * image.getHeight() / image.getWidth();

            TextureRegionDrawable normal = new TextureRegionDrawable(new TextureRegion(image));
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.imageUp = sized(normal, width, height);
            style.imageOver = sized(normal.tint(new Color(0.85f, 0.85f, 0.85f, 1f)), width, height); // escurece de leve ao passar o mouse
            style.imageDown = sized(normal.tint(new Color(0.65f, 0.65f, 0.65f, 1f)), width, height); // escurece mais ao clicar
            button = new ImageButton(style);
        } else {
            TextButton textButton = new TextButton(option.label, skin);
            textButton.getLabel().setFontScale(1.6f);
            button = textButton;
        }

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pendingAction = option.action;
            }
        });
        return button;
    }
    
    private Drawable sized(Drawable drawable, float width, float height) {
        drawable.setMinWidth(width);
        drawable.setMinHeight(height);
        return drawable;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.05f, 0.05f, 1f);

        stage.act(delta);
        stage.draw();

        // Enter aciona o primeiro botão (Jogar ou Recomeçar)
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) && options.length > 0) {
            pendingAction = options[0].action;
        }

        if (pendingAction != null) {
            Runnable action = pendingAction;
            pendingAction = null;
            action.run();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}