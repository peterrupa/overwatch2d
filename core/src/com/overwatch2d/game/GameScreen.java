package com.overwatch2d.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

class GameScreen implements Screen, InputProcessor {
    private final Overwatch2D game;

    private Stage stage;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private TestActor myActor;

    GameScreen(final Overwatch2D gam) {
        game = gam;

        float w = Gdx.graphics.getWidth(),
              h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();

        stage = new Stage(new ExtendViewport(w, h, camera));

        TiledMap tiledMap = new TmxMapLoader().load("sampleMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);

        myActor = new TestActor();

        stage.addActor(myActor);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        stage.getViewport().setCamera(camera);
        stage.draw();
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-10,0);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(10,0);
        if(keycode == Input.Keys.UP)
            camera.translate(0,10);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0,-10);

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
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
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        myActor.setPosition(position.x, position.y);

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
}