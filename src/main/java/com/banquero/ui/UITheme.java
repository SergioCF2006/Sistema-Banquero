package com.banquero.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Define el sistema de diseño visual (UI Theme) para dar a la aplicación Swing
 * una apariencia moderna, limpia, profesional y de alto contraste.
 */
public class UITheme {

    // Paleta de Colores
    public static final Color BG_MAIN = new Color(241, 245, 249);       // Slate 100
    public static final Color BG_CARD = Color.WHITE;                     // Pure White
    public static final Color BG_HEADER = new Color(15, 23, 42);         // Slate 900
    public static final Color BG_HEADER_SUB = new Color(30, 41, 59);     // Slate 800

    // Colores de Botones (Gris con Texto Blanco solicitado)
    public static final Color BTN_GRAY = new Color(71, 85, 105);          // Gris Slate 600
    public static final Color BTN_GRAY_DARK = new Color(51, 65, 85);     // Gris oscuro Slate 700
    public static final Color BTN_GRAY_LIGHT = new Color(100, 116, 139); // Gris medio Slate 500

    public static final Color PRIMARY = new Color(71, 85, 105);          // Mapeado a Gris profesional
    public static final Color PRIMARY_HOVER = new Color(51, 65, 85);
    public static final Color SUCCESS = new Color(71, 85, 105);
    public static final Color SUCCESS_DARK = new Color(51, 65, 85);
    public static final Color DANGER = new Color(239, 68, 68);           // Red 500
    public static final Color DANGER_DARK = new Color(220, 38, 38);      // Red 600
    public static final Color WARNING = new Color(71, 85, 105);
    public static final Color INFO = new Color(71, 85, 105);
    public static final Color SECONDARY = new Color(71, 85, 105);
    public static final Color SECONDARY_HOVER = new Color(51, 65, 85);

    public static final Color TEXT_DARK = new Color(15, 23, 42);         // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);     // Slate 500
    public static final Color BORDER = new Color(203, 213, 225);         // Slate 300
    public static final Color TABLE_ROW_ALT = new Color(248, 250, 252);  // Slate 50

    // Tipografías
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);

    /**
     * Crea un botón de color gris con texto blanco, renderizado con dibujo directo (Graphics2D)
     * para asegurar máxima visibilidad y evitar que el Look & Feel de Windows lo aclare.
     */
    public static JButton createButton(String text, Color bgColor, Color fgColor) {
        final Color baseColor = (bgColor != null) ? bgColor : BTN_GRAY;
        final Color textColor = (fgColor != null) ? fgColor : Color.WHITE;

        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color fill;
                if (!isEnabled()) {
                    fill = new Color(203, 213, 225); // Deshabilitado gris claro
                } else if (getModel().isPressed()) {
                    fill = baseColor.darker().darker();
                } else if (getModel().isRollover()) {
                    fill = baseColor.darker();
                } else {
                    fill = baseColor;
                }

                // Fondo redondeado gris
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Borde suave
                g2.setColor(fill.darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(FONT_BOLD);
        button.setForeground(textColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        return button;
    }

    /**
     * Sobrecarga conveniente para crear botones grises con texto blanco por defecto.
     */
    public static JButton createButton(String text) {
        return createButton(text, BTN_GRAY, Color.WHITE);
    }

    /**
     * Crea un panel contenedor con estilo de tarjeta (Card).
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));
        return panel;
    }

    /**
     * Crea un panel con borde titulado elegante.
     */
    public static JPanel createTitledCard(String title) {
        JPanel panel = createCardPanel();
        panel.setLayout(new BorderLayout(8, 8));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SECTION);
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        panel.add(titleLabel, BorderLayout.NORTH);
        return panel;
    }

    /**
     * Aplica formato moderno y nítido a una JTable (cabeceras oscuras legibles, celdas centradas, filas alternadas).
     */
    public static void formatTable(JTable table, boolean editable) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(TEXT_DARK);

        // Cabecera de la tabla con renderizador explícito de alto contraste
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setPreferredSize(new Dimension(0, 32));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(t, val, isSelected, hasFocus, row, col);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(FONT_BOLD);
                label.setBackground(new Color(30, 41, 59)); // Slate 800 oscuro
                label.setForeground(Color.WHITE);           // Texto blanco nítido
                label.setOpaque(true);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(71, 85, 105)),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
                return label;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        // Renderizador de celdas centrado con filas alternadas
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected) {
                    if (col == 0) {
                        c.setBackground(new Color(241, 245, 249)); // Columna de procesos P0, P1...
                        c.setFont(FONT_BOLD);
                        c.setForeground(new Color(30, 41, 59));
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ROW_ALT);
                        c.setFont(FONT_REGULAR);
                        c.setForeground(TEXT_DARK);
                    }
                }
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    /**
     * Crea un JTextField con padding y estilo consistente.
     */
    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(FONT_REGULAR);
        tf.setHorizontalAlignment(SwingConstants.CENTER);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }
}
