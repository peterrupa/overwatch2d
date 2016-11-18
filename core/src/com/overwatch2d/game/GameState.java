package com.overwatch2d.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by geeca on 11/16/16.
 * Source from CircleWars (Hermocilla)
 * This class contains the state of the game
 * The game state refers to the current position of the players
 */

public class GameState {

    private Map players=new HashMap();

    public GameState(){}

    public void update(String name, Player player){
        players.put(name, player);
    }

    public String toString(){
        String val = "";
        for(Iterator ite = players.keySet().iterator(); ite.hasNext();){
            String name = (String)ite.next();
            Player player = (Player)players.get(name);
            val += player.toString()+":";
        }
        return val;
    }

    public Map getPlayers(){
        return players;
    }
}
