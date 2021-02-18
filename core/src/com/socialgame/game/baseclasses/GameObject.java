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

    public GameObject(SocialGame game, float x, float y, float width, float height) {
        this.game = game;
        setScale(SCALE, SCALE);
        setBounds(x, y, width, height);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setupRigidBody();
    }

    public GameObject(SocialGame game, float width, float height) {
        this(game, 0, 0, width, height);
    }

    public GameObject(SocialGame game) {
        this(game, 0, 0, 0, 0);
    }

    public TextureRegion getKeyFrame(float time) {
        return texture;
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
        this.setPosition(body.getPosition().x - getOriginX(), body.getPosition().y - getOriginY(), false);
        this.setRotation((float) Math.toDegrees(body.getAngle()));
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getKeyFrame(game.elapsedTime),
                getX(),
                getY(),
                getOriginX(),
                getOriginY(),
                getWidth(),
                getHeight(),
                getScaleX(), getScaleY(),
                getRotation());
    }

    @Override
    public void setPosition(float x, float y) {
        setPosition(x, y, true);
    }

    public void setPosition(float x, float y, boolean updateBody) {
        if (updateBody)
            body.setTransform(x + getOriginX(), y + getOriginY(), body.getAngle());
        super.setPosition(x, y);
    }

    @Override
    public void setRotation(float degrees) {
        super.setRotation(degrees);
        body.setTransform(body.getPosition().x, body.getPosition().y, (float) Math.toRadians(degrees));
    }
}
