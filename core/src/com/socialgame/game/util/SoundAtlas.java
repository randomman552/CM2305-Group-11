package com.socialgame.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class to contain all sounds from the game so they can be easily accessed by the game.
 */
public class SoundAtlas implements Disposable {
    private final Map<String, Sound> soundMap;
    private final Map<String, Music> musicMap;

    private static final String basePath = "audio/";
    private static final String[] soundFiles = {
            "death.ogg",
            "fail.ogg",
            "footsteps.ogg",
            "success.ogg"
    };
    private static final String[] musicFiles = {
            "ambiance.ogg"
    };

    public SoundAtlas() {
        soundMap = new HashMap<>();
        musicMap = new HashMap<>();

        for (String fileName: soundFiles) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(basePath + fileName));
            String soundKey = fileName.split("\\.")[0];
            addSound(soundKey, sound);
        }
        for (String fileName: musicFiles) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(basePath + fileName));
            String musicKey = fileName.split("\\.")[0];
            addMusic(musicKey, music);
        }
    }

    public Sound getSound(String key) {
        return soundMap.get(key);
    }

    public void addSound(String key, Sound sound) {
        soundMap.put(key, sound);
    }

    public Music getMusic(String key) {
        return musicMap.get(key);
    }

    public void addMusic(String key, Music music) {
        musicMap.put(key, music);
    }

    @Override
    public void dispose() {
        for (Iterator<Sound> i = soundMap.values().iterator(); i.hasNext();) {
            Sound sound = i.next();
            sound.dispose();
            i.remove();
        }

        for (Iterator<Music> i = musicMap.values().iterator(); i.hasNext();) {
            Music music = i.next();
            music.dispose();
            i.remove();
        }
    }
}
