package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Peter on 12/6/2016.
 */
public class Reaper extends Hero {
    public Reaper(float initialX, float initialY, Player player) {
        super(
            initialX,
            initialY,
            player,
            new Texture(Gdx.files.internal("sprites/actor.png")),
            4f,
            250,
            new Texture(Gdx.files.internal("portraits/reaper.png")),
            Gdx.audio.newSound(Gdx.files.internal("sfx/reaper/spawn.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sfx/reaper/respawn.mp3"))
        );

        weapon = new HellfireShotguns(this);
    }
}
