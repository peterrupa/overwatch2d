package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.io.IOException;

/**
 * Created by Pauweey on 12/7/2016.
 */
public class Chat {
    private ChatServer server;
    private ChatClient client;
    private TextField message;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    Chat(Stage UIStage){
        try {
            server = new ChatServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client = new ChatClient();

        message = new TextField("", skin);
        message.setMessageText("");
        message.setWidth(500);
        message.setPosition(10, 10);

        UIStage.addActor(message);
    }
}
