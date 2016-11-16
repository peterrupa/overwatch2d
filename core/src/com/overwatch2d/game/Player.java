package com.overwatch2d.game;
import java.net.InetAddress;

/**
 * Created by geeca on 11/16/16.
 * Reference CircleWars (Hermocilla)
 * Network of players
 */

public class Player {

    private InetAddress address;
    private int port;
    private String name;
    private Hero hero;
    float x;
    float y;


    //constructor
    public Player(String name, InetAddress address, int port){
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress(){
        return address;
    }

    public int getPort(){
        return port;
    }

    public Hero getHero(){
        return hero;
    }

    public void updateX(){
        x = getHero().getBody().getWorldCenter().x;
    }

    public void updateY(){
        y = getHero().getBody().getWorldCenter().y;
    }

    public String toString(){
        String str = "";
        str += "PLAYER ";
        str += name+" ";
        str += getHero().getBody().getWorldCenter().x + " ";
        str += getHero().getBody().getWorldCenter().y ;
        return str;
    }


}
