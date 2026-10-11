package com.banquero;

import com.banquero.ui.MainFrame;

import javax.swing.*;

/**
 * Punto de entrada principal para el Simulador del Algoritmo del Banquero.
 * Configura el Look & Feel del sistema operativo y lanza la interfaz gráfica en el hilo de eventos de Swing (EDT).
 */
public class Main {

    public static void main(String[] args) {
        // Habilitar renderizado de texto nítido (Anti-Aliasing) en Java Swing
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                // Aplicar Look & Feel del Sistema Operativo para aspecto nativo y moderno
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si falla, Swing utilizará su Look & Feel por defecto de forma segura
            }

            // Iniciar y mostrar la ventana principal
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
