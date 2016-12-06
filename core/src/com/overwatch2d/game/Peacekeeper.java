package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;

public class Peacekeeper extends Weapon {
    public Peacekeeper(Hero hero) {
        super(
            hero,
            2f,
            5,
            6,
            1.5f,
            70,
            0.25f,
            0f,
            Gdx.audio.newSound(Gdx.files.internal("sfx/mccree/reload.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sfx/mccree/fire.mp3"))
        );
    }
}
