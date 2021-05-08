package com.socialgame.game.util.customisation;

/**
 * Version of customisation that is not read from disk.
 * Used for customisation sent between clients to prevent any changes from
 * accidentally being made to local customisation settings.
 */
public class UnlinkedCustomisation extends Customisation {
    private int colorSelection;
    private int hatSelection;
    private int topSelection;

    public UnlinkedCustomisation() {
        this(0, 0, 0);
    }

    public UnlinkedCustomisation(int colorSelection, int hatSelection, int topSelection) {
        this.colorSelection = colorSelection;
        this.hatSelection = hatSelection;
        this.topSelection = topSelection;
    }


    @Override
    public int getColorSelection() {
        return colorSelection;
    }

    @Override
    public void setColorSelection(int val) {
        if (val < 0 || val > colors.length - 1) return;
        colorSelection = val;
    }


    @Override
    public int getHatSelection() {
        return hatSelection;
    }

    @Override
    public void setHatSelection(int val) {
        if (val < 0 || val > hats.length - 1) return;
        hatSelection = val;
    }


    @Override
    public int getTopSelection() {
        return topSelection;
    }

    @Override
    public void setTopSelection(int val) {
        if (val < 0 || val > tops.length - 1) return;
        topSelection = val;
    }
}
