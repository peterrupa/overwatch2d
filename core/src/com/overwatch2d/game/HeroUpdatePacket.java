package com.overwatch2d.game;

import java.io.Serializable;

/**
 * Created by Peter on 11/27/2016.
 */
public class HeroUpdatePacket implements Serializable {
    private String name;
    private float x;
    private float y;
    private float angle;
    private int currentHP;
    private boolean isDead;
    private float timeToRespawn;

    public HeroUpdatePacket(String name, float x, float y, float angle, int currentHP, boolean isDead, float timeToRespawn) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.currentHP = currentHP;
        this.isDead = isDead;
        this.timeToRespawn = timeToRespawn;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public boolean isDead() {
        return isDead;
    }

    public float getTimeToRespawn() {
        return timeToRespawn;
    }
}
