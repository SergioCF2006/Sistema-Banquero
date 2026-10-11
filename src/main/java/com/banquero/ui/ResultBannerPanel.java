package com.banquero.ui;

import com.banquero.model.SafetyResult;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Panel banner visual que muestra de forma destacada el resultado del análisis:
 * - Estado Seguro (Verde) con la Secuencia Segura en grande.
 * - Estado Inseguro (Rojo) con advertencia de posible interbloqueo y procesos bloqueados.
 */
public class ResultBannerPanel extends JPanel {

    private final JLabel lblStatusTitle;
    private final JLabel lblSequenceLabel;
    private final JLabel lblSequenceValue;
    private final JLabel lblDetails;
    private final JPanel contentBox;

    public ResultBannerPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(4, 0, 8, 0));

        contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBackground(Color.WHITE);
        contentBox.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(14, 20, 14, 20)
        ));

        // Título del Estado
        lblStatusTitle = new JLabel("ESTADO DEL SISTEMA: Pendiente de análisis");
        lblStatusTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatusTitle.setForeground(UITheme.TEXT_MUTED);

        // Secuencia Segura
        JPanel seqPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        seqPanel.setOpaque(false);

        lblSequenceLabel = new JLabel("Secuencia Segura:");
        lblSequenceLabel.setFont(UITheme.FONT_BOLD);
        lblSequenceLabel.setForeground(UITheme.TEXT_DARK);

        lblSequenceValue = new JLabel("—");
        lblSequenceValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSequenceValue.setForeground(UITheme.PRIMARY);

        seqPanel.add(lblSequenceLabel);
        seqPanel.add(lblSequenceValue);

        // Detalles adicionales
        lblDetails = new JLabel("Presione 'Analizar Estado' para ejecutar el Algoritmo del Banquero.");
        lblDetails.setFont(UITheme.FONT_REGULAR);
        lblDetails.setForeground(UITheme.TEXT_MUTED);

        contentBox.add(lblStatusTitle);
        contentBox.add(Box.createVerticalStrut(4));
        contentBox.add(seqPanel);
        contentBox.add(Box.createVerticalStrut(2));
        contentBox.add(lblDetails);

        add(contentBox, BorderLayout.CENTER);
    }

    /**
     * Muestra el resultado de seguridad con estilos visuales diferenciados.
     */
    public void displayResult(SafetyResult result) {
        if (result == null) {
            reset();
            return;
        }

        if (result.isSafe()) {
            // ESTADO SEGURO - Verde esmeralda
            contentBox.setBackground(new Color(240, 253, 244)); // Emerald 50
            contentBox.setBorder(new CompoundBorder(
                    new LineBorder(UITheme.SUCCESS_DARK, 2, true),
                    new EmptyBorder(14, 20, 14, 20)
            ));

            lblStatusTitle.setText("✔  ESTADO SEGURO (SAFE STATE)");
            lblStatusTitle.setForeground(UITheme.SUCCESS_DARK);

            lblSequenceLabel.setText("Secuencia Segura de Ejecución:");
            lblSequenceLabel.setForeground(UITheme.TEXT_DARK);

            lblSequenceValue.setText(result.getFormattedSequence());
            lblSequenceValue.setForeground(new Color(4, 120, 87)); // Emerald 700

            lblDetails.setText("El sistema garantiza que todos los procesos pueden concluir sin interbloqueo (Deadlock).");
            lblDetails.setForeground(new Color(6, 95, 70));
        } else {
            // ESTADO INSEGURO - Rojo carmesí
            contentBox.setBackground(new Color(254, 242, 242)); // Red 50
            contentBox.setBorder(new CompoundBorder(
                    new LineBorder(UITheme.DANGER_DARK, 2, true),
                    new EmptyBorder(14, 20, 14, 20)
            ));

            lblStatusTitle.setText("✖  ESTADO INSEGURO - POSIBLE INTERBLOQUEO (DEADLOCK)");
            lblStatusTitle.setForeground(UITheme.DANGER_DARK);

            lblSequenceLabel.setText("Secuencia Segura:");
            lblSequenceLabel.setForeground(UITheme.DANGER_DARK);

            lblSequenceValue.setText("NO EXISTE SECUENCIA SEGURA");
            lblSequenceValue.setForeground(UITheme.DANGER_DARK);

            StringBuilder blocked = new StringBuilder();
            for (int p : result.getUnfinishedProcesses()) {
                if (blocked.length() > 0) blocked.append(", ");
                blocked.append("P").append(p);
            }

            lblDetails.setText("Procesos bloqueados por falta de recursos: " + (blocked.length() > 0 ? blocked.toString() : "Todos")
                    + ". El sistema se encuentra en riesgo inminente de inanición o bloqueo mutuo.");
            lblDetails.setForeground(new Color(153, 27, 27)); // Red 800
        }

        revalidate();
        repaint();
    }

    /**
     * Reinicia el banner al estado inicial.
     */
    public void reset() {
        contentBox.setBackground(Color.WHITE);
        contentBox.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(14, 20, 14, 20)
        ));

        lblStatusTitle.setText("ESTADO DEL SISTEMA: Pendiente de análisis");
        lblStatusTitle.setForeground(UITheme.TEXT_MUTED);

        lblSequenceLabel.setText("Secuencia Segura:");
        lblSequenceLabel.setForeground(UITheme.TEXT_DARK);

        lblSequenceValue.setText("—");
        lblSequenceValue.setForeground(UITheme.PRIMARY);

        lblDetails.setText("Presione 'Analizar Estado' para ejecutar el Algoritmo del Banquero.");
        lblDetails.setForeground(UITheme.TEXT_MUTED);

        revalidate();
        repaint();
    }
}
