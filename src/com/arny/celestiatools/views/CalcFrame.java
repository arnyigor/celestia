/*
 * Created by JFormDesigner on Sun Sep 03 16:42:32 MSK 2017
 */

package com.arny.celestiatools.views;

import com.arny.celestiatools.controller.Controller;
import com.arny.celestiatools.utils.AstroUtils;
import com.arny.celestiatools.utils.DateTimeUtils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

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
        calcLabel.setText("Результат = " + result);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        calcBtn1 = new JButton();
        calcLabel = new JLabel();
        textField1 = new JTextField();
        textFieldLat = new JTextField();
        textFieldLon = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("Calc");
        setIconImage(new ImageIcon("C:\\Users\\Sedoy\\Desktop\\img1.jpg").getImage());
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- calcBtn1 ----
                calcBtn1.setText("text");
                calcBtn1.addActionListener(e -> {
			button1ActionPerformed(e);
			button1ActionPerformed(e);
		});

                //---- calcLabel ----
                calcLabel.setText("text");

                //---- textField1 ----
                textField1.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                textField1.setToolTipText("Date");

                //---- textFieldLat ----
                textFieldLat.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                textFieldLat.setToolTipText("Latitude");
                textFieldLat.setText("0.00");

                //---- textFieldLon ----
                textFieldLon.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                textFieldLon.setToolTipText("Latitude");
                textFieldLon.setText("0.00");

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addComponent(calcBtn1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(textFieldLat, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(textFieldLon, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(calcLabel, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(calcLabel)
                                .addComponent(textFieldLon, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldLat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(calcBtn1))
                            .addContainerGap(230, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JButton calcBtn1;
    private JLabel calcLabel;
    private JTextField textField1;
    private JTextField textFieldLat;
    private JTextField textFieldLon;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
