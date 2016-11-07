package com.overwatch2d.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Overwatch2D extends Game {
    SpriteBatch batch;
    BitmapFont font;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        font = createFont("fonts/bignoodletoo.ttf", 54);

        this.setScreen(new MainMenu(this));
    }

    public void render() {
        super.render();
    }

    private BitmapFont createFont(String filename, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filename));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        return font;
    }
}
