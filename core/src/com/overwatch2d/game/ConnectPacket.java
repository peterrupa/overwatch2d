package com.overwatch2d.game;

import java.io.Serializable;

public class ConnectPacket implements Serializable {
    private String name;

    public ConnectPacket(String name) {
        this.name = name;
    }

    public ConnectPacket() {

    }

    public String getName() {
        return this.name;
    }
}
