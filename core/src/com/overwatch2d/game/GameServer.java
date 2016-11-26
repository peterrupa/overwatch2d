package com.overwatch2d.game;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by geeca on 11/16/16.
 */
public class GameServer implements Constants {
    ArrayList<Player> players = new ArrayList<Player>();

    /*************************************
     * for broadcasting data to all players
     *************************************/
    public GameServer() {
        try {
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

    public void changeTeam(String name, int team) {
        Player changer = players.stream().filter(p -> p.getName().equals(name)).collect(Collectors.toList()).get(0);

        changer.setTeam(team);

        broadcast(new PlayerListPacket(players));

        HostScreen.setPlayers(players);
    }

    public void startGame() {
        broadcast(new StartGamePacket());
    }
}