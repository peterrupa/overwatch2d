package com.overwatch2d.game;

import com.badlogic.gdx.utils.Json;

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

import static java.lang.Thread.sleep;

/**
 * Created by geeca on 11/16/16.
 */
public class GameServer implements Constants {
    ArrayList<Player> players = new ArrayList<Player>();

    /*************************************
     * for broadcasting data to all players
     *************************************/
    public GameServer() {
        System.out.println("Game server started");
    }

    public void broadcast(Packet packet) {
        for(Player p: players) {
            send(p, packet);
        }
    }

    /*************************************
     * for sending a message to a player
     *************************************/

    public void send(Player player, Packet packet) {
        try {
            NetworkHelper.serverSend(packet, player.getAddress(), player.getPort());
        }
        catch(Exception e) {}
    }

    public void connectPlayer(String name, InetAddress address, int port) {
        players.add(new Player(name, address, port));

        System.out.println(name + " " + address + ":" + port + " joined");

        broadcast(new Packet("PLAYER_LIST", new PlayerListPacket(players)));

        HostScreen.setPlayers(players);
    }

    public void changeTeam(String name, int team) {
        Player changer = players.stream().filter(p -> p.getName().equals(name)).collect(Collectors.toList()).get(0);

        changer.setTeam(team);
        broadcast(new Packet("CHANGE_TEAM", new ChangeTeamPacket(name, team)));

        HostScreen.setPlayers(players);
    }

    public void startGame() {
        broadcast(new Packet("START_GAME", new StartGamePacket()));
    }

    public void spawnHero(String name) {
        broadcast(new Packet("HERO_SPAWN", new HeroSpawnPacket(name)));
    }
}