package com.socialgame.game.player.clothing;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.MultiSprite;
import com.socialgame.game.util.customisation.Customisation;

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
    /*hat1*/ new Vector3(-0.075f,-0.1f,0),
    /*hat2*/ new Vector3(-0.15f,0f,-13),
    /*hat3*/ new Vector3(-0.1f,-0.1f,5),
    /*hat4*/ new Vector3(-0.1f,-0.1f,0),
    /*hat5*/ new Vector3(-0.1f,-0.1f,0),
    /*hat6*/ new Vector3(-0.16f,-0.02f,-17)
    };

    private Customisation customisation;

    public Hat(SocialGame game) {
        this(game, game.customisation);
    }

    public Hat(SocialGame game, Customisation customisation) {
        super(game, SIZE.x, SIZE.y);
        setCustomisation(customisation);
    }

    public void setHatType(int type) {
        this.textures.clear();
        this.colorMask.clear();
        Array<TextureAtlas.AtlasRegion> textures = game.wearablesSpriteSheet.findRegions(customisation.getHatName(type));
        for (int i = 0; i < textures.size; i++) {
            TextureRegion t = new TextureRegion(textures.get(i));
            // Only allow colors to be applied if this is the last texture in the array
            addTexture(t, i == textures.size - 1);
        }
        setRotation(getOffset().z);
    }

    /**
     * Update hat customisation based on passed customisation object
     */
    public void setCustomisation(Customisation customisation) {
        this.customisation = customisation;
        setColor(customisation.getColor());
        setHatType(customisation.getHatSelection());
    }


    public Vector3 getOffset() {
        return hatOffSet[customisation.getHatSelection()];
    }
}
