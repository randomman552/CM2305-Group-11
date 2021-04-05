package com.socialgame.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;

import javax.swing.plaf.multi.MultiSpinnerUI;
import java.util.ArrayList;

public abstract class MultiSprite extends GameObject {
    private final ArrayList<Drawable> drawables = new ArrayList<>();
    private final ArrayList<Boolean> colorMask = new ArrayList<>();

    public MultiSprite(SocialGame game) {
        this(game, game.customisation);
    }

    public MultiSprite(SocialGame game, PlayerCustomisation customisation) {
        super(game);
        setColor(customisation.getUserColour());
    }

    public void addDrawable(Drawable d, boolean colorState) {
        drawables.add(d);
        colorMask.add(colorState);
    }

    public void addDrawable(Drawable d) {
        addDrawable(d, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color oldCol = batch.getColor();

        for (int i = 0; i < drawables.size(); i++) {
            Drawable d = drawables.get(i);
            boolean cMask = colorMask.get(i);

            // Set color of batch if mask requires it
            if (cMask) {
                batch.setColor(this.getColor());
            } else {
                batch.setColor(oldCol);
            }

            if (getFlip())
                d.draw(batch, getX(), getY(), getWidth(), getHeight());
            else
                d.draw(batch, getX() + getWidth(), getY(), -getWidth(), getHeight());
        }

        batch.setColor(oldCol);
    }
}
