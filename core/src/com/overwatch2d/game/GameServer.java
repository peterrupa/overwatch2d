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
public class GameServer implements Runnable{

    String playerData;
    int playerCount = 0;
    DatagramSocket serverSocket = null;

}

public void broadcast(String msg){
    for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
        String name=(String)ite.next();
        NetPlayer player=(NetPlayer)game.getPlayers().get(name);
        send(player,msg);
    }
}
