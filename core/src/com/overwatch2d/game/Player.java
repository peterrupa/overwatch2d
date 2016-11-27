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

    public String toString(){
        String str = "{name: \"" + name + "\", team: " + team + ", address: \"" + address.toString() + "\"}";

        if(getHero() != null) {
            str += getHero().getBody().getWorldCenter().x + " ";
            str += getHero().getBody().getWorldCenter().y ;
        }

        return str;
    }


    public int getPort() {
        return port;
    }
}
