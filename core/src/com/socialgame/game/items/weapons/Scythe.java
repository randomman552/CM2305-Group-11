package com.socialgame.game.items.weapons;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Weapon;

public class Scythe extends Weapon {
    public static final float SCALE = 1f;
    public static final float ROTATION = 0;

    public Scythe(SocialGame game) {
        super(game, game.spriteSheet.findRegion("scythe"), SCALE, ROTATION);
    }
}
