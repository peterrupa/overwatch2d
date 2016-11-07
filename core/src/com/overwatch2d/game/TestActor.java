package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

class TestActor extends Actor {
    private Texture texture = new Texture(Gdx.files.internal("actor.png"));

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(texture, getX() - texture.getWidth() / 2, getY() - texture.getHeight() / 2);
    }
}
