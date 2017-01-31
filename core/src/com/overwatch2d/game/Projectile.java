package com.overwatch2d.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Projectile extends Actor {
    private Texture texture;
    private Body physicsBody;
    private float speed = 0.01f;
    private int damage;
    private Hero owner;
    private float density = 0.01f;
    private float ttl = 0.5f;
    private boolean hit = false;

    Projectile(float initialX, float initialY, float destX, float destY, int damage, Hero owner, Texture texture) {
        this.texture = texture;
        this.owner = owner;
        this.damage = damage;

        setSize(texture.getWidth(), texture.getHeight());

        setPosition(initialX, initialY);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / Config.PIXELS_TO_METERS, getY() / Config.PIXELS_TO_METERS);

        physicsBody = GameScreen.getGameState().getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2 / Config.PIXELS_TO_METERS, getHeight()/2 / Config.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;

        if(owner.getPlayer().getTeam() == 0) {
            fixtureDef.filter.categoryBits = Config.PROJECTILE_ENTITY_0;
            fixtureDef.filter.maskBits = Config.HERO_ENTITY_1 | Config.OBSTACLES;
        }
        else {
            fixtureDef.filter.categoryBits = Config.PROJECTILE_ENTITY_1;
            fixtureDef.filter.maskBits = Config.HERO_ENTITY_0 | Config.OBSTACLES;
        }

        physicsBody.createFixture(fixtureDef);
        physicsBody.setBullet(true);

        shape.dispose();

        GameScreen.stage.addActor(this);

        double degrees = Math.atan2(
                destY / Config.PIXELS_TO_METERS - physicsBody.getWorldCenter().y,
                destX / Config.PIXELS_TO_METERS - physicsBody.getWorldCenter().x
        ) * 180.0d / Math.PI;

        physicsBody.setFixedRotation(true);

        physicsBody.setUserData(this);

        physicsBody.setTransform(physicsBody.getWorldCenter(), (float)Math.toRadians(degrees));

        float xPoint = speed * (float)Math.cos(Math.toRadians(degrees));
        float yPoint = speed * (float)Math.sin(Math.toRadians(degrees));

        physicsBody.applyLinearImpulse(
            xPoint,
            yPoint,
            physicsBody.getWorldCenter().x,
            physicsBody.getWorldCenter().y,
            true
        );
    }

    public void draw(Batch batch, float alpha){
        ttl -= Gdx.graphics.getDeltaTime();

        if(ttl < 0) {
            die();
        }

        batch.draw(
            texture,
            getX() - texture.getWidth() / 2,
            getY() - texture.getHeight() / 2,
            (float)texture.getWidth() / 2,
            (float)texture.getHeight() / 2,
            (float)texture.getWidth(),
            (float)texture.getHeight(),
            1f,
            1f,
            getRotation(),
            (int)getX(),
            (int)getY(),
            texture.getWidth(),
            texture.getHeight(),
            false,
            false
        );
    }

    public Body getBody() {
        return physicsBody;
    }

    public Hero getOwner() { return owner; }

    public void hit(Hero hitHero) {
        hitHero.damaged(damage, owner);

        hit = true;

        die();
    }

    public void die() {
        hit = true;
        GameScreen.getGameState().getProjectilesDestroyed().add(this);
        this.remove();
    }

    public boolean isHit() {
        return hit;
    }
}
