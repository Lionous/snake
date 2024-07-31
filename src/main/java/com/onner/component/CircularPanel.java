package com.onner.component;

import javax.swing.*;
import java.awt.*;

public class CircularPanel extends JPanel {

    public CircularPanel() {
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillOval(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        int diameter = Math.min(size.width, size.height);
        return new Dimension(diameter, diameter);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        int diameter = Math.min(width, height);
        super.setBounds(x, y, diameter, diameter);
    }
}
