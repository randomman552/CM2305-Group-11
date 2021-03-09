package com.socialgame.game.baseclasses;

import com.badlogic.gdx.graphics.Color;
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
    protected SocialGame game;

    public Body body;
    public TextureRegion texture;

    public GameObject(SocialGame game, float x, float y, float width, float height) {
        this.game = game;
        setOrigin(width / 2, height / 2);
        setBounds(x - getOriginX(), y - getOriginY(), width, height);
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
        // Need to scale up the body's position by scale so that all movements are translated in the correct way
        super.setPosition((body.getPosition().x - getOriginX()) * getScaleX(), (body.getPosition().y - getOriginY()) * getScaleY());
        super.setRotation((float) Math.toDegrees(body.getAngle()));
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color oldCol = batch.getColor();
        batch.setColor(this.getColor());

        // Use super getX and getY to get screen space coordinates rather than game space ones
        batch.draw(getKeyFrame(game.elapsedTime),
                super.getX(), super.getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());

        batch.setColor(oldCol);
    }

    /**
     * Set position of this GameObject (in game world coordinates).
     * @param x Desired x coordinate
     * @param y Desired y coordinate
     */
    @Override
    public void setPosition(float x, float y) {
        body.setTransform(x + getOriginX(), y + getOriginY(), body.getAngle());
        super.setPosition(x, y);
    }

    /**
     * Set position of this GameObject about it's origin (in game world coordinates).
     * @param x Desired x coordinate
     * @param y Desired y coordinate
     */
    public void setPositionAboutOrigin(float x, float y) {
        body.setTransform(x, y, body.getAngle());
        super.setPosition(x - getOriginX(), y - getOriginY());
    }

    /**
     * Set rotation of this GameObject
     * @param degrees The angle to apply, in degrees
     */
    @Override
    public void setRotation(float degrees) {
        body.setTransform(body.getPosition().x, body.getPosition().y, (float) Math.toRadians(degrees));
    }

    @Override
    public float getX() {
        return super.getX() + getOriginX();
    }

    @Override
    public float getY() {
        return super.getY() + getOriginY();
    }
}
