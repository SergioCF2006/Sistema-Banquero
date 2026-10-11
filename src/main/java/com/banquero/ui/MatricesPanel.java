package com.banquero.ui;

import com.banquero.model.BankerModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel que contiene el Vector Available, las matrices Allocation, Max y Need,
 * y los botones de acción "Calcular Need" y "Analizar Estado".
 */
public class MatricesPanel extends JPanel {

    private int numProcesses;
    private int numResources;

    // Vector Available
    private JPanel availableFieldsContainer;
    private final List<JTextField> availableInputs = new ArrayList<>();

    // Tablas
    private JTable tblAllocation;
    private JTable tblMax;
    private JTable tblNeed;

    private DefaultTableModel modelAllocation;
    private DefaultTableModel modelMax;
    private DefaultTableModel modelNeed;

    // Botones de acción
    private final JButton btnCalculateNeed;
    private final JButton btnAnalyzeSafety;

    public MatricesPanel() {
        setLayout(new BorderLayout(12, 12));
        setOpaque(false);

        // 1. Panel Superior: Vector Available
        JPanel availableCard = UITheme.createTitledCard("1. VECTOR AVAILABLE (Recursos Disponibles Actualmente)");
        availableFieldsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        availableFieldsContainer.setOpaque(false);
        availableCard.add(availableFieldsContainer, BorderLayout.CENTER);

        // 2. Panel Central: Tablas Allocation, Max y Need en cuadrícula
        JPanel tablesGrid = new JPanel(new GridLayout(1, 3, 14, 0));
        tablesGrid.setOpaque(false);

        // Card Allocation
        JPanel cardAllocation = UITheme.createTitledCard("2. ALLOCATION (Asignados)");
        modelAllocation = createMatrixTableModel(false);
        tblAllocation = new JTable(modelAllocation);
        UITheme.formatTable(tblAllocation, true);
        JScrollPane scrollAlloc = new JScrollPane(tblAllocation);
        scrollAlloc.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        cardAllocation.add(scrollAlloc, BorderLayout.CENTER);

        // Card Max
        JPanel cardMax = UITheme.createTitledCard("3. MAX (Máximo Necesario)");
        modelMax = createMatrixTableModel(false);
        tblMax = new JTable(modelMax);
        UITheme.formatTable(tblMax, true);
        JScrollPane scrollMax = new JScrollPane(tblMax);
        scrollMax.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        cardMax.add(scrollMax, BorderLayout.CENTER);

        // Card Need
        JPanel cardNeed = UITheme.createTitledCard("4. NEED (Max - Allocation)");
        modelNeed = createMatrixTableModel(true);
        tblNeed = new JTable(modelNeed);
        UITheme.formatTable(tblNeed, false);
        JScrollPane scrollNeed = new JScrollPane(tblNeed);
        scrollNeed.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        cardNeed.add(scrollNeed, BorderLayout.CENTER);

        tablesGrid.add(cardAllocation);
        tablesGrid.add(cardMax);
        tablesGrid.add(cardNeed);

        // 3. Panel Inferior: Botones de Acción
        JPanel actionsBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionsBar.setOpaque(false);

        btnCalculateNeed = UITheme.createButton("Calcular Need (Max - Alloc)", UITheme.BTN_GRAY, Color.WHITE);
        btnCalculateNeed.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCalculateNeed.setToolTipText("Calcula automáticamente la matriz Need = Max - Allocation");

        btnAnalyzeSafety = UITheme.createButton("ANALIZAR ESTADO (Algoritmo del Banquero)", UITheme.BTN_GRAY_DARK, Color.WHITE);
        btnAnalyzeSafety.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAnalyzeSafety.setToolTipText("Ejecuta el algoritmo del banquero para determinar si el sistema es seguro y hallar la secuencia");

        actionsBar.add(btnCalculateNeed);
        actionsBar.add(btnAnalyzeSafety);

        // Ensamblado
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);
        topContainer.add(availableCard, BorderLayout.NORTH);
        topContainer.add(tablesGrid, BorderLayout.CENTER);

        add(topContainer, BorderLayout.CENTER);
        add(actionsBar, BorderLayout.SOUTH);

        // Inicializar con dimensiones estándar (5 procesos, 3 recursos)
        rebuild(5, 3);
    }

    /**
     * Construye o reconstruye los campos de texto del vector Available y las tres tablas
     * según las nuevas dimensiones especificadas.
     */
    public void rebuild(int processes, int resources) {
        this.numProcesses = processes;
        this.numResources = resources;

        // 1. Reconstruir campos del Vector Available
        availableFieldsContainer.removeAll();
        availableInputs.clear();

        for (int j = 0; j < resources; j++) {
            JPanel resItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            resItem.setOpaque(false);

            JLabel lbl = new JLabel("R" + j + ":");
            lbl.setFont(UITheme.FONT_BOLD);
            lbl.setForeground(UITheme.TEXT_DARK);

            JTextField tf = UITheme.createTextField(4);
            tf.setText("0");

            resItem.add(lbl);
            resItem.add(tf);

            availableInputs.add(tf);
            availableFieldsContainer.add(resItem);
        }
        availableFieldsContainer.revalidate();
        availableFieldsContainer.repaint();

        // 2. Reconstruir modelos de tablas
        String[] columnNames = new String[resources + 1];
        columnNames[0] = "Proceso";
        for (int j = 0; j < resources; j++) {
            columnNames[j + 1] = "R" + j;
        }

        modelAllocation = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Columna de Proceso no editable
            }
        };

        modelMax = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };

        modelNeed = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Matriz Need es de solo lectura calculada
            }
        };

        // Llenar filas iniciales con ceros
        for (int i = 0; i < processes; i++) {
            Object[] rowAlloc = new Object[resources + 1];
            Object[] rowMax = new Object[resources + 1];
            Object[] rowNeed = new Object[resources + 1];

            rowAlloc[0] = "P" + i;
            rowMax[0] = "P" + i;
            rowNeed[0] = "P" + i;

            for (int j = 0; j < resources; j++) {
                rowAlloc[j + 1] = "0";
                rowMax[j + 1] = "0";
                rowNeed[j + 1] = "0";
            }

            modelAllocation.addRow(rowAlloc);
            modelMax.addRow(rowMax);
            modelNeed.addRow(rowNeed);
        }

        tblAllocation.setModel(modelAllocation);
        tblMax.setModel(modelMax);
        tblNeed.setModel(modelNeed);

        UITheme.formatTable(tblAllocation, true);
        UITheme.formatTable(tblMax, true);
        UITheme.formatTable(tblNeed, false);
    }

    /**
     * Carga un modelo con datos predefinidos en las tablas y campos del panel.
     */
    public void loadModel(BankerModel model) {
        // Asegurar dimensiones correctas primero
        rebuild(model.getNumProcesses(), model.getNumResources());

        // Cargar Available
        int[] avail = model.getAvailable();
        for (int j = 0; j < numResources; j++) {
            availableInputs.get(j).setText(String.valueOf(avail[j]));
        }

        // Cargar Allocation: limpiar y volver a poblar fila por fila
        int[][] alloc = model.getAllocation();
        modelAllocation.setRowCount(0); // limpiar filas existentes
        for (int i = 0; i < numProcesses; i++) {
            Object[] row = new Object[numResources + 1];
            row[0] = "P" + i;
            for (int j = 0; j < numResources; j++) {
                row[j + 1] = String.valueOf(alloc[i][j]);
            }
            modelAllocation.addRow(row);
        }

        // Cargar Max: limpiar y volver a poblar fila por fila
        int[][] max = model.getMax();
        modelMax.setRowCount(0);
        for (int i = 0; i < numProcesses; i++) {
            Object[] row = new Object[numResources + 1];
            row[0] = "P" + i;
            for (int j = 0; j < numResources; j++) {
                row[j + 1] = String.valueOf(max[i][j]);
            }
            modelMax.addRow(row);
        }

        // Calcular y cargar Need
        model.calculateNeed();
        int[][] need = model.getNeed();
        modelNeed.setRowCount(0);
        for (int i = 0; i < numProcesses; i++) {
            Object[] row = new Object[numResources + 1];
            row[0] = "P" + i;
            for (int j = 0; j < numResources; j++) {
                row[j + 1] = String.valueOf(need[i][j]);
            }
            modelNeed.addRow(row);
        }

        // Forzar refresco visual de las tres tablas
        modelAllocation.fireTableDataChanged();
        modelMax.fireTableDataChanged();
        modelNeed.fireTableDataChanged();

        UITheme.formatTable(tblAllocation, true);
        UITheme.formatTable(tblMax, true);
        UITheme.formatTable(tblNeed, false);

        tblAllocation.revalidate();
        tblAllocation.repaint();
        tblMax.revalidate();
        tblMax.repaint();
        tblNeed.revalidate();
        tblNeed.repaint();
    }

    /**
     * Actualiza la tabla visual de la matriz Need.
     */
    public void updateNeedTable(int[][] need) {
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                modelNeed.setValueAt(String.valueOf(need[i][j]), i, j + 1);
            }
        }
    }

    /**
     * Extrae y valida todos los datos de los componentes visuales.
     * Detiene la edición activa de celdas para no perder el último valor ingresado.
     *
     * @return BankerModel con los datos validados
     * @throws IllegalArgumentException si algún campo está vacío, no es entero, o es negativo
     */
    public BankerModel extractModel() throws IllegalArgumentException {
        // Detener edición si el usuario estaba escribiendo en una celda
        if (tblAllocation.isEditing()) {
            tblAllocation.getCellEditor().stopCellEditing();
        }
        if (tblMax.isEditing()) {
            tblMax.getCellEditor().stopCellEditing();
        }

        BankerModel model = new BankerModel(numProcesses, numResources);

        // 1. Extraer Vector Available
        int[] avail = new int[numResources];
        for (int j = 0; j < numResources; j++) {
            String txt = availableInputs.get(j).getText().trim();
            if (txt.isEmpty()) {
                throw new IllegalArgumentException("El campo Available para el recurso R" + j + " está vacío.");
            }
            try {
                int val = Integer.parseInt(txt);
                if (val < 0) {
                    throw new IllegalArgumentException("El recurso Available R" + j + " no puede ser negativo (" + val + ").");
                }
                avail[j] = val;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El valor de Available en R" + j + " debe ser un número entero válido (encontrado: '" + txt + "').");
            }
        }
        model.setAvailable(avail);

        // 2. Extraer Matriz Allocation
        int[][] alloc = new int[numProcesses][numResources];
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                Object cellVal = modelAllocation.getValueAt(i, j + 1);
                String txt = (cellVal == null) ? "" : cellVal.toString().trim();
                if (txt.isEmpty()) {
                    throw new IllegalArgumentException("Falta un valor en Allocation para el proceso P" + i + ", recurso R" + j + ".");
                }
                try {
                    int val = Integer.parseInt(txt);
                    if (val < 0) {
                        throw new IllegalArgumentException("Allocation[P" + i + "][R" + j + "] no puede ser negativo (" + val + ").");
                    }
                    alloc[i][j] = val;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Allocation[P" + i + "][R" + j + "] debe ser un número entero (encontrado: '" + txt + "').");
                }
            }
        }
        model.setAllocation(alloc);

        // 3. Extraer Matriz Max
        int[][] max = new int[numProcesses][numResources];
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                Object cellVal = modelMax.getValueAt(i, j + 1);
                String txt = (cellVal == null) ? "" : cellVal.toString().trim();
                if (txt.isEmpty()) {
                    throw new IllegalArgumentException("Falta un valor en Max para el proceso P" + i + ", recurso R" + j + ".");
                }
                try {
                    int val = Integer.parseInt(txt);
                    if (val < 0) {
                        throw new IllegalArgumentException("Max[P" + i + "][R" + j + "] no puede ser negativo (" + val + ").");
                    }
                    if (val < alloc[i][j]) {
                        throw new IllegalArgumentException("Error en P" + i + ", R" + j + ": Max (" + val 
                                + ") no puede ser menor que Allocation (" + alloc[i][j] + ").");
                    }
                    max[i][j] = val;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Max[P" + i + "][R" + j + "] debe ser un número entero (encontrado: '" + txt + "').");
                }
            }
        }
        model.setMax(max);

        // 4. Calcular Need
        model.calculateNeed();
        return model;
    }

    /**
     * Limpia los valores de todas las celdas y campos dejándolos en 0.
     */
    public void clearAll() {
        for (JTextField tf : availableInputs) {
            tf.setText("0");
        }
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                modelAllocation.setValueAt("0", i, j + 1);
                modelMax.setValueAt("0", i, j + 1);
                modelNeed.setValueAt("0", i, j + 1);
            }
        }
    }

    private DefaultTableModel createMatrixTableModel(boolean readOnly) {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !readOnly && column > 0;
            }
        };
    }

    // ================= EVENT LISTENERS =================

    public void setOnCalculateNeedAction(ActionListener listener) {
        btnCalculateNeed.addActionListener(listener);
    }

    public void setOnAnalyzeSafetyAction(ActionListener listener) {
        btnAnalyzeSafety.addActionListener(listener);
    }

    public int getNumProcesses() {
        return numProcesses;
    }

    public int getNumResources() {
        return numResources;
    }
}
