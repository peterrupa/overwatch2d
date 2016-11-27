package com.overwatch2d.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.overwatch2d.game.GameScreen.flashObjective;

class GameScreen implements Screen, InputProcessor {
    private static Overwatch2D game = null;

    private static final int HERO_SELECTION = 0;
    private static final int IN_BATTLE = 1;
    private static final int POST_GAME = 2;

    private final float objective1x1 = 600;
    private final float objective1y1 = 1200;
    private final float objective1x2 = 1000;
    private final float objective1y2 = 1400;

    private final float objective2x1 = 1200;
    private final float objective2y1 = 600;
    private final float objective2x2 = 1400;
    private final float objective2y2 = 800;

    private static final float NOTIFICATION_DURATION = 3f;

    static Stage stage;
    static Stage UIStage;
    static Stage PostStage;
    static Stage selectionStage;
    static OrthographicCamera camera;
    static OrthogonalTiledMapRenderer tiledMapRenderer;

    private static Hero playerHero;
    private Cursor cursor;

    private static boolean WHold = false;
    private static boolean AHold = false;
    private static boolean SHold = false;
    private static boolean DHold = false;
    private static boolean LeftMouseHold = false;

    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;

    private static ArrayList<ParticleEffect> particles = new ArrayList<ParticleEffect>();
    private static ArrayList<ParticleEffect> particlesDestroyed = new ArrayList<ParticleEffect>();

    private static Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("sfx/hit/hit.mp3"));
    private static Sound initSound = Gdx.audio.newSound(Gdx.files.internal("sfx/music/start.mp3"));

    private Label healthLabel;
    private Label ammoCountLabel;
    private Label gunNameLabel;
    private static Label countdownLabel;
    private static Label flashLabel;
    private Label victoryLabel;
    private Label defeatLabel;
    private Label battleCountdownLabel;
    private static Label objectiveLabel;

    private static float objectiveFlashTTL;

    private static Texture heroPortraitTexture;
    private static Sprite heroPortraitSprite;

    private static TextButton okButton;

    private InputMultiplexer inputs;

    private static Player currentPlayer;

    private static final float OBJECTIVE_FLASH_TIME = 7f;

    private final Sound victoryAnnouncerSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/victory.mp3"));
    private final Sound defeatAnnouncerSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/defeat.mp3"));
    private final Sound victoryMusicSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/music/victory.mp3"));
    private final Sound defeatMusicSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/music/defeat.mp3"));

    private final Sound captureTheObjectiveSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/capture_the_objective.mp3"));
    private final Sound defendTheObjectiveSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/defend_the_objective.mp3"));
    private final Sound objectiveCapturedSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/objective_captured.mp3"));
    private final Sound objectiveLostSFX = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/objective_lost.mp3"));

    private final Sound announce10 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/10.mp3"));
    private boolean announce10Flag = false;
    private final Sound announce9 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/9.mp3"));
    private boolean announce9Flag = false;
    private final Sound announce8 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/8.mp3"));
    private boolean announce8Flag = false;
    private final Sound announce7 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/7.mp3"));
    private boolean announce7Flag = false;
    private final Sound announce6 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/6.mp3"));
    private boolean announce6Flag = false;
    private final Sound announce5 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/5.mp3"));
    private boolean announce5Flag = false;
    private final Sound announce4 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/4.mp3"));
    private boolean announce4Flag = false;
    private final Sound announce3 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/3.mp3"));
    private boolean announce3Flag = false;
    private final Sound announce2 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/2.mp3"));
    private boolean announce2Flag = false;
    private final Sound announce1 = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/1.mp3"));
    private boolean announce1Flag = false;

    private final Sound announce60Remaining = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/60_remaining.mp3"));
    private boolean announce60RemainingFlag = false;
    private final Sound announce30Remaining = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/30_remaining.mp3"));
    private boolean announce30RemainingFlag = false;
    private final Sound announce10Remaining = Gdx.audio.newSound(Gdx.files.internal("sfx/announcer/10_remaining.mp3"));
    private boolean announce10RemainingFlag = false;

    private static float flashNotificationTTL = 0;
    private static String flashNotificationMessage = "";

    private boolean isAltTabbed = false;

    private static GameState gameState;

    GameScreen(final Overwatch2D gam, ArrayList<Player> players, String name) {
        game = gam;
        GameScreen.gameState = new GameState(players);

        try {
            for(Player p: players) {
                if(p.getName().equals(name)) {
                    currentPlayer = p;

                    break;
                }
            }
        }
        catch(Exception e) {

        }

        float w = Gdx.graphics.getWidth(),
              h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();

        stage = new Stage(new ExtendViewport(w, h, camera));
        UIStage = new Stage();
        PostStage = new Stage();
        selectionStage = new Stage();

        TiledMap tiledMap = new TmxMapLoader().load("map/sampleMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // create physics world
        gameState.setWorld(new World(new Vector2(0, 0), true));

        debugRenderer = new Box2DDebugRenderer();

        Vector3 mouseCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 position = camera.unproject(mouseCoordinates);

        cursor = new Cursor(position.x, position.y);

        stage.addActor(cursor);

        gameState.getWorld().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if((contact.getFixtureA().getBody().getUserData() instanceof Integer &&
                   (Integer)contact.getFixtureA().getBody().getUserData() == 1 &&
                   contact.getFixtureB().getBody().getUserData() instanceof Hero) ||
                   (contact.getFixtureB().getBody().getUserData() instanceof Integer &&
                   contact.getFixtureA().getBody().getUserData() instanceof Hero &&
                   (Integer)contact.getFixtureB().getBody().getUserData() == 1) &&
                    gameState.getCurrentObjective() == 1) {

                    Hero h;

                    if(contact.getFixtureA().getBody().getUserData() instanceof Hero) {
                        h = (Hero) contact.getFixtureA().getBody().getUserData();
                    }
                    else {
                        h = (Hero) contact.getFixtureB().getBody().getUserData();
                    }

                    gameState.getObjective1Heroes().add(h);
                }

                if((contact.getFixtureA().getBody().getUserData() instanceof Integer &&
                  (Integer)contact.getFixtureA().getBody().getUserData() == 2 &&
                   contact.getFixtureB().getBody().getUserData() instanceof Hero) ||
                  (contact.getFixtureB().getBody().getUserData() instanceof Integer &&
                   contact.getFixtureA().getBody().getUserData() instanceof Hero &&
                  (Integer)contact.getFixtureB().getBody().getUserData() == 2) &&
                    gameState.getCurrentObjective() == 2) {

                    Hero h;

                    if(contact.getFixtureA().getBody().getUserData() instanceof Hero) {
                        h = (Hero) contact.getFixtureA().getBody().getUserData();
                    }
                    else {
                        h = (Hero) contact.getFixtureB().getBody().getUserData();
                    }

                    gameState.getObjective2Heroes().add(h);
                }

                if((contact.getFixtureA().getBody().getUserData() instanceof Projectile &&
                    contact.getFixtureB().getBody().getUserData() instanceof Hero) ||
                   (contact.getFixtureA().getBody().getUserData() instanceof Hero &&
                    contact.getFixtureB().getBody().getUserData() instanceof Projectile)
                ) {
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

                    if(gameState.getProjectilesDestroyed().indexOf(projectile) < 0 && !hitHero.isDead()) {
                        projectile.hit(hitHero);
                        hitSound.play();

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
                if((contact.getFixtureA().getBody().getUserData() instanceof Integer &&
                   (Integer)contact.getFixtureA().getBody().getUserData() == 1 &&
                    contact.getFixtureB().getBody().getUserData() instanceof Hero) ||
                   (contact.getFixtureB().getBody().getUserData() instanceof Integer &&
                    contact.getFixtureA().getBody().getUserData() instanceof Hero &&
                   (Integer)contact.getFixtureB().getBody().getUserData() == 1) &&
                    gameState.getCurrentObjective() == 1) {

                    Hero h;

                    if(contact.getFixtureA().getBody().getUserData() instanceof Hero) {
                        h = (Hero) contact.getFixtureA().getBody().getUserData();
                    }
                    else {
                        h = (Hero) contact.getFixtureB().getBody().getUserData();
                    }

                    gameState.getObjective1Heroes().remove(h);
                }

                if((contact.getFixtureA().getBody().getUserData() instanceof Integer &&
                    (Integer)contact.getFixtureA().getBody().getUserData() == 2 &&
                    contact.getFixtureB().getBody().getUserData() instanceof Hero) ||
                   (contact.getFixtureB().getBody().getUserData() instanceof Integer &&
                    contact.getFixtureA().getBody().getUserData() instanceof Hero &&
                   (Integer)contact.getFixtureB().getBody().getUserData() == 2) &&
                    gameState.getCurrentObjective() == 2) {

                    Hero h;

                    if(contact.getFixtureA().getBody().getUserData() instanceof Hero) {
                        h = (Hero) contact.getFixtureA().getBody().getUserData();
                    }
                    else {
                        h = (Hero) contact.getFixtureB().getBody().getUserData();
                    }

                    gameState.getObjective2Heroes().remove(h);
                }
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

        initSound.play();

        createObjective(objective1x1, objective1y1, objective1x2, objective1y2, 1);
        createObjective(objective2x1, objective2y1, objective2x2, objective2y2, 2);
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
        gameState.getWorld().dispose();
        UIStage.dispose();
        PostStage.dispose();
        selectionStage.dispose();
    }

    @Override
    public void render(float delta) {
        if(!gameState.isBattleHasStarted()) {
            gameState.setPreparationDuration(gameState.getPreparationDuration() - Gdx.graphics.getDeltaTime());

            int time = (int)Math.ceil(gameState.getPreparationDuration());

            battleCountdownLabel.setText(toTime(time));

            if(time == 10 && !announce10Flag) {
                announce10.play();

                announce10Flag = true;
            }
            if(time == 9 && !announce9Flag) {
                announce9.play();

                announce9Flag = true;
            }
            if(time == 8 && !announce8Flag) {
                announce8.play();

                announce8Flag = true;
            }
            if(time == 7 && !announce7Flag) {
                announce7.play();

                announce7Flag = true;
            }
            if(time == 6 && !announce6Flag) {
                announce6.play();

                announce6Flag = true;
            }
            if(time == 5 && !announce5Flag) {
                announce5.play();

                announce5Flag = true;
            }
            if(time == 4 && !announce4Flag) {
                announce4.play();

                announce4Flag = true;
            }
            if(time == 3 && !announce3Flag) {
                announce3.play();

                announce3Flag = true;
            }
            if(time == 2 && !announce2Flag) {
                announce2.play();

                announce2Flag = true;
            }
            if(time == 1 && !announce1Flag) {
                announce1.play();

                announce1Flag = true;
            }

            if(gameState.getPreparationDuration() < 0) {
                gameState.setBattleHasStarted(true);

                setObjective(1);

                if(currentPlayer.getTeam() == 0) {
                    captureTheObjectiveSFX.play();
                }
                else {
                    defendTheObjectiveSFX.play();
                }

                // @TODO: remove base limits
            }
        }

        if(gameState.getState() == IN_BATTLE) {
            if(gameState.getCurrentObjective() == 1) {
                if(getNumberOfHeroesTeam(gameState.getObjective1Heroes(), 0) > 0 && getNumberOfHeroesTeam(gameState.getObjective1Heroes(), 1) == 0) {
                    gameState.setObjective1Capture(gameState.getObjective1Capture() + Gdx.graphics.getDeltaTime() * gameState.getObjective1Heroes().size() * Config.CAPPING_MODIFIER);

                    if(gameState.getObjective1Capture() > 100) {
                        setObjective(2);

                        announce60RemainingFlag = false;
                        announce30RemainingFlag = false;
                        announce10RemainingFlag = false;

                        if(currentPlayer.getTeam() == 0) {
                            // captured
                            objectiveCapturedSFX.play();

                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    captureTheObjectiveSFX.play();
                                }
                            }, 1.8f);
                        }
                        else {
                            // lost
                            objectiveLostSFX.play();

                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    defendTheObjectiveSFX.play();
                                }
                            }, 1.8f);
                        }
                    }
                }

                else if(getNumberOfHeroesTeam(gameState.getObjective1Heroes(), 0) > 0 && getNumberOfHeroesTeam(gameState.getObjective1Heroes(), 1) > 0) {
                    // contested
                }

                else {
                    gameState.setObjective1Capture(gameState.getObjective1Capture() - Gdx.graphics.getDeltaTime() / 2 * Config.CAPPING_MODIFIER);

                    if(gameState.getObjective1Capture() < 0) {
                        gameState.setObjective1Capture(0);
                    }
                }
            }
            else if(gameState.getCurrentObjective() == 2) {
                if(getNumberOfHeroesTeam(gameState.getObjective2Heroes(), 0) > 0 && getNumberOfHeroesTeam(gameState.getObjective2Heroes(), 1) == 0) {
                    gameState.setObjective2Capture(gameState.getObjective2Capture() + Gdx.graphics.getDeltaTime() * gameState.getObjective2Heroes().size() * Config.CAPPING_MODIFIER);

                    if(gameState.getObjective2Capture() > 100) {
                        if(currentPlayer.getTeam() == 0) {
                            victory();
                        }
                        else {
                            defeat();
                        }
                    }
                }

                else if(getNumberOfHeroesTeam(gameState.getObjective2Heroes(), 0) > 0 && getNumberOfHeroesTeam(gameState.getObjective2Heroes(), 1) > 0) {
                    // contested
                }

                else {
                    gameState.setObjective2Capture(gameState.getObjective2Capture() - Gdx.graphics.getDeltaTime() / 2 * Config.CAPPING_MODIFIER);

                    if(gameState.getObjective2Capture() < 0) {
                        gameState.setObjective2Capture(0);
                    }
                }
            }
        }

        if(gameState.isBattleHasStarted() && gameState.getState() == IN_BATTLE) {
            gameState.setGameTimer(gameState.getGameTimer() - Gdx.graphics.getDeltaTime());

            int time = (int)Math.ceil(gameState.getGameTimer());

            battleCountdownLabel.setText(toTime(time));

            if(time == 60 && !announce60RemainingFlag) {
                announce60Remaining.play();
                announce60RemainingFlag = true;
            }
            if(time == 30 && !announce30RemainingFlag) {
                announce30Remaining.play();
                announce30RemainingFlag = true;
            }
            if(time == 10 && !announce10RemainingFlag) {
                announce10Remaining.play();
                announce10RemainingFlag = true;
            }

            if(gameState.getGameTimer() <= 0) {
                if(currentPlayer.getTeam() == 0) {
                    defeat();
                }
                else {
                    victory();
                }
            }
        }

        objectiveFlashTTL -= Gdx.graphics.getDeltaTime();
        flashNotificationTTL -= Gdx.graphics.getDeltaTime();

        if(objectiveFlashTTL < 0) {
            flashLabel.setText("");
        }

        if(!gameState.isHeroSelectionFinished()) {
            gameState.setCountdown(gameState.getCountdown() - Gdx.graphics.getDeltaTime());

            if(gameState.getCountdown() < 1) {
                gameState.setHeroSelectionFinished(true);

                selectionStage.addActor(okButton);
                countdownLabel.remove();
            }
            else {
                countdownLabel.setText("Assemble your team: " + (int)Math.floor(gameState.getCountdown()));
            }
        }

        if(playerHero != null) {
            healthLabel.setText(playerHero.getCurrentHealth() + "/" + playerHero.getMaxHealth());
            ammoCountLabel.setText(playerHero.getCurrentAmmo() + "/" + playerHero.getMaxAmmo());
        }

        if(LeftMouseHold && !playerHero.isDead()) {
            Vector3 hoverCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 position = GameScreen.camera.unproject(hoverCoordinates);
            playerHero.firePrimary(position.x, position.y);
        }
        else {
            LeftMouseHold = false;
        }

        for(Projectile p: gameState.getProjectilesDestroyed()) {
            gameState.getWorld().destroyBody(p.getBody());
            gameState.getProjectiles().remove(p);
        }

        for(Hero b: gameState.getHeroesDestroyed()) {
            gameState.getWorld().destroyBody(b.getBody());
            gameState.getHeroes().remove(b);
        }

        gameState.getProjectilesDestroyed().clear();
        gameState.getHeroesDestroyed().clear();

        if(playerHero != null && !playerHero.isDead()) {
            updateSpeed(playerHero);
        }

        if(gameState.isSlowmo()) {
            gameState.getWorld().step(1f/240f, 6, 2);
        }
        else {
            gameState.getWorld().step(1f/60f, 6, 2);
        }

        for(ParticleEffect pe: particles) {
            pe.update(Gdx.graphics.getDeltaTime());
        }

        for(Hero hero: gameState.getHeroes()) {
            hero.setPosition(hero.getBody().getPosition().x * Config.PIXELS_TO_METERS, hero.getBody().getPosition().y * Config.PIXELS_TO_METERS);

            hero.setRotation((float)Math.toDegrees(hero.getBody().getAngle()));
        }

        for(Projectile projectile: gameState.getProjectiles()) {
            projectile.setPosition(projectile.getBody().getPosition().x * Config.PIXELS_TO_METERS, projectile.getBody().getPosition().y * Config.PIXELS_TO_METERS);

            projectile.setRotation((float)Math.toDegrees(projectile.getBody().getAngle()));
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(gameState.getState() == HERO_SELECTION) {
            // kunwari base
            camera.position.x = 1100;
            camera.position.y = 1100;
        }
        else if(gameState.getState() == IN_BATTLE || gameState.getState() == POST_GAME) {
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

        renderObjective(1);
        renderObjective(2);

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

        if(gameState.getState() == HERO_SELECTION) {
            selectionStage.draw();
        }

        if(gameState.getState() == IN_BATTLE) {
            UIStage.draw();

            if(flashNotificationTTL > 0) {
                GlyphLayout layout = new GlyphLayout();

                layout.setText(Overwatch2D.gameUIFlashNotificationFont, flashNotificationMessage, Color.WHITE, Gdx.graphics.getWidth(), Align.center, false);

                UIStage.getBatch().begin();

                Overwatch2D.gameUIFlashNotificationFont.draw(UIStage.getBatch(), layout, 0, Gdx.graphics.getHeight()/2 - 150);
                UIStage.getBatch().end();
            }

            UIStage.getBatch().begin();

            heroPortraitSprite.draw(UIStage.getBatch());

            UIStage.getBatch().end();
        }

        if(gameState.getState() == POST_GAME) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            Overwatch2D.shapeRenderer.setAutoShapeType(true);
            Overwatch2D.shapeRenderer.setProjectionMatrix(PostStage.getBatch().getProjectionMatrix());

            Overwatch2D.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Overwatch2D.shapeRenderer.setColor(0f, 0f, 0f, 0.65f);
            Overwatch2D.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Overwatch2D.shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);

            PostStage.draw();
        }

        for(ParticleEffect pe: particlesDestroyed) {
            particles.remove(pe);
        }

        particlesDestroyed.clear();

        debugRenderer.render(gameState.getWorld(), debugMatrix);

        mouseMoved(Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public boolean keyUp(int keycode) {
        if((gameState.getState() == IN_BATTLE || gameState.getState() == POST_GAME) && !playerHero.isDead()) {
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
        if((gameState.getState() == IN_BATTLE || gameState.getState() == POST_GAME) && !playerHero.isDead()) {
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

    private static void setState(int state) {
        gameState.setState(state);

        if(state == IN_BATTLE) {
            Gdx.input.setCursorCatched(true);
            selectionStage.clear();
        }
        else if(state == POST_GAME) {
            gameState.setSlowmo(true);
        }
        else if(state == HERO_SELECTION) {
            Gdx.input.setCursorCatched(false);
            initSelectionStage();
        }
    }

    private void updateSpeed(Hero hero) {
        Body body = hero.getBody();

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

        if(gameState.getState() == HERO_SELECTION) {
            Gdx.input.setCursorCatched(false);
        }
        if(gameState.getState() == IN_BATTLE || gameState.getState() == POST_GAME) {
            Gdx.input.setCursorCatched(true);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        Gdx.input.setCursorCatched(false);

        isAltTabbed = true;
    }

    @Override
    public void resume() {
        if(gameState.getState() == HERO_SELECTION) {
            Gdx.input.setCursorCatched(false);
        }
        if(gameState.getState() == IN_BATTLE || gameState.getState() == POST_GAME) {
            Gdx.input.setCursorCatched(true);
        }

        isAltTabbed = false;
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
        if(gameState.getState() == IN_BATTLE || gameState.getState() == POST_GAME) {
            if(!playerHero.isDead()) {
                LeftMouseHold = true;
            }
        }


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(gameState.getState() == IN_BATTLE || gameState.getState() == POST_GAME) {
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
        if(gameState.getState() == IN_BATTLE || gameState.getState() == POST_GAME) {
            if(!isAltTabbed) {
                Gdx.input.setCursorPosition(Math.max(Math.min(screenX, Gdx.graphics.getWidth()), 0), Math.max(Math.min(screenY, Gdx.graphics.getHeight()), 0));
            }

            Vector3 hoverCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 position = camera.unproject(hoverCoordinates);

            cursor.setPosition(position.x, position.y);

            double degrees = Math.atan2(
                position.y / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().y,
                position.x / Config.PIXELS_TO_METERS - playerHero.getBody().getWorldCenter().x
            ) * 180.0d / Math.PI;

            if(!playerHero.isDead()) {
                playerHero.getBody().setTransform(playerHero.getBody().getWorldCenter(), (float)Math.toRadians(degrees));
            }
        }

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private static void initSelectionStage() {
        TextButton.TextButtonStyle okStyle = new TextButton.TextButtonStyle();
        okStyle.font = game.gameSelectionOKFont;

        okButton = new TextButton("OK", okStyle);
        okButton.setPosition(Gdx.graphics.getWidth()/2, 60);

        okButton.clearListeners();

        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if(gameState.getState() == HERO_SELECTION) {
                    NetworkHelper.clientSend(new Packet("HERO_SPAWN", new HeroSpawnPacket(Overwatch2D.getName())), NetworkHelper.getHost());
                }
            }
        });

        if(gameState.isHeroSelectionFinished()) {
            selectionStage.addActor(okButton);
        }

        Label.LabelStyle selectHeroStyle = new Label.LabelStyle();
        selectHeroStyle.font = game.font;

        Label selectHeroLabel = new Label("Select your Hero", selectHeroStyle);
        selectHeroLabel.setPosition(40, 650);

        selectionStage.addActor(selectHeroLabel);

        Label.LabelStyle countdownStyle = new Label.LabelStyle();
        countdownStyle.font = game.gameSelectionCountdownFont;

        countdownLabel = new Label("", countdownStyle);
        countdownLabel.setPosition(Gdx.graphics.getWidth()/2 - 70, 100);

        selectionStage.addActor(countdownLabel);
    }

    private static void spawnHero(Hero e) {
        if(e.getPlayerName() == currentPlayer.getName()) {
            playerHero = e;

            initPortrait();
        }

        gameState.getHeroes().add(e);
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

        Label.LabelStyle flashStyle = new Label.LabelStyle();
        flashStyle.font = game.gameUIFlashFont;

        flashLabel = new Label("", flashStyle);
        flashLabel.setPosition(Gdx.graphics.getWidth() / 2 - 100, 600);

        UIStage.addActor(flashLabel);

        Label.LabelStyle flashNotificationStyle = new Label.LabelStyle();
        flashNotificationStyle.font = game.gameUIFlashFont;

        Label.LabelStyle battleCountdownStyle = new Label.LabelStyle();
        battleCountdownStyle.font = game.gameUITimerFont;

        battleCountdownLabel = new Label("", battleCountdownStyle);
        battleCountdownLabel.setPosition(Gdx.graphics.getWidth() / 2 - 20, 700);

        UIStage.addActor(battleCountdownLabel);

        Label.LabelStyle objectiveStyle = new Label.LabelStyle();
        objectiveStyle.font = game.gameUITimerFont;

        objectiveLabel = new Label("", objectiveStyle);
        objectiveLabel.setPosition(Gdx.graphics.getWidth() / 2 + 30, 700);

        if(currentPlayer.getTeam() == 0) {
            objectiveLabel.setText("Prepare to attack");
        }
        else {
            objectiveLabel.setText("Prepare your defenses");
        }

        UIStage.addActor(objectiveLabel);
    }

    private static void initPortrait() {
        heroPortraitTexture = playerHero.getPortraitTexture();
        heroPortraitSprite = new Sprite(heroPortraitTexture);
        heroPortraitSprite.setPosition(100 - heroPortraitSprite.getWidth()/2, 90 - heroPortraitSprite.getHeight()/2);
        heroPortraitSprite.setScale(0.5f);
    }

    private void createObjective(float x1, float y1, float x2, float y2, int id) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((x1 + x2) / 2 / Config.PIXELS_TO_METERS, (y1 + y2) / 2 / Config.PIXELS_TO_METERS);

        Body physicsBody = GameScreen.getGameState().getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((x2 - x1) / 2 / Config.PIXELS_TO_METERS, (y2 - y1) / 2 / Config.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = Config.OBJECTIVE_ENTITY;
        fixtureDef.filter.maskBits = Config.HERO_ENTITY_0 | Config.HERO_ENTITY_1;
        fixtureDef.isSensor = true;

        physicsBody.createFixture(fixtureDef);
        physicsBody.setUserData(id);

        shape.dispose();
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void flashObjective(String msg) {
        flashLabel.setText(msg);
        objectiveLabel.setText(msg);

        objectiveFlashTTL = OBJECTIVE_FLASH_TIME;
    }

    private void setObjective(int obj) {
        gameState.setCurrentObjective(obj);

        if(obj == 1) {
            gameState.setObjective1Capture(0);
        }
        else if(obj == 2) {
            gameState.setObjective2Capture(0);
        }

        gameState.setGameTimer(gameState.getGameTimer() + gameState.getBattleDuration());

        if(currentPlayer.getTeam() == 0) {
            flashObjective("Capture Objective " + Character.toString((char) (64 + gameState.getCurrentObjective())));
        }
        else {
            flashObjective("Defend Objective " + Character.toString((char) (64 + gameState.getCurrentObjective())));
        }
    }

    private int getNumberOfHeroesTeam(ArrayList<Hero> arr, int team) {
        int count = 0;

        for(Hero h: arr) {
            if(h.getPlayer().getTeam() == team && !h.isDead()) count++;
        }

        return count;
    }

    private void renderObjective(int obj) {
        Color c;

        if(obj >= gameState.getCurrentObjective()) {
            c = new Color(0.64f, 0.0f, 0.0f, 1);
        }
        else {
            c = new Color(0.14f, 0.7098f, 0.74901f, 1);
        }

        float objectivex1;
        float objectivex2;
        float objectivey2;
        float objectiveCapture;

        if(obj == 1) {
            objectivex1 = objective1x1;
            objectivex2 = objective1x2;
            objectivey2 = objective1y2;
            objectiveCapture = gameState.getObjective1Capture();
        }
        else {
            objectivex1 = objective2x1;
            objectivex2 = objective2x2;
            objectivey2 = objective2y2;
            objectiveCapture = gameState.getObjective2Capture();
        }

        GlyphLayout layout = new GlyphLayout();

        layout.setText(Overwatch2D.gameObjectiveLabelFont, Character.toString((char) (64 + obj)), c, (objectivex2 - objectivex1), Align.center, false);

        stage.getBatch().begin();

        Overwatch2D.gameObjectiveLabelFont.draw(stage.getBatch(), layout, objectivex1, objectivey2 - 60);

        stage.getBatch().end();

        Overwatch2D.shapeRenderer.setAutoShapeType(true);
        Overwatch2D.shapeRenderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());

        Overwatch2D.shapeRenderer.begin();
        Overwatch2D.shapeRenderer.setColor(c);
        Overwatch2D.shapeRenderer.rect(objectivex1 + (objectivex2 - objectivex1) / 2 - 120/2, objectivey2 - 130, 120, 20);
        Overwatch2D.shapeRenderer.end();

        Overwatch2D.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Overwatch2D.shapeRenderer.setColor(c);
        Overwatch2D.shapeRenderer.rect(objectivex1 + (objectivex2 - objectivex1) / 2 - 120/2, objectivey2 - 130, 120 * (objectiveCapture / 100), 20);
        Overwatch2D.shapeRenderer.end();
    }

    public static void setSepia() {
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(
            "attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
            "attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
            "attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +

            "uniform mat4 u_projTrans;\n" +
            " \n" +
            "varying vec4 vColor;\n" +
            "varying vec2 vTexCoord;\n" +

            "void main() {\n" +
            "	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
            "	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
            "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
            "}",

            "#ifdef GL_ES\n" //
            + "#define LOWP lowp\n" //
            + "precision mediump float;\n" //
            + "#else\n" //
            + "#define LOWP \n" //
            + "#endif\n" + //
            "//texture 0\n" +
            "uniform sampler2D u_texture;\n" +
            "\n" +
            "//our screen resolution, set from Java whenever the display is resized\n" +
            "uniform vec2 resolution;\n" +
            "\n" +
            "//\"in\" attributes from our vertex shader\n" +
            "varying LOWP vec4 vColor;\n" +
            "varying vec2 vTexCoord;\n" +
            "\n" +
            "//RADIUS of our vignette, where 0.5 results in a circle fitting the screen\n" +
            "const float RADIUS = 0.75;\n" +
            "\n" +
            "//softness of our vignette, between 0.0 and 1.0\n" +
            "const float SOFTNESS = 0.45;\n" +
            "\n" +
            "//sepia colour, adjust to taste\n" +
            "const vec3 SEPIA = vec3(1.2, 1.0, 0.8); \n" +
            "\n" +
            "void main() {\n" +
            "	//sample our texture\n" +
            "	vec4 texColor = texture2D(u_texture, vTexCoord);\n" +
            "		\n" +
            "	//1. VIGNETTE\n" +
            "	\n" +
            "	//determine center position\n" +
            "	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);\n" +
            "	\n" +
            "	//determine the vector length of the center position\n" +
            "	float len = length(position);\n" +
            "	\n" +
            "	//use smoothstep to create a smooth vignette\n" +
            "	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);\n" +
            "	\n" +
            "	//apply the vignette with 50% opacity\n" +
            "	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);\n" +
            "		\n" +
            "	//2. GRAYSCALE\n" +
            "	\n" +
            "	//convert to grayscale using NTSC conversion weights\n" +
            "	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n" +
            "	\n" +
            "	//3. SEPIA\n" +
            "	\n" +
            "	//create our sepia tone from some constant value\n" +
            "	vec3 sepiaColor = vec3(gray) * SEPIA;\n" +
            "		\n" +
            "	//again we'll use mix so that the sepia effect is at 75%\n" +
            "	texColor.rgb = mix(texColor.rgb, sepiaColor, 0.75);\n" +
            "		\n" +
            "	//final colour, multiplied by vertex colour\n" +
            "	gl_FragColor = texColor * vColor;\n" +
            "}"
        );

        stage.getBatch().setShader(shader);
        tiledMapRenderer.getBatch().setShader(shader);
    }

    private String toTime(int n) {
        int minutes = n / 60;
        int seconds = n % 60;

        return trailZero(minutes) + ":" +trailZero(seconds);
    }

    private String trailZero(int n) {
        if(n < 10) return "0" + n;

        return Integer.toString(n);
    }

    private void victory() {
        setState(POST_GAME);

        Label.LabelStyle victoryStyle = new Label.LabelStyle();
        victoryStyle.font = Overwatch2D.gamePostgamefont;
        victoryStyle.fontColor = Color.YELLOW;

        victoryLabel = new Label("VICTORY", victoryStyle);
        victoryLabel.setPosition(500, Gdx.graphics.getHeight()/2 - 75);

        victoryMusicSFX.play();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                PostStage.addActor(victoryLabel);
                victoryAnnouncerSFX.play(3f);
            }
        }, 2f);
    }

    private void defeat() {
        setState(POST_GAME);

        final Label.LabelStyle defeatStyle = new Label.LabelStyle();
        defeatStyle.font = Overwatch2D.gamePostgamefont;
        defeatStyle.fontColor = Color.RED;

        defeatLabel = new Label("DEFEAT", defeatStyle);
        defeatLabel.setPosition(500, Gdx.graphics.getHeight()/2 - 75);

        defeatMusicSFX.play();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                PostStage.addActor(defeatLabel);

                defeatAnnouncerSFX.play();
            }
        }, 2f);
    }

    public static void flashNotification(String message) {
        flashNotificationTTL = NOTIFICATION_DURATION;
        flashNotificationMessage = message;
    }

    public static void resetMovement() {
        WHold = false;
        AHold = false;
        SHold = false;
        DHold = false;
        LeftMouseHold = false;

        stage.getBatch().setShader(null);
        tiledMapRenderer.getBatch().setShader(null);
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void spawnPlayer(String playername) {
        float spawnX, spawnY;

        Player player = gameState.getPlayers().stream().filter(p -> p.getName().equals(playername)).collect(Collectors.toList()).get(0);

        if(player.getTeam() == 0) {
            spawnX = gameState.getAttackersSpawnX();
            spawnY = gameState.getAttackersSpawnY();
        }
        else {
            spawnX = gameState.getDefendersSpawnX();
            spawnY = gameState.getDefendersSpawnY();
        }

        Hero h = new Hero(spawnX, spawnY, player);

        spawnHero(h);

        if(playername.equals(currentPlayer.getName())) {
            setState(IN_BATTLE);

            currentPlayer.setHero(h);

            playerHero.playSelectedSound();
        }
    }
}