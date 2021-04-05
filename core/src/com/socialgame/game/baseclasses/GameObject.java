package com.socialgame.game.baseclasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.socialgame.game.SocialGame;

/**
 * Base class from which all in game objects are derived.
 */
public abstract class GameObject extends Actor {
    protected SocialGame game;

    public Body body;
    public TextureRegion texture;

    private boolean flip = false;

    public GameObject(SocialGame game, float width, float height, float x, float y) {
        this.game = game;
        setOrigin(width / 2, height / 2);
        setSize(width, height);
        setupRigidBody();
        setPositionAboutOrigin(x, y);
    }

    public GameObject(SocialGame game, float width, float height) {
        this(game, width, height, 0, 0);
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
        body = game.getPhysWorld().createBody(bodyDef);
        body.setUserData(this);

        // Synchronise the position of the body and GameObject
        setPosition(getX(), getY());
    }

    /**
     * Set position of this GameObject about it's origin (in game world coordinates).
     * @param x Desired x coordinate
     * @param y Desired y coordinate
     */
    public void setPositionAboutOrigin(float x, float y) {
        super.setPosition(x - getOriginX(), y - getOriginY());
    }

    /**
     * Set X coordinate of this GameObject (about it's origin)
     * @param x Desired X coordinate
     */
    public void setXAboutOrigin(float x) {
        setPositionAboutOrigin(x, getY());
    }

    /**
     * Set Y coordinate of this GameObject (about it's origin)
     * @param y Desired Y coordinate
     */
    public void setYAboutOrigin(float y) {
        setPositionAboutOrigin(getX(), y);
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public boolean getFlip() {
        return flip;
    }

    public float getBottomLeftX() {
        return super.getX();
    }

    public float getBottomLeftY() {
        return super.getY();
    }

    @Override
    protected void positionChanged() {
        body.setTransform(getX(), getY(), body.getAngle());
    }

    @Override
    protected void rotationChanged() {
        body.setTransform(body.getPosition().x, body.getPosition().y, (float) Math.toRadians(getRotation()));
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
    }

    @Override
    public float getX() {
        return super.getX() + getOriginX();
    }

    @Override
    public float getY() {
        return super.getY() + getOriginY();
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

        // Flip texture if moving left and it is not already flipped
        // Or flip it back to default if we are not moving left and it is already flipped
        if (flip && !texture.isFlipX() || !flip && texture.isFlipX())
            texture.flip(true, false);

        // Use super getX and getY to get screen space coordinates rather than game space ones
        batch.draw(getKeyFrame(game.elapsedTime),
                getBottomLeftX(), getBottomLeftY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());

        batch.setColor(oldCol);
    }
}
