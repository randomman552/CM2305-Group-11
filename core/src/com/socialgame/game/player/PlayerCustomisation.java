package com.socialgame.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;

/**
 * Class holding information for player customisation
 * Is used to pass a clients customisation information between the main menu and the game screen.
 * Can be saved and loaded from disk using the save and load methods
 */
public class PlayerCustomisation {
    private Preferences pref;

    private final String colorSelectionKey = "colorSelection";
    private final String hatSelectionKey = "hatSelection";
    private final String topSelectionKey = "topSelection";

    private int defaultColorSelection = 0;
    private int defaultHatSelection = 0;
    private int defaultTopSelection = 0;

    private final String fileName = "SocialGame/customisation.xml";
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
            "hat4",
            "hat5",
            "hat6"
    };

    /**
     * Array of tops players can choose between.
     * NOTE: This only contains the names of these tops, not the textures themselves.
     * TODO: Add tops (or choose to not implement them)
     */
    private final static String[] tops = {

    };

    public PlayerCustomisation() {
        pref = Gdx.app.getPreferences(fileName);
    }

    /**
     * Method to get the currently selected color for this customisation instance.
     * @return Currently selected user color.
     */
    public Color getColor() {
        return getColor(pref.getInteger(colorSelectionKey, defaultColorSelection));
    }

    /**
     * Method to get the specified color from the permitted colors.
     * @param idx Index to query.
     * @return The color with the given index. Or null if index is out of range.
     */
    public Color getColor(int idx) {
        if (idx < 0 || idx > colors.length - 1) return null;
        return colors[idx];
    }


    public int getColorSelection() {
        return pref.getInteger(colorSelectionKey, defaultColorSelection);
    }

    public void setColorSelection(int val) {
        if (val < 0 || val > colors.length - 1) return;
        pref.putInteger(colorSelectionKey, val);
    }


    public int getHatSelection() {
        return pref.getInteger(hatSelectionKey, defaultHatSelection);
    }

    public String getHatName() {
        return hats[getHatSelection()];
    }

    public String getHatName(int idx) {
        if (idx < 0 || idx > hats.length - 1) return null;
        return hats[idx];
    }

    public void setHatSelection(int val) {
        if (val < 0 || val > hats.length - 1) return;
        pref.putInteger(hatSelectionKey, val);
    }

    public int getTopSelection() {
        return pref.getInteger(topSelectionKey, defaultTopSelection);
    }

    public String getTopName() {
        return tops[getTopSelection()];
    }

    public String getTopName(int idx) {
        if (idx < 0 || idx > tops.length - 1) return null;
        return tops[idx];
    }

    public void setTopSelection(int val) {
        if (val < 0 || val > tops.length - 1) return;
        pref.putInteger(topSelectionKey, val);
    }

    public void save() {
        pref.flush();
    }

}
