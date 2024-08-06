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
    private List<Point> routes;
    private int lastDirectionX = 0;
    private int lastDirectionY = 0;

    public SnakeProcess(JPanel spacegame, JLabel food) {
        this.spacegame = spacegame;
        this.food = food;
        this.snakeBody = new ArrayList<>();
        this.routes = new ArrayList<>();
        initSnake();
    }

    private void initSnake() {
        this.snake = new RoundedPanel();
        this.snake.setLayout(null);
        this.snake.setBackground(Color.DARK_GRAY);
        this.snake.setBounds(positionInitialX, positionInitialY, widthSnake, heightSnake);
        spacegame.add(snake);
        routes.add(new Point(positionInitialX, positionInitialY));
    }

    @Override
    public void run() {
        int motionPixel = 40;
        int velocity = (int) (500 / GlobalVariables.speedSnake);
        int cursorToCenterX = widthSnake / 2, cursorToCenterY = heightSnake / 2;
        try {
            for (;;) {
                int newHeadX = this.snake.getBounds().x;
                int newHeadY = this.snake.getBounds().y;

                if (this.snake.getBounds().x < GlobalVariables.mousePositionX - cursorToCenterX) {
                    this.snake.setBounds(this.snake.getBounds().x + motionPixel, this.snake.getBounds().y, widthSnake, heightSnake);
                    lastDirectionX = 1;
                    lastDirectionY = 0;
                }
                if (this.snake.getBounds().x > GlobalVariables.mousePositionX - cursorToCenterX) {
                    this.snake.setBounds(this.snake.getBounds().x - motionPixel, this.snake.getBounds().y, widthSnake, heightSnake);
                    lastDirectionX = -1;
                    lastDirectionY = 0;
                }
                if (this.snake.getBounds().y < GlobalVariables.mousePositionY - cursorToCenterY) {
                    this.snake.setBounds(this.snake.getBounds().x, this.snake.getBounds().y + motionPixel, widthSnake, heightSnake);
                    lastDirectionX = 0;
                    lastDirectionY = 1;
                }
                if (this.snake.getBounds().y > GlobalVariables.mousePositionY - cursorToCenterY) {
                    this.snake.setBounds(this.snake.getBounds().x, this.snake.getBounds().y - motionPixel, widthSnake, heightSnake);
                    lastDirectionX = 0;
                    lastDirectionY = -1;
                }

                if (Math.abs(newHeadX - routes.get(routes.size() - 1).x) >= widthSnake || Math.abs(newHeadY - routes.get(routes.size() - 1).y) >= heightSnake) {
                    routes.add(new Point(newHeadX, newHeadY));
                }
                updateSnakeBody();
                if (checkCollisionWithFood(food)) {
                    newRoundedPanel();
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
            if (snakeBounds.intersects(foodBounds)) {
                GlobalVariables.collision = true;
                return true;
            }
        }
        return false;
    }

    private void newRoundedPanel() {
        RoundedPanel newSegment = new RoundedPanel();
        newSegment.setLayout(null);
        newSegment.setBackground(Color.GRAY);
        newSegment.setSize(widthSnake, heightSnake);
        snakeBody.add(newSegment);
        spacegame.add(newSegment);
        Space.score.setText(snakeBody.size() - 1+ "");
        System.out.println("collision:" + GlobalVariables.collision);
    }

    private void updateSnakeBody() {
        if (snakeBody.isEmpty()) return;
        while (routes.size() < snakeBody.size() + 1) {
            routes.add(new Point(routes.get(routes.size() - 1).x, routes.get(routes.size() - 1).y));
        }
        int routesIndex = routes.size() - 1;

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            RoundedPanel segment = snakeBody.get(i);
            Point point = routes.get(routesIndex--);
            segment.setBounds(point.x, point.y, widthSnake, heightSnake);
        }
    }
}
