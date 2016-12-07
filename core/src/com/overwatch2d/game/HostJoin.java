package com.overwatch2d.game;

/**
 * Created by peterbenedict on 11/11/16.
 **/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class HostJoin implements Screen, InputProcessor {

    private final Overwatch2D game;
    private OrthographicCamera camera;
    private Stage stage;

    HostJoin(final Overwatch2D gam) {
        float w = Gdx.graphics.getWidth(),
                h = Gdx.graphics.getHeight();

        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        stage = new Stage(new ExtendViewport(w, h, camera));

        Image background = new Image(new Texture(Gdx.files.internal("background/backblue.png")));
        background.setSize(w, h);

        stage.addActor(background);

        TextButtonStyle textStyle = new TextButtonStyle();
        textStyle.font = game.font;

        TextButton tutorial = new TextButton("Tutorial", textStyle);
        tutorial.setPosition(290,400 - tutorial.getHeight()/2);

        stage.addActor(tutorial);

        final Image tutorialGradient = new Image(new Texture(Gdx.files.internal("effects/orange.jpg")));
        tutorialGradient.setScale(0.45f);
        tutorialGradient.setPosition(240, 450 - tutorialGradient.getHeight()/2);
        tutorialGradient.setColor(1, 1, 1, 0);

        stage.addActor(tutorialGradient);

        tutorialGradient.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new Instructions(game));
                dispose();
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                tutorialGradient.setVisible(true);
                tutorialGradient.addAction(Actions.fadeIn(0.1f));
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                tutorialGradient.addAction(Actions.fadeOut(0.1f));
            }
        });

        TextButton hgame = new TextButton("Host Game", textStyle);
        hgame.setPosition(590,400 - hgame.getHeight()/2);

        stage.addActor(hgame);

        final Image hgameGradient = new Image(new Texture(Gdx.files.internal("effects/orange.jpg")));
        hgameGradient.setScale(0.45f);
        hgameGradient.setPosition(540, 450 - hgameGradient.getHeight()/2);
        hgameGradient.setColor(1, 1, 1, 0);

        stage.addActor(hgameGradient);

        hgameGradient.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new HostScreen(game) {

                });
                dispose();
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                hgameGradient.setVisible(true);
                hgameGradient.addAction(Actions.fadeIn(0.1f));
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                hgameGradient.addAction(Actions.fadeOut(0.1f));
            }
        });

        TextButton jgame = new TextButton("Join Game", textStyle);
        jgame.setPosition(890, 400 - jgame.getHeight()/2);

        stage.addActor(jgame);

        final Image jgameGradient = new Image(new Texture(Gdx.files.internal("effects/orange.jpg")));
        jgameGradient.setScale(0.45f);
        jgameGradient.setPosition(840, 450 - jgameGradient.getHeight()/2);
        jgameGradient.setColor(1, 1, 1, 0);

        stage.addActor(jgameGradient);

        jgameGradient.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new JoinGameScreen(game));
                dispose();
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                jgameGradient.setVisible(true);
                jgameGradient.addAction(Actions.fadeIn(0.1f));
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                jgameGradient.addAction(Actions.fadeOut(0.1f));
            }
        });

        TextButton back = new TextButton("Back", textStyle);
        back.setPosition(890, 100 - back.getHeight()/2);

        stage.addActor(back);

        final Image backGradient = new Image(new Texture(Gdx.files.internal("effects/orange.jpg")));
        backGradient.setScale(0.45f);
        backGradient.setPosition(850, 150 - backGradient.getHeight()/2);
        backGradient.setColor(1, 1, 1, 0);

        stage.addActor(backGradient);

        backGradient.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MainMenu(game));
                dispose();
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                backGradient.setVisible(true);
                backGradient.addAction(Actions.fadeIn(0.1f));
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                backGradient.addAction(Actions.fadeOut(0.1f));
            }
        });

        Image logo = new Image(new Texture(Gdx.files.internal("logo/overwatch_logo.png")));
        logo.setScale(0.55f);
        logo.setPosition(20, 720 - logo.getHeight()/2);

        stage.addActor(logo);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        game.setScreen(new HostJoin(game));
        dispose();

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}