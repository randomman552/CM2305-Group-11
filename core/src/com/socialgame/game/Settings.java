package com.socialgame.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Class used to store and pass around game settings between screens
 * If a setting needs to be stored between game sessions it should be implemented here
 * Settings are stored in a file in the working directory of the game
 * Can be loaded and saved using the load and save methods
 */
public class Settings {
    /**
     * Desired resolution of the game stored as a string in form "widthxheight"
     * For example "1929x1080".
     */
    private String resolution = "1280x720";
    /**
     * Desired master volume (scalar between 0 and 1)
     */
    private float masterVol = 1;
    /**
     * Desired effects volume (scalar between 0 and 1)
     */
    private float SFXVol = 1;
    /**
     * Desired music volume (scalar between 0 and 1)
     */
    private float musicVol = 1;

    /**
     * Location of the file this settings object corresponds to.
     */
    private final String fileLoc = "./settings.cfg";

    public Settings() {
        load();
    }


    public void setResolution(String resolution) {
        this.resolution = resolution;
        save();
    }

    public String getResolution() {
        return resolution;
    }


    public void setMasterVol(float vol) {
        this.masterVol = vol;
        save();
    }

    public float getMasterVol() {
        return masterVol;
    }


    public void setSFXVol(float vol) {
        this.SFXVol = vol;
        save();
    }

    public float getSFXVol() {
        return this.SFXVol;
    }


    public void setMusicVol(float vol) {
        this.musicVol = vol;
        save();
    }

    public float getMusicVol() {
        return this.musicVol;
    }


    @Override
    public String toString() {
        return "{\n" +
                "\tresolution:" + resolution + ",\n" +
                "\tmasterVol:" + masterVol + ",\n" +
                "\tSFXVol:" + SFXVol + ",\n" +
                "\tmusicVol:" + musicVol + ",\n" +
                "}\n";
    }

    public void save() {
        try {
            PrintWriter out = new PrintWriter(fileLoc);
            out.print(toString());
            out.close();
        } catch (FileNotFoundException ignored){}
    }

    // FIXME: 05/04/2021 LibGDX has a built in JSON parser we can use instead of this mess
    private void load(Scanner reader) {
        while (reader.hasNextLine()) {
            // Split each line by json separator and remove final , character
            String[] pair = reader.nextLine().replace(",", "").split(":");

            // Load the value for each pair into the appropriate field
            // Ignores rows with no known match
            switch (pair[0]) {
                case "resolution":
                    setResolution(pair[1]);
                    break;
                case "masterVol":
                    setMasterVol(Float.parseFloat(pair[1]));
                    break;
                case "SFXVol":
                    setSFXVol(Float.parseFloat(pair[1]));
                    break;
                case "musicVol":
                    setMusicVol(Float.parseFloat(pair[1]));
                    break;
                default:
                    break;
            }
        }
    }

    private void load(String s) {
        Scanner reader = new Scanner(s);
        load(reader);
        reader.close();
    }

    private void load(File f) throws FileNotFoundException {
        File file = new File(fileLoc);
        Scanner reader = new Scanner(file);
        load(reader);
        reader.close();
    }

    public void load() {
        try {
            File file = new File(fileLoc);
            load(file);
        } catch (FileNotFoundException e) {
            // If the file is not found, save default values
            save();
        }
    }
}
