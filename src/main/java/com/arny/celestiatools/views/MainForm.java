/*
 * Created by JFormDesigner on Mon Dec 18 12:44:00 MSK 2017
 */

package com.arny.celestiatools.views;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.arny.celestiatools.controller.Controller;
import com.arny.celestiatools.models.CelestiaAsteroid;
import com.arny.celestiatools.models.onResultCallback;
import com.arny.celestiatools.utils.UtilsKt;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Arny
 */
public class MainForm extends JFrame {
    private Controller controller;
    private ArrayList<CelestiaAsteroid> celestiaAsteroids,selectedAsteroids;
    private CelestiaAsteroid celestiaAsteroid;
    private TableAsteroidModel tableModel;
    private TableRowSorter<TableAsteroidModel> rowSorter;

    public MainForm() {
        controller = new Controller();
        initComponents();
        initUI();
        initTableAsteroids();
    }

    private void initTableAsteroids() {
        celestiaAsteroids = new ArrayList<>();
        selectedAsteroids = new ArrayList<>();
        Observable<ArrayList<CelestiaAsteroid>> asterTableData = controller.getAsterTableData();
        asterTableData.subscribeOn(Schedulers.io()).subscribe(asteroids -> {
            celestiaAsteroids = asteroids;
            setModelToTable();
            if (celestiaAsteroids.size() > 0) {
                celestiaAsteroid = celestiaAsteroids.get(0);
                pnlAsteroidData.setText(controller.formatAsteroidData(celestiaAsteroid));
            }
            panel3.setBorder(new TitledBorder("Данные.Всего:" + celestiaAsteroids.size()));
            progressBar.setValue(0);
        }, throwable -> {
            MessageResultCallback("dbwrite", false, throwable.getMessage());
        });
    }

    private void initUI() {
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            ImageIcon img = new ImageIcon("icon.png");
            setIconImage(img.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initButtons();
        setVisible(true);
    }


    private void initButtons() {
        btnDownload.addActionListener(e -> {
            controller.downloadFile(jComboBoxSource.getSelectedIndex(), this::MessageResultCallback, (method, total, progress, estimate) -> {
                labelInfo.setText(estimate);
            });
        });
        btnUpdate.addActionListener(e -> {
            progressBar.setMinimum(0);
            progressBar.setStringPainted(true);
            labelInfo.setText("Обновление данных...");
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
            btnCancel.setEnabled(false);
            controller.updateDb(orbitalTypes, (method, success, result) -> {
                btnCancel.setEnabled(true);
                MessageResultCallback(method, success, result);
            }, asteroids -> {
                btnCancel.setEnabled(true);
                celestiaAsteroids = asteroids;
                setModelToTable();
                panel3.setBorder(new TitledBorder("Данные.Всего:" + celestiaAsteroids.size()));
                if (celestiaAsteroids.size() > 0) {
                    celestiaAsteroid = celestiaAsteroids.get(0);
                    pnlAsteroidData.setText(controller.formatAsteroidData(celestiaAsteroid));
                }
                progressBar.setValue(0);
//                initTableAsteroids();
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
        btnOrbitViewer.addActionListener(e -> controller.orbitViewerStart(celestiaAsteroid));
        btnCelestiaFiles.setText("Записать орбиты Celestia");
        btnCelestiaFiles.addActionListener(e -> {
            ArrayList<CelestiaAsteroid> asteroids = tableModel.getSelected(tblAsteroidsData.getSelectedRows());
            controller.writeOrbitsFiles(asteroids, this::MessageResultCallback);
        });
        btnCancel.setText("Отмена");
        btnCancel.addActionListener(e -> controller.cancel(this::MessageResultCallback));
    }

    private void MessageResultCallback(String method, boolean success, String result) {
        System.out.println("MessageResultCallback: in thread:" + Thread.currentThread().getName());
        String message = UtilsKt.getMessage(success, method);
        labelInfo.setText(message);
        int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, result, UtilsKt.getTitle(method), messType);
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
        textFieldSearch.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                String srch = textFieldSearch.getText().trim();
                if (srch.length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + srch));
                    tblAsteroidsData.updateUI();
                }
            }
        });
        tblAsteroidsData.setModel(tableModel);
        tblAsteroidsData.setRowSorter(rowSorter);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        panel3 = new JPanel();
        scrollPane1 = new JScrollPane();
        tblAsteroidsData = new JTable();
        panel5 = new JPanel();
        jComboBoxSource = new JComboBox<>();
        btnCelestiaFiles = new JButton();
        btnDownload = new JButton();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        checkBox3 = new JCheckBox();
        btnUpdate = new JButton();
        btnOrbitViewer = new JButton();
        panel6 = new JPanel();
        textFieldSearch = new JTextField();
        label1 = new JLabel();
        panel7 = new JPanel();
        pnlAsteroidData = new JTextPane();
        panel8 = new JPanel();
        labelInfo = new JLabel();
        progressBar = new JProgressBar();
        btnCancel = new JButton();
        panel2 = new JPanel();

        //======== this ========
        setTitle("Celestia Tools");
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {

                //======== panel3 ========
                {
                    panel3.setBorder(new TitledBorder("\u0414\u0430\u043d\u043d\u044b\u0435"));

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setEnabled(false);

                        //---- tblAsteroidsData ----
                        tblAsteroidsData.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
                        tblAsteroidsData.setFillsViewportHeight(true);
                        tblAsteroidsData.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null, null},
                                {null, null, null, null},
                            },
                            new String[] {
                                "No", null, null, null
                            }
                        ));
                        scrollPane1.setViewportView(tblAsteroidsData);
                    }

                    GroupLayout panel3Layout = new GroupLayout(panel3);
                    panel3.setLayout(panel3Layout);
                    panel3Layout.setHorizontalGroup(
                        panel3Layout.createParallelGroup()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                    );
                    panel3Layout.setVerticalGroup(
                        panel3Layout.createParallelGroup()
                            .addComponent(scrollPane1)
                    );
                }

                //======== panel5 ========
                {
                    panel5.setBorder(new TitledBorder("\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438"));

                    //---- jComboBoxSource ----
                    jComboBoxSource.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Dayly",
                        "PHAs",
                        "NEAs",
                        "MCORB"
                    }));

                    //---- btnCelestiaFiles ----
                    btnCelestiaFiles.setText("\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u043e\u0440\u0431\u0438\u0442\u044b");

                    //---- btnDownload ----
                    btnDownload.setText("\u0417\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c");

                    //---- checkBox1 ----
                    checkBox1.setText("Apollo");

                    //---- checkBox2 ----
                    checkBox2.setText("Amor");

                    //---- checkBox3 ----
                    checkBox3.setText("Aten");

                    //---- btnUpdate ----
                    btnUpdate.setText("\u041e\u0431\u043d\u043e\u0432\u0438\u0442\u044c");

                    GroupLayout panel5Layout = new GroupLayout(panel5);
                    panel5.setLayout(panel5Layout);
                    panel5Layout.setHorizontalGroup(
                        panel5Layout.createParallelGroup()
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnDownload, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxSource, GroupLayout.Alignment.LEADING)
                                    .addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCelestiaFiles, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel5Layout.createParallelGroup()
                                    .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(panel5Layout.createSequentialGroup()
                                        .addGroup(panel5Layout.createParallelGroup()
                                            .addComponent(checkBox2, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(checkBox3, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 6, Short.MAX_VALUE)))
                                .addContainerGap())
                    );
                    panel5Layout.setVerticalGroup(
                        panel5Layout.createParallelGroup()
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBoxSource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(checkBox1))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnDownload)
                                    .addComponent(checkBox2))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(checkBox3)
                                    .addComponent(btnUpdate))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCelestiaFiles))
                    );
                }

                //---- btnOrbitViewer ----
                btnOrbitViewer.setText("\u041e\u0440\u0431\u0438\u0442\u0430");

                //======== panel6 ========
                {
                    panel6.setBorder(new TitledBorder("\u0418\u043d\u0444\u043e"));
                    panel6.setMinimumSize(new Dimension(94, 50));
                    panel6.setPreferredSize(new Dimension(391, 200));

                    //---- textFieldSearch ----
                    textFieldSearch.setToolTipText("\u041f\u043e\u0438\u0441\u043a");

                    //---- label1 ----
                    label1.setText("\u041f\u043e\u0438\u0441\u043a");

                    //======== panel7 ========
                    {
                        panel7.setBorder(new TitledBorder("\u0414\u0430\u043d\u043d\u044b\u0435 \u0430\u0441\u0442\u0435\u0440\u043e\u0438\u0434\u0430"));

                        GroupLayout panel7Layout = new GroupLayout(panel7);
                        panel7.setLayout(panel7Layout);
                        panel7Layout.setHorizontalGroup(
                            panel7Layout.createParallelGroup()
                                .addGroup(panel7Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(pnlAsteroidData, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                                    .addContainerGap())
                        );
                        panel7Layout.setVerticalGroup(
                            panel7Layout.createParallelGroup()
                                .addGroup(panel7Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(pnlAsteroidData, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                    .addContainerGap())
                        );
                    }

                    GroupLayout panel6Layout = new GroupLayout(panel6);
                    panel6.setLayout(panel6Layout);
                    panel6Layout.setHorizontalGroup(
                        panel6Layout.createParallelGroup()
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel6Layout.createParallelGroup()
                                    .addComponent(textFieldSearch, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                                    .addGroup(panel6Layout.createSequentialGroup()
                                        .addGroup(panel6Layout.createParallelGroup()
                                            .addComponent(label1, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                                            .addComponent(panel7, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap())))
                    );
                    panel6Layout.setVerticalGroup(
                        panel6Layout.createParallelGroup()
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addComponent(label1, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFieldSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                                .addComponent(panel7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                    );
                }

                //======== panel8 ========
                {
                    panel8.setPreferredSize(new Dimension(250, 50));

                    //---- labelInfo ----
                    labelInfo.setText("\u0421\u0442\u0430\u0442\u0443\u0441:");

                    GroupLayout panel8Layout = new GroupLayout(panel8);
                    panel8.setLayout(panel8Layout);
                    panel8Layout.setHorizontalGroup(
                        panel8Layout.createParallelGroup()
                            .addGroup(panel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelInfo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                    );
                    panel8Layout.setVerticalGroup(
                        panel8Layout.createParallelGroup()
                            .addGroup(panel8Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(labelInfo)
                                .addContainerGap(19, Short.MAX_VALUE))
                    );
                }

                //---- btnCancel ----
                btnCancel.setText("text");

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addComponent(panel8, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(panel6, GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
                                    .addGap(29, 29, 29)
                                    .addComponent(btnCancel)
                                    .addGap(0, 17, Short.MAX_VALUE))
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addComponent(btnOrbitViewer, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(237, Short.MAX_VALUE))
                                .addComponent(panel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(panel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(19, 19, 19)
                            .addComponent(btnOrbitViewer)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel6, GroupLayout.PREFERRED_SIZE, 339, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnCancel))
                            .addGap(12, 12, 12))
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                );
            }
            tabbedPane1.addTab("\u0410\u0441\u0442\u0435\u0440\u043e\u0438\u0434\u044b", panel1);

            //======== panel2 ========
            {

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGap(0, 946, Short.MAX_VALUE)
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGap(0, 564, Short.MAX_VALUE)
                );
            }
            tabbedPane1.addTab("text", panel2);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel panel3;
    private JScrollPane scrollPane1;
    private JTable tblAsteroidsData;
    private JPanel panel5;
    private JComboBox<String> jComboBoxSource;
    private JButton btnCelestiaFiles;
    private JButton btnDownload;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JButton btnUpdate;
    private JButton btnOrbitViewer;
    private JPanel panel6;
    private JTextField textFieldSearch;
    private JLabel label1;
    private JPanel panel7;
    private JTextPane pnlAsteroidData;
    private JPanel panel8;
    private JLabel labelInfo;
    private JProgressBar progressBar;
    private JButton btnCancel;
    private JPanel panel2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
