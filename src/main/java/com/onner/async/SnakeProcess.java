package com.onner.async;

import com.onner.component.CircularPanel;
import com.onner.global.GlobalVariables;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class SnakeProcess implements Runnable {
    private CircularPanel snake = null;
    private JPanel spacegame = null;
    private int positionInitialX = 500;
    private int positionInitialY = 500;
    private int widthSnake = 40;
    private int heightSnake = 40;

    public SnakeProcess(JPanel spacegame) {
        this.spacegame = spacegame;
        initSnake();
    }

    private void initSnake() {
        this.snake = new CircularPanel();
        this.snake.setLayout(null);
        this.snake.setBackground(Color.DARK_GRAY);
        this.snake.setBounds(positionInitialX,positionInitialY,widthSnake,heightSnake);
        spacegame.add(snake);
    }

    @Override
    public void run() {
        int motionPixel = 1;
        int velocity = (int)(10/GlobalVariables.speedSnake);
        int cursorToCenterX = widthSnake/2, cursorToCenterY = heightSnake/2;
        try {
            for(;;) {
                if(this.snake.getBounds().x < GlobalVariables.mousePositionX - cursorToCenterX) {
                    this.snake.setBounds(this.snake.getBounds().x + motionPixel, this.snake.getBounds().y,widthSnake, heightSnake);
                }

                if(this.snake.getBounds().x > GlobalVariables.mousePositionX - cursorToCenterX) {
                    this.snake.setBounds(this.snake.getBounds().x - motionPixel, this.snake.getBounds().y,widthSnake, heightSnake);
                }

                if(this.snake.getBounds().y < GlobalVariables.mousePositionY - cursorToCenterY) {
                    this.snake.setBounds(this.snake.getBounds().x, this.snake.getBounds().y + motionPixel,widthSnake, heightSnake);
                }

                if(this.snake.getBounds().y > GlobalVariables.mousePositionY - cursorToCenterY) {
                    this.snake.setBounds(this.snake.getBounds().x, this.snake.getBounds().y - motionPixel,widthSnake, heightSnake);
                }
                sleep(velocity);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SnakeProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}