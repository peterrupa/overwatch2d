package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by Peter on 12/5/2016.
 */
public class PulseRifle extends Weapon {
    public PulseRifle(Hero hero) {
        super(
            hero,
            10f,
            20,
            25,
            1.5f,
            15,
            0.15f,
            0.25f,
            Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/reload.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sfx/soldier76/fire.ogg"))
        );
    }
}
