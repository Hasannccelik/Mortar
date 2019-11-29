package com.dalexpow.mortar;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class SoundPlayer {
    private static final String TAG = "SoundPlayer";
    private static final String SOUND_FOLDER = "sounds";
    private static final int MAX_SOUNDS = 3;
    private ArrayList<Object> soundList;
    private SoundPool soundPool;
    private final AssetManager assetManager;

    public SoundPlayer(Context appContext) {
        this.assetManager = appContext.getAssets();
        soundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        soundList = new ArrayList<>();
        fetchSounds();
    }

    private void fetchSounds() {
        String[] soundFiles;
        try {
            soundFiles = assetManager.list(SOUND_FOLDER);
            Log.d(TAG, "Fetched " + soundFiles.length + " sound files");
        } catch (IOException e) {
            Log.e(TAG, "Error accessing sound folder", e);
            return;
        }
        for (String fileName : soundFiles) {
            try {
                String path = SOUND_FOLDER + "/" + fileName;
                Sound s = new Sound(path);
                load(s);
                soundList.add(s);
            } catch (IOException e) {
                Log.e(TAG, "Could not load sound: " + fileName, e);
            }
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(sound.getPathName());
        int soundId = soundPool.load(fileDescriptor, 1);
        sound.setId(soundId);
    }

    public void play(Object sound) {
        Integer soundId = sound.getId();
        if (soundId == null) return;
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        Log.d(TAG, "Cleaning resources..");
        soundPool.release();
    }

    public ArrayList<Object> getSounds() {
        return this.soundList;
    }

}
