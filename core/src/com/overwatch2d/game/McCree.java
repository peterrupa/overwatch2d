package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class McCree extends Hero {
    public McCree(float initialX, float initialY, Player player) {
        super(
                initialX,
                initialY,
                player,
                new Texture(Gdx.files.internal("sprites/actor.png")),
                4f,
                200,
                new Texture(Gdx.files.internal("portraits/mccree.png")),
                Gdx.audio.newSound(Gdx.files.internal("sfx/mccree/spawn.mp3")),
                Gdx.audio.newSound(Gdx.files.internal("sfx/mccree/respawn.mp3"))
        );

        weapon = new Peacekeeper(this);
    }
}
