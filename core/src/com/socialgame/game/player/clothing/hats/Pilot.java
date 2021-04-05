package com.socialgame.game.player.clothing.hats;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.MultiSprite;

public class Pilot extends MultiSprite {
    private static final Vector2 SIZE = new Vector2(0.65f, 0.65f);

    public Pilot(SocialGame game) {
        super(game);
        addDrawable(new TextureRegionDrawable(game.wearablesSpriteSheet.findRegion("hat3", 1)));
        addDrawable(new TextureRegionDrawable(game.wearablesSpriteSheet.findRegion("hat3", 2)), true);
        setSize(SIZE.x, SIZE.y);
        setOrigin(SIZE.x / 2, SIZE.y / 2);
    }
}
