package com.socialgame.game.util.customisation;

import com.badlogic.gdx.graphics.Color;

/**
 * Class holding information for player customisation.
 * Use {@link LinkedCustomisation} LinkedCustomisation is used for local client.
 * Use {@link UnlinkedCustomisation} UnlinkedCustomisation is used for inter client communication of customisation.
 */
public abstract class Customisation {
    /**
     * Array of colors which players can choose between
     */
    protected Color[] colors = {
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
    protected String[] hats = {
            "hat1",
            "hat2",
            "hat3",
            "hat4",
            "hat5",
            "hat6"
    };

    /**
     * Array of tops players can choose between.
     * NOTE: This only contains the names of these tops, not the textures themselves.
     * TODO: Add tops (or choose to not implement them)
     */
    protected String[] tops = {

    };

    /**
     * @return Users current color selection
     */
    public abstract int getColorSelection();

    /**
     * Set users current color selection
     */
    public abstract void setColorSelection(int val);


    /**
     * @return Users current hat selection
     */
    public abstract int getHatSelection();

    /**
     * Set the users hat selection.
     */
    public abstract void setHatSelection(int val);


    /**
     * @return The users current top selection.
     */
    public abstract int getTopSelection();

    /**
     * Set the users current top selection.
     */
    public abstract void setTopSelection(int val);


    /**
     * Helper method to retrieve the top name of this customisation instance.
     */
    public String getTopName() {
        return getTopName(getTopSelection());
    }

    /**
     * @param idx Index to query.
     * @return Top name for the top with the given index.
     */
    public String getTopName(int idx) {
        if (idx < 0 || idx > tops.length - 1) return null;
        return tops[idx];
    }

    /**
     * Helper method to retrieve the hat name of this customisation instance.
     */
    public String getHatName() {
        return getHatName(getHatSelection());
    }

    /**
     * @param idx Index to query.
     * @return Hat name for the top with the given index.
     */
    public String getHatName(int idx) {
        if (idx < 0 || idx > hats.length - 1) return null;
        return hats[idx];
    }


    /**
     * Method to get the currently selected color for this customisation instance.
     * @return Currently selected user color.
     */
    public Color getColor() {
        return getColor(getColorSelection());
    }

    /**
     * Method to get the specified color from the permitted colors.
     * @param idx Index to query.
     * @return The color with the given index. Or null if index is out of range.
     */
    public  Color getColor(int idx) {
        if (idx < 0 || idx > colors.length - 1) return null;
        return colors[idx];
    }
}
