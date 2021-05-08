package com.socialgame.game.util.customisation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Version of customisation that is saved to a file.
 * DO NOT use this to send information between clients, as it will cause issues.
 */
public class LinkedCustomisation extends Customisation {
    private static final String colorSelectionKey = "colorSelection";
    private static final String hatSelectionKey = "hatSelection";
    private static final String topSelectionKey = "topSelection";

    private static final int defaultColorSelection = 0;
    private static final int defaultHatSelection = 0;
    private static final int defaultTopSelection = 0;

    private static final String fileName = "SocialGame/customisation.xml";


    private final Preferences pref;

    public LinkedCustomisation() {
        pref = Gdx.app.getPreferences(fileName);
    }


    @Override
    public int getColorSelection() {
        return pref.getInteger(colorSelectionKey, defaultColorSelection);
    }

    @Override
    public void setColorSelection(int val) {
        if (val < 0 || val > colors.length - 1) return;
        pref.putInteger(colorSelectionKey, val);
    }


    @Override
    public int getHatSelection() {
        return pref.getInteger(hatSelectionKey, defaultHatSelection);
    }

    @Override
    public void setHatSelection(int val) {
        if (val < 0 || val > hats.length - 1) return;
        pref.putInteger(hatSelectionKey, val);
    }


    @Override
    public int getTopSelection() {
        return pref.getInteger(topSelectionKey, defaultTopSelection);
    }

    @Override
    public void setTopSelection(int val) {
        if (val < 0 || val > tops.length - 1) return;
        pref.putInteger(topSelectionKey, val);
    }


    public void save() {
        pref.flush();
    }
}
