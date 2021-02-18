package com.socialgame.game.items.weapons;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Weapon;

public class Wrench extends Weapon {
    public static final float SCALE = 0.25f;
    public static final float ROTATION = -45;

    public Wrench(SocialGame game) {
        this(game, 0, 0);
    }

    public Wrench(SocialGame game, float x, float y) {
        super(game, game.spriteSheet.findRegion("wrench"), SCALE, ROTATION, x, y);
    }
}
