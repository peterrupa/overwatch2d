package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;

import java.net.*;
import java.util.ArrayList;

public class NetworkHelper implements Constants {
    private static InetAddress host;

    public static void connect(String host, String name) {
        try {
            System.out.println("Sending CONNECT to " + host);

            InetAddress hostAddress = InetAddress.getByName(host);

            NetworkHelper.host = hostAddress;

            NetworkHelper.clientSend(new ConnectPacket(name), hostAddress);
        }
        catch(Exception e) {}
    }

    public static Thread createClientReceiver() {
        Thread t = new Thread(() -> {
            System.out.println("Client receiver started");

            while(true) {
                try {
                    byte[] bytes = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

                    System.out.println("Client waiting to receive.");

                    MulticastSocket s = new MulticastSocket(CLIENT_PORT);

                    s.receive(packet);

                    s.close();

                    Object rawData = Serialize.toObject(packet.getData());

                    Packet receivedPacket = (Packet)(rawData);

                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();

                    System.out.println("[Client] Received " + receivedPacket.getType() + " from " + address.toString() + ":" + port);

                    switch(receivedPacket.getType()) {
                        case "PLAYER_LIST":
                            ArrayList<Player> players = ((PlayerListPacket)receivedPacket).getPlayers();

                            JoinScreen.setPlayers(players);

                            break;
                        case "START_GAME":
                            new Thread(() -> Gdx.app.postRunnable(() -> JoinScreen.startGame())).start();

                            break;
                    }
                } catch (Exception ioe) {
                    System.out.println("Client Error:");
                    System.out.println(ioe);
                }
            }
        });

        return t;
    }

    public static Thread createServerReceiver() {
        Thread t = new Thread(() -> {
            System.out.println("Server receiver started");

            while(true) {
                try {
                    byte[] bytes = new byte[256];
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

                    System.out.println("Server waiting for request!");

                    MulticastSocket s = new MulticastSocket(PORT);

                    s.receive(packet);

                    s.close();

                    Object rawData = Serialize.toObject(packet.getData());

                    Packet receivedPacket = (Packet)(rawData);

                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();

                    System.out.println("[Server] Received " + receivedPacket.getType() + " from " + address.toString() + ":" + port);

                    switch(receivedPacket.getType()) {
                        case "CONNECT": {
                            String name = ((ConnectPacket)receivedPacket).getName();

                            Overwatch2D.getServer().connectPlayer(name, address);
                            break;
                        }
                        case "CHANGE_TEAM": {
                            String name = ((ChangeTeamPacket)receivedPacket).getName();
                            int team = ((ChangeTeamPacket)receivedPacket).getTeam();

                            Overwatch2D.getServer().changeTeam(name, team);
                        }
                    }
                } catch (Exception ioe) {
                    System.out.println("Server Error:");
                    System.out.println(ioe);
                }
            }
        });

        return t;
    }

    public static void clientSend(Packet p, InetAddress address) {
        try {
            DatagramPacket packet;
            byte buf[] = Serialize.toBytes(p);
            packet = new DatagramPacket(buf, buf.length, address, PORT);

            MulticastSocket s = new MulticastSocket(CLIENT_PORT);

            s.send(packet);

            s.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    public static void serverSend(Packet p, InetAddress address) {
        try {
            DatagramPacket packet;
            byte buf[] = Serialize.toBytes(p);

            packet = new DatagramPacket(buf, buf.length, address, CLIENT_PORT);

            MulticastSocket s = new MulticastSocket(PORT);

            s.send(packet);

            s.close();

            System.out.println("Server sent to " + address.toString() + ":" + CLIENT_PORT);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    public static InetAddress getHost() {
        return NetworkHelper.host;
    }
}
