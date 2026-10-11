package com.banquero.ui;

import com.banquero.algorithm.BankerAlgorithm;
import com.banquero.model.BankerModel;
import com.banquero.model.SafetyResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana principal de la aplicación.
 * Ensambla y coordina los paneles de configuración, matrices, resultados,
 * solicitudes de recursos y bitácora pedagógica.
 */
public class MainFrame extends JFrame {

    private final HeaderPanel headerPanel;
    private final ConfigPanel configPanel;
    private final MatricesPanel matricesPanel;
    private final ResultBannerPanel resultBannerPanel;
    private final RequestPanel requestPanel;
    private final ProcessLogPanel processLogPanel;

    private BankerModel currentModel;

    public MainFrame() {
        super("Simulador del Algoritmo del Banquero - Sistemas Operativos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1220, 840);
        setMinimumSize(new Dimension(1020, 700));
        setLocationRelativeTo(null);

        // Contenedor Principal
        JPanel rootPanel = new JPanel(new BorderLayout(0, 0));
        rootPanel.setBackground(UITheme.BG_MAIN);

        // 1. Cabecera y Configuración en el Norte
        JPanel northContainer = new JPanel(new BorderLayout());
        headerPanel = new HeaderPanel();
        configPanel = new ConfigPanel();
        northContainer.add(headerPanel, BorderLayout.NORTH);
        northContainer.add(configPanel, BorderLayout.SOUTH);
        rootPanel.add(northContainer, BorderLayout.NORTH);

        // 2. Contenido Central desplazable (Scrollable)
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(UITheme.BG_MAIN);
        scrollContent.setBorder(new EmptyBorder(14, 18, 18, 18));

        // Inicializar paneles de contenido
        matricesPanel = new MatricesPanel();
        resultBannerPanel = new ResultBannerPanel();
        requestPanel = new RequestPanel();
        processLogPanel = new ProcessLogPanel();

        scrollContent.add(matricesPanel);
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(resultBannerPanel);
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(requestPanel);
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(processLogPanel);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UITheme.BG_MAIN);

        rootPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(rootPanel);

        // Configurar Eventos y Callbacks
        setupEventHandlers();

        // Cargar por defecto el ejemplo clásico al iniciar la aplicación
        loadClassicExample();
    }

    private void setupEventHandlers() {
        // 1. Botón "Crear Sistema"
        configPanel.setOnCreateAction(e -> handleCreateSystem());

        // 2. Botón "Cargar Ejemplo"
        configPanel.setOnLoadExampleAction(e -> loadClassicExample());

        // 3. Botón "Limpiar / Reiniciar"
        configPanel.setOnResetAction(e -> handleReset());

        // 4. Botón "Acerca del proyecto"
        configPanel.setOnAboutAction(e -> new AboutDialog(this).setVisible(true));

        // 5. Botón "Calcular Need"
        matricesPanel.setOnCalculateNeedAction(e -> handleCalculateNeed());

        // 6. Botón "Analizar Estado"
        matricesPanel.setOnAnalyzeSafetyAction(e -> handleAnalyzeSafety());

        // 7. Botón "Evaluar Solicitud" en RequestPanel
        requestPanel.getBtnEvaluate().addActionListener(e -> handleEvaluateRequest());

        // 8. Callback cuando se aplica una solicitud aprobada al sistema
        requestPanel.setOnApplyRequestCallback(simulatedModel -> {
            this.currentModel = simulatedModel;
            matricesPanel.loadModel(simulatedModel);
            SafetyResult result = BankerAlgorithm.checkSafety(simulatedModel);
            resultBannerPanel.displayResult(result);
            processLogPanel.setLogSteps(result.getLogSteps());
        });
    }

    /**
     * Crea un sistema con dimensiones en blanco según los valores seleccionados.
     */
    private void handleCreateSystem() {
        int p = configPanel.getSelectedProcesses();
        int r = configPanel.getSelectedResources();

        matricesPanel.rebuild(p, r);
        requestPanel.rebuild(p, r);
        resultBannerPanel.reset();
        processLogPanel.reset();
        currentModel = new BankerModel(p, r);

        JOptionPane.showMessageDialog(
                this,
                "Sistema creado exitosamente con " + p + " procesos y " + r + " tipos de recursos.\n"
                + "Ingrese los valores en el vector Available y en las matrices Allocation y Max.",
                "Sistema Creado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Carga el ejemplo clásico de Sistemas Operativos (5 procesos, 3 recursos).
     */
    private void loadClassicExample() {
        currentModel = BankerModel.createClassicExample();
        configPanel.setSelectedProcesses(5);
        configPanel.setSelectedResources(3);

        matricesPanel.loadModel(currentModel);
        requestPanel.rebuild(5, 3);

        // Ejecutar análisis automático para mostrar el estado inicial
        SafetyResult safety = BankerAlgorithm.checkSafety(currentModel);
        resultBannerPanel.displayResult(safety);
        processLogPanel.setLogSteps(safety.getLogSteps());
    }

    /**
     * Reinicia los datos de la interfaz a cero.
     */
    private void handleReset() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Desea limpiar todos los datos del sistema?",
                "Confirmar Reinicio",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            matricesPanel.clearAll();
            resultBannerPanel.reset();
            processLogPanel.reset();
            currentModel = null;
        }
    }

    /**
     * Calcula y actualiza la matriz Need = Max - Allocation tras validar los datos.
     */
    private void handleCalculateNeed() {
        try {
            currentModel = matricesPanel.extractModel();
            currentModel.calculateNeed();
            matricesPanel.updateNeedTable(currentModel.getNeed());

            JOptionPane.showMessageDialog(
                    this,
                    "Matriz Need calculada exitosamente mediante la fórmula:\nNeed[i][j] = Max[i][j] - Allocation[i][j]",
                    "Need Calculada",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ejecuta el Algoritmo del Banquero y actualiza los paneles con los resultados.
     */
    private void handleAnalyzeSafety() {
        try {
            currentModel = matricesPanel.extractModel();
            currentModel.calculateNeed();
            matricesPanel.updateNeedTable(currentModel.getNeed());

            // Ejecución del algoritmo de seguridad
            SafetyResult safetyResult = BankerAlgorithm.checkSafety(currentModel);

            // Actualizar interfaz con el veredicto
            resultBannerPanel.displayResult(safetyResult);
            processLogPanel.setLogSteps(safetyResult.getLogSteps());

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Evalúa una solicitud de recursos ingresada en el RequestPanel.
     */
    private void handleEvaluateRequest() {
        try {
            // Extraer y sincronizar el modelo actual con lo que el usuario editó en las tablas
            currentModel = matricesPanel.extractModel();
            requestPanel.evaluateRequest(currentModel, log -> processLogPanel.appendText(log));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
