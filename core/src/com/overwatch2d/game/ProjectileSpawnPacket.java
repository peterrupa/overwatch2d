package com.overwatch2d.game;

import java.io.Serializable;

/**
 * Created by Peter on 12/7/2016.
 */
public class ProjectileSpawnPacket implements Serializable {
    private float initialX;
    private float initialY;
    private float destX;
    private float destY;
    private int damage;
    private String heroName;

    public ProjectileSpawnPacket(float initialX, float initialY, float destX, float destY, int damage, String heroName) {
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

    public float getDestX() {
        return destX;
    }

    public float getDestY() {
        return destY;
    }

    public int getDamage() {
        return damage;
    }

    public String getHeroName() {
        return heroName;
    }
}
