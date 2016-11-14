package com.overwatch2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Hero extends Actor {
    private Texture texture = new Texture(Gdx.files.internal("actor.png"));
    private Body physicsBody;
    private float speed = 3f;

    Hero(float initialX, float initialY) {
        setSize(texture.getWidth(), texture.getHeight());

        setPosition(initialX, initialY);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / Config.PIXELS_TO_METERS, getY() / Config.PIXELS_TO_METERS);

        physicsBody = GameScreen.world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2 / Config.PIXELS_TO_METERS, getHeight()/2 / Config.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 100f;
        fixtureDef.restitution = 0f;

        physicsBody.setLinearDamping(5f);
        physicsBody.setAngularDamping(5f);

        physicsBody.createFixture(fixtureDef);

        shape.dispose();

        GameScreen.stage.addActor(this);
    }

    public void setBody(Body body) {
        physicsBody = body;
    }

    @Override
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

    public float getSpeed() {
        return speed;
    }

    public void firePrimary() {
        Vector3 hoverCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 position = GameScreen.camera.unproject(hoverCoordinates);

        GameScreen.projectiles.add(new Projectile(physicsBody.getWorldCenter().x * Config.PIXELS_TO_METERS, physicsBody.getWorldCenter().y * Config.PIXELS_TO_METERS, position.x, position.y));
    }
}
