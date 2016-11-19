package com.overwatch2d.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Overwatch2D extends Game {
    static SpriteBatch batch;
    static BitmapFont font;
    static BitmapFont gamePlayerNameFont;
    static BitmapFont gameUIHealthFont;
    static BitmapFont gameUIAmmoCountFont;
    static BitmapFont gameUIGunNameFont;
    static BitmapFont gameSelectionOKFont;
    static BitmapFont gameSelectionCountdownFont;
    static ShapeRenderer shapeRenderer;

    public void create() {
        batch = new SpriteBatch();
        font = createFont("fonts/bignoodletoo.ttf", 54);
        gamePlayerNameFont = createFont("fonts/koverwatch.ttf", 18);
        shapeRenderer = new ShapeRenderer();
        gameUIHealthFont = createFont("fonts/bignoodletoo.ttf", 50);
        gameUIAmmoCountFont = createFont("fonts/bignoodletoo.ttf", 70);
        gameUIGunNameFont = createFont("fonts/bignoodletoo.ttf", 14);
        gameSelectionOKFont = createFont("fonts/koverwatch.ttf", 30);
        gameSelectionCountdownFont = createFont("fonts/bignoodletoo.ttf", 24);

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
