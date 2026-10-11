package com.banquero.ui;

import com.banquero.algorithm.BankerAlgorithm;
import com.banquero.model.BankerModel;
import com.banquero.model.RequestResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Panel para simular y evaluar Solicitudes de Recursos (Resource-Request Algorithm):
 * - Selección de proceso (P0, P1, ...)
 * - Ingreso de vector de solicitud (R0, R1, ...)
 * - Evaluación matemática contra Need y Available
 * - Simulación preventiva del estado seguro
 * - Opción para aplicar la asignación al sistema real si resulta aprobada
 */
public class RequestPanel extends JPanel {

    private final JComboBox<String> processCombo;
    private final JPanel requestInputsPanel;
    private final List<JTextField> requestInputs = new ArrayList<>();

    private final JButton btnEvaluate;
    private final JButton btnApply;
    private final JLabel lblStatus;

    private int numProcesses;
    private int numResources;
    private RequestResult lastResult;
    private Consumer<BankerModel> onApplyRequestCallback;

    public RequestPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));

        // Título de la sección
        JLabel titleLabel = new JLabel("5. SOLICITUD DE RECURSOS (Algoritmo de Solicitud de Recursos)");
        titleLabel.setFont(UITheme.FONT_SECTION);
        titleLabel.setForeground(UITheme.TEXT_DARK);
        add(titleLabel, BorderLayout.NORTH);

        // Contenedor Central de Controles
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Fila 1: Selección de Proceso y Campos de Solicitud
        JPanel inputsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        inputsRow.setOpaque(false);

        JLabel lblProc = new JLabel("Proceso solicitante:");
        lblProc.setFont(UITheme.FONT_BOLD);
        lblProc.setForeground(UITheme.TEXT_DARK);

        processCombo = new JComboBox<>();
        processCombo.setFont(UITheme.FONT_BOLD);
        processCombo.setPreferredSize(new Dimension(80, 30));

        JLabel lblReq = new JLabel("Vector Request:");
        lblReq.setFont(UITheme.FONT_BOLD);
        lblReq.setForeground(UITheme.TEXT_DARK);

        requestInputsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        requestInputsPanel.setOpaque(false);

        inputsRow.add(lblProc);
        inputsRow.add(processCombo);
        inputsRow.add(Box.createHorizontalStrut(10));
        inputsRow.add(lblReq);
        inputsRow.add(requestInputsPanel);

        // Fila 2: Botones y Estado
        JPanel actionsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        actionsRow.setOpaque(false);

        btnEvaluate = UITheme.createButton("Evaluar Solicitud", UITheme.BTN_GRAY, Color.WHITE);
        btnEvaluate.setFont(UITheme.FONT_BOLD);
        btnEvaluate.setToolTipText("Verifica si la solicitud es viable y no conduce a un estado inseguro");

        btnApply = UITheme.createButton("Aplicar al Sistema", UITheme.BTN_GRAY_DARK, Color.WHITE);
        btnApply.setFont(UITheme.FONT_BOLD);
        btnApply.setEnabled(false);
        btnApply.setToolTipText("Aplica la asignación evaluada y aprobada a las matrices del sistema");

        lblStatus = new JLabel("Seleccione un proceso, ingrese cantidades y presione 'Evaluar Solicitud'.");
        lblStatus.setFont(UITheme.FONT_REGULAR);
        lblStatus.setForeground(UITheme.TEXT_MUTED);

        actionsRow.add(btnEvaluate);
        actionsRow.add(btnApply);
        actionsRow.add(Box.createHorizontalStrut(10));
        actionsRow.add(lblStatus);

        centerPanel.add(inputsRow);
        centerPanel.add(actionsRow);

        add(centerPanel, BorderLayout.CENTER);

        // Eventos
        btnApply.addActionListener(e -> applyRequestIfApproved());
    }

    /**
     * Actualiza la cantidad de procesos y recursos en los selectores del panel.
     */
    public void rebuild(int processes, int resources) {
        this.numProcesses = processes;
        this.numResources = resources;
        this.lastResult = null;
        btnApply.setEnabled(false);
        lblStatus.setText("Seleccione un proceso, ingrese cantidades y presione 'Evaluar Solicitud'.");
        lblStatus.setForeground(UITheme.TEXT_MUTED);

        // Actualizar combo de procesos
        processCombo.removeAllItems();
        for (int i = 0; i < processes; i++) {
            processCombo.addItem("P" + i);
        }

        // Actualizar campos de recursos
        requestInputsPanel.removeAll();
        requestInputs.clear();

        for (int j = 0; j < resources; j++) {
            JPanel resItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
            resItem.setOpaque(false);

            JLabel lbl = new JLabel("R" + j + ":");
            lbl.setFont(UITheme.FONT_BOLD);
            lbl.setForeground(UITheme.TEXT_DARK);

            JTextField tf = UITheme.createTextField(3);
            tf.setText("0");

            resItem.add(lbl);
            resItem.add(tf);

            requestInputs.add(tf);
            requestInputsPanel.add(resItem);
        }

        requestInputsPanel.revalidate();
        requestInputsPanel.repaint();
    }

    /**
     * Evalúa la solicitud usando el modelo actual del sistema.
     *
     * @param currentModel Modelo con los datos activos
     * @param logCallback Callback para imprimir la bitácora en el panel de análisis
     */
    public void evaluateRequest(BankerModel currentModel, Consumer<String> logCallback) {
        if (currentModel == null) {
            JOptionPane.showMessageDialog(this, "Debe cargar o ingresar los datos del sistema primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int processIndex = processCombo.getSelectedIndex();
        if (processIndex < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un proceso solicitante.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extraer vector Request
        int[] requestVector = new int[numResources];
        for (int j = 0; j < numResources; j++) {
            String txt = requestInputs.get(j).getText().trim();
            if (txt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El valor de solicitud para R" + j + " no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int val = Integer.parseInt(txt);
                if (val < 0) {
                    JOptionPane.showMessageDialog(this, "La solicitud para R" + j + " no puede ser negativa (" + val + ").", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                requestVector[j] = val;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La solicitud para R" + j + " debe ser un número entero válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Ejecutar algoritmo de solicitud
        RequestResult result = BankerAlgorithm.evaluateRequest(currentModel, processIndex, requestVector);
        this.lastResult = result;

        // Registrar en bitácora
        if (logCallback != null) {
            logCallback.accept(result.getDetailLog());
        }

        if (result.isApproved()) {
            lblStatus.setText("✔  " + result.getMessage());
            lblStatus.setForeground(UITheme.SUCCESS_DARK);
            btnApply.setEnabled(true);

            JOptionPane.showMessageDialog(
                    this,
                    result.getMessage() + "\n\nPresione 'Aplicar al Sistema' si desea consolidar este cambio en las tablas.",
                    "Solicitud Aprobada",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            lblStatus.setText("✖  " + result.getMessage());
            lblStatus.setForeground(UITheme.DANGER_DARK);
            btnApply.setEnabled(false);

            JOptionPane.showMessageDialog(
                    this,
                    result.getMessage(),
                    "Solicitud Denegada / Inválida",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void applyRequestIfApproved() {
        if (lastResult != null && lastResult.isApproved() && lastResult.getSimulatedModel() != null) {
            if (onApplyRequestCallback != null) {
                onApplyRequestCallback.accept(lastResult.getSimulatedModel());
            }
            btnApply.setEnabled(false);
            lblStatus.setText("Asignación consolidada exitosamente en las matrices del sistema.");
            lblStatus.setForeground(UITheme.PRIMARY);

            JOptionPane.showMessageDialog(
                    this,
                    "Los recursos han sido asignados permanentemente al proceso seleccionado.\n"
                    + "Las tablas Allocation, Need y Available han sido actualizadas.",
                    "Asignación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void setOnEvaluateAction(Consumer<Consumer<String>> action) {
        btnEvaluate.addActionListener(e -> action.accept(null));
    }

    public void setOnApplyRequestCallback(Consumer<BankerModel> callback) {
        this.onApplyRequestCallback = callback;
    }

    public JButton getBtnEvaluate() {
        return btnEvaluate;
    }
}
