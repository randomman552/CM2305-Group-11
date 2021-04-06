package com.socialgame.game.player;

import com.badlogic.gdx.graphics.Color;

/**
 * Class holding information for player customisation
 * Is used to pass a clients customisation information between the main menu and the game screen.
 * Can be saved and loaded from disk using the save and load methods
 */
public class PlayerCustomisation {
    private int colorSelection = 0;
    private int hatSelection = 2;
    private int topSelection = 0;

    /**
     * Array of colors which players can choose between
     */
    private final static Color[] colors = {
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
        new Color(0x777777ff),   // 119,119,119,255
        new Color(0x0d6685ff),   // 13,102,133,255
    };

    /**
     * Array of hats players can choose between.
     * NOTE: this only contains the names of the hats, not the textures themselves.
     */
    private final static String[] hats = {
            "hat1",
            "hat2",
            "hat3",
            "hat4"
    };

    /**
     * Array of tops players can choose between.
     * NOTE: This only contains the names of these tops, not the textures themselves.
     * TODO: Add tops (or choose to not implement them)
     */
    private final static String[] tops = {

    };


    /**
     * Method to get the currently selected color for this customisation instance.
     * @return Currently selected user color.
     */
    public Color getColor() {
        return getColor(colorSelection);
    }

    /**
     * Method to get the specified color from the permitted colors.
     * @param idx Index to query.
     * @return The color with the given index. Or null if index is out of range.
     */
    public Color getColor(int idx) {
        if (idx < 0 || idx > colors.length) return null;
        return colors[idx];
    }


    public int getColorSelection() {
        return colorSelection;
    }

    public void setColorSelection(int val) {
        if (val < 0 || val > colors.length) return;
        colorSelection = val;
    }


    public int getHatSelection() {
        return hatSelection;
    }

    public String getHatName() {
        return hats[getHatSelection()];
    }

    public void setHatSelection(int val) {
        if (val < 0 || val > hats.length) return;
        hatSelection = val;
    }


    public int getTopSelection() {
        return topSelection;
    }

    public String getTopName() {
        return tops[getTopSelection()];
    }

    public void setTopSelection(int val) {
        if (val < 0 || val > tops.length) return;
        topSelection = val;
    }


    //TODO: Implement save and load methods
    public void save() {

    }

    public void load() {

    }
}
