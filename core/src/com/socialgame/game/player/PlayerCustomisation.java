package com.socialgame.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class holding information for player customisation
 * Is used to pass a clients customisation information between the main menu and the game screen.
 * Can be saved and loaded from disk using the save and load methods
 */
public class PlayerCustomisation {
    public int userColor; //Defaults to 0
    public TextureRegion top;
    public TextureRegion hat;

    // Defaults to c1
    public final static Color[] colourList = {
        new Color(0xcd4229ff),   // 205,66,41,255
        new Color(0xdd7327ff),   // 221,115,39,255
        new Color(0xf1ea57ff),   // 241,234,87,255
        new Color(0x6ea516ff),   // 110,165,22,255
        new Color(0x29cda2ff),   // 41,205,162,255
        new Color(0x29afcdff),   // 41,175,205,255
        new Color(0xbc8de8ff),   // 188,141,232,255
        new Color(0x83439cff),   // 131,67,156,255
        new Color(0x995936ff),   // 153,89,54,255
        new Color(0x4f7513ff),   // 79,117,19,255
        new Color(0x777777ff),  // 119,119,119,255
        new Color(0x0d6685ff),  // 13,102,133,255
    };

    public Color colour(int colourIndex) {
        return colourList[colourIndex];
    }

    public Color getUserColour() {
        return colourList[userColor];
    }

    public int getColourIdx() {
        return userColor;
    }

    public void setUserColour(int colourListIndex) {
        userColor = colourListIndex;
    }


    public void save() {

    }

    public void load() {

    }
}
