package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by geeca on 11/17/16.
 */
public class HostScreen implements Screen{
    private static Overwatch2D game = null;
    private OrthographicCamera camera;
    private Stage stage;
    private static ArrayList<Player> players = new ArrayList<Player>();
    private static VerticalGroup attackerVG;
    private static VerticalGroup defenderVG;

    HostScreen(final Overwatch2D gam) {
        Overwatch2D.createServer();
        Overwatch2D.createClient();
        NetworkHelper.connect("localhost", Overwatch2D.getName());

        float w = Gdx.graphics.getWidth(),
                h = Gdx.graphics.getHeight();

        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        stage = new Stage(new ExtendViewport(w, h, camera));

        Image background = new Image(new Texture(Gdx.files.internal("background/hostGame.jpg")));
        background.setSize(w, h);

        stage.addActor(background);

        attackerVG = new VerticalGroup();
        attackerVG.setPosition(500, 700);

        defenderVG = new VerticalGroup();
        defenderVG.setPosition(attackerVG.getX() + 300, 700);

        stage.addActor(attackerVG);
        stage.addActor(defenderVG);

        updateTeamsUI();

        TextButton.TextButtonStyle textStyle = new TextButton.TextButtonStyle();
        textStyle.font = game.font;

        TextButton begin = new TextButton("Start", textStyle);
        begin.setPosition(890, begin.getHeight()/2);

        stage.addActor(begin);

        final Image beginGradient = new Image(new Texture(Gdx.files.internal("effects/orange.jpg")));
        beginGradient.setScale(0.45f);
        beginGradient.setPosition(890, begin.getHeight()/2);
        beginGradient.setColor(1, 1, 1, 0);

        stage.addActor(beginGradient);

        beginGradient.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Overwatch2D.getServer().startGame();
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                beginGradient.setVisible(true);
                beginGradient.addAction(Actions.fadeIn(0.1f));
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {
                beginGradient.addAction(Actions.fadeOut(0.1f));
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public static void setPlayers(ArrayList<Player> players) {
        HostScreen.players = players;

        updateTeamsUI();
    }

    public static void startGame() {
        game.setScreen(new GameScreen(game, players, Overwatch2D.getName()));
    }

    private static Label createPlayerLabel(String text) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Overwatch2D.gameUIFlashFont;

        if(text.equals(Overwatch2D.getName())) {
            style.fontColor = Color.GOLD;
        }

        return new Label(text, style);
    }

    private static void updateTeamsUI() {
        attackerVG.clear();
        defenderVG.clear();

        TextButton.TextButtonStyle attackersTextButtonStyle = new TextButton.TextButtonStyle();
        attackersTextButtonStyle.font = Overwatch2D.gameTeamLabelsFont;
        attackersTextButtonStyle.fontColor = Color.RED;
        TextButton attackersTextButton = new TextButton("Attack", attackersTextButtonStyle);
        attackersTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                NetworkHelper.clientSend(new Packet("CHANGE_TEAM", new ChangeTeamPacket(Overwatch2D.getName(), 0)), NetworkHelper.getHost());
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {

            }
        });
        attackerVG.addActor(attackersTextButton);

        TextButton.TextButtonStyle defendersTextButtonStyle = new TextButton.TextButtonStyle();
        defendersTextButtonStyle.font = Overwatch2D.gameTeamLabelsFont;
        defendersTextButtonStyle.fontColor = Color.BLUE;
        TextButton defendersTextButton = new TextButton("Defend", defendersTextButtonStyle);
        defendersTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                NetworkHelper.clientSend(new Packet("CHANGE_TEAM", new ChangeTeamPacket(Overwatch2D.getName(), 1)), NetworkHelper.getHost());
            }

            @Override
            public void enter(InputEvent e, float x, float y, int pointer, Actor fromActor) {
            }

            @Override
            public void exit(InputEvent e, float x, float y, int pointer, Actor fromActor) {

            }
        });
        defenderVG.addActor(defendersTextButton);

        ArrayList<Player> attackers = (ArrayList<Player>) players.stream().filter(p -> p.getTeam() == 0).collect(Collectors.toList());

        for(Player p: attackers) {
            attackerVG.addActor(createPlayerLabel(p.getName()));
        }

        ArrayList<Player> defenders = (ArrayList<Player>) players.stream().filter(p -> p.getTeam() == 1).collect(Collectors.toList());

        for(Player p: defenders) {
            defenderVG.addActor(createPlayerLabel(p.getName()));
        }
    }
}
