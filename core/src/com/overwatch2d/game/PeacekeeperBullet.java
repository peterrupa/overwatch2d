package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Peter on 12/6/2016.
 */
public class PeacekeeperBullet extends Projectile {
    public PeacekeeperBullet(float initialX, float initialY, float destX, float destY, int damage, Hero owner) {
        super(
            initialX,
            initialY,
            destX,
            destY,
            damage,
            owner,
            new Texture(Gdx.files.internal("projectiles/sampleBullet.png"))
        );
    }
}
