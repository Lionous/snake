package com.onner.async;

import com.onner.global.GlobalVariables;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoodProcess implements Runnable {

    private int speed = 1;
    private int secondsQuantity = 3;
    private int positionInitialX = 0;
    private int positionInitialY = 0;
    private int randomPositionX = 0;
    private int randomPositionY = 0;
    private int width = 50, height = 44;

    private Random random = null;

    private JLabel food = null;
    private JLabel time = null;
    private JPanel playspace = null;
    private long currentTime = 0;

    public FoodProcess() {}
    public FoodProcess(JPanel _space, JLabel _food, JLabel _time) {
        this.playspace = _space;
        this.food = _food;
        this.time = _time;
        setNewRandom();
    }

    @Override
    public void run() {
        try {
            for(;;) {
                this.time.setText(((System.currentTimeMillis() / 1000) - this.currentTime) + "");
                if(GlobalVariables.collision || this.minutesElapsed(this.currentTime, secondsQuantity)) {
                    this.currentTime = System.currentTimeMillis() / 1000;
                    GlobalVariables.collision = false;
                    setNewPosition();
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FoodProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setNewRandom() {
        random = new Random();
        this.currentTime = System.currentTimeMillis() / 1000;
        this.time.setText(this.currentTime + "");
    }

    private void setNewPosition() {
        int positionSpaceX = this.playspace.getBounds().x;
        int positionSpaceY = this.playspace.getBounds().y;
        int widthSpace = this.playspace.getBounds().width;
        int heightSpace = this.playspace.getBounds().height;

        int maximumWidthSpace = widthSpace - this.food.getBounds().width;
        int maximumHeightSpace = heightSpace - this.food.getBounds().height;

        this.randomPositionX = random.nextInt(maximumWidthSpace - positionSpaceX + 1) + positionSpaceX;
        this.randomPositionY = random.nextInt(maximumHeightSpace - positionSpaceY + 1) + positionSpaceY;

        this.food.setLocation(this.randomPositionX, this.randomPositionY);
    }

    private boolean minutesElapsed(long initTime, int secondsQuantity) {
        long currentTimeTemp = System.currentTimeMillis() / 1000;
        return ((currentTimeTemp - initTime) >= secondsQuantity);
    }
}
