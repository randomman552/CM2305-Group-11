package com.socialgame.game.baseclasses;

import com.socialgame.game.SocialGame;
import com.socialgame.game.player.Player;

/**
 * Base class from which all items are derived
 * Extension of Interactable
 */
public abstract class Item extends Interactable {
    public Item(SocialGame game) {
        super(game);
    }

    @Override
    public void interact(GameObject caller) {
        if (caller instanceof Player)
            ((Player) caller).pickupItem(this);
    }
}
