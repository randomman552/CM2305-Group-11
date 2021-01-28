package com.socialgame.game.baseclasses;

import com.badlogic.gdx.math.Vector2;
import com.socialgame.game.SocialGame;

/**
 * Base class form which all weapons are derived
 * Extension of Item class
 */
public class Weapon extends Item{

    public Weapon(SocialGame game) {
        super(game);
    }

    /**
     * Attack using this weapon along the given vector
     * @param v 2D vector to attack along
     */
    public void attack(Vector2 v) {

    }
}
