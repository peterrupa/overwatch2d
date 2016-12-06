package com.overwatch2d.game;

import java.io.Serializable;

/**
 * Created by Peter on 11/27/2016.
 */
public class HeroFirePrimary implements Serializable {
    private String name;
    private float x;
    private float y;

    public HeroFirePrimary(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;

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
}
