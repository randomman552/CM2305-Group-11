package com.socialgame.game.baseclasses;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.socialgame.game.SocialGame;
import com.socialgame.game.items.weapons.*;
import com.socialgame.game.player.Player;
import com.socialgame.game.tasks.Task;
import com.socialgame.game.tasks.async.ClockCalibrationTask;
import com.socialgame.game.tasks.async.SimonSaysTask;

/**
 * Base class from which all items are derived
 * Extension of Interactable
 */
public abstract class Item extends Interactable {
    private boolean flip = false;

    public static Item create(int i, SocialGame game, float x, float y) {
        i = i % 5;
        switch (i) {
            case 0:
                return new Axe(game, x, y);
            case 1:
                return new Lightsword(game, x, y);
            case 2:
                return new Scythe(game, x, y);
            case 3:
                return new Sword(game, x, y);
            case 4:
                return new Wrench(game, x, y);
        }
        return null;
    }

    public Item(SocialGame game, float x, float y, float width, float height) {
        super(game, x, y, width, height);
    }

    public Item(SocialGame game, float width, float height) {
        super(game, width, height);
    }

    public Item(SocialGame game) {
        super(game);
    }

    @Override
    public void interact(GameObject caller) {
        if (caller instanceof Player)
            ((Player) caller).pickupItem(this);
    }

    public void setFlip(boolean val) {
        flip = val;
    }

    @Override
    public TextureRegion getKeyFrame(float time) {
        if (flip && !texture.isFlipX() || !flip && texture.isFlipX())
            texture.flip(true, false);
        return texture;
    }
}
