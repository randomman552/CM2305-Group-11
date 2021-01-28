package com.socialgame.game;

import com.badlogic.gdx.utils.Json;

/**
 * TODO: Settings
 * Class used to store and pass around game settings
 * If a setting needs to be stored between game sessions it should be implemented here
 * Settings are stored in a file in the working directory of the game
 * Can be loaded and saved using the load and save methods
 */
public class Settings {
    /**
     * Desired resolution of the game stored as a string in form "widthxheight"
     * For example "1929x1080".
     */
    public String resolution;
    /**
     * Desired master volume (scalar between 0 and 1)
     */
    public float masterVol;
    /**
     * Desired effects volume (scalar between 0 and 1)
     */
    public float SFXVol;
    /**
     * Desired music volume (scalar between 0 and 1)
     */
    public float musicVol;

    /**
     * Location of the file this settings object corresponds to.
     */
    protected String fileLoc;

    /**
     * @param fileLoc Location of the settings file to be used
     */
    public Settings(String fileLoc) {
        this.fileLoc = fileLoc;
    }

    public void save() {

    }

    public void load() {

    }
}
