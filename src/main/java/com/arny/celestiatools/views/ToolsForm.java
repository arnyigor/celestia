package com.arny.celestiatools.views;

import com.arny.celestiatools.models.CelestiaAsteroid;
import com.arny.celestiatools.controller.Controller;
import com.arny.celestiatools.models.onResultCallback;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.util.ArrayList;

public class ToolsForm extends JFrame {
    private JFrame mainFrame;
    private CalcFrame calcFrame;
    private JPanel panel;
    private JTabbedPane tabbedPane1;
    private JPanel asteroidPanel, CalcPanel;
    private JButton btnDownload, btnWriteOrbits, btnOrbitViewer, btnCelestiaFiles, btnCalc, btnThreadCancel;
    private JLabel labelInfo;
    private JComboBox jComboBoxSource;
    private JCheckBox checkBox1, checkBox2, checkBox3;
    private JTable tblAsteroidsData;
    private JTextPane lblCalcRes, pnlAsteroidData;
    private JProgressBar progressBar;
    private JTextField textField1;
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 600;
    private Controller controller;
    private ArrayList<CelestiaAsteroid> celestiaAsteroids;
    private CelestiaAsteroid celestiaAsteroid;
    private TableAsteroidModel tableModel;
    private TableRowSorter<TableAsteroidModel> rowSorter;

    public ToolsForm() {
        initUI();
        controller = new Controller();
        initTableAsteroids();
    }

    private void initTableAsteroids() {
        celestiaAsteroids = new ArrayList<>();
        controller.getAsterTableData(asteroids -> {
            celestiaAsteroids = asteroids;
            setModelToTable();
            if (celestiaAsteroids.size() > 0) {
                celestiaAsteroid = celestiaAsteroids.get(0);
                pnlAsteroidData.setText(controller.formatAsteroidData(celestiaAsteroid));
            }
            progressBar.setValue(0);
        });
    }

    private void initUI() {
        mainFrame = new JFrame("Celestia Tools");
        mainFrame.setResizable(false);
        mainFrame.setContentPane(panel);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            ImageIcon img = new ImageIcon("icon.png");
            mainFrame.setIconImage(img.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Controller.setFrameForm(mainFrame, WIDTH, HEIGHT);
        initButtons();
        checkBox1.setText("Apollo");
        checkBox2.setText("Amor");
        checkBox3.setText("Aten");
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void showMainFrameWindow() {
        mainFrame.setVisible(true);
    }

    private void initButtons() {
        btnDownload.setText("Загрузить");
        btnDownload.addActionListener(e -> {
            labelInfo.setText("Загрузка файла...");
            controller.downloadFile(jComboBoxSource.getSelectedIndex(), this::MessageResultCallback,(method, total, progress, estimate) -> labelInfo.setText("estimate"));
        });
        btnWriteOrbits.setText("Обновить БД");
        progressBar.setMinimum(0);
        progressBar.setStringPainted(true);
        btnWriteOrbits.addActionListener(e -> {
            labelInfo.setText("Запись файла...");
            ArrayList<String> orbitalTypes = new ArrayList<>();
            orbitalTypes.clear();
            if (checkBox1.isSelected()) {
                orbitalTypes.add(checkBox1.getText());
            }
            if (checkBox2.isSelected()) {
                orbitalTypes.add(checkBox2.getText());
            }
            if (checkBox3.isSelected()) {
                orbitalTypes.add(checkBox3.getText());
            }
            controller.writeOrbitalParamFile(orbitalTypes, this::MessageResultCallback, asteroids -> {
                celestiaAsteroids = asteroids;
                setModelToTable();
                if (celestiaAsteroids.size() > 0) {
                    celestiaAsteroid = celestiaAsteroids.get(0);
                    pnlAsteroidData.setText(controller.formatAsteroidData(celestiaAsteroid));
                }
                progressBar.setValue(0);
            }, (method, total, progress, estimate) -> {
                progressBar.setMaximum(total);
                progressBar.setValue(progress);
                labelInfo.setText("Примерно осталось:" + estimate);
            });
        });
        tblAsteroidsData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblAsteroidsData.convertRowIndexToModel(tblAsteroidsData.getSelectedRow());
                int row = tblAsteroidsData.convertRowIndexToModel(tblAsteroidsData.getSelectedRow());
                celestiaAsteroid = celestiaAsteroids.get(row);
                pnlAsteroidData.setText(controller.formatAsteroidData(celestiaAsteroid));
            }
        });
        lblCalcRes.setText("Результат:");
        btnCalc.setText("Расчет");
        btnCalc.addActionListener(e -> {
        });
        btnOrbitViewer.addActionListener(e -> controller.orbitViewerStart(celestiaAsteroid));
        btnCelestiaFiles.setText("Записать орбиты Celestia");
        btnCelestiaFiles.addActionListener(e -> {
            ArrayList<CelestiaAsteroid> asteroids = tableModel.getSelected(tblAsteroidsData.getSelectedRows());
            controller.writeOrbitsFiles(asteroids, this::MessageResultCallback);
        });
        btnThreadCancel.setText("Отмена");
        btnThreadCancel.addActionListener(e -> controller.InterruptThread(this::MessageResultCallback));
    }

    private void MessageResultCallback(String method, boolean success, String result) {
        String message = Controller.getMessage(success, method);
        labelInfo.setText(message);
        int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, result, method, messType);
    }

    private class TableAsteroidModel extends AbstractTableModel {
        String[] columnNames = {"№", "Name", "Type", "Radius,km", "Update"};

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public int getRowCount() {
            return celestiaAsteroids.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return celestiaAsteroids.get(rowIndex).getName();
                case 2:
                    return celestiaAsteroids.get(rowIndex).getOrbitType();
                case 3:
                    return celestiaAsteroids.get(rowIndex).getRadius();
                case 4:
                    return celestiaAsteroids.get(rowIndex).getUpdateTime();
            }
            return "";
        }

        private ArrayList<CelestiaAsteroid> getSelected(int[] rows) {
            ArrayList<CelestiaAsteroid> selectedAsteroids = new ArrayList<>();
            for (int i = 0; i < rows.length; i++) {
                selectedAsteroids.add(celestiaAsteroids.get(i));
            }
            return selectedAsteroids;
        }
    }

    private void setModelToTable() {
        tableModel = new TableAsteroidModel();
        rowSorter = new TableRowSorter<>(tableModel);
        textField1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String srch = textField1.getText().trim();
                if (srch.length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + srch));
                }
            }
        });
        tblAsteroidsData.setModel(tableModel);
        tblAsteroidsData.setRowSorter(rowSorter);
    }

}
