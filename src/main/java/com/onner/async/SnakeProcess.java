package com.onner.async;

import com.onner.component.RoundedPanel;
import com.onner.form.Space;
import com.onner.global.GlobalVariables;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SnakeProcess implements Runnable {
    private JPanel spacegame;
    private int widthSnake = 40;
    private int heightSnake = 40;
    int motionPixel = 40;
    private boolean running = false;

    private JLabel food;
    private List<RoundedPanel> snake;
    private List<Point> routes;

    private SoundProcess sound;

    public SnakeProcess() {}
    public SnakeProcess(JPanel spacegame, JLabel food, SoundProcess sound) {
        this.spacegame = spacegame;
        this.food = food;
        this.sound = sound;
        this.snake = new ArrayList<>();
        this.routes = new ArrayList<>();
        initSnake();
    }

    private void initSnake() {
        GlobalVariables.startGame = true;
        RoundedPanel newSegment = new RoundedPanel();
        newSegment.setLayout(null);
        newSegment.setBackground(new Color(56, 52, 52, 255));
        newSegment.setBounds(500, 500, widthSnake, heightSnake);
        snake.add(newSegment);
        spacegame.add(newSegment);
        routes.add(new Point(500, 500));
    }

    @Override
    public void run() {
        switch (GlobalVariables.sizeSnake) {
            case "small": {motionPixel = 20; widthSnake= 20; heightSnake = 20;} break;
            case "medium": {motionPixel = 30; widthSnake= 30; heightSnake = 30;} break;
            case "big": {motionPixel = 40; widthSnake= 40; heightSnake = 40;} break;
            case "bright": {motionPixel = 50; widthSnake= 50; heightSnake = 50;} break;
        }

        int velocity = (int) (500 / GlobalVariables.speedSnake);
        int cursorToCenterX = widthSnake / 2 - 5, cursorToCenterY = heightSnake / 2 - 5;

        try {
            while (GlobalVariables.startGame) {
                RoundedPanel head = snake.getFirst();
                if (head != null) {
                    int newHeadX = head.getBounds().x;
                    int newHeadY = head.getBounds().y;

                    if (newHeadX < GlobalVariables.mousePositionX - cursorToCenterX) {
                        newHeadX += motionPixel;
                    }
                    if (newHeadX > GlobalVariables.mousePositionX - cursorToCenterX) {
                        newHeadX -= motionPixel;
                    }
                    if (newHeadY < GlobalVariables.mousePositionY - cursorToCenterY) {
                        newHeadY += motionPixel;
                    }
                    if (newHeadY > GlobalVariables.mousePositionY - cursorToCenterY) {
                        newHeadY -= motionPixel;
                    }
                    head.setBounds(newHeadX, newHeadY, widthSnake, heightSnake);
                    obtainPoints( newHeadX, newHeadY);
                    updateSnakeBody();
                    if (checkCollisionWithFood(food)) {
                        newRoundedPanel();
                        sound.startSoundEat();
                    }
                    if (checkCollisionWithWalls(head)) {
                        AgainstTheWall();
                    }
                }
                Thread.sleep(velocity);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SnakeProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkCollisionWithFood(JLabel food) {
        if (!GlobalVariables.collision) {
            Rectangle snakeBounds = this.snake.getFirst().getBounds();
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
        snake.add(newSegment);
        spacegame.add(newSegment);
        Space.score.setText(snake.size()-1 + "");
        System.out.println("Collision with food, snake size: " + snake.size());
        System.out.println("puntos : " + routes.size());
    }

    private void obtainPoints(int x, int y) {
        if (routes.isEmpty()) {
            routes.add(new Point(x, y));
        } else {
            Point firstPoint = routes.getFirst();
            if (Math.abs(x - firstPoint.x) >= widthSnake || Math.abs(y - firstPoint.y) >= heightSnake) {
                routes.add(0, new Point(x, y));
                if (routes.size() > snake.size()) {
                    if (!routes.isEmpty()) {
                        routes.remove(routes.size() - 1);
                    }
                }
            }
        }
    }

    private void updateSnakeBody() {
        for (int i = 1; i < snake.size(); i++) {
            if (i < routes.size()) {
                Point point = routes.get(i );
                RoundedPanel segment = snake.get(i);
                segment.setBounds(point.x, point.y, widthSnake, heightSnake);
            }
        }
    }

    private boolean checkCollisionWithWalls(RoundedPanel head) {
        Rectangle panelBounds = spacegame.getBounds();
        Rectangle headBounds = head.getBounds();
        return !panelBounds.contains(headBounds);
    }

    private void AgainstTheWall() {
        running = false;
        GlobalVariables.startGame = false;
        spacegame.repaint();
        snake.clear();
        routes.clear();
        for (int i = 3; i > 0; i--) {
            System.out.println("Restarting in " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        initSnake();
        running = true;
        new Thread(SnakeProcess.this).start();
    }

}
