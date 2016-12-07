package com.overwatch2d.game;

import java.net.InetAddress;
import java.util.ArrayList;
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
        NetworkHelper.setIsHost(true);
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

    public void spawnHero(String name, int heroType) {
        broadcast(new Packet("HERO_SPAWN", new HeroSpawnPacket(name, heroType)));
    }

    public void updateHero(String name, float x, float y, float angle, int currentHP, boolean isDead, float timeToRespawn) {
        broadcast(new Packet("HERO_UPDATE", new HeroUpdatePacket(name, x, y, angle, currentHP, isDead, timeToRespawn)));
    }

    public void updatePlayer(String name, int eliminations, int deaths) {
        broadcast(new Packet("PLAYER_UPDATE", new PlayerUpdatePacket(name, eliminations, deaths)));
    }

    public void updateWorld(int currentObjective, float objective1Capture, float objective2Capture, float gameTimer, boolean battleHasStarted, float preparationDuration) {
        broadcast(new Packet("WORLD_UPDATE", new WorldUpdatePacket(currentObjective, objective1Capture, objective2Capture, gameTimer, battleHasStarted, preparationDuration)));
    }

    public void updateHeroAngle(String name, float angle) {
        broadcast(new Packet("HERO_ANGLE_UPDATE", new HeroAngleUpdatePacket(name, angle)));
    }

    public void updatePlayerInput(String name, boolean WHold, boolean AHold, boolean SHold, boolean DHold) {
        broadcast(new Packet("PLAYER_INPUT_UPDATE", new PlayerInputUpdatePacket(name, WHold, AHold, SHold, DHold)));
    }

    public void spawnProjectile(float initialX, float initialY, float destX, float destY, int damage, String heroName) {
        broadcast(new Packet("PROJECTILE_SPAWN", new ProjectileSpawnPacket(initialX, initialY, destX, destY, damage, heroName)));
    }

    public void spawnHellfireProjectile(float initialX, float initialY, ArrayList<Float> destX, ArrayList<Float> destY, int damage, String heroName) {
        broadcast(new Packet("PROJECTILE_HELLFIRE_SPAWN", new ProjectileHellfireSpawnPacket(initialX, initialY, destX, destY, damage, heroName)));
    }

    public void firePrimary(String name, float x, float y) {
        broadcast(new Packet("HERO_FIRE_PRIMARY", new HeroFirePrimary(name, x, y)));
    }
}