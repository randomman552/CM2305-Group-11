package com.socialgame.game.player.clothing;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.MultiSprite;
import com.socialgame.game.player.PlayerCustomisation;

// FIXME: 05/04/2021 Hats do not all align in the same way when drawn on the player.
// This can be fixed with an individual offset for each hat type, or standardisation of sprites.
public class Hat extends MultiSprite {
    private static final Vector2 SIZE = new Vector2(0.65f, 0.65f);
    /**
     * Array of offsets for drawing hats.
     * z element is a rotation amount in degrees.
     * FIXME: This is kinda janky
     */
    private final static Vector3[] hatOffSet = {
            new Vector3(-0.1f,-0.1f,0),     //hat1
            new Vector3(-0.125f,0f,-15),    //hat2
            new Vector3(-0.1f,-0.25f,0),    //hat3
            new Vector3(-0.1f,-0.1f,0)      //hat4
    };

    private final PlayerCustomisation customisation;

    public Hat(SocialGame game) {
        this(game, game.customisation);
    }

    public Hat(SocialGame game, PlayerCustomisation customisation) {
        super(game, SIZE.x, SIZE.y);
        setColor(customisation.getColor());

        this.customisation = customisation;

        // Prepare textures
        Array<TextureAtlas.AtlasRegion> textures = game.wearablesSpriteSheet.findRegions(customisation.getHatName());
        for (int i = 0; i < textures.size; i++) {
            TextureRegion t = new TextureRegion(textures.get(i));
            // Only allow colors to be applied if this is the last texture in the array
            addDrawable(t, i == textures.size - 1);
        }

        setRotation(getOffset().z);
    }//

    public Vector3 getOffset() {
        return hatOffSet[customisation.getHatSelection()];
    }
}
