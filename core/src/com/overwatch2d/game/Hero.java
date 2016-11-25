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

    private Texture texture = new Texture(Gdx.files.internal("sprites/actor.png"));
    private Body physicsBody;
    private float speed = 4f;
    private float projectileSpawnDistance = 0.30f;
    private float projectileXOffset = 0.25f;
    private int MAX_HEALTH;
    private int currentHP;
    private Texture portrait = new Texture(Gdx.files.internal("portraits/soldier76.png"));
    private Player player;

    private static Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/fire.ogg"));
    private static Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/spawn.ogg"));
    private static Sound respawnSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/respawn.ogg"));
    private static Sound eliminationSound = Gdx.audio.newSound(Gdx.files.internal("sfx/elimination/elimination.mp3"));
    private static Sound hitMarkerSound = Gdx.audio.newSound(Gdx.files.internal("sfx/hit/hitmarker.wav"));

    private boolean isDead = false;

    // @TODO: Port to a weapon class
    private boolean weaponCanFire = true;
    private boolean isReloading = false;
    private float weaponFPS = 10f;
    private float weaponSpread = 20;
    private final int MAX_BULLET_CAPACITY = 25;
    private final float TIME_TO_RELOAD = 1.5f;
    private final int DAMAGE_PER_SHOT = 15;
    private int currentBullets;
    private static Sound reloadSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/reload.mp3"));

    private float timeToRespawn = 0;

    Hero(float initialX, float initialY, Player player) {
        this.MAX_HEALTH = 200;
        this.currentHP = 200;
        this.replenishAmmo();
        this.player = player;

        setSize(texture.getWidth(), texture.getHeight());

        setPosition(initialX, initialY);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / Config.PIXELS_TO_METERS, getY() / Config.PIXELS_TO_METERS);

        physicsBody = GameScreen.world.createBody(bodyDef);

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

        batch.draw(
            texture,
            getX() - texture.getWidth() / 2,
            getY() - texture.getHeight() / 2,
            (float)texture.getWidth() / 2,
            (float)texture.getHeight() / 2,
            (float)texture.getWidth(),
            (float)texture.getHeight(),
            1f,
            1f,
            getRotation(),
            (int)getX(),
            (int)getY(),
            texture.getWidth(),
            texture.getHeight(),
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

    public void firePrimary(float x, float y) {
        if(weaponCanFire && currentBullets > 0 && !isReloading) {
            double angle = Math.atan2(
                y / Config.PIXELS_TO_METERS - physicsBody.getWorldCenter().y,
                x / Config.PIXELS_TO_METERS - physicsBody.getWorldCenter().x
            ) * 180.0d / Math.PI;

            x = physicsBody.getWorldCenter().x * Config.PIXELS_TO_METERS + 5f * (float)Math.cos(Math.toRadians(angle)) * Config.PIXELS_TO_METERS;
            y = physicsBody.getWorldCenter().y * Config.PIXELS_TO_METERS + 5f * (float)Math.sin(Math.toRadians(angle)) * Config.PIXELS_TO_METERS;

            x = x + MathUtils.random(-weaponSpread, weaponSpread);
            y = y + MathUtils.random(-weaponSpread, weaponSpread);

            float initialX = ((physicsBody.getWorldCenter().x + projectileXOffset * (float)Math.cos(Math.toRadians(angle - 45))) + projectileSpawnDistance * (float)Math.cos(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;
            float initialY = ((physicsBody.getWorldCenter().y + projectileXOffset * (float)Math.sin(Math.toRadians(angle - 45))) + projectileSpawnDistance * (float)Math.sin(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;

            GameScreen.projectiles.add(new Projectile(
                initialX,
                initialY,
                x,
                y,
                DAMAGE_PER_SHOT,
                this
            ));

            GameScreen.addParticleGunshot(Gdx.files.internal("particles/gunfire_allied.party"), initialX, initialY, (float)angle, 10f);

            fireSound.play();

            weaponCanFire = false;

            currentBullets--;

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    weaponCanFire = true;
                }
            }, 1f / weaponFPS);

            // reload
            if(currentBullets <= 0) {
                reload();
            }
        }
    }

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

    private void replenishAmmo() {
        this.currentBullets = MAX_BULLET_CAPACITY;
    }

    public void reload() {
        if(currentBullets < MAX_BULLET_CAPACITY && !isReloading) {
            reloadSound.play();
            isReloading = true;

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    replenishAmmo();
                    isReloading = false;
                }
            }, TIME_TO_RELOAD);
        }
    }

    public int getCurrentHealth() {
        return currentHP;
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public int getMaxAmmo() {
        return MAX_BULLET_CAPACITY;
    }

    public int getCurrentAmmo() {
        return currentBullets;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public Texture getPortraitTexture() {
        return portrait;
    }

    public void playSelectedSound() {
        Hero.selectSound.play();
    }

    public void respawn() {
        if(this == GameScreen.getCurrentPlayer().getHero()) {
            GameScreen.resetMovement();

            respawnSound.play();
        }

        float spawnX, spawnY;

        if(getPlayer().getTeam() == 0) {
            spawnX = GameScreen.ATTACKERS_SPAWN_X;
            spawnY = GameScreen.ATTACKERS_SPAWN_Y;
        }
        else {
            spawnX = GameScreen.DEFENDERS_SPAWN_X;
            spawnY = GameScreen.DEFENDERS_SPAWN_Y;
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

        currentBullets = MAX_BULLET_CAPACITY;
        weaponCanFire = true;
        isReloading = false;

        isDead = false;
    }

    public void die(Hero killer) {
        physicsBody.getFixtureList().get(0).setSensor(true);
        physicsBody.getFixtureList().get(0).getFilterData().categoryBits = Config.DEAD_HERO;
        isDead = true;

        if(GameScreen.getCurrentPlayer().getHero() == this) {
            GameScreen.setSepia();

            GameScreen.flashNotification("You have been eliminated by " + killer.getPlayer().getName());
        }
        else if(GameScreen.getCurrentPlayer().getHero() == killer) {
            GameScreen.flashNotification("Eliminated " + getPlayerName());
            eliminationSound.play();
        }

        timeToRespawn = RESPAWN_TIMER;
    }

    public void dispose() {
        GameScreen.heroesDestroyed.add(this);
        this.remove();
    }

    public boolean isDead() {
        return isDead;
    }

    public float getTimeToRespawn() {
        return timeToRespawn;
    }
}
