package com.overwatch2d.game;

/**
 * Created by Peter on 11/26/2016.
 */
public class ChangeTeamPacket extends Packet {
    private String name;
    private int team;

    public ChangeTeamPacket(String name, int team) {
        super("CHANGE_TEAM");

        this.name = name;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public int getTeam() {
        return team;
    }
}
