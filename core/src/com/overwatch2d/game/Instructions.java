package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Created by geeca on 12/7/16.
 */

public class Instructions implements Screen {
    private static Overwatch2D game = null;
    private OrthographicCamera camera;
    private Stage stage;

    Instructions(final Overwatch2D gam) {
        float w = Gdx.graphics.getWidth(),
                h = Gdx.graphics.getHeight();

        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        stage = new Stage(new ExtendViewport(w, h, camera));

        Image background = new Image(new Texture(Gdx.files.internal("background/tutorials.jpg")));
        background.setSize(w, h);

        stage.addActor(background);

        TextButton.TextButtonStyle textStyle = new TextButton.TextButtonStyle();
        textStyle.font = game.font;

        TextButton login = new TextButton("Back", textStyle);
        login.setPosition(850, 60);
        stage.addActor(login);

        final Image beginGradient = new Image(new Texture(Gdx.files.internal("effects/button_gradient.png")));
        beginGradient.setScale(0.45f);
        beginGradient.setPosition(800, 40);
        beginGradient.setColor(1, 1, 1, 0);

        stage.addActor(beginGradient);

        beginGradient.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MainMenu(game));
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                beginGradient.setVisible(true);
                beginGradient.addAction(Actions.fadeIn(0.1f));
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                beginGradient.addAction(Actions.fadeOut(0.1f));
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

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
