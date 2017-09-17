/*
 * Created by JFormDesigner on Sun Sep 03 16:42:32 MSK 2017
 */

package com.arny.celestiatools.views;

import com.arny.celestiatools.controller.Controller;
import com.arny.celestiatools.utils.AstroUtils;
import com.arny.celestiatools.utils.DateTimeUtils;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
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

    private void button1ActionPerformed(ActionEvent e) {
        String result = AstroUtils.getSunsetRise(DateTimeUtils.convertTimeStringToLong("25 06 1990", "dd MM yyyy"), 40.9, -74.3, true, AstroUtils.TWILIGHT);
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        textFieldGradFloat = new JTextField();
        formattedGrads = new JFormattedTextField();
        label1 = new JLabel();
        label2 = new JLabel();
        panel2 = new JPanel();

        //======== this ========
        setTitle("\u041a\u0430\u043b\u044c\u043a\u0443\u043b\u044f\u0442\u043e\u0440");
        setIconImage(null);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

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

                        //---- textFieldGradFloat ----
                        textFieldGradFloat.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                        textFieldGradFloat.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                textFieldGradFloatKeyReleased(e);
                            }
                        });

                        //---- formattedGrads ----
                        formattedGrads.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent e) {
                                formattedTextFieldKeyReleased(e);
                            }
                        });

                        //---- label1 ----
                        label1.setText("Градусы минуты секунды(float)");

                        //---- label2 ----
                        label2.setText("Градусы(float)");

                        GroupLayout panel1Layout = new GroupLayout(panel1);
                        panel1.setLayout(panel1Layout);
                        panel1Layout.setHorizontalGroup(
                            panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(label1, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                        .addComponent(formattedGrads, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addComponent(label2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addComponent(textFieldGradFloat, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE)))
                                    .addContainerGap(331, Short.MAX_VALUE))
                        );
                        panel1Layout.setVerticalGroup(
                            panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1)
                                        .addComponent(label2))
                                    .addGap(5, 5, 5)
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(formattedGrads, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textFieldGradFloat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(214, Short.MAX_VALUE))
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
                                .addGap(0, 265, Short.MAX_VALUE)
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
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField textFieldGradFloat;
    private JFormattedTextField formattedGrads;
    private JLabel label1;
    private JLabel label2;
    private JPanel panel2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
