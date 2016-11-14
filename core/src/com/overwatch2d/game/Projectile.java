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
    private Texture texture = new Texture(Gdx.files.internal("projectiles/sampleBullet.png"));
    private Body physicsBody;
    private float speed = 0.01f;
    private int damage = 4;
    private Hero owner;

    Projectile(float initialX, float initialY, float destX, float destY, Hero owner) {
        this.owner = owner;

        setSize(texture.getWidth(), texture.getHeight());

        setPosition(initialX, initialY);

        // insert particles here
        // GameScreen.stage.addActor(new Cursor(initialX, initialY));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / Config.PIXELS_TO_METERS, getY() / Config.PIXELS_TO_METERS);

        physicsBody = GameScreen.world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2 / Config.PIXELS_TO_METERS, getHeight()/2 / Config.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f;
        fixtureDef.filter.categoryBits = Config.PROJECTILE_ENTITY;
        fixtureDef.filter.maskBits = Config.HERO_ENTITY;

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

    public void hit(Hero hitHero) {
        hitHero.damaged(damage, owner);

        GameScreen.projectilesDestroyed.add(this);
        this.remove();
    }
}
