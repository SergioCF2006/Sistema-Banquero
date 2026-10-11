package com.banquero.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel de configuración inicial y botones de control del sistema:
 * - Selección de Procesos (3 a 10)
 * - Selección de Recursos (1 a 6)
 * - Crear Sistema, Cargar Ejemplo, Reiniciar y Acerca de.
 */
public class ConfigPanel extends JPanel {

    private final JSpinner processSpinner;
    private final JSpinner resourceSpinner;

    private final JButton btnCreate;
    private final JButton btnLoadExample;
    private final JButton btnReset;
    private final JButton btnAbout;

    public ConfigPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(10, 18, 10, 18)
        ));

        // Panel Izquierdo: Selectores
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);

        JLabel lblProcesses = new JLabel("Procesos:");
        lblProcesses.setFont(UITheme.FONT_BOLD);
        lblProcesses.setForeground(UITheme.TEXT_DARK);

        // Spinner de 3 a 10, valor por defecto 5
        processSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        styleSpinner(processSpinner);

        JLabel lblResources = new JLabel("Tipos de Recursos:");
        lblResources.setFont(UITheme.FONT_BOLD);
        lblResources.setForeground(UITheme.TEXT_DARK);

        // Spinner de 1 a 6, valor por defecto 3
        resourceSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
        styleSpinner(resourceSpinner);

        btnCreate = UITheme.createButton("Crear Sistema", UITheme.BTN_GRAY, Color.WHITE);
        btnCreate.setToolTipText("Genera dinámicamente las tablas para la cantidad de procesos y recursos especificada");

        leftPanel.add(lblProcesses);
        leftPanel.add(processSpinner);
        leftPanel.add(Box.createHorizontalStrut(8));
        leftPanel.add(lblResources);
        leftPanel.add(resourceSpinner);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(btnCreate);

        // Panel Derecho: Acciones Rápidas
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        btnLoadExample = UITheme.createButton("Cargar Ejemplo", UITheme.BTN_GRAY_DARK, Color.WHITE);
        btnLoadExample.setToolTipText("Carga el ejemplo clásico de Sistemas Operativos (5 procesos, 3 recursos)");

        btnReset = UITheme.createButton("Limpiar / Reiniciar", UITheme.BTN_GRAY, Color.WHITE);
        btnReset.setToolTipText("Limpia todas las tablas y reinicia los valores");

        btnAbout = UITheme.createButton("Acerca del proyecto", UITheme.BTN_GRAY, Color.WHITE);
        btnAbout.setToolTipText("Muestra información académica del proyecto");

        rightPanel.add(btnLoadExample);
        rightPanel.add(btnReset);
        rightPanel.add(btnAbout);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(UITheme.FONT_BOLD);
        spinner.setPreferredSize(new Dimension(55, 30));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            defaultEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
            defaultEditor.getTextField().setFont(UITheme.FONT_BOLD);
        }
    }

    // ================= GETTERS Y REGISTRO DE EVENTOS =================

    public int getSelectedProcesses() {
        return (int) processSpinner.getValue();
    }

    public void setSelectedProcesses(int value) {
        processSpinner.setValue(value);
    }

    public int getSelectedResources() {
        return (int) resourceSpinner.getValue();
    }

    public void setSelectedResources(int value) {
        resourceSpinner.setValue(value);
    }

    public void setOnCreateAction(ActionListener listener) {
        btnCreate.addActionListener(listener);
    }

    public void setOnLoadExampleAction(ActionListener listener) {
        btnLoadExample.addActionListener(listener);
    }

    public void setOnResetAction(ActionListener listener) {
        btnReset.addActionListener(listener);
    }

    public void setOnAboutAction(ActionListener listener) {
        btnAbout.addActionListener(listener);
    }
}
