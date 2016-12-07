package com.overwatch2d.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;

public class Weapon {
    protected String name;
    protected boolean weaponCanFire = true;
    protected boolean isReloading = false;
    protected float weaponFPS = 10f;
    protected float weaponSpread = 20;
    protected final int MAX_BULLET_CAPACITY;
    protected final float TIME_TO_RELOAD;
    protected final int DAMAGE_PER_SHOT;
    protected float projectileSpawnDistance;
    protected float projectileXOffset;
    protected int currentBullets;
    protected Sound reloadSound;
    protected Sound fireSound;

    protected Hero hero;

    public Weapon(Hero hero, float weaponFPS, float weaponSpread, int max_bullet, float time_to_reload, int damage_per_shot, float projectileSpawnDistance, float projectileXOffset, Sound reloadSound, Sound fireSound, String name) {
        this.weaponFPS = weaponFPS;
        this.weaponSpread = weaponSpread;
        this.MAX_BULLET_CAPACITY = max_bullet;
        this.currentBullets = max_bullet;
        this.TIME_TO_RELOAD = time_to_reload;
        this.DAMAGE_PER_SHOT = damage_per_shot;
        this.projectileSpawnDistance = projectileSpawnDistance;
        this.projectileXOffset = projectileXOffset;
        this.reloadSound = reloadSound;
        this.fireSound = fireSound;
        this.hero = hero;
        this.name = name;
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

            NetworkHelper.clientSend(new Packet("PROJECTILE_SPAWN", new ProjectileSpawnPacket(initialX, initialY, x, y, DAMAGE_PER_SHOT, hero.getPlayerName())), NetworkHelper.getHost());

            String particleFile;

            if(hero.getPlayer().getTeam() == GameScreen.getCurrentPlayer().getTeam()) {
                particleFile = "particles/gunfire_allied.party";
            }
            else {
                particleFile = "particles/gunfire_hostile.party";
            }

            GameScreen.addParticleGunshot(Gdx.files.internal(particleFile), initialX, initialY, (float)angle, 10f);

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

    public Sound getReloadSound() {
        return reloadSound;
    }

    public Sound getFireSound() {
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

    public String getName() { return name; }
}

