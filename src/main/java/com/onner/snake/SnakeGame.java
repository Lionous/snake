package com.onner.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 150;

    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer gameTimer;
    private Timer foodTimer;

    public SnakeGame() {
        this.setTitle("Snake Game");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.addKeyListener(this);
        this.setVisible(true);

        startGame();
    }

    public void startGame() {
        snake.clear();
        int startX = WIDTH / 2 - (WIDTH / 2 % UNIT_SIZE);
        int startY = HEIGHT / 2 - (HEIGHT / 2 % UNIT_SIZE);
        snake.add(new Point(startX, startY));
        snake.add(new Point(startX - UNIT_SIZE, startY));
        snake.add(new Point(startX - 2 * UNIT_SIZE, startY));
        running = true;
        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
        startFoodTimer();
    }

    public void startFoodTimer() {
        foodTimer = new Timer(5000, e -> spawnFood());
        foodTimer.start();
        spawnFood(); // Spawning initial food
    }

    public void spawnFood() {
        Random random = new Random();
        int x = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        int y = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        food = new Point(x, y);
    }

    public void move() {
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, new Point(snake.get(i - 1)));
        }
        Point head = snake.get(0);
        switch (direction) {
            case 'U' -> head.translate(0, -UNIT_SIZE);
            case 'D' -> head.translate(0, UNIT_SIZE);
            case 'L' -> head.translate(-UNIT_SIZE, 0);
            case 'R' -> head.translate(UNIT_SIZE, 0);
        }
        snake.set(0, head);
    }

    public void checkFood() {
        if (snake.get(0).equals(food)) {
            snake.add(new Point(food));
            spawnFood();
        }
    }

    public void checkCollisions() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        } else {
            gameTimer.stop();
            foodTimer.stop();
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);
            for (Point p : snake) {
                g.setColor(Color.GREEN);
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> {
                if (direction != 'R') direction = 'L';
            }
            case KeyEvent.VK_D -> {
                if (direction != 'L') direction = 'R';
            }
            case KeyEvent.VK_W -> {
                if (direction != 'D') direction = 'U';
            }
            case KeyEvent.VK_S -> {
                if (direction != 'U') direction = 'D';
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new SnakeGame();
    }
}
