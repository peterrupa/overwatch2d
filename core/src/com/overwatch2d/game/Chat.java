package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
    private TextField chat;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    Chat(Stage UIStage){
        message = new TextField("", skin);
        message.setMessageText("Enter Message");
        message.setWidth(500);
        message.setPosition(10, 10);

        UIStage.setKeyboardFocus(message);
        message.getOnscreenKeyboard().show(true);


        //SET UP CHAT BOX
        chat = new TextField("", skin);
        chat.setMessageText("");
        chat.setWidth(500);
        chat.setHeight(300);
        chat.setPosition(10, 70);
        chat.setDisabled(true);

        UIStage.addActor(message);
        UIStage.addActor(chat);
    }

    private void appendChatBody(){
        String msg = message.getText();
        chat.appendText(msg);
    }
}
