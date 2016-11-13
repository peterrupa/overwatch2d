package com.overwatch2d.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;

class GameScreen implements Screen, InputProcessor {
    private final Overwatch2D game;

    private Stage stage;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;

    private ArrayList<Hero> heroes = new ArrayList<>();
    private Hero playerHero;

    private World world;

    private boolean WHold = false;
    private boolean AHold = false;
    private boolean SHold = false;
    private boolean DHold = false;

    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;

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

        // create physics world
        world = new World(new Vector2(0, 0), true);

        heroes.add(new Hero(world, 1100, 1100));
        heroes.add(new Hero(world, 1200, 1200));
        heroes.add(new Hero(world, 1300, 1200));

        playerHero = heroes.get(0);

        for(Hero hero: heroes) {
            stage.addActor(hero);
        }

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }

    @Override
    public void render(float delta) {
        updateSpeed(playerHero);

        world.step(1f/60f, 6, 2);

        for(Hero hero: heroes) {
            hero.setPosition(hero.getBody().getPosition().x * Config.PIXELS_TO_METERS, hero.getBody().getPosition().y * Config.PIXELS_TO_METERS);

            hero.setRotation((float)Math.toDegrees(hero.getBody().getAngle()));
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mouseCoordinates = camera.unproject(mouseCoordinates);

        double angle = Math.atan2(
            mouseCoordinates.y / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().y,
            mouseCoordinates.x / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().x
        ) * 180.0d / Math.PI;

        camera.position.x = playerHero.getX() + 1.5f * Config.PIXELS_TO_METERS * (float)Math.cos(Math.toRadians(angle));
        camera.position.y = playerHero.getY() + 1.5f * Config.PIXELS_TO_METERS * (float)Math.sin(Math.toRadians(angle));

        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        stage.getViewport().setCamera(camera);

        debugMatrix = stage.getBatch().getProjectionMatrix().cpy().scale(Config.PIXELS_TO_METERS,
                Config.PIXELS_TO_METERS, 0);

        stage.draw();

        debugRenderer.render(world, debugMatrix);

        mouseMoved(Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.W) {
            WHold = false;
        }
        if(keycode == Input.Keys.S) {
            SHold = false;
        }
        if(keycode == Input.Keys.D) {
            DHold = false;
        }
        if(keycode == Input.Keys.A) {
            AHold = false;
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.W) {
            WHold = true;
        }
        if(keycode == Input.Keys.S) {
            SHold = true;
        }
        if(keycode == Input.Keys.D) {
            DHold = true;
        }
        if(keycode == Input.Keys.A) {
            AHold = true;
        }

        return false;
    }

    private void updateSpeed(Hero hero) {
        Body body = hero.getBody();

        if (WHold && !AHold && !DHold && !SHold) {
            body.setLinearVelocity(0f, hero.getSpeed());
        }
        else if (WHold && DHold && !AHold && !SHold) {
            body.setLinearVelocity(hero.getSpeed(), hero.getSpeed());
        }
        else if (WHold && DHold && !AHold && !SHold) {
            body.setLinearVelocity(hero.getSpeed(), hero.getSpeed());
        }
        else if (!WHold && DHold && !AHold && !SHold) {
            body.setLinearVelocity(hero.getSpeed(), 0);
        }
        else if (!WHold && DHold && !AHold && SHold) {
            body.setLinearVelocity(hero.getSpeed(), -hero.getSpeed());
        }
        else if (!WHold && !DHold && !AHold && SHold) {
            body.setLinearVelocity(0f, -hero.getSpeed());
        }
        else if(!WHold && !DHold && AHold && SHold) {
            body.setLinearVelocity(-hero.getSpeed(), -hero.getSpeed());
        }
        else if(!WHold && !DHold && AHold && !SHold) {
            body.setLinearVelocity(-hero.getSpeed(), 0f);
        }
        else if(WHold && !DHold && AHold && !SHold) {
            body.setLinearVelocity(-hero.getSpeed(), hero.getSpeed());
        }
        else if(!WHold && !DHold && !AHold && !SHold) {
            body.setLinearVelocity(0f, 0f);
        }
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
        Vector3 hoverCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(hoverCoordinates);

        double degrees = Math.atan2(
            position.y / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().y,
            position.x / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().x
        ) * 180.0d / Math.PI;

        playerHero.getBody().setTransform(playerHero.getBody().getWorldCenter(), (float)Math.toRadians(degrees));

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}