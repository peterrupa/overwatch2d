package com.overwatch2d.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;

/**
 * Created by geeca on 11/16/16.
 * Reference from Circle Wars (Hermocilla)
 */
public class GameServer implements Runnable, Constants {

    String playerData;
    int playerCount = 0;
    int gameStage = WAITING_FOR_PLAYERS;

    GameState game;

    public GameServer(){

        System.out.println("Game created");
    }

    /*************************************
     * for broadcasting data to all players
     *************************************/

    public void broadcast(String msg) {
        for (Iterator i = game.getPlayers().keySet().iterator(); i.hasNext(); ) {
            String name = (String) i.next();
            Player player = (Player) game.getPlayers().get(name);
            send(player, msg);
        }
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

                System.out.println("Received request.");

                bytes = "Hello world".getBytes();

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                packet = new DatagramPacket(bytes, bytes.length, address, port);

                serverSocket.send(packet);
                serverSocket.close();

                System.out.println("Sent pong to " + address);
            } catch (Exception ioe) {
                System.out.println(ioe);
            }

//            playerData = new String(bytes);
//            playerData = playerData.trim(); //remove excess bytes


//            switch (gameStage) {
//                case WAITING_FOR_PLAYERS:
//                    if (playerData.startsWith("CONNECT")) {
//                        String tokens[] = playerData.split(" ");
//                        Player player = new Player(tokens[1], packet.getAddress(), packet.getPort());
//                        System.out.println("Player connected: " + tokens[1]);
//                        game.update(tokens[1].trim(), player);
//                        broadcast("CONNECTED " + tokens[1]);
//                        playerCount++;
//                    }
//                    break;
//
//                case GAME_START:
//                    System.out.println("Game State: START");
//                    broadcast("START");
//                    gameStage = IN_PROGRESS;
//                    break;
//
//                case IN_PROGRESS:
//                    if (playerData.startsWith("PLAYER")) {
//                        //Tokenize:
//                        //The format: PLAYER <player name> <x> <y>
//                        String[] playerInfo = playerData.split(" ");
//                        String pname = playerInfo[1];
//                        int x = Integer.parseInt(playerInfo[2].trim());
//                        int y = Integer.parseInt(playerInfo[3].trim());
//                        //Get the player from the game state
//                        Player player = (Player) game.getPlayers().get(pname);
//                        //Update the game state
//                        game.update(pname, player);
//                        //Send to all the updated game state
//                        broadcast(game.toString());
//                    }
//                    break;
//            }
        }
    }
}