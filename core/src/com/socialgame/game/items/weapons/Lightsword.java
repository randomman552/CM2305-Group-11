package com.socialgame.game.items.weapons;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Weapon;

public class Lightsword extends Weapon {
    public static final float SCALE = 0.15f;
    public static final float ROTATION = 90;

    public Lightsword(SocialGame game) {
        super(game, game.spriteSheet.findRegion("lightsword"), SCALE, ROTATION);
    }
}
