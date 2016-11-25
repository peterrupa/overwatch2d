package com.overwatch2d.game;

import java.util.ArrayList;

public class PlayerListPacket extends Packet{
    private ArrayList<Player> players;

    public PlayerListPacket(ArrayList<Player> players) {
        super("PLAYER_LIST");

        this.players = players;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
