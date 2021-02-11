package com.socialgame.game.baseclasses;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.socialgame.game.SocialGame;
import com.socialgame.game.screens.GameScreen;

/**
 * Base class from which all in game objects are derived.
 */
public abstract class GameObject extends Actor {
    /**
     * Defines the size of the physics units compared to pixels. This applies to ALL GameObject instances.
     * For example, scale of 50 means that 1 unit is equal to 50 pixels
     */
    public static float SCALE = 50;

    protected SocialGame game;

    public Body body;
    public TextureRegion texture;

    public GameObject(SocialGame game) {
        this.game = game;
        setScale(SCALE, SCALE);
        setupRigidBody();
    }

    /**
     * Method used to initialise the rigidbody for this object.
     * TODO: Currently this method only sets up a body for velocity, not collisions.
     */
    protected void setupRigidBody() {
        // Define body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Create body
        body = ((GameScreen) game.getScreen()).world.createBody(bodyDef);
        body.setUserData(this);

        // Synchronise the position of the body and GameObject
        setPosition(getX(), getY());
    }

    @Override
    public void act(float delta) {
        // Update position based on movement of the rigid body
        this.setX(body.getPosition().x - getOriginX());
        this.setY(body.getPosition().y - getOriginY());
        this.setRotation(body.getAngle() * (float)(180/Math.PI));



        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,
                (getX() + getOriginX()) * SCALE,
                (getY() + getOriginY()) * SCALE,
                getOriginX(),
                getOriginY(),
                getWidth(),
                getHeight(),
                SCALE, SCALE,
                getRotation());
    }

    @Override
    public void setPosition(float x, float y) {
        body.setTransform(x, y, body.getAngle());
    }
}
