package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;

import java.net.*;
import java.util.ArrayList;

public class NetworkHelper implements Constants {
    private static InetAddress host;
    private static MulticastSocket clientSocket = null;
    private static boolean isServer = false;

    static {
        try {
            int port = 1000 + (int)Math.floor(Math.random() * 7000);

            clientSocket = new MulticastSocket(port);

            System.out.println("Client socket initialized at :" + clientSocket.getLocalPort());
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void connect(String host, String name) {
        try {
            InetAddress hostAddress = InetAddress.getByName(host);

            NetworkHelper.host = hostAddress;

            NetworkHelper.clientSend(new Packet("CONNECT", new ConnectPacket(name)), hostAddress);
        }
        catch(Exception e) {}
    }

    public static Thread createClientReceiver() {
        Thread t = new Thread(() -> {
            while(true) {
                try {
                    byte[] bytes = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

                    clientSocket.receive(packet);

                    Object rawData = Serialize.toObject(packet.getData());

                    Packet receivedPacket = (Packet)(rawData);

                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();

                    System.out.println("[Client] Received " + receivedPacket.getType() + " from " + address.toString() + ":" + port);

                    switch(receivedPacket.getType()) {
                        case "PLAYER_LIST": {
                            ArrayList<Player> players = ((PlayerListPacket)receivedPacket.getPayload()).getPlayers();

                            JoinScreen.setPlayers(players);

                            break;
                        }
                        case "START_GAME": {
                            if(isServer) {
                                new Thread(() -> Gdx.app.postRunnable(() -> HostScreen.startGame())).start();
                            }
                            else {
                                new Thread(() -> Gdx.app.postRunnable(() -> JoinScreen.startGame())).start();
                            }

                            break;
                        }
                        case "HERO_SPAWN": {
                            String playername = ((HeroSpawnPacket)receivedPacket.getPayload()).getPlayername();

                            Gdx.app.postRunnable(() -> GameScreen.spawnPlayer(playername));

                            break;
                        }
                        case "CHANGE_TEAM": {
                            String name = ((ChangeTeamPacket)receivedPacket.getPayload()).getName();
                            int team = ((ChangeTeamPacket)receivedPacket.getPayload()).getTeam();

                            JoinScreen.changeTeam(name, team);
                            break;
                        }
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
            isServer = true;

            while(true) {
                try {
                    byte[] bytes = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

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
                            String name = ((ConnectPacket)receivedPacket.getPayload()).getName();

                            Overwatch2D.getServer().connectPlayer(name, address, port);
                            break;
                        }
                        case "CHANGE_TEAM": {
                            String name = ((ChangeTeamPacket)receivedPacket.getPayload()).getName();
                            int team = ((ChangeTeamPacket)receivedPacket.getPayload()).getTeam();

                            Overwatch2D.getServer().changeTeam(name, team);
                        }
                        case "HERO_SPAWN": {
                            String name = ((HeroSpawnPacket)receivedPacket.getPayload()).getPlayername();

                            Overwatch2D.getServer().spawnHero(name);
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

            System.out.println("[Client] Sending " + p.getType() + " (" + packet.getLength() +"B) to " + address);

            clientSocket.send(packet);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    public static void serverSend(Packet p, InetAddress address, int port) {
        try {
            DatagramPacket packet;
            byte buf[] = Serialize.toBytes(p);

            packet = new DatagramPacket(buf, buf.length, address, port);

            MulticastSocket s = new MulticastSocket(PORT);

            System.out.println("[Server] Sending " + p.getType() + " (" + packet.getLength() +"B) to " + address + ":" + port);

            s.send(packet);

            s.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    public static InetAddress getHost() {
        return NetworkHelper.host;
    }
}
