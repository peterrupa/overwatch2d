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
    static BitmapFont gameObjectiveLabelFont;
    static BitmapFont gameUIFlashNotificationFont;
    static BitmapFont gameUIHealthFont;
    static BitmapFont gameUIAmmoCountFont;
    static BitmapFont gameUIGunNameFont;
    static BitmapFont gameUIFlashFont;
    static BitmapFont gameUITimerFont;
    static BitmapFont gameSelectionOKFont;
    static BitmapFont gameSelectionCountdownFont;
    static BitmapFont gamePostgamefont;
    static BitmapFont gameTeamLabelsFont;
    static BitmapFont gameInformationFont;
    static BitmapFont gameUIDeathsFont;
    static ShapeRenderer shapeRenderer;
    static BitmapFont gameUIEliminationsFont;
    static Thread clientReceiver;
    static Thread serverReceiver;
    private static String name;

    private static GameServer server;

    public void create() {
        batch = new SpriteBatch();
        font = createFont("fonts/bignoodletoo.ttf", 54);
        gamePlayerNameFont = createFont("fonts/koverwatch.ttf", 18);
        gameObjectiveLabelFont = createFont("fonts/koverwatch.ttf", 50);
        gameUIFlashNotificationFont = createFont("fonts/bignoodletoo.ttf", 30);
        gameUIHealthFont = createFont("fonts/bignoodletoo.ttf", 50);
        gameUIAmmoCountFont = createFont("fonts/bignoodletoo.ttf", 70);
        gameUIGunNameFont = createFont("fonts/bignoodletoo.ttf", 14);
        gameUIFlashFont = createFont("fonts/bignoodletoo.ttf", 40);
        gameUITimerFont = createFont("fonts/koverwatch.ttf", 24);
        gameSelectionOKFont = createFont("fonts/koverwatch.ttf", 30);
        gameSelectionCountdownFont = createFont("fonts/bignoodletoo.ttf", 24);
        gamePostgamefont = createFont("fonts/bignoodletoo.ttf", 150);
        gameUIDeathsFont = createFont("fonts/bignoodletoo.ttf", 30);
        gameUIEliminationsFont = createFont("fonts/bignoodletoo.ttf", 30);
        gameTeamLabelsFont = createFont("fonts/bignoodletoo.ttf", 60);
        gameInformationFont = createFont("fonts/koverwatch.ttf", 15);
        shapeRenderer = new ShapeRenderer();

        this.setScreen(new LoginScreen(this));
    }

    public void render() {
        try {
            super.render();
        }
        catch(Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            Gdx.app.exit();
        }
    }

    private BitmapFont createFont(String filename, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filename));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        return font;
    }

    public void setName(String name){
        this.name = name;
    }

    public static void createServer() {
        NetworkHelper.createServerReceiver().start();
        server = new GameServer();
    }

    public static void createClient() {
        clientReceiver = NetworkHelper.createClientReceiver();
        clientReceiver.start();
    }

    public static GameServer getServer() {
        return server;
    }

    public static String getName() {
        return Overwatch2D.name;
    }
}
