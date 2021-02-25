package com.socialgame.game.items.weapons;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Weapon;

public class Sword extends Weapon {
    public static final float SCALE = 1f;
    public static final String SPRITE_NAME = "sword";

    public Sword(SocialGame game) {
        this(game, 0, 0, 0);
    }

    public Sword(SocialGame game, float x, float y) {
        this(game, x, y, 0);
    }

    public Sword(SocialGame game, float x, float y, float rotation) {
        super(game, game.spriteSheet.findRegion(SPRITE_NAME), SCALE, rotation, x, y);
    }
}
