package com.overwatch2d.game;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by geeca on 11/16/16.
 * Reference CircleWars (Hermocilla)
 * Network of players
 */

public class Player implements Serializable {

    private InetAddress address;
    private String name;
    private Hero hero = null;
    private int team = 0;
    private int port;
    private int eliminations = 0;
    private int deaths = 0;

    //constructor
    public Player(String name, InetAddress address, int port){
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public Player() {}

    public InetAddress getAddress(){
        return address;
    }

    public Hero getHero(){
        return hero;
    }

    public int getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setHero(Hero h) {
        this.hero = h;
    }

    public int getDeaths(){ return deaths; }

    public int getEliminations(){ return eliminations
            ; }

    public void incDeaths(){ deaths = deaths + 1; }

    public void incEliminations(){ eliminations = eliminations + 1; }

    public String toString(){
        String str = "{name: \"" + name + "\", team: " + team + ", address: \"" + address.toString() + "\"}";

        return str;
    }


    public int getPort() {
        return port;
    }

    public void setEliminations(int eliminations) {
        this.eliminations = eliminations;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
}
