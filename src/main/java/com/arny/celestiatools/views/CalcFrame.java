/*
 * Created by JFormDesigner on Sun Sep 03 16:42:32 MSK 2017
 */

package com.arny.celestiatools.views;

import com.arny.celestiatools.controller.Controller;
import com.arny.celestiatools.utils.MathUtils;
import com.arny.celestiatools.utils.UtilsKt;
import com.arny.celestiatools.utils.astronomy.AstroConst;
import com.arny.celestiatools.utils.astronomy.AstroUtils;
import com.arny.celestiatools.utils.DateTimeUtils;
import com.arny.celestiatools.utils.circularmotion.CircularMotion;
import com.arny.celestiatools.utils.circularmotion.EllipseMotion;
import com.arny.celestiatools.utils.circularmotion.OrbitCalcKt;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 * @author Arny
 */
public class CalcFrame extends JFrame {
    private Controller controller;

    public CalcFrame(String name) {
        super(name);
        initComponents();
        initUI();
        controller = new Controller();
    }

    private void initUI() {
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            formattedGrads.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("### ## ##.###")));
            formattedTextFieldDate.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("## ## #### ##:##:##")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            ImageIcon img = new ImageIcon("icon.png");
            setIconImage(img.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Controller.setFrameForm(this, 600, 500);
        setVisible(true);
    }

    private void textFieldGradFloatKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
        String s = controller.calcGradToMasked(textFieldGradFloat.getText());
        formattedGrads.setValue(s);
    }

    private void formattedTextFieldKeyReleased(KeyEvent ke) {
        textFieldEnterFilteredStr(ke, "0123456789.");
        textFieldGradFloat.setText(String.valueOf(controller.calcGradToFloat(formattedGrads.getText())));
    }

    private void formattedTextFieldDateKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789");
        textFieldJD.setText(controller.calcDatTimeToJD(formattedTextFieldDate.getText()));

    }

    private void textFieldJDKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
        formattedTextFieldDate.setText(controller.calcJDToDatTime(textFieldJD.getText()));
    }

    private void textFieldEnterFilteredStr(KeyEvent e, String s) {
        char c = e.getKeyChar();
        if (s.indexOf(c) < 0) {
            e.consume();
        }
    }

    private void menuItem1ActionPerformed(ActionEvent e) {
        long timestamp = DateTimeUtils.convertTimeStringToLong("11 06 2014", "dd MM yyyy");
        double lat = 55.753655;
        double lon = 37.619872;
        StringBuilder res = new StringBuilder();
        String twRise = AstroUtils.getSunsetRise(timestamp, lat, lon, true, AstroConst.TWILIGHT, 4);
        String ctwRise = AstroUtils.getSunsetRise(timestamp, lat, lon, true, AstroConst.CIVIL_TWILIGHT, 4);
        String ntwRise = AstroUtils.getSunsetRise(timestamp, lat, lon, true, AstroConst.NAUTICAL_TWILIGHT, 4);
        String atwRise = AstroUtils.getSunsetRise(timestamp, lat, lon, true, AstroConst.ASTRONOMICAL_TWILIGHT, 4);
        String twSet = AstroUtils.getSunsetRise(timestamp, lat, lon, false, AstroConst.TWILIGHT, 4);
        String ctwSet = AstroUtils.getSunsetRise(timestamp, lat, lon, false, AstroConst.CIVIL_TWILIGHT, 4);
        String ntwSet = AstroUtils.getSunsetRise(timestamp, lat, lon, false, AstroConst.NAUTICAL_TWILIGHT, 4);
        String atwSet = AstroUtils.getSunsetRise(timestamp, lat, lon, false, AstroConst.ASTRONOMICAL_TWILIGHT, 4);
        res.append("twRise:").append(twRise).append("\n");
        res.append("ctwRise:").append(ctwRise).append("\n");
        res.append("ntwRise:").append(ntwRise).append("\n");
        res.append("atwRise:").append(atwRise).append("\n");
        res.append("twSet:").append(twSet).append("\n");
        res.append("ctwSet:").append(ctwSet).append("\n");
        res.append("ntwSet:").append(ntwSet).append("\n");
        res.append("atwSet:").append(atwSet).append("\n");
//        System.out.println(res);
    }

    private void menuItem2ActionPerformed(ActionEvent e) {

    }

    private void btnOrbitCalcActionPerformed(ActionEvent e) {
        double mass = UtilsKt.validateInputDouble(edtMass.getText());
        double radius = UtilsKt.validateInputDouble(edtRadius.getText());
        double hp = UtilsKt.validateInputDouble(edtHp.getText());
        double ha = UtilsKt.validateInputDouble(edtHa.getText());
        double ecc = UtilsKt.validateInputDouble(edtEcc.getText());
        double sma = UtilsKt.validateInputDouble(edtSma.getText());
        double vp = UtilsKt.validateInputDouble(edtVp.getText());
        double period = UtilsKt.validateInputDouble(edtPeriod.getText());
        StringBuilder results = new StringBuilder();
        if (vp > 0 || ha > 0 || ecc != 0.0 || sma != 0.0 || period != 0.0) {
            EllipseMotion ellipseMotion = OrbitCalcKt.calcEllipseOrbit(mass, radius, hp, ha, vp, ecc, sma, period);
            results.append("1я космическая:").append(MathUtils.round(ellipseMotion.getV1(), 3)).append("(м/с)\n");
            results.append("\n2я космическая:").append(MathUtils.round(ellipseMotion.getV2(), 3)).append("(м/с)\n");
            results.append("\nСкорость апогея:").append(MathUtils.round(ellipseMotion.getVa(), 3)).append("(м/с)\n");
            results.append("\nСкорость перигея:").append(MathUtils.round(ellipseMotion.getVp(), 3)).append("(м/с)\n");
            results.append("\nБольшая полуось:").append(MathUtils.toExpo(ellipseMotion.getSma() / 1e3, 6)).append("(км)\n");
            results.append("\nЭксцентриситет:").append(ellipseMotion.getEcc()).append("\n");
            edtEcc.setText(String.valueOf(ellipseMotion.getEcc()));
            edtHa.setText(String.valueOf(MathUtils.round(ellipseMotion.getHa(), 3)));
            edtHp.setText(String.valueOf(MathUtils.round(ellipseMotion.getHp(), 3)));
            edtVp.setText(String.valueOf(MathUtils.round(ellipseMotion.getVp(), 3)));
            edtSma.setText(String.valueOf(MathUtils.round(ellipseMotion.getSma(), 3)));
            results.append("\nПеригей:").append(MathUtils.toExpo(hp / 1e3, 6)).append("(км) \n");
            results.append("\nАпогей:").append(MathUtils.toExpo(ellipseMotion.getHa() / 1e3, 6)).append("(км) \n");
            results.append("\nУскорение:").append(MathUtils.round(ellipseMotion.getUskorenie(), 3)).append("(м/с2)\n");
            int hour = ellipseMotion.getHour();
            int min = ellipseMotion.getMin();
            int sec = ellipseMotion.getSec();
            String timeSec = String.valueOf(ellipseMotion.getPtime());
            results.append("\nПериод:").append(DateTimeUtils.convertTime(DateTimeUtils.convertTime(hour, min, sec))).append(" : ").append(timeSec).append("(сек)");
        } else {
            CircularMotion circularMotion = OrbitCalcKt.calcCircularOrbit(mass, radius, hp);
            results.append("1я космическая:").append(MathUtils.round(circularMotion.getV1(), 3)).append("(м/с)\n");
            results.append("\n2я космическая:").append(MathUtils.round(circularMotion.getV2(), 3)).append("(м/с)\n");
            results.append("\nУскорение:").append(MathUtils.round(circularMotion.getUskorenie(), 3)).append("(м/с2)\n");
            edtEcc.setText(String.valueOf(circularMotion.getEcc()));
            int hour = circularMotion.getHour();
            int min = circularMotion.getMin();
            int sec = circularMotion.getSec();
            results.append("\nПериод:").append(DateTimeUtils.convertTime(DateTimeUtils.convertTime(hour, min, sec))).append(" : ").append(String.valueOf(circularMotion.getPtime())).append("(сек)");
        }
        txtPaneOrbitInfo.setText(results.toString());
    }

    private void edtMassKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
    }

    private void edtRadiusKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
    }

    private void edtHpKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
    }

    private void edtHaKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
    }

    private void edtVpKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
    }

    private void edtEccKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
    }

    private void edtSmaKeyReleased(KeyEvent e) {
        textFieldEnterFilteredStr(e, "0123456789.");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        dialogPane = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        panel3 = new JPanel();
        label1 = new JLabel();
        formattedGrads = new JFormattedTextField();
        label2 = new JLabel();
        textFieldGradFloat = new JTextField();
        panel4 = new JPanel();
        formattedTextFieldDate = new JFormattedTextField();
        textFieldJD = new JTextField();
        label3 = new JLabel();
        label4 = new JLabel();
        panel2 = new JPanel();
        edtMass = new JTextField();
        edtRadius = new JTextField();
        edtHp = new JTextField();
        edtHa = new JTextField();
        edtEcc = new JTextField();
        edtSma = new JTextField();
        btnOrbitCalc = new JButton();
        edtVp = new JTextField();
        edtPeriod = new JTextField();
        scrollPane1 = new JScrollPane();
        txtPaneOrbitInfo = new JTextPane();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();

        //======== this ========
        setTitle("\u041a\u0430\u043b\u044c\u043a\u0443\u043b\u044f\u0442\u043e\u0440");
        setIconImage(null);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("\u041c\u0435\u043d\u044e");

                //---- menuItem1 ----
                menuItem1.setText("Menu1");
                menuItem1.addActionListener(e -> menuItem1ActionPerformed(e));
                menu1.add(menuItem1);

                //---- menuItem2 ----
                menuItem2.setText("Menu 2");
                menuItem2.addActionListener(e -> menuItem2ActionPerformed(e));
                menu1.add(menuItem2);
            }
            menuBar1.add(menu1);
        }
        setJMenuBar(menuBar1);

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== tabbedPane1 ========
            {

                //======== panel1 ========
                {

                    //======== panel3 ========
                    {
                        panel3.setBorder(new TitledBorder("\u0413\u0440\u0430\u0434\u0443\u0441\u044b"));

                        //---- label1 ----
                        label1.setText("\u0413\u0440\u0430\u0434\u0443\u0441\u044b \u043c\u0438\u043d\u0443\u0442\u044b \u0441\u0435\u043a\u0443\u043d\u0434\u044b");

                        //---- formattedGrads ----
                        formattedGrads.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                formattedTextFieldKeyReleased(e);
                            }
                        });

                        //---- label2 ----
                        label2.setText("\u0413\u0440\u0430\u0434\u0443\u0441\u044b");

                        //---- textFieldGradFloat ----
                        textFieldGradFloat.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                        textFieldGradFloat.setToolTipText("Latitude");
                        textFieldGradFloat.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                textFieldGradFloatKeyReleased(e);
                            }
                        });

                        GroupLayout panel3Layout = new GroupLayout(panel3);
                        panel3.setLayout(panel3Layout);
                        panel3Layout.setHorizontalGroup(
                            panel3Layout.createParallelGroup()
                                .addGroup(panel3Layout.createSequentialGroup()
                                    .addGroup(panel3Layout.createParallelGroup()
                                        .addComponent(label1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panel3Layout.createSequentialGroup()
                                            .addGap(0, 0, Short.MAX_VALUE)
                                            .addComponent(formattedGrads, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panel3Layout.createParallelGroup()
                                        .addComponent(label2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panel3Layout.createSequentialGroup()
                                            .addComponent(textFieldGradFloat, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))))
                        );
                        panel3Layout.setVerticalGroup(
                            panel3Layout.createParallelGroup()
                                .addGroup(panel3Layout.createSequentialGroup()
                                    .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1)
                                        .addComponent(label2))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(formattedGrads, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldGradFloat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(11, Short.MAX_VALUE))
                        );
                    }

                    //======== panel4 ========
                    {
                        panel4.setBorder(new TitledBorder("JD"));

                        //---- formattedTextFieldDate ----
                        formattedTextFieldDate.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                formattedTextFieldDateKeyReleased(e);
                            }
                        });

                        //---- textFieldJD ----
                        textFieldJD.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                textFieldJDKeyReleased(e);
                            }
                        });

                        //---- label3 ----
                        label3.setText("\u0414\u0430\u0442\u0430(dd MM yyyy HH:mm:ss)");

                        //---- label4 ----
                        label4.setText("JD");

                        GroupLayout panel4Layout = new GroupLayout(panel4);
                        panel4.setLayout(panel4Layout);
                        panel4Layout.setHorizontalGroup(
                            panel4Layout.createParallelGroup()
                                .addGroup(panel4Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(label3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(formattedTextFieldDate))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panel4Layout.createParallelGroup()
                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldJD, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        );
                        panel4Layout.setVerticalGroup(
                            panel4Layout.createParallelGroup()
                                .addGroup(panel4Layout.createSequentialGroup()
                                    .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label3)
                                        .addComponent(label4))
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(panel4Layout.createSequentialGroup()
                                    .addGap(17, 17, 17)
                                    .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(formattedTextFieldDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldJD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        );
                    }

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panel4, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(345, Short.MAX_VALUE))
                    );
                    panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(140, Short.MAX_VALUE))
                    );
                }
                tabbedPane1.addTab("\u041e\u0431\u0449\u0435\u0435", panel1);

                //======== panel2 ========
                {

                    //---- edtMass ----
                    edtMass.setToolTipText("\u041c\u0430\u0441\u0441\u0430");
                    edtMass.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            edtMassKeyReleased(e);
                        }
                    });

                    //---- edtRadius ----
                    edtRadius.setToolTipText("\u0420\u0430\u0434\u0438\u0443\u0441");
                    edtRadius.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            edtRadiusKeyReleased(e);
                        }
                    });

                    //---- edtHp ----
                    edtHp.setToolTipText("\u0412\u044b\u0441\u043e\u0442\u0430 \u043f\u0435\u0440\u0438\u0433\u0435\u044f");
                    edtHp.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            edtHpKeyReleased(e);
                        }
                    });

                    //---- edtHa ----
                    edtHa.setToolTipText("\u0412\u044b\u0441\u043e\u0442\u0430 \u0430\u043f\u043e\u0433\u0435\u044f");
                    edtHa.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            edtHaKeyReleased(e);
                        }
                    });

                    //---- edtEcc ----
                    edtEcc.setToolTipText("\u042d\u043a\u0441\u0446\u0435\u043d\u0442\u0440\u0438\u0441\u0438\u0442\u0435\u0442");
                    edtEcc.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            edtEccKeyReleased(e);
                        }
                    });

                    //---- edtSma ----
                    edtSma.setToolTipText("\u0411\u043e\u043b\u044c\u0448\u0430\u044f \u043f\u043e\u043b\u0443\u043e\u0441\u044c");
                    edtSma.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            edtSmaKeyReleased(e);
                        }
                    });

                    //---- btnOrbitCalc ----
                    btnOrbitCalc.setText("Calc");
                    btnOrbitCalc.addActionListener(e -> btnOrbitCalcActionPerformed(e));

                    //---- edtVp ----
                    edtVp.setToolTipText("\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u043f\u0435\u0440\u0438\u0433\u0435\u044f");
                    edtVp.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    edtVp.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            edtVpKeyReleased(e);
                        }
                    });

                    //---- edtPeriod ----
                    edtPeriod.setToolTipText("\u041f\u0435\u0440\u0438\u043e\u0434");
                    edtPeriod.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            edtSmaKeyReleased(e);
                        }
                    });

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setViewportView(txtPaneOrbitInfo);
                    }

                    //---- label5 ----
                    label5.setText("M:");

                    //---- label6 ----
                    label6.setText("R:");

                    //---- label7 ----
                    label7.setText("Peri:");

                    //---- label8 ----
                    label8.setText("Apo:");

                    //---- label9 ----
                    label9.setText("Vp:");

                    //---- label10 ----
                    label10.setText("Ecc:");

                    //---- label11 ----
                    label11.setText("Sma:");

                    //---- label12 ----
                    label12.setText("P:");

                    GroupLayout panel2Layout = new GroupLayout(panel2);
                    panel2.setLayout(panel2Layout);
                    panel2Layout.setHorizontalGroup(
                        panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel2Layout.createParallelGroup()
                                    .addComponent(label5)
                                    .addComponent(label6)
                                    .addComponent(label7)
                                    .addComponent(label8)
                                    .addComponent(label9)
                                    .addComponent(label10)
                                    .addComponent(label11)
                                    .addComponent(label12))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel2Layout.createParallelGroup()
                                    .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(edtMass)
                                        .addComponent(edtRadius)
                                        .addComponent(edtHp)
                                        .addComponent(edtHa, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(edtVp, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edtEcc, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edtSma, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edtPeriod, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnOrbitCalc, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 429, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(12, Short.MAX_VALUE))
                    );
                    panel2Layout.setVerticalGroup(
                        panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel2Layout.createParallelGroup()
                                    .addGroup(panel2Layout.createSequentialGroup()
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(edtMass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label5))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(edtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label6))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(edtHp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label7))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(edtHa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label8))
                                        .addGap(8, 8, 8)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(edtVp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label9))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(edtEcc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label10))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(edtSma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label11))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(edtPeriod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label12))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnOrbitCalc)
                                        .addGap(0, 30, Short.MAX_VALUE))
                                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)))
                    );
                }
                tabbedPane1.addTab("\u041e\u0440\u0431\u0438\u0442\u0430", panel2);
            }
            dialogPane.add(tabbedPane1, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.NORTH);
        setSize(665, 415);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JPanel dialogPane;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel panel3;
    private JLabel label1;
    private JFormattedTextField formattedGrads;
    private JLabel label2;
    private JTextField textFieldGradFloat;
    private JPanel panel4;
    private JFormattedTextField formattedTextFieldDate;
    private JTextField textFieldJD;
    private JLabel label3;
    private JLabel label4;
    private JPanel panel2;
    private JTextField edtMass;
    private JTextField edtRadius;
    private JTextField edtHp;
    private JTextField edtHa;
    private JTextField edtEcc;
    private JTextField edtSma;
    private JButton btnOrbitCalc;
    private JTextField edtVp;
    private JTextField edtPeriod;
    private JScrollPane scrollPane1;
    private JTextPane txtPaneOrbitInfo;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JLabel label10;
    private JLabel label11;
    private JLabel label12;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
