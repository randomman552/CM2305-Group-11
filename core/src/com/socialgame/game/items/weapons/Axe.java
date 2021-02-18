package com.socialgame.game.items.weapons;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Weapon;

public class Axe extends Weapon {
    public static final float SCALE = 1f;
    public static final float ROTATION = 0;

    public Axe(SocialGame game) {
        this(game, 0, 0);
    }

    public Axe(SocialGame game, float x, float y) {
        super(game, game.spriteSheet.findRegion("axe"), SCALE, ROTATION, x, y);
    }
}
