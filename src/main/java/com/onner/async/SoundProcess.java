package com.onner.async;

import com.onner.component.SoundPlayer;

public class SoundProcess implements Runnable {
    private final SoundPlayer soundPlayer;
    private volatile boolean running = false;
    private volatile String soundToPlay = null;

    public SoundProcess(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void run() {
        while (running) {
            if (soundToPlay != null) {
                SoundPlayer.playSound(soundToPlay);
                soundToPlay = null;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void startSoundEat() {
        soundToPlay = soundPlayer.getSoundEat();
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }

    public void stopSoundEat() {
        running = false;
    }

    public void startSoundLoss() {
        soundToPlay = soundPlayer.getSoundLoss();
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }

    public void stopSoundLoss() {
        running = false;
    }
}
