package com.socialgame.game.player.clothing.hats;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.MultiSprite;
import com.socialgame.game.player.PlayerCustomisation;

public class Crown extends MultiSprite {
    private static final Vector2 SIZE = new Vector2(0.65f, 0.65f);

    public Crown(SocialGame game) {
        super(game);
        addDrawable(new TextureRegionDrawable(game.wearablesSpriteSheet.findRegion("hat2", 1)));
        addDrawable(new TextureRegionDrawable(game.wearablesSpriteSheet.findRegion("hat2", 2)), true);
        setSize(SIZE.x, SIZE.y);
    }

}
