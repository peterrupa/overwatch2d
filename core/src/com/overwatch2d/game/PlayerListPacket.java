package com.overwatch2d.game;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerListPacket implements Serializable {
    private ArrayList<Player> players;

    public PlayerListPacket(ArrayList<Player> players) {
        this.players = players;
    }

    public PlayerListPacket() {}

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
