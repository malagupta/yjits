package conf.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    private Color normalColor;
    private Color hoverColor;

    public StyledButton(String text, Color color) {
        super(text);
        this.normalColor = color;
        // Create a slightly lighter color for hover effect
        this.hoverColor = new Color(
            Math.min(normalColor.getRed() + 20, 255),
            Math.min(normalColor.getGreen() + 20, 255),
            Math.min(normalColor.getBlue() + 20, 255)
        );

        setBackground(normalColor);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 16));
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);

        // Add hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }
        });
    }
}