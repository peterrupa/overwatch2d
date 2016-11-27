package com.overwatch2d.game;

import java.io.Serializable;

public class HeroSpawnPacket implements Serializable {
    private String playername;

    public HeroSpawnPacket(String playername) {
        this.playername = playername;
    }

    public HeroSpawnPacket() {}

    public String getPlayername() {
        return playername;
    }
}
