package com.banquero.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.List;

/**
 * Panel que muestra la bitácora paso a paso del Algoritmo del Banquero ("Proceso de análisis").
 * Diseñado específicamente para apoyar explicaciones orales y defensas de proyectos universitarios.
 */
public class ProcessLogPanel extends JPanel {

    private final JTextArea logArea;
    private final JButton btnCopy;
    private final JButton btnClear;

    public ProcessLogPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(UITheme.BG_CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));

        // Cabecera del Panel
        JPanel headerPanel = new JPanel(new BorderLayout(8, 0));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("6. PROCESO DE ANÁLISIS (Explicación Paso a Paso para la Presentación)");
        titleLabel.setFont(UITheme.FONT_SECTION);
        titleLabel.setForeground(UITheme.TEXT_DARK);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnPanel.setOpaque(false);

        btnCopy = UITheme.createButton("Copiar Bitácora", UITheme.BTN_GRAY, Color.WHITE);
        btnCopy.setFont(UITheme.FONT_SMALL);

        btnClear = UITheme.createButton("Limpiar", UITheme.BTN_GRAY, Color.WHITE);
        btnClear.setFont(UITheme.FONT_SMALL);

        btnPanel.add(btnCopy);
        btnPanel.add(btnClear);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(btnPanel, BorderLayout.EAST);

        // Área de Texto
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(248, 250, 252)); // Slate 50
        logArea.setForeground(new Color(15, 23, 42));     // Slate 900
        logArea.setMargin(new Insets(10, 12, 10, 12));
        logArea.setText("Presione 'Analizar Estado' o 'Evaluar Solicitud' para observar aquí el desglose detallado paso a paso.\n");

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scrollPane.setPreferredSize(new Dimension(0, 190));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Acciones
        btnClear.addActionListener(e -> logArea.setText(""));
        btnCopy.addActionListener(e -> {
            String text = logArea.getText();
            if (!text.isEmpty()) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
                JOptionPane.showMessageDialog(this, "Bitácora copiada al portapapeles.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Muestra una lista de pasos generados por el algoritmo.
     */
    public void setLogSteps(List<String> steps) {
        StringBuilder sb = new StringBuilder();
        for (String step : steps) {
            sb.append(step).append("\n");
        }
        logArea.setText(sb.toString());
        logArea.setCaretPosition(0);
    }

    /**
     * Añade texto a la bitácora existente.
     */
    public void appendText(String text) {
        logArea.append("\n" + text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    /**
     * Limpia la bitácora y coloca el mensaje por defecto.
     */
    public void reset() {
        logArea.setText("Presione 'Analizar Estado' o 'Evaluar Solicitud' para observar aquí el desglose detallado paso a paso.\n");
    }
}
