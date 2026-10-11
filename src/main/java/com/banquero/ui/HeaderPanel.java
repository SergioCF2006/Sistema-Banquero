package com.banquero.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel de encabezado con diseño moderno, título institucional y subtítulo.
 */
public class HeaderPanel extends JPanel {

    public HeaderPanel() {
        setLayout(new BorderLayout(15, 0));
        setBackground(UITheme.BG_HEADER);
        setBorder(new EmptyBorder(16, 24, 16, 24));

        // Contenedor de Textos
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("SIMULADOR DEL ALGORITMO DEL BANQUERO");
        titleLabel.setFont(UITheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel subLabel = new JLabel("Prevención de interbloqueos - Sistemas Operativos");
        subLabel.setFont(UITheme.FONT_SUBTITLE);
        subLabel.setForeground(new Color(148, 163, 184)); // Slate 400

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subLabel);

        // Insignia a la derecha
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        badgePanel.setOpaque(false);

        JLabel badge = new JLabel(" Java 17 • Swing • Dijkstra ");
        badge.setFont(UITheme.FONT_SMALL);
        badge.setForeground(new Color(199, 210, 254)); // Indigo 200
        badge.setOpaque(true);
        badge.setBackground(UITheme.BG_HEADER_SUB);
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(79, 70, 229), 1, true),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        badgePanel.add(badge);

        add(textPanel, BorderLayout.CENTER);
        add(badgePanel, BorderLayout.EAST);
    }
}
