package com.overwatch2d.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import java.io.Serializable;

class Hero extends Actor implements Serializable {
    private final float RESPAWN_TIMER = 5f;

    private static Texture texture;
    private static Texture deadTexture;
    private Body physicsBody;
    private float speed;

    private int MAX_HEALTH;
    private int currentHP;
    private static Texture portrait;
    private Player player;

    private Sound selectSound;
    private Sound respawnSound;
    private Sound eliminationSound = Gdx.audio.newSound(Gdx.files.internal("sfx/elimination/elimination.mp3"));
    private Sound hitMarkerSound = Gdx.audio.newSound(Gdx.files.internal("sfx/hit/hitmarker.wav"));

    private boolean isDead = false;

    protected Weapon weapon;

    private float timeToRespawn = 0;

    Hero(float initialX, float initialY, Player player, Texture texture, Texture deadTexture, float speed, int max_health, Texture portrait, Sound selectSound, Sound respawnSound) {
        this.MAX_HEALTH = max_health;
        this.currentHP = max_health;
        this.player = player;
        this.texture = texture;
        this.deadTexture = deadTexture;
        this.speed = speed;
        this.portrait = portrait;
        this.selectSound = selectSound;
        this.respawnSound = respawnSound;

        setSize(texture.getWidth(), texture.getHeight());

        setPosition(initialX, initialY);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / Config.PIXELS_TO_METERS, getY() / Config.PIXELS_TO_METERS);

        physicsBody = GameScreen.getGameState().getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2 / Config.PIXELS_TO_METERS, getHeight()/2 / Config.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        if(getPlayer().getTeam() == 0) {
            fixtureDef.filter.categoryBits = Config.HERO_ENTITY_0;
        }
        else {
            fixtureDef.filter.categoryBits = Config.HERO_ENTITY_1;
        }

        physicsBody.setLinearDamping(5f);
        physicsBody.setAngularDamping(5f);

        physicsBody.createFixture(fixtureDef);
        physicsBody.setUserData(this);

        shape.dispose();

        GameScreen.stage.addActor(this);

        Hero self = this;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                damaged(1000, self);
            }
        }, 3);
    }

    public void setBody(Body body) {
        physicsBody = body;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void draw(Batch batch, float alpha){
        GlyphLayout layout = new GlyphLayout();

        Color c;

        if(isDead) {
            timeToRespawn -= Gdx.graphics.getDeltaTime();

            if(timeToRespawn < 0) {
                respawn();
            }
        }

        if(isDead) {
            c = Color.GRAY;
        }
        else if(getPlayer().getTeam() == GameScreen.getCurrentPlayer().getTeam()) {
            c = new Color(0.14f, 0.7098f, 0.74901f, 1);
        }
        else {
            c = new Color(0.64f, 0.0f, 0.0f, 1);
        }

        layout.setText(Overwatch2D.gamePlayerNameFont, getPlayer().getName(), c, Gdx.graphics.getWidth(), Align.center, false);

        Overwatch2D.gamePlayerNameFont.draw(batch, layout, getX() - Gdx.graphics.getWidth() / 2, getY() + 50);

        batch.end();

        Overwatch2D.shapeRenderer.setAutoShapeType(true);
        Overwatch2D.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        Overwatch2D.shapeRenderer.begin();
        Overwatch2D.shapeRenderer.setColor(c);
        Overwatch2D.shapeRenderer.rect(getX() - 80 / 2, getY() + 60, 80, 10);
        Overwatch2D.shapeRenderer.end();

        if(!isDead()) {
            Overwatch2D.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Overwatch2D.shapeRenderer.setColor(c);
            Overwatch2D.shapeRenderer.rect(getX() - 80 / 2, getY() + 60, Math.max(0, 80f * ((float)this.currentHP/(float)this.MAX_HEALTH)), 10);
            Overwatch2D.shapeRenderer.end();
        }
        else {
            Overwatch2D.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Overwatch2D.shapeRenderer.setColor(c);
            Overwatch2D.shapeRenderer.rect(getX() - 80 / 2, getY() + 60, 80f * (getTimeToRespawn() / RESPAWN_TIMER), 10);
            Overwatch2D.shapeRenderer.end();
        }


        batch.begin();

        Texture drawTexture;

        if(isDead) {
            drawTexture = deadTexture;
        }
        else {
            drawTexture = texture;
        }

        batch.draw(
            drawTexture,
            getX() - drawTexture.getWidth() / 2,
            getY() - drawTexture.getHeight() / 2,
            (float)drawTexture.getWidth() / 2,
            (float)drawTexture.getHeight() / 2,
            (float)drawTexture.getWidth(),
            (float)drawTexture.getHeight(),
            1f,
            1f,
            getRotation(),
            0,
            0,
            drawTexture.getWidth(),
            drawTexture.getHeight(),
            false,
            false
        );
    }

    public Body getBody() {
        return physicsBody;
    }

    public float getSpeed() {
        return speed;
    }

    public Weapon getWeapon() { return weapon; }

    public void damaged(int damage, Hero attacker) {
        currentHP -= damage;

        if(attacker.getPlayer() == GameScreen.getCurrentPlayer()) {
            hitMarkerSound.play();
        }

        if(currentHP <= 0) {
            currentHP = 0;

            die(attacker);
        }
    }

    public int getCurrentHealth() {
        return currentHP;
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public Texture getPortraitTexture() {
        return portrait;
    }

    public void playSelectedSound() {
        selectSound.play();
    }

    public void respawn() {
        if(this == GameScreen.getCurrentPlayer().getHero()) {
            GameScreen.resetMovement();

            respawnSound.play();
        }

        float spawnX, spawnY;

        if(getPlayer().getTeam() == 0) {
            spawnX = GameState.getAttackersSpawnX();
            spawnY = GameState.getAttackersSpawnY();
        }
        else {
            spawnX = GameState.getDefendersSpawnX();
            spawnY = GameState.getDefendersSpawnY();
        }

        physicsBody.setTransform(spawnX / Config.PIXELS_TO_METERS, spawnY / Config.PIXELS_TO_METERS, physicsBody.getAngle());

        physicsBody.getFixtureList().get(0).setSensor(false);

        currentHP = MAX_HEALTH;

        if(this.getPlayer().getTeam() == 0) {
            physicsBody.getFixtureList().get(0).getFilterData().categoryBits = Config.HERO_ENTITY_0;
        }
        else {
            physicsBody.getFixtureList().get(0).getFilterData().categoryBits = Config.HERO_ENTITY_1;
        }

        weapon.restart();

        isDead = false;
    }

    public void die(Hero killer) {
        physicsBody.getFixtureList().get(0).setSensor(true);
        physicsBody.getFixtureList().get(0).getFilterData().categoryBits = Config.DEAD_HERO;
        isDead = true;

        if(GameScreen.getCurrentPlayer().getHero() == this) {
            GameScreen.setSepia();
            GameScreen.getCurrentPlayer().incDeaths();
            GameScreen.updateDeathsLabel("Deaths: " + GameScreen.getCurrentPlayer().getDeaths());
            GameScreen.flashNotification("You have been eliminated by " + killer.getPlayer().getName());

        }
        else if(GameScreen.getCurrentPlayer().getHero() == killer) {
            GameScreen.getCurrentPlayer().incEliminations();
            GameScreen.updateEliminationsLabel("Kills: " + GameScreen.getCurrentPlayer().getEliminations());
            GameScreen.flashNotification("Eliminated " + getPlayerName());
            eliminationSound.play();
        }

        timeToRespawn = RESPAWN_TIMER;
    }

    public void dispose() {
        GameScreen.getGameState().getHeroesDestroyed().add(this);
        this.remove();
    }

    public boolean isDead() {
        return isDead;
    }

    public float getTimeToRespawn() {
        return timeToRespawn;
    }

    public void setCurrentHP(int x) {
        currentHP = x;
    }

    public void setIsDead(boolean x) {
        isDead = x;
    }

    public void setTimeToRespawn(float x) {
        timeToRespawn = x;
    }
}
