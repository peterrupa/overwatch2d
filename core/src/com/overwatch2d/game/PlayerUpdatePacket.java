package com.overwatch2d.game;

import java.io.Serializable;

/**
 * Created by Peter on 12/7/2016.
 */
public class PlayerUpdatePacket implements Serializable {
    private String name;
    private int eliminations;
    private int deaths;

    public PlayerUpdatePacket(String name, int eliminations, int deaths) {
        this.name = name;
        this.eliminations = eliminations;
        this.deaths = deaths;
    }

    public String getName() {
        return name;
    }

    public int getEliminations() {
        return eliminations;
    }

    public int getDeaths() {
        return deaths;
    }
}
