package com.socialgame.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;

import java.util.ArrayList;

/**
 * Class to represent GameObjects with multiple layers.
 * Textures added to this class are drawn in the order they are added.
 * All provided textures should be the same size for results to be as expected.
 * All GameObject methods remain the same.
 */
public abstract class MultiSprite extends GameObject {
    protected final ArrayList<TextureRegion> textures = new ArrayList<>();
    protected final ArrayList<Boolean> colorMask = new ArrayList<>();

    public MultiSprite(SocialGame game) {
        this(game, 1, 1);
    }

    public MultiSprite(SocialGame game, float width, float height) {
        super(game, width, height, 0, 0);
    }

    public MultiSprite(SocialGame game, float width, float height, float x, float y) {
        super(game, width, height, x, y);
    }

    public void addDrawable(TextureRegion t, boolean applyColor) {
        textures.add(t);
        colorMask.add(applyColor);
    }

    public void addDrawable(TextureRegion t) {
        addDrawable(t, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color oldCol = batch.getColor();
        Color newCol = this.getColor();
        newCol.a = parentAlpha;

        for (int i = 0; i < textures.size(); i++) {
            TextureRegion t = textures.get(i);
            boolean cMask = colorMask.get(i);

            // Set color of batch if mask requires it
            if (cMask) {
                batch.setColor(newCol);
            } else {
                batch.setColor(oldCol);
            }

            if (!(getFlip() && !t.isFlipX() || !getFlip() && t.isFlipX()))
                t.flip(true, false);

            batch.draw(t,
                    getBottomLeftX(), getBottomLeftY(),
                    getOriginX(), getOriginY(),
                    getWidth(), getHeight(),
                    getScaleX(), getScaleY(),
                    (getFlip()) ? getRotation() : -getRotation());
        }

        batch.setColor(oldCol);
    }
}
