package com.onner.component;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private int cornerRadius = 25; // Radio de las esquinas redondeadas
    private int borderWidth = 2; // Ancho del borde

    public RoundedPanel() {
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth, getHeight() - borderWidth, cornerRadius, cornerRadius);
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.drawRoundRect(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth, getHeight() - borderWidth, cornerRadius, cornerRadius);
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }
}
