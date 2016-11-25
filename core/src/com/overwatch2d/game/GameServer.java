package com.overwatch2d.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by geeca on 11/16/16.
 */
public class GameServer implements Constants {
    ArrayList<Player> players = new ArrayList<Player>();

    /*************************************
     * for broadcasting data to all players
     *************************************/
    public GameServer(String name) {
        try {
            players.add(new Player(name, InetAddress.getByName("localhost")));

            HostScreen.setPlayers(players);
        }
        catch(Exception e) {

        }
    }

    public void broadcast(Packet packet) {
        System.out.println("Server broadcasting");

        for(Player p: players) {
            send(p, packet);
        }
    }

    /*************************************
     * for sending a message to a player
     *************************************/

    public void send(Player player, Packet packet) {
        try {
            NetworkHelper.serverSend(packet, player.getAddress());
        }
        catch(Exception e) {}
    }

    public void connectPlayer(String name, InetAddress address) {
        players.add(new Player(name, address));

        System.out.println(name + " " + address + " joined");

        broadcast(new PlayerListPacket(players));

        HostScreen.setPlayers(players);
    }

    public void startGame() {
        broadcast(new StartGamePacket());
    }
}