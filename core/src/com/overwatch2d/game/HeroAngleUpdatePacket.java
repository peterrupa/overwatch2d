package com.overwatch2d.game;

import java.io.Serializable;

/**
 * Created by Peter on 12/7/2016.
 */
public class HeroAngleUpdatePacket implements Serializable {
    private String name;
    private float angle;

    public HeroAngleUpdatePacket(String name, float angle) {
        this.name = name;
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    public String getName() {
        return name;
    }
}
