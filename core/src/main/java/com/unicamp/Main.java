package com.unicamp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unicamp.view.GameplayScreen;
import com.unicamp.view.MenuScreen;
import com.unicamp.view.MenuScreen.Option;
import com.unicamp.view.RendererFactory;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private SpriteBatch batch;

    @Override
    public void create() {
        RendererFactory.build();
        batch = new SpriteBatch();
        showMenu();
    }

    
    // menu inicial
    public void showMenu() {
        changeScreen(new MenuScreen(
            "ui/menu_bg.png",
            new Option("Jogar", "ui/btn_jogar.png", this::startGame),
            new Option("Sair", "ui/btn_sair.png", Gdx.app::exit)
        ));
    }

    // jogo
    public void startGame() {
        changeScreen(new GameplayScreen(batch, this::showGameOver));
    }

    // menu de morte
    public void showGameOver() {
        changeScreen(new MenuScreen(
            "ui/gameover_bg.png",
            new Option("Recomeçar", "ui/btn_recomecar.png", this::startGame),
            new Option("Menu", "ui/btn_menu.png", this::showMenu),
            new Option("Sair", "ui/btn_sair.png", Gdx.app::exit)
        ));
    }


    // troca de tela 
    private void changeScreen(Screen next) {
        Screen previous = getScreen();
        setScreen(next);
        if (previous != null) {
            previous.dispose();
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Screen current = getScreen();
        if (current != null) {
            current.dispose();
        }
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}