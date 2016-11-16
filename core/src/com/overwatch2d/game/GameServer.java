package com.overwatch2d.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by geeca on 11/16/16.
 */
public class GameServer implements Runnable, Constants {

    String playerData;
    int playerCount = 0;
    DatagramSocket serverSocket = null;

    GameState game;

    /*************************************
     * for broadcasting data to all players
     *************************************/

    public void broadcast(String msg) {
        for (Iterator ite = game.getPlayers().keySet().iterator(); ite.hasNext(); ) {
            String name = (String) ite.next();
            Player player = (Player) game.getPlayers().get(name);
            send(player, msg);
        }
    }

    /*************************************
     * for sending a message to a player
     *************************************/

    public void send(Player player, String msg) {
        DatagramPacket packet;
        byte buf[] = msg.getBytes();
        packet = new DatagramPacket(buf, buf.length, player.getAddress(), player.getPort());
        try {
            serverSocket.send(packet);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run(){

    }
}