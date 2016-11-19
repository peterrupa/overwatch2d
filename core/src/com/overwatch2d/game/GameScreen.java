package com.overwatch2d.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;

class GameScreen implements Screen, InputProcessor {
    private final Overwatch2D game;

    private final String PLAYER_NAME = "xxHARAMBE619xx";

    private final int HERO_SELECTION = 0;
    private final int IN_BATTLE = 1;

    private int state;

    static Stage stage;
    static Stage UIStage;
    static Stage selectionStage;
    static OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;

    static ArrayList<Hero> heroes = new ArrayList<Hero>();
    static ArrayList<Hero> heroesDestroyed = new ArrayList<Hero>();
    static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    static ArrayList<Projectile> projectilesDestroyed = new ArrayList<Projectile>();
    private Hero playerHero;
    private Cursor cursor;

    static World world;

    private boolean WHold = false;
    private boolean AHold = false;
    private boolean SHold = false;
    private boolean DHold = false;
    private boolean LeftMouseHold = false;

    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;

    private static ArrayList<ParticleEffect> particles = new ArrayList<ParticleEffect>();
    private static ArrayList<ParticleEffect> particlesDestroyed = new ArrayList<ParticleEffect>();

    private static Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("sfx/hit/hit.mp3"));

    private Label healthLabel;
    private Label ammoCountLabel;
    private Label gunNameLabel;

    private Texture heroPortraitTexture;
    private Sprite heroPortraitSprite;

    private InputMultiplexer inputs;

    GameScreen(final Overwatch2D gam) {
        game = gam;

        float w = Gdx.graphics.getWidth(),
              h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();

        stage = new Stage(new ExtendViewport(w, h, camera));
        UIStage = new Stage();
        selectionStage = new Stage();

        TiledMap tiledMap = new TmxMapLoader().load("sampleMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // create physics world
        world = new World(new Vector2(0, 0), true);

        heroes.add(new Hero(1100, 1100));
        heroes.add(new Hero(1200, 1200));
        heroes.add(new Hero(1300, 1200));

        debugRenderer = new Box2DDebugRenderer();

        Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 position = camera.unproject(mouseCoordinates);

        cursor = new Cursor(position.x, position.y);

        stage.addActor(cursor);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if((contact.getFixtureA().getBody().getUserData() instanceof Projectile &&
                    contact.getFixtureB().getBody().getUserData() instanceof Hero) ||
                   (contact.getFixtureA().getBody().getUserData() instanceof Hero &&
                    contact.getFixtureB().getBody().getUserData() instanceof Projectile)
                ) {
                    hitSound.play();

                    Projectile projectile;
                    Hero hitHero;

                    if(contact.getFixtureA().getBody().getUserData() instanceof Projectile) {
                        projectile = (Projectile) contact.getFixtureA().getBody().getUserData();
                        hitHero = (Hero) contact.getFixtureB().getBody().getUserData();
                    }
                    else {
                        projectile = (Projectile) contact.getFixtureB().getBody().getUserData();
                        hitHero = (Hero) contact.getFixtureA().getBody().getUserData();
                    }

                    if(projectilesDestroyed.indexOf(projectile) < 0) {
                        projectile.hit(hitHero);

                        addParticle(
                            Gdx.files.internal("particles/gunshot_allied.party"),
                            contact.getWorldManifold().getPoints()[0].x * Config.PIXELS_TO_METERS,
                            contact.getWorldManifold().getPoints()[0].y * Config.PIXELS_TO_METERS
                        );
                    }

                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        // UI elements
        initUIElements();

        // Hero selection
        initSelectionStage();

        this.setState(HERO_SELECTION);
    }

    public static void addParticle(FileHandle file, float x, float y) {
        ParticleEffect pe = new ParticleEffect();
        pe.load(file, Gdx.files.internal(""));
        pe.getEmitters().first().setPosition(x, y);
        pe.start();

        particles.add(pe);
    }

    public static void addParticleGunshot(FileHandle file, float x, float y, float angle, float spread) {
        ParticleEffect pe = new ParticleEffect();
        pe.load(file, Gdx.files.internal(""));

        for(ParticleEmitter p: pe.getEmitters()) {
            p.setPosition(x, y);
        }

        pe.getEmitters().first().getAngle().setHigh(angle - spread, angle + spread);
        pe.start();

        particles.add(pe);
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        UIStage.dispose();
        selectionStage.dispose();
    }

    @Override
    public void render(float delta) {
        if(playerHero != null) {
            healthLabel.setText(playerHero.getCurrentHealth() + "/" + playerHero.getMaxHealth());
            ammoCountLabel.setText(playerHero.getCurrentAmmo() + "/" + playerHero.getMaxAmmo());
        }

        if(LeftMouseHold) {
            playerHero.firePrimary();
        }

        for(Projectile p: projectilesDestroyed) {
            world.destroyBody(p.getBody());
            projectiles.remove(p);
        }

        for(Hero b: heroesDestroyed) {
            world.destroyBody(b.getBody());
            heroes.remove(b);
        }

        projectilesDestroyed.clear();

        heroesDestroyed.clear();

        if(playerHero != null) {
            updateSpeed(playerHero);
        }

        world.step(1f/60f, 6, 2);

        for(ParticleEffect pe: particles) {
            pe.update(Gdx.graphics.getDeltaTime());
        }

        for(Hero hero: heroes) {
            hero.setPosition(hero.getBody().getPosition().x * Config.PIXELS_TO_METERS, hero.getBody().getPosition().y * Config.PIXELS_TO_METERS);

            hero.setRotation((float)Math.toDegrees(hero.getBody().getAngle()));
        }

        for(Projectile projectile: projectiles) {
            projectile.setPosition(projectile.getBody().getPosition().x * Config.PIXELS_TO_METERS, projectile.getBody().getPosition().y * Config.PIXELS_TO_METERS);

            projectile.setRotation((float)Math.toDegrees(projectile.getBody().getAngle()));
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(state == HERO_SELECTION) {
            // kunwari base
            camera.position.x = 1100;
            camera.position.y = 1100;
        }
        else if(state == IN_BATTLE) {
            Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            mouseCoordinates = camera.unproject(mouseCoordinates);

            double angle = Math.atan2(
                    mouseCoordinates.y / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().y,
                    mouseCoordinates.x / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().x
            ) * 180.0d / Math.PI;

            camera.position.x = playerHero.getX() + Config.CAMERA_OFFSET * Config.PIXELS_TO_METERS * (float)Math.cos(Math.toRadians(angle));
            camera.position.y = playerHero.getY() + Config.CAMERA_OFFSET * Config.PIXELS_TO_METERS * (float)Math.sin(Math.toRadians(angle));
        }

        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        stage.getViewport().setCamera(camera);

        debugMatrix = stage.getBatch().getProjectionMatrix().cpy().scale(Config.PIXELS_TO_METERS,
                Config.PIXELS_TO_METERS, 0);

        stage.draw();
        stage.getBatch().begin();

        for(ParticleEffect pe: particles) {
            pe.draw(stage.getBatch());

            if (pe.isComplete()) {
                particlesDestroyed.add(pe);
                pe.dispose();
            }
        }

        stage.getBatch().end();

        if(state == HERO_SELECTION) {
            selectionStage.draw();
        }

        if(state == IN_BATTLE) {
            UIStage.draw();

            UIStage.getBatch().begin();
            heroPortraitSprite.draw(UIStage.getBatch());
            UIStage.getBatch().end();
        }

        for(ParticleEffect pe: particlesDestroyed) {
            particles.remove(pe);
        }

        particlesDestroyed.clear();

//        debugRenderer.render(world, debugMatrix);

        mouseMoved(Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public boolean keyUp(int keycode) {
        if(state == IN_BATTLE) {
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

            if(keycode == Input.Keys.H) {
                this.setState(HERO_SELECTION);
                playerHero.dispose();
            }
            if(keycode == Input.Keys.R) {
                playerHero.reload();
            }
        }

        if(keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == IN_BATTLE) {
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
        }

        return false;
    }

    private void setState(int state) {
        this.state = state;

        if(state == IN_BATTLE) {
            Gdx.input.setCursorCatched(true);
            selectionStage.clear();
        }
        else if(state == HERO_SELECTION) {
            Gdx.input.setCursorCatched(false);
            initSelectionStage();
        }
    }

    private void updateSpeed(Hero hero) {
        Body body = hero.getBody();

        // @TODO: Preserve momentum

        if (WHold) {
            body.applyForceToCenter(0f, hero.getSpeed(), true);
        }

        if (SHold) {
            body.applyForceToCenter(0f, -hero.getSpeed(), true);
        }

        if (AHold) {
            body.applyForceToCenter(-hero.getSpeed(), 0f, true);
        }

        if (DHold) {
            body.applyForceToCenter(hero.getSpeed(), 0f, true);
        }

        if(!WHold && !DHold && !AHold && !SHold) {
            body.applyForceToCenter(0, 0f, true);
        }
    }

    @Override
    public void show() {
        inputs = new InputMultiplexer(selectionStage, this);
        Gdx.input.setInputProcessor(inputs);

        if(state == HERO_SELECTION) {
            Gdx.input.setCursorCatched(false);
        }
        if(state == IN_BATTLE) {
            Gdx.input.setCursorCatched(true);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void resume() {
        if(state == HERO_SELECTION) {
            Gdx.input.setCursorCatched(false);
        }
        if(state == IN_BATTLE) {
            Gdx.input.setCursorCatched(true);
        }
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
        if(state == IN_BATTLE) {
            LeftMouseHold = true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(state == IN_BATTLE) {
            LeftMouseHold = false;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(state == IN_BATTLE) {
            Gdx.input.setCursorPosition(Math.max(Math.min(screenX, Gdx.graphics.getWidth()), 0), Math.max(Math.min(screenY, Gdx.graphics.getHeight()), 0));

            Vector3 hoverCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 position = camera.unproject(hoverCoordinates);

            cursor.setPosition(position.x, position.y);

            double degrees = Math.atan2(
                position.y / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().y,
                position.x / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().x
            ) * 180.0d / Math.PI;

            playerHero.getBody().setTransform(playerHero.getBody().getWorldCenter(), (float)Math.toRadians(degrees));
        }

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void initSelectionStage() {
        TextButton.TextButtonStyle okStyle = new TextButton.TextButtonStyle();
        okStyle.font = game.gameSelectionOKFont;

        TextButton okButton = new TextButton("OK", okStyle);
        okButton.setPosition(650, 60);

        float width = 300;
        float height = 100;

        okButton.setBounds(okButton.getX() - width/2, okButton.getY() - height/2, width, height);

        okButton.clearListeners();

        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                setState(IN_BATTLE);
                spawnHero(new Hero(100, 100));
                playerHero.playSelectedSound();
            }
        });

        selectionStage.addActor(okButton);
    }

    private void spawnHero(Hero e) {
        if(e.getPlayerName() == PLAYER_NAME) {
            playerHero = e;

            initPortrait();
        }

        heroes.add(e);
    }

    private void initUIElements() {
        // UI Elements
        Label.LabelStyle healthStyle = new Label.LabelStyle();
        healthStyle.font = game.gameUIHealthFont;

        healthLabel = new Label("", healthStyle);
        healthLabel.setPosition(160, 90);

        UIStage.addActor(healthLabel);

        Label.LabelStyle ammoCountStyle = new Label.LabelStyle();
        ammoCountStyle.font = game.gameUIAmmoCountFont;

        ammoCountLabel = new Label("", ammoCountStyle);
        ammoCountLabel.setPosition(1160, 70);

        UIStage.addActor(ammoCountLabel);

        Label.LabelStyle gunNameStyle = new Label.LabelStyle();
        gunNameStyle.font = game.gameUIGunNameFont;

        gunNameLabel = new Label("Pulse Rifle", gunNameStyle);
        gunNameLabel.setPosition(1160, 100);

        UIStage.addActor(gunNameLabel);
    }

    private void initPortrait() {
        heroPortraitTexture = playerHero.getPortraitTexture();
        heroPortraitSprite = new Sprite(heroPortraitTexture);
        heroPortraitSprite.setPosition(100 - heroPortraitSprite.getWidth()/2, 90 - heroPortraitSprite.getHeight()/2);
        heroPortraitSprite.setScale(0.5f);
    }
}