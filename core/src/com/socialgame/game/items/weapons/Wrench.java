package com.socialgame.game.items.weapons;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Weapon;

public class Wrench extends Weapon {
    public static final float SCALE = 0.5f;
    public static final String SPRITE_NAME = "wrench";

    public Wrench(SocialGame game) {
        this(game, 0, 0, 0);
    }

    public Wrench(SocialGame game, float x, float y) {
        this(game, x, y, 0);
    }

    public Wrench(SocialGame game, float x, float y, float rotation) {
        super(game, game.spriteSheet.findRegion(SPRITE_NAME), SCALE, rotation, x, y);
    }
}
