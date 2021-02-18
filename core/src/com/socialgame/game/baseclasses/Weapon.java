package com.socialgame.game.baseclasses;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.socialgame.game.SocialGame;

/**
 * Base class form which all weapons are derived
 * Extension of Item class
 */
public abstract class Weapon extends Item{
    /**
     * Weapon constructor, sets the size and rotation of the given weapon based on the sprite and parameters provided.
     * @param game Instance of the SocialGame class
     * @param texture TextureRegion to be used when drawing this weapon instance
     * @param scale Scale factor to scale the given texture region by
     * @param rotation The amount the weapon is rotated by default (in degrees)
     */
    public Weapon(SocialGame game, TextureRegion texture, float scale, float rotation) {
        super(game);
        this.texture = texture;

        // Set the size, origin, and rotation of this weapon instance
        float sizeRatio = (float)texture.getRegionWidth() / (float)texture.getRegionHeight();
        setSize(scale * sizeRatio, scale);
        setOrigin(scale * sizeRatio / 2, scale / 2);
        setRotation(rotation);
    }

    /**
     * Attack using this weapon along the given vector
     * @param v 2D vector to attack along
     */
    public void attack(Vector2 v) {

    }
}
