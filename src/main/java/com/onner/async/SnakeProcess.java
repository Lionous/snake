package com.onner.async;

import com.onner.component.RoundedPanel;
import com.onner.form.Space;
import com.onner.global.GlobalVariables;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SnakeProcess implements Runnable {
    private RoundedPanel snake = null;
    private JPanel spacegame = null;
    private int positionInitialX = 500;
    private int positionInitialY = 500;
    private int widthSnake = 40;
    private int heightSnake = 40;

    private JLabel food = null;
    private List<RoundedPanel> snakeBody;
    private int lastDirectionX = 0;
    private int lastDirectionY = 0;

    public SnakeProcess(JPanel spacegame, JLabel food) {
        this.spacegame = spacegame;
        this.food = food;
        initSnake();
        this.snakeBody = new ArrayList<>();
    }

    private void initSnake() {
        this.snake = new RoundedPanel();
        this.snake.setLayout(null);
        this.snake.setBackground(Color.DARK_GRAY);
        this.snake.setBounds(positionInitialX,positionInitialY,widthSnake,heightSnake);
        spacegame.add(snake);
    }

    @Override
    public void run() {
        int motionPixel = 1;
        int velocity = (int)(125/GlobalVariables.speedSnake);
        int cursorToCenterX = widthSnake/2, cursorToCenterY = heightSnake/2;
        try {
            for(;;) {
                if(this.snake.getBounds().x < GlobalVariables.mousePositionX - cursorToCenterX) {
                    this.snake.setBounds(this.snake.getBounds().x + motionPixel, this.snake.getBounds().y,widthSnake, heightSnake);
                    lastDirectionX = 1;
                    lastDirectionY = 0;
                }
                if(this.snake.getBounds().x > GlobalVariables.mousePositionX - cursorToCenterX) {
                    this.snake.setBounds(this.snake.getBounds().x - motionPixel, this.snake.getBounds().y,widthSnake, heightSnake);
                    lastDirectionX = -1;
                    lastDirectionY = 0;
                }
                if(this.snake.getBounds().y < GlobalVariables.mousePositionY - cursorToCenterY) {
                    this.snake.setBounds(this.snake.getBounds().x, this.snake.getBounds().y + motionPixel,widthSnake, heightSnake);
                    lastDirectionX = 0;
                    lastDirectionY = 1;
                }
                if(this.snake.getBounds().y > GlobalVariables.mousePositionY - cursorToCenterY) {
                    this.snake.setBounds(this.snake.getBounds().x, this.snake.getBounds().y - motionPixel,widthSnake, heightSnake);
                    lastDirectionX = 0;
                    lastDirectionY = -1;
                }

                int oldX = this.snake.getBounds().x;
                int oldY = this.snake.getBounds().y;
                obtainingNewPoints(oldX,oldY);
                if (checkCollisionWithFood(food)) {
                    newRoudedPanel();
                }
                Thread.sleep(velocity);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SnakeProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkCollisionWithFood(JLabel food) {
        if (!GlobalVariables.collision) {
            Rectangle snakeBounds = this.snake.getBounds();
            Rectangle foodBounds = food.getBounds();
            boolean collision = false;
            if (lastDirectionX < 0 && snakeBounds.x == foodBounds.x + foodBounds.width &&
                    snakeBounds.y + snakeBounds.height > foodBounds.y &&
                    snakeBounds.y < foodBounds.y + foodBounds.height) {
                collision = true;
            }
            else if (lastDirectionX > 0 && snakeBounds.x + snakeBounds.width == foodBounds.x &&
                    snakeBounds.y + snakeBounds.height > foodBounds.y &&
                    snakeBounds.y < foodBounds.y + foodBounds.height) {
                collision = true;
            }
            else if (lastDirectionY < 0 && snakeBounds.y == foodBounds.y + foodBounds.height &&
                    snakeBounds.x + snakeBounds.width > foodBounds.x &&
                    snakeBounds.x < foodBounds.x + foodBounds.width) {
                collision = true;
            }
            else if (lastDirectionY > 0 && snakeBounds.y + snakeBounds.height == foodBounds.y &&
                    snakeBounds.x + snakeBounds.width > foodBounds.x &&
                    snakeBounds.x < foodBounds.x + foodBounds.width) {
                collision = true;
            }
            if (collision) {
                GlobalVariables.collision = true;
                return true;
            }
        }
        return false;
    }


    private void newRoudedPanel() {
        int headPositionX;
        int headPositionY;
        if (snakeBody.isEmpty()) {
            headPositionX = this.snake.getBounds().x;
            headPositionY = this.snake.getBounds().y;
        } else {
            RoundedPanel lastElement = snakeBody.get(snakeBody.size() - 1);
            headPositionX = lastElement.getBounds().x;
            headPositionY = lastElement.getBounds().y;
        }
        if (lastDirectionX > 0) { // Moving right
            headPositionX -= widthSnake;
        } else if (lastDirectionX < 0) { // Moving left
            headPositionX += widthSnake;
        } else if (lastDirectionY > 0) { // Moving down
            headPositionY -= heightSnake;
        } else if (lastDirectionY < 0) { // Moving up
            headPositionY += heightSnake;
        }

        RoundedPanel newSegment = new RoundedPanel();
        newSegment.setLayout(null);
        newSegment.setBackground(Color.GRAY);
        newSegment.setBounds(headPositionX, headPositionY, widthSnake, heightSnake);
        snakeBody.add(newSegment);
        spacegame.add(newSegment);
        Space.score.setText(snakeBody.size()+"");
        System.out.println("collision:"+GlobalVariables.collision);
    }

    private int lastAssignedX = Integer.MIN_VALUE;
    private int lastAssignedY = Integer.MIN_VALUE;
    private final int step = 40; // The interval to consider

    private void obtainingNewPoints(int positionInitialX, int positionInitialY) {
        if (Math.abs(positionInitialX - lastAssignedX) >= step ||
                Math.abs(positionInitialY - lastAssignedY) >= step) {
            lastAssignedX = positionInitialX;
            lastAssignedY = positionInitialY;
            updateSnakeBody(lastAssignedX, lastAssignedY);
            System.out.println("New (x, y) position: (" + lastAssignedX + ", " + lastAssignedY + ")");
        }
    }

    private void updateSnakeBody(int newHeadX, int newHeadY) {

        if (snakeBody.isEmpty()) {
            return;
        }

        RoundedPanel headSegment = snakeBody.get(0);
        headSegment.setBounds(newHeadX, newHeadY, headSegment.getWidth(), headSegment.getHeight());

        for (int i = snakeBody.size() - 1; i > 0; i--) {
            RoundedPanel currentSegment = snakeBody.get(i);
            RoundedPanel prevSegment = snakeBody.get(i - 1);
            currentSegment.setBounds(prevSegment.getBounds().x, prevSegment.getBounds().y, currentSegment.getWidth(), currentSegment.getHeight());
        }
    }
}