package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Cursor extends Actor {
    private Texture texture = new Texture(Gdx.files.internal("cursor/cursor1.png"));

    Cursor(float x, float y) {
        setSize(texture.getWidth(), texture.getHeight());

        setPosition(x, y);
    }

    public void draw(Batch batch, float alpha){
        batch.draw(
            texture,
            getX() - texture.getWidth() / 2,
            getY() - texture.getHeight() / 2
        );
    }
}
