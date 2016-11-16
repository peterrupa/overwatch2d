package com.overwatch2d.game;

import com.badlogic.gdx.Screen;

/**
 * Created by geeca on 11/17/16.
 */
public abstract class HostScreen implements Screen, Constants {

    int stage = WAITING_FOR_PLAYERS;

    while(true){
        switch (stage) {
            case WAITING_FOR_PLAYERS:
                dispose();
                break;
            case GAME_START:
                break;
        }
    }

    @Override
    public void dispose() {

    }
}
