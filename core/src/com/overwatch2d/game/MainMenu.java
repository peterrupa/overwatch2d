package com.overwatch2d.game;

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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


class MainMenu implements Screen, InputProcessor {
    private final Overwatch2D game;
    private OrthographicCamera camera;
    private Stage stage;
    private Sound menuSound = Gdx.audio.newSound(Gdx.files.internal("sfx/music/menu.mp3"));;

    MainMenu(final Overwatch2D gam) {
        float w = Gdx.graphics.getWidth(),
              h = Gdx.graphics.getHeight();

        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        stage = new Stage(new ExtendViewport(w, h, camera));

        menuSound.play();

        Image background = new Image(new Texture(Gdx.files.internal("background/menubackground.jpg")));
        background.setSize(w, h);

        stage.addActor(background);

        Image pharah = new Image(new Texture(Gdx.files.internal("images/pharah.png")));
        pharah.setScale(0.6f);
        pharah.setPosition(1030 - pharah.getWidth()/2, 500 - pharah.getHeight()/2);

        stage.addActor(pharah);

        TextButtonStyle textStyle = new TextButtonStyle();
        textStyle.font = game.font;

        TextButton play = new TextButton("Play", textStyle);
        play.setPosition(50, 500 - play.getHeight()/2);

        stage.addActor(play);

        final Image playGradient = new Image(new Texture(Gdx.files.internal("effects/button_gradient.png")));
        playGradient.setScale(0.45f);
        playGradient.setPosition(20, 560 - playGradient.getHeight()/2);
        playGradient.setColor(1, 1, 1, 0);

        stage.addActor(playGradient);

        playGradient.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new HostJoin(game));
                dispose();
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                playGradient.setVisible(true);
                playGradient.addAction(Actions.fadeIn(0.1f));
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                playGradient.addAction(Actions.fadeOut(0.1f));
            }
        });

        TextButton exit = new TextButton("Exit", textStyle);
        exit.setPosition(50, 410 - exit.getHeight()/2);

        stage.addActor(exit);

        final Image exitGradient = new Image(new Texture(Gdx.files.internal("effects/button_gradient.png")));
        exitGradient.setScale(0.45f);
        exitGradient.setPosition(20, 470 - exitGradient.getHeight()/2);
        exitGradient.setColor(1, 1, 1, 0);

        stage.addActor(exitGradient);

        exitGradient.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                exitGradient.setVisible(true);
                exitGradient.addAction(Actions.fadeIn(0.1f));
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                exitGradient.addAction(Actions.fadeOut(0.1f));
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
        menuSound.stop();
    }
}
