package com.socialgame.game.items.weapons;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Weapon;

public class Sword extends Weapon {
    public static final float SCALE = 0.25f;
    public static final float ROTATION = 90;

    public Sword(SocialGame game) {
        this(game, 0, 0);
    }

    public Sword(SocialGame game, float x, float y) {
        super(game, game.spriteSheet.findRegion("sword"), SCALE, ROTATION, x, y);
    }
}
