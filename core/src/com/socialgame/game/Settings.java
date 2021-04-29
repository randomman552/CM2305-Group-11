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
    private static final String fileName = "SocialGame/settings.xml";

    private static final String resolutionKey = "resolution";
    private static final String masterVolKey = "masterVol";
    private static final String SFXVolKey = "SFXVol";
    private static final String musicVolKey = "musicVol";
    private static final String micVolKey = "micVol";
    private static final String debugKey = "debug";

    private static final String defaultResolution = "1280x720";
    private static final float defaultMasterVol = 1;
    private static final float defaultSFXVol = 1;
    private static final float defaultMusicVol = 1;
    private static final float defaultMicVolKey = 1;
    private static final boolean defaultDebug = false;

    public Settings() {
        pref = Gdx.app.getPreferences(fileName);
    }


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


    public boolean getDebug() {
        return pref.getBoolean(debugKey, defaultDebug);
    }

    public void setDebug(boolean val) {
        pref.putBoolean(debugKey, val);
    }


    public void setMic(String mic) {
        pref.putString(micKey, mic);
    }

    public String getMic() { 
        return pref.getString(micKey, defaultMic); 
    }


    public void setMicVol(float val) {
        pref.putFloat(micVolKey, val);
    }

    public float getMicVol() {
        return pref.getFloat(micVolKey, defaultMicVolKey);
    }


    public void save() {
        pref.flush();
    }

    public void reset() {
        pref.clear();
    }
}
