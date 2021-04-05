package com.socialgame.game.player.clothing;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.MultiSprite;
import com.socialgame.game.player.PlayerCustomisation;

public class Hat extends MultiSprite {
    private static final Vector2 SIZE = new Vector2(0.65f, 0.65f);

    public Hat(SocialGame game) {
        this(game, game.customisation);
    }

    public Hat(SocialGame game, PlayerCustomisation customisation) {
        super(game, SIZE.x, SIZE.y);
        setColor(customisation.getColor());

        // Prepare textures
        Array<TextureAtlas.AtlasRegion> textures = game.wearablesSpriteSheet.findRegions(customisation.getHatName());
        for (int i = 0; i < textures.size; i++) {
            TextureRegionDrawable d = new TextureRegionDrawable(textures.get(i));
            // Only allow colors to be applied if this is the last texture in the array
            addDrawable(d, i == textures.size - 1);
        }
    }
}
