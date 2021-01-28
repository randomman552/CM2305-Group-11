package com.socialgame.game.baseclasses;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.socialgame.game.SocialGame;

/**
 * Base class from which all in game objects are derived.
 */
public class GameObject extends Actor {
    protected SocialGame game;

    public BodyDef body;
    public TextureRegion texture;

    public GameObject(SocialGame game) {
        this.game = game;
        body = new BodyDef();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Doesn't currently handle rotation
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
