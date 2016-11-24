package com.overwatch2d.game;

import java.io.Serializable;

public class Packet implements Serializable{
    private String type;

    public Packet(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
