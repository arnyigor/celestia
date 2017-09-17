/*
 * Created by JFormDesigner on Sun Sep 03 16:42:32 MSK 2017
 */

package com.arny.celestiatools.views;

import com.arny.celestiatools.controller.Controller;
import com.arny.celestiatools.utils.astronomy.AstroUtils;
import com.arny.celestiatools.utils.DateTimeUtils;

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
        Controller.setFrameForm(this,600,500);
        setVisible(true);
    }

    private void textFieldGradFloatKeyReleased(KeyEvent e) {
        String filterStr = "0123456789.";
        char c = e.getKeyChar();
        if(filterStr.indexOf(c)<0){
            e.consume();
        }
        String s = controller.calcGradToMasked(textFieldGradFloat.getText());
        formattedGrads.setValue(s);
    }

    private void formattedTextFieldKeyReleased(KeyEvent ke) {
        String filterStr = "0123456789.";
        char c = ke.getKeyChar();
        if(filterStr.indexOf(c)<0){
            ke.consume();
        }
        textFieldGradFloat.setText(String.valueOf(controller.calcGradToFloat(formattedGrads.getText())));
    }

    private void formattedTextFieldDateKeyReleased(KeyEvent e) {
        String filterStr = "0123456789";
        char c = e.getKeyChar();
        if(filterStr.indexOf(c)<0){
            e.consume();
        }
       textFieldJD.setText(controller.calcDatTimeToJD(formattedTextFieldDate.getText()));

    }

    private void textFieldJDKeyReleased(KeyEvent e) {
        String filterStr = "0123456789.";
        char c = e.getKeyChar();
        if(filterStr.indexOf(c)<0){
            e.consume();
        }
        formattedTextFieldDate.setText(controller.calcJDToDatTime(textFieldJD.getText()));
    }

    private void menuItem1ActionPerformed(ActionEvent e) {
        String sr = AstroUtils.getSunsetRise(DateTimeUtils.convertTimeStringToLong("17 09 2017", "dd MM yyyy"), 55.61666666666667, 38.61666666666667, false, AstroUtils.TWILIGHT);
        System.out.println(sr);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        dialogPane = new JPanel();
        contentPanel = new JPanel();
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

        //======== this ========
        setTitle("\u041a\u0430\u043b\u044c\u043a\u0443\u043b\u044f\u0442\u043e\u0440");
        setIconImage(null);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("\u041c\u0435\u043d\u044e");

                //---- menuItem1 ----
                menuItem1.setText("text");
                menuItem1.addActionListener(e -> menuItem1ActionPerformed(e));
                menu1.add(menuItem1);

                //---- menuItem2 ----
                menuItem2.setText("text");
                menu1.add(menuItem2);
            }
            menuBar1.add(menu1);
        }
        setJMenuBar(menuBar1);

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

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
                                    .addContainerGap(321, Short.MAX_VALUE))
                        );
                        panel1Layout.setVerticalGroup(
                            panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(81, Short.MAX_VALUE))
                        );
                    }
                    tabbedPane1.addTab("\u041e\u0431\u0449\u0435\u0435", panel1);

                    //======== panel2 ========
                    {

                        GroupLayout panel2Layout = new GroupLayout(panel2);
                        panel2.setLayout(panel2Layout);
                        panel2Layout.setHorizontalGroup(
                            panel2Layout.createParallelGroup()
                                .addGap(0, 625, Short.MAX_VALUE)
                        );
                        panel2Layout.setVerticalGroup(
                            panel2Layout.createParallelGroup()
                                .addGap(0, 243, Short.MAX_VALUE)
                        );
                    }
                    tabbedPane1.addTab("\u0421\u043f\u0435\u0446", panel2);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(tabbedPane1)
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addComponent(tabbedPane1)
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JPanel dialogPane;
    private JPanel contentPanel;
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
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
