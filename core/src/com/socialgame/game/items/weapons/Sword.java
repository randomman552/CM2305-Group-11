package com.socialgame.game.items.weapons;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Weapon;

public class Sword extends Weapon {
    public static final float SCALE = 1.25f;
    public static final float ROTATION = 90;

    public Sword(SocialGame game) {
        super(game, game.spriteSheet.findRegion("sword"), SCALE, ROTATION);
    }
}
