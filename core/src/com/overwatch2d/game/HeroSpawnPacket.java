package com.overwatch2d.game;

import java.io.Serializable;

public class HeroSpawnPacket implements Serializable {
    private String playername;
    private int heroType;

    public HeroSpawnPacket(String playername, int heroType) {
        this.playername = playername;
        this.heroType = heroType;
    }

    public HeroSpawnPacket() {}

    public String getPlayername() {
        return playername;
    }

    public int getHeroType() {
        return heroType;
    }
}
