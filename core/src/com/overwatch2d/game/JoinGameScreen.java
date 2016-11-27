package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class JoinGameScreen implements Screen, InputProcessor {
    private static Overwatch2D game = null;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    JoinGameScreen(final Overwatch2D gam) {
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

        Image logo = new Image(new Texture(Gdx.files.internal("logo/overwatch_logo.png")));
        logo.setScale(0.55f);
        logo.setPosition(20, 720 - logo.getHeight()/2);

        stage.addActor(logo);

        TextArea textbox = new TextArea("", skin);
        textbox.setPosition(Gdx.graphics.getWidth()/2 - textbox.getWidth()/2, Gdx.graphics.getHeight()/2);

        stage.addActor(textbox);

        TextButtonStyle joinStyle = new TextButtonStyle();
        joinStyle.font = Overwatch2D.gameSelectionOKFont;

        TextButton join = new TextButton("Join Game", joinStyle);
        join.setPosition(Gdx.graphics.getWidth()/2, 60);

        join.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                String ip = textbox.getText();

                Overwatch2D.createClient();
                NetworkHelper.connect(ip, Overwatch2D.getName());
                game.setScreen(new JoinScreen(game)); // temp
                dispose();
            }
        });

        stage.addActor(join);

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