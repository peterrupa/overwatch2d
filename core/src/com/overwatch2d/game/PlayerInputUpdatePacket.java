package com.overwatch2d.game;

import java.io.Serializable;

/**
 * Created by Peter on 12/7/2016.
 */
public class PlayerInputUpdatePacket implements Serializable {
    private String name;
    private boolean WHold;
    private boolean AHold;
    private boolean SHold;
    private boolean DHold;

    public PlayerInputUpdatePacket(String name, boolean WHold, boolean AHold, boolean SHold, boolean DHold) {
        this.name = name;
        this.WHold = WHold;
        this.AHold = AHold;
        this.SHold = SHold;
        this.DHold = DHold;
    }

    public boolean isWHold() {
        return WHold;
    }

    public boolean isAHold() {
        return AHold;
    }

    public boolean isSHold() {
        return SHold;
    }

    public boolean isDHold() {
        return DHold;
    }

    public String getName() {
        return name;
    }
}
