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
 * Reference from Circle Wars (Hermocilla)
 */
public class GameServer implements Runnable, Constants {

    String playerData;
    int playerCount = 0;
    int gameStage = WAITING_FOR_PLAYERS;

    ArrayList<Player> players = new ArrayList<Player>();

    public GameServer(){

        System.out.println("Game created");
    }

    /*************************************
     * for broadcasting data to all players
     *************************************/

    public void broadcast(String msg) {
//        for (Iterator i = game.getPlayers().keySet().iterator(); i.hasNext(); ) {
//            String name = (String) i.next();
//            Player player = (Player) game.getPlayers().get(name);
//            send(player, msg);
//        }
    }

    /*************************************
     * for sending a message to a player
     *************************************/

    public void send(Player player, String msg) {
//        DatagramPacket packet;
//        byte buf[] = msg.getBytes();
//        packet = new DatagramPacket(buf, buf.length, player.getAddress(), player.getPort());
//        try {
//            serverSocket.send(packet);
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
    }

    public void setGameState(int setGameState){
        this.gameStage = gameStage;
    }

    @Override
    public void run(){
        while(true) {
            try {
                DatagramSocket serverSocket = new DatagramSocket(PORT);

                byte[] bytes = new byte[256];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

                serverSocket.receive(packet);

                serverSocket.close();

                Object rawData = NetworkHelper.fromString(new String(packet.getData()));

                Packet receivedPacket = (Packet)(rawData);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                System.out.println("Received " + receivedPacket.getType() + " from " + address.toString() + ":" + port);

                switch(receivedPacket.getType()) {
                    case "CONNECT":
                        String name = ((ConnectPacket)receivedPacket).getName();

                        System.out.println("Adding " + name + " from " + address.toString() + ":" + port + " to players list");
                        players.add(new Player(name, address, port));

                        System.out.println(players);
                        break;
                }


            } catch (Exception ioe) {
                System.out.println("Server Error:");
                System.out.println(ioe);
            }
        }
    }
}