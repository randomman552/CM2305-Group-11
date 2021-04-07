package com.socialgame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Class used to store and pass around game settings between screens
 * If a setting needs to be stored between game sessions it should be implemented here
 * Settings are stored in a file in the working directory of the game
 * Can be loaded and saved using the load and save methods
 */
public class Settings {
    private final Preferences pref;

    private final String resolutionKey = "resolution";
    private final String masterVolKey = "masterVol";
    private final String SFXVolKey = "SFXVol";
    private final String musicVolKey = "musicVolKey";

    private final String defaultResolution = "1280x720";
    private final float defaultMasterVol = 1;
    private final float defaultSFXVol = 1;
    private final float defaultMusicVol = 1;

    private final String fileName = "SocialGame/settings.xml";

    public Settings() {
        pref = Gdx.app.getPreferences(fileName);
    }

    //FIXME:
    // - Implement the save function into the options screen.
    // - Add and implement a pref.clear function to return to defaults.


    public void setResolution(String resolution) {
        pref.putString(resolutionKey, resolution);
    }

    public String getResolution() {
        return pref.getString(resolutionKey, defaultResolution);
    }


    public void setMasterVol(float vol) {
        pref.putFloat(masterVolKey, vol);
    }

    public float getMasterVol() {
        return pref.getFloat(masterVolKey, defaultMasterVol);
    }


    public void setSFXVol(float vol) {
        pref.putFloat(SFXVolKey, vol);
    }

    public float getSFXVol() {
        return pref.getFloat(SFXVolKey, defaultSFXVol);
    }


    public void setMusicVol(float vol) {
        pref.putFloat(musicVolKey, vol);
    }

    public float getMusicVol() {
        return pref.getFloat(musicVolKey, defaultMusicVol);
    }


    public void save() {
        pref.flush();
    }
}
