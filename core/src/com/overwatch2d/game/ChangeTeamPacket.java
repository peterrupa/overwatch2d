package com.overwatch2d.game;

import java.io.Serializable;

/**
 * Created by Peter on 11/26/2016.
 */
public class ChangeTeamPacket implements Serializable {
    private String name;
    private int team;

    public ChangeTeamPacket(String name, int team) {
        this.name = name;
        this.team = team;
    }

    public ChangeTeamPacket() {}

    public String getName() {
        return name;
    }

    public int getTeam() {
        return team;
    }
}
