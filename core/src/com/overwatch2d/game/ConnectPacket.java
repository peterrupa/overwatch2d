package com.overwatch2d.game;

public class ConnectPacket extends Packet{
    private String name;

    public ConnectPacket(String name) {
        super("CONNECT");

        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
