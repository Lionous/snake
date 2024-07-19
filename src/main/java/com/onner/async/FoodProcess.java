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
    private int positionInitialX = 10;
    private int positionInitialY = 10;
    private int randomPositionX = 0;
    private int randomPositionY = 0;
    private int width = 50, height = 44;

    private Random random = null;
    private long currentTime = 0;

    private JLabel food = null;
    private JLabel currentPositionCounter = null;
    private JPanel playSpace = null;

    public FoodProcess() {}
    public FoodProcess(JPanel space, JLabel food, JLabel currentPositionCounter) {
        this.playSpace = space;
        this.food = food;
        this.currentPositionCounter = currentPositionCounter;
        random = new Random();
        initialPositionOfFood();
        setNewRandom();
    }

    @Override
    public void run() {
        try {
            for(;;) {
                this.currentPositionCounter.setText(((System.currentTimeMillis() / 1000) - this.currentTime) + "");
                if(GlobalVariables.collision || this.minutesElapsed(this.currentTime, this.secondsQuantity)) {
                    this.currentTime = System.currentTimeMillis() / 1000;
                    GlobalVariables.collision = false;
                    randomFoodPosition();
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FoodProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setNewRandom() {
        this.currentTime = System.currentTimeMillis() / 1000;
        this.currentPositionCounter.setText(this.currentTime + "");
    }

    private void randomFoodPosition() {
        int positionSpaceX = this.playSpace.getBounds().x;
        int positionSpaceY = this.playSpace.getBounds().y;
        int widthSpace = this.playSpace.getBounds().width;
        int heightSpace = this.playSpace.getBounds().height;
        int maximumWidthSpace = widthSpace - this.food.getBounds().width;
        int maximumHeightSpace = heightSpace - this.food.getBounds().height;
        this.randomPositionX = random.nextInt(maximumWidthSpace - positionSpaceX + 1) + positionSpaceX;
        this.randomPositionY = random.nextInt(maximumHeightSpace - positionSpaceY + 1) + positionSpaceY;
        food.setLocation(this.randomPositionX, this.randomPositionY);
    }

    private boolean minutesElapsed(long initTime, int secondsQuantity) {
        long currentTimeTemp = System.currentTimeMillis() / 1000;
        return ((currentTimeTemp - initTime) >= secondsQuantity);
    }

    private void initialPositionOfFood() {
        int margin = 50;
        int maxX = this.playSpace.getWidth() - this.food.getWidth() - margin;
        int maxY = this.playSpace.getHeight() - this.food.getHeight() - margin;
        this.positionInitialX = margin + random.nextInt(maxX - margin );
        this.positionInitialY = margin + random.nextInt(maxY - margin );
        food.setLocation(this.positionInitialX, this.positionInitialY);
    }
}
