package com.onner.async;

import com.onner.global.GlobalVariables;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoodProcess implements Runnable {

    private int secondsQuantity = 5;
    private int positionInitialX;
    private int positionInitialY;
    private int randomPositionX;
    private int randomPositionY;
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
                checkSnakeActivity ();
                Thread.sleep(250);
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
        positionInitialX = (this.playSpace.getBounds().width/2);
        positionInitialY = (this.playSpace.getBounds().height/2) ;
        food.setLocation(positionInitialX, positionInitialY);
    }

    private void checkSnakeActivity() {
        food.setVisible(GlobalVariables.startGame);
    }
}