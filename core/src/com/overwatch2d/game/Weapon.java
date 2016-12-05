package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;

public class Weapon {
    private boolean weaponCanFire = true;
    private boolean isReloading = false;
    private float weaponFPS = 10f;
    private float weaponSpread = 20;
    private final int MAX_BULLET_CAPACITY;
    private final float TIME_TO_RELOAD;
    private final int DAMAGE_PER_SHOT;
    private float projectileSpawnDistance = 0.30f;
    private float projectileXOffset = 0.25f;
    private int currentBullets;
    private static Sound reloadSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/reload.mp3"));
    private static Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/fire.ogg"));

    private Hero hero;

    public Weapon(Hero hero) {
        weaponFPS = 10f;
        weaponSpread = 20;
        MAX_BULLET_CAPACITY = 25;
        currentBullets = MAX_BULLET_CAPACITY;
        TIME_TO_RELOAD = 1.5f;
        DAMAGE_PER_SHOT = 15;
        reloadSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/reload.mp3"));
        fireSound = Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/fire.ogg"));
        this.hero = hero;
    }

    public void firePrimary(float x, float y) {
        if(weaponCanFire && currentBullets > 0 && !isReloading) {
            double angle = Math.atan2(
                y / Config.PIXELS_TO_METERS - hero.getBody().getWorldCenter().y,
                x / Config.PIXELS_TO_METERS - hero.getBody().getWorldCenter().x
            ) * 180.0d / Math.PI;

            x = hero.getBody().getWorldCenter().x * Config.PIXELS_TO_METERS + 5f * (float)Math.cos(Math.toRadians(angle)) * Config.PIXELS_TO_METERS;
            y = hero.getBody().getWorldCenter().y * Config.PIXELS_TO_METERS + 5f * (float)Math.sin(Math.toRadians(angle)) * Config.PIXELS_TO_METERS;

            x = x + MathUtils.random(-weaponSpread, weaponSpread);
            y = y + MathUtils.random(-weaponSpread, weaponSpread);

            float initialX = ((hero.getBody().getWorldCenter().x + projectileXOffset * (float)Math.cos(Math.toRadians(angle - 45))) + projectileSpawnDistance * (float)Math.cos(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;
            float initialY = ((hero.getBody().getWorldCenter().y + projectileXOffset * (float)Math.sin(Math.toRadians(angle - 45))) + projectileSpawnDistance * (float)Math.sin(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;

            GameScreen.getGameState().getProjectiles().add(new Projectile(
                initialX,
                initialY,
                x,
                y,
                DAMAGE_PER_SHOT,
                hero
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

    public void restart() {
        replenishAmmo();
        weaponCanFire = true;
        isReloading = false;
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

    public static Sound getReloadSound() {
        return reloadSound;
    }

    public static Sound getFireSound() {
        return fireSound;
    }

    public boolean isWeaponCanFire() {
        return weaponCanFire;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public float getWeaponFPS() {
        return weaponFPS;
    }

    public float getWeaponSpread() {
        return weaponSpread;
    }

    public int getMAX_BULLET_CAPACITY() {
        return MAX_BULLET_CAPACITY;
    }

    public float getTIME_TO_RELOAD() {
        return TIME_TO_RELOAD;
    }

    public int getDAMAGE_PER_SHOT() {
        return DAMAGE_PER_SHOT;
    }

    public int getCurrentBullets() {
        return currentBullets;
    }
}

