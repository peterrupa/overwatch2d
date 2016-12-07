package com.overwatch2d.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Peter on 12/7/2016.
 */
public class ProjectileHellfireSpawnPacket implements Serializable {
    private float initialX;
    private float initialY;
    private ArrayList<Float> destX;
    private ArrayList<Float> destY;
    private int damage;
    private String heroName;

    public ProjectileHellfireSpawnPacket(float initialX, float initialY, ArrayList<Float> destX, ArrayList<Float> destY, int damage, String heroName) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.destX = destX;
        this.destY = destY;
        this.damage = damage;
        this.heroName = heroName;
    }

    public float getInitialX() {
        return initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public ArrayList<Float> getDestX() {
        return destX;
    }

    public ArrayList<Float> getDestY() {
        return destY;
    }

    public int getDamage() {
        return damage;
    }

    public String getHeroName() {
        return heroName;
    }
}
