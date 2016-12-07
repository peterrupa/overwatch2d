package com.overwatch2d.game;

import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    public static final float ATTACKERS_SPAWN_X = 100;
    public static final float ATTACKERS_SPAWN_Y = 100;
    public static final float DEFENDERS_SPAWN_X = 1800;
    public static final float DEFENDERS_SPAWN_Y = 1500;

    private final float HERO_SELECTION_DURATION = 5f;
    private final float GAME_PREPARATION_DURATION = 5f + HERO_SELECTION_DURATION;
    private final float BATTLE_DURATION = 300f;

    private int state;

    private boolean heroSelectionFinished = false;

    private ArrayList<Hero> heroes = new ArrayList<Hero>();
    private ArrayList<Hero> heroesDestroyed = new ArrayList<Hero>();
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private ArrayList<Projectile> projectilesDestroyed = new ArrayList<Projectile>();

    private World world;

    private float countdown;

    private ArrayList<Player> players = new ArrayList<Player>();

    private int currentObjective;

    private float objective1Capture = 0f;
    private ArrayList<Hero> objective1Heroes = new ArrayList<Hero>();

    private float objective2Capture = 0f;
    private ArrayList<Hero> objective2Heroes = new ArrayList<Hero>();

    private boolean slowmo = false;

    private float preparationDuration = GAME_PREPARATION_DURATION;
    private boolean battleHasStarted = false;

    private float gameTimer = 1;

    GameState(ArrayList<Player> players) {
        this.countdown = HERO_SELECTION_DURATION;
        this.players = players;
    }

    public void setCurrentObjective(int obj) {
        currentObjective = obj;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean isBattleHasStarted() {
        return battleHasStarted;
    }

    public float getPreparationDuration() {
        return preparationDuration;
    }

    public int getCurrentObjective() {
        return currentObjective;
    }

    public float getObjective1Capture() {
        return objective1Capture;
    }

    public void setObjective1Capture(float x) {
        objective1Capture = x;
    }

    public float getObjective2Capture() {
        return objective2Capture;
    }

    public void setObjective2Capture(float x) {
        objective2Capture = x;
    }

    public float getGameTimer() {
        return gameTimer;
    }

    public void setGameTimer(float x) {
        gameTimer = x;
    }

    public boolean isHeroSelectionFinished() {
        return heroSelectionFinished;
    }

    public void setHeroSelectionFinished(boolean x) {
        heroSelectionFinished = x;
    }

    public float getCountdown() {
        return countdown;
    }

    public void setCountdown(float x) {
        countdown = x;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World w) {
        this.world = w;
    }

    public static float getAttackersSpawnX() {
        return ATTACKERS_SPAWN_X;
    }

    public static float getAttackersSpawnY() {
        return ATTACKERS_SPAWN_Y;
    }

    public static float getDefendersSpawnX() {
        return DEFENDERS_SPAWN_X;
    }

    public static float getDefendersSpawnY() {
        return DEFENDERS_SPAWN_Y;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public ArrayList<Projectile> getProjectilesDestroyed() {
        return projectilesDestroyed;
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public ArrayList<Hero> getHeroesDestroyed() {
        return heroesDestroyed;
    }

    public ArrayList<Hero> getObjective1Heroes() {
        return objective1Heroes;
    }

    public ArrayList<Hero> getObjective2Heroes() {
        return objective2Heroes;
    }

    public void setPreparationDuration(float x) {
        preparationDuration = x;
    }

    public void setBattleHasStarted(boolean x) {
        battleHasStarted = x;
    }

    public int getState() {
        return state;
    }

    public void setState(int x) {
        state = x;
    }

    public boolean isSlowmo() {
        return slowmo;
    }

    public void setSlowmo(boolean x) {
        slowmo = x;
    }

    public float getBattleDuration() {
        return BATTLE_DURATION;
    }
}
