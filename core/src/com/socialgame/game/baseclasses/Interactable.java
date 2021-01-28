package com.socialgame.game.baseclasses;

import com.socialgame.game.SocialGame;

/**
 * Base class from which all interactable game objects are derived
 * Extension of GameObject
 */
public class Interactable extends GameObject {
    public Interactable(SocialGame game) {
        super(game);
    }

    /**
     * @param caller The GameObject which called interact on this Interactable
     */
    public void interact(GameObject caller) {

    }
}
