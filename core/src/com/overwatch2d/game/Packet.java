package com.overwatch2d.game;

import java.io.Serializable;

public class Packet implements Serializable {
    private String type;
    private Object payload;

    public Packet(String type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public Packet() {

    }

    public String getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }
}
