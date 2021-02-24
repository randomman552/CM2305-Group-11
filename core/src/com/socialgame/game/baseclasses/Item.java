package com.socialgame.game.baseclasses;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.Player;

/**
 * Base class from which all items are derived
 * Extension of Interactable
 */
public abstract class Item extends Interactable {
    private boolean flip = false;

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
