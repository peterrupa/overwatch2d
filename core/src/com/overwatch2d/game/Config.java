package com.overwatch2d.game;

class Config {
    static final float PIXELS_TO_METERS = 100f;
    static final float CAMERA_OFFSET = 1.25f;
    static final short HERO_ENTITY_0 = 0x2 << 1;
    static final short HERO_ENTITY_1 = 0x2 << 2;
    static final short PROJECTILE_ENTITY_0 = 0x2 << 3;
    static final short PROJECTILE_ENTITY_1 = 0x2 << 4;
    static final short OBJECTIVE_ENTITY = 0x2 << 5;
    static final short DEAD_HERO = 0x2 << 6;
    static final float CAPPING_MODIFIER = 5f;
}
