package com.socialgame.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class holding information for player customisation
 * Is used to pass a clients customisation information between the main menu and the game screen.
 * Can be saved and loaded from disk using the save and load methods
 */
public class PlayerCustomisation {
    public static int userColor; //Defaults to 0
    public TextureRegion top;
    public TextureRegion hat;
    // Colour codes:
    //RGBA8888 0x[red][green][blue][alpha]
    Color c0 = new Color(0xcd4229ff);   // 205,66,41,255
    Color c1 = new Color(0xdd7327ff);   // 221,115,39,255
    Color c2 = new Color(0xf1ea57ff);   // 241,234,87,255
    Color c3 = new Color(0x6ea516ff);   // 110,165,22,255
    Color c4 = new Color(0x29cda2ff);   // 41,205,162,255
    Color c5 = new Color(0x29afcdff);   // 41,175,205,255
    Color c6 = new Color(0xbc8de8ff);   // 188,141,232,255
    Color c7 = new Color(0x83439cff);   // 131,67,156,255
    Color c8 = new Color(0x995936ff);   // 153,89,54,255
    Color c9 = new Color(0x4f7513ff);  // 79,117,19,255
    Color c10 = new Color(0x777777ff);  // 119,119,119,255
    Color c11 = new Color(0x0d6685ff);  // 13,102,133,255
    // Defaults to c1
    public Color[] colourList = {c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11};

    public Color colour(int colourIndex) {
        return colourList[colourIndex];
    }

    public Color getUserColour() {
        return colourList[userColor];
    }

    public void setUserColour(int colourListIndex) {
        userColor = colourListIndex;
    }


    public void save() {

    }

    public void load() {

    }
}
