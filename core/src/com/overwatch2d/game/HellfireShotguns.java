package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

/**
 * Created by Peter on 12/6/2016.
 */
public class HellfireShotguns extends Weapon {
    int shotgunState;

    public HellfireShotguns(Hero hero) {
        super(
            hero,
            2f,
            50,
            8,
            1.5f,
            7,
            0.15f,
            0.2f,
            Gdx.audio.newSound(Gdx.files.internal("sfx/reaper/reload.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sfx/reaper/fire.mp3"))
        );

        this.shotgunState = 1;
    }

    @Override
    public void firePrimary(float x, float y) {
        if(weaponCanFire && currentBullets > 0 && !isReloading) {
            float initialX = 0, initialY = 0;
            double angle = 0;

            ArrayList<Float> destX = new ArrayList<Float>();
            ArrayList<Float> destY = new ArrayList<Float>();

            for(int i = 0; i < 20; i++) {
                angle = Math.atan2(
                        y / Config.PIXELS_TO_METERS - hero.getBody().getWorldCenter().y,
                        x / Config.PIXELS_TO_METERS - hero.getBody().getWorldCenter().x
                ) * 180.0d / Math.PI;

                x = hero.getBody().getWorldCenter().x * Config.PIXELS_TO_METERS + 5f * (float)Math.cos(Math.toRadians(angle)) * Config.PIXELS_TO_METERS;
                y = hero.getBody().getWorldCenter().y * Config.PIXELS_TO_METERS + 5f * (float)Math.sin(Math.toRadians(angle)) * Config.PIXELS_TO_METERS;

                x = x + MathUtils.random(-weaponSpread, weaponSpread);
                y = y + MathUtils.random(-weaponSpread, weaponSpread);

                if(shotgunState == 1) {
                    initialX = ((hero.getBody().getWorldCenter().x + projectileXOffset * (float)Math.cos(Math.toRadians(angle - 45))) + projectileSpawnDistance * (float)Math.cos(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;
                    initialY = ((hero.getBody().getWorldCenter().y + projectileXOffset * (float)Math.sin(Math.toRadians(angle - 45))) + projectileSpawnDistance * (float)Math.sin(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;
                }
                else {
                    initialX = ((hero.getBody().getWorldCenter().x - projectileXOffset * (float)Math.cos(Math.toRadians(angle - 45))) + 2.75f * projectileSpawnDistance * (float)Math.cos(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;
                    initialY = ((hero.getBody().getWorldCenter().y - projectileXOffset * (float)Math.sin(Math.toRadians(angle - 45))) + 2.75f * projectileSpawnDistance * (float)Math.sin(Math.toRadians(angle))) * Config.PIXELS_TO_METERS;
                }

                destX.add(x);
                destY.add(y);
            }

            NetworkHelper.clientSend(new Packet("PROJECTILE_HELLFIRE_SPAWN", new ProjectileHellfireSpawnPacket(initialX, initialY, destX, destY, DAMAGE_PER_SHOT, hero.getPlayerName())), NetworkHelper.getHost());

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

            shotgunState = shotgunState == 0 ? 1 : 0;

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
}
