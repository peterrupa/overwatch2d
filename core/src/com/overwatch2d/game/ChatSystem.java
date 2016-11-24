package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import javax.swing.*;
import java.io.File;

/**
 * Created by Pauweey on 11/23/2016.
 */
public class Chat extends Actor {
    TextField msg;
    TextField chatTextField;
    Skin uiSkin;

    public ChatSystem(Stage stage){
        uiSkin = new Skin(Gdx.files.internal("skin/skin.json"));
        uiSkin.addRegions(new TextureAtlas("skin/skin.atlas"));

        chatTextField = new TextField("Enter message", uiSkin);
        stage.addActor(chatTextField);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        Stage stageToRender;
        if (isStarted) {
            stageToRender = chatStage;
        } else {
            stageToRender = startStage;
        }

        renderStage(stageToRender);
    }

    private void renderStage(Stage stage) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
