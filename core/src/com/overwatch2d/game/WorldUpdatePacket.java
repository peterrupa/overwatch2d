package com.overwatch2d.game;

import java.io.Serializable;

/**
 * Created by Peter on 12/7/2016.
 */
public class WorldUpdatePacket implements Serializable {
    private int currentObjective;
    private float objective1Capture;
    private float objective2Capture;
    private float gameTimer;
    private boolean battleHasStarted;
    private float preparationDuration;

    public WorldUpdatePacket(int currentObjective, float objective1Capture, float objective2Capture, float gameTimer, boolean battleHasStarted, float preparationDuration) {
        this.currentObjective = currentObjective;
        this.objective1Capture = objective1Capture;
        this.objective2Capture = objective2Capture;
        this.gameTimer = gameTimer;
        this.battleHasStarted = battleHasStarted;
        this.preparationDuration = preparationDuration;
    }

    public int getCurrentObjective() {
        return currentObjective;
    }

    public float getObjective1Capture() {
        return objective1Capture;
    }

    public float getObjective2Capture() {
        return objective2Capture;
    }

    public float getGameTimer() {
        return gameTimer;
    }

    public boolean isBattleHasStarted() {
        return battleHasStarted;
    }

    public float getPreparationDuration() {
        return preparationDuration;
    }
}
