package com.banquero.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Cuadro de diálogo modal con información académica e institucional del proyecto.
 */
public class AboutDialog extends JDialog {

    public AboutDialog(Frame owner) {
        super(owner, "Acerca del Proyecto - Algoritmo del Banquero", true);
        setSize(520, 380);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(22, 24, 20, 24));

        // Cabecera
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel title = new JLabel("Simulador del Algoritmo del Banquero");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UITheme.PRIMARY);

        JLabel sub = new JLabel("Materia: Sistemas Operativos • Prevención de Deadlocks");
        sub.setFont(UITheme.FONT_REGULAR);
        sub.setForeground(UITheme.TEXT_MUTED);

        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(sub, BorderLayout.CENTER);

        // Contenido
        JTextPane descPane = new JTextPane();
        descPane.setContentType("text/html");
        descPane.setEditable(false);
        descPane.setText(
                "<html><body style='font-family: Segoe UI, sans-serif; font-size: 11pt; color: #1e293b; line-height: 1.4;'>"
                + "<p><b>Descripción Académica:</b><br>"
                + "Aplicación didáctica interactiva que simula la prevención de interbloqueos (<i>Deadlocks</i>) "
                + "mediante el <b>Algoritmo del Banquero</b> formulado originalmente por <b>Edsger Dijkstra</b>.</p>"
                + "<p><b>Tecnologías y Estándares:</b></p>"
                + "<ul>"
                + "<li><b>Lenguaje:</b> Java 17 LTS</li>"
                + "<li><b>Interfaz Gráfica:</b> Java Swing (sin frameworks externos innecesarios)</li>"
                + "<li><b>Gestor de Proyecto:</b> Apache Maven</li>"
                + "<li><b>Arquitectura:</b> Separación estricta entre Lógica (Model/Algorithm) y Vista (UI)</li>"
                + "</ul>"
                + "<p><b>Fórmulas Principales:</b><br>"
                + "<code>Need[i][j] = Max[i][j] - Allocation[i][j]</code><br>"
                + "<code>Criterio de Seguridad: Need[i] &le; Work &rarr; Work = Work + Allocation[i]</code></p>"
                + "</body></html>"
        );
        descPane.setBorder(null);

        // Botón Cerrar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setOpaque(false);

        JButton btnClose = UITheme.createButton("Cerrar", UITheme.BTN_GRAY, Color.WHITE);
        btnClose.addActionListener(e -> dispose());
        bottomPanel.add(btnClose);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(descPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}
