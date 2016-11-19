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

class Hero extends Actor {
    private Texture texture = new Texture(Gdx.files.internal("actor.png"));
    private Body physicsBody;
    private float speed = 4f;
    private float projectileSpawnDistance = 0.30f;
    private float projectileXOffset = 0.25f;
    private String name = "xxHARAMBE619xx";
    private int MAX_HEALTH;
    private int currentHP;
    private Texture portrait = new Texture(Gdx.files.internal("portraits/soldier76.png"));

    private static Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/fire.ogg"));
    private static Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/spawn.ogg"));

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

    Hero(float initialX, float initialY) {
        this.MAX_HEALTH = 200;
        this.currentHP = 200;
        this.replenishAmmo();

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
        fixtureDef.filter.categoryBits = Config.HERO_ENTITY;
        fixtureDef.filter.maskBits = Config.HERO_ENTITY | Config.PROJECTILE_ENTITY;

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

    @Override
    public void draw(Batch batch, float alpha){
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Overwatch2D.gamePlayerNameFont, name, new Color(0.14f, 0.7098f, 0.74901f, 1), Gdx.graphics.getWidth(), Align.center, false);

        Overwatch2D.gamePlayerNameFont.draw(batch, layout, getX() - Gdx.graphics.getWidth() / 2, getY() + 50);

        batch.end();

        Overwatch2D.shapeRenderer.setAutoShapeType(true);
        Overwatch2D.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        Overwatch2D.shapeRenderer.begin();
        Overwatch2D.shapeRenderer.setColor(new Color(0.14f, 0.7098f, 0.74901f, 1));
        Overwatch2D.shapeRenderer.rect(getX() - 80 / 2, getY() + 60, 80, 10);
        Overwatch2D.shapeRenderer.end();

        Overwatch2D.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Overwatch2D.shapeRenderer.setColor(new Color(0.14f, 0.7098f, 0.74901f, 1));
        Overwatch2D.shapeRenderer.rect(getX() - 80 / 2, getY() + 60, Math.max(0, 80f * ((float)this.currentHP/(float)this.MAX_HEALTH)), 10);
        Overwatch2D.shapeRenderer.end();

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

    public void firePrimary() {
        if(weaponCanFire && currentBullets > 0 && !isReloading) {
            Vector3 hoverCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 position = GameScreen.camera.unproject(hoverCoordinates);

            position.x = position.x + MathUtils.random(-weaponSpread, weaponSpread);
            position.y = position.y + MathUtils.random(-weaponSpread, weaponSpread);

            double angle = Math.atan2(
                    position.y / Config.PIXELS_TO_METERS - physicsBody.getWorldCenter().y,
                    position.x / Config.PIXELS_TO_METERS - physicsBody.getWorldCenter().x
            ) * 180.0d / Math.PI;

            float initialX = ((physicsBody.getWorldCenter().x + projectileXOffset * (float)Math.cos(Math.toRadians(angle - 45))) + projectileSpawnDistance * (float)Math.cos(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;
            float initialY = ((physicsBody.getWorldCenter().y + projectileXOffset * (float)Math.sin(Math.toRadians(angle - 45))) + projectileSpawnDistance * (float)Math.sin(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;

            GameScreen.projectiles.add(new Projectile(
                initialX,
                initialY,
                position.x,
                position.y,
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
    }

    private void replenishAmmo() {
        this.currentBullets = MAX_BULLET_CAPACITY;
    }

    public void reload() {
        if(currentBullets < MAX_BULLET_CAPACITY) {
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
        return name;
    }

    public Texture getPortraitTexture() {
        return portrait;
    }

    public void playSelectedSound() {
        Hero.selectSound.play();
    }

    public void dispose() {
        GameScreen.heroesDestroyed.add(this);
        this.remove();
    }
}
