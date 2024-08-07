package com.onner.component;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private String soundEat;
    private String soundLoss;
    private String soundGame;

    private static Clip clip;
    private static AudioInputStream audioStream;
    private static long clipTimePosition = 0;

    public SoundPlayer() {
        this.soundEat = "E:/2024-I/Parallel-Programming/unit-ii/snake/src/main/java/com/onner/resources/eat.wav";
        this.soundLoss = "E:/2024-I/Parallel-Programming/unit-ii/snake/src/main/java/com/onner/resources/loss.wav";
        this.soundGame = "E:/2024-I/Parallel-Programming/unit-ii/snake/src/main/java/com/onner/resources/pixel-fight-8-bit.wav";
    }

    public static void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            if (clip != null) {
                clip.close();
            }
            audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clipTimePosition = 0;
                        clip.close();
                    }
                }
            });
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | javax.sound.sampled.LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public String getSoundGame() {
        return soundGame;
    }

    public String getSoundEat() {
        return soundEat;
    }

    public String getSoundLoss() {
        return soundLoss;
    }
}
