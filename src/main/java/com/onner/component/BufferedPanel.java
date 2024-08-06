package com.onner.component;

import javax.swing.*;

public class BufferedPanel extends JPanel {
    public BufferedPanel() {
        setDoubleBuffered(true);
    }
}