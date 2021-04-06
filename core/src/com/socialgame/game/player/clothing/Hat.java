package com.socialgame.game.player.clothing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.MultiSprite;
import com.socialgame.game.player.PlayerCustomisation;

import java.util.ArrayList;

// FIXME: 05/04/2021 Hats do not all align in the same way when drawn on the player.
// This can be fixed with an individual offset for each hat type, or standardisation of sprites.
public class Hat extends MultiSprite {
    private static final Vector2 SIZE = new Vector2(0.65f, 0.65f);
    private final static Vector2[] hatOffSet = {
            new Vector2(0f,0f),//hat1
            new Vector2(0f,0f),//hat2
            new Vector2(0f,0f),//hat3
            new Vector2(0f,0f),//hat4
            new Vector2(0f,0f),
            new Vector2(0f,0f)
    };



    public Hat(SocialGame game) {
        this(game, game.customisation);
    }

    public Hat(SocialGame game, PlayerCustomisation customisation) {
        super(game, SIZE.x, SIZE.y);
        float x = hatOffSet[customisation.getHatSelection()].x;
        float y = hatOffSet[customisation.getHatSelection()].y;
        setColor(customisation.getColor());
        setOrigin(x, y);

        // Prepare textures
        Array<TextureAtlas.AtlasRegion> textures = game.wearablesSpriteSheet.findRegions(customisation.getHatName());
        for (int i = 0; i < textures.size; i++) {
            TextureRegionDrawable d = new TextureRegionDrawable(textures.get(i));
            // Only allow colors to be applied if this is the last texture in the array
            addDrawable(d, i == textures.size - 1);
        }
    }
}
