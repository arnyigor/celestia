/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arny.celestiatools;

import com.arny.celestiatools.models.Controller;
import com.arny.celestiatools.models.onResultParse;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.*;

/**
 * @author Arny
 */
public class MainForm extends JFrame {
	private ButtonGroup buttonGroup1;
	private JButton jButton4;
	private JButton jButtonCreateOrbit;
	private JButton jButtonDowload;
	private JButton jButtonUnzip;
	private JCheckBox jCheckBox1;
	private JCheckBox jCheckBox2;
	private JCheckBox jCheckBox3;
	private JComboBox<String> jComboBoxSource;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JMenuBar jMenuBar1;
	private JPanel jPanel1;
	private JPanel jPanel3;
	private JScrollPane jScrollPane1;
	private JTabbedPane jTabbedPane1;
	private JTextArea jTextArea1;
	private Controller controller;

	/**
	 * Creates new form MainForm
	 */
	public MainForm() {
		initComponents();
		controller = new Controller();
	}

	private void initComponents() {
		buttonGroup1 = new ButtonGroup();
		jTabbedPane1 = new JTabbedPane();
		jPanel1 = new JPanel();
		jButtonDowload = new JButton();
		jButtonUnzip = new JButton();
		jButtonCreateOrbit = new JButton();
		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();
		jCheckBox1 = new JCheckBox();
		jCheckBox2 = new JCheckBox();
		jCheckBox3 = new JCheckBox();
		jComboBoxSource = new JComboBox<>();
		jPanel3 = new JPanel();
		jButton4 = new JButton();
		jLabel2 = new JLabel();
		jLabel1 = new JLabel();
		jMenuBar1 = new JMenuBar();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Celestia Tools");
		setResizable(false);

		jButtonDowload.setText("Загрузить астероиды");
		jButtonDowload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButtonDowloadActionPerformed(evt);
			}
		});

		jButtonUnzip.setText("Открыть файл Gzip");
		jButtonUnzip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButtonUnzipActionPerformed(evt);
			}
		});

		jButtonCreateOrbit.setText("Создать файл ssc");
		jButtonCreateOrbit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButtonCreateOrbitActionPerformed(evt);
			}
		});

		jTextArea1.setEditable(false);
		jTextArea1.setBackground(new java.awt.Color(241, 241, 241));
		jTextArea1.setColumns(20);
		jTextArea1.setLineWrap(true);
		jTextArea1.setRows(5);
		jTextArea1.setWrapStyleWord(true);
		jTextArea1.setBorder(null);
		jScrollPane1.setViewportView(jTextArea1);
		jCheckBox1.setText("Apollo");
		jCheckBox2.setText("Amor");
		jCheckBox3.setText("Aten");
		jComboBoxSource.setModel(new DefaultComboBoxModel<>(new String[]{"PHAs", "NEAs"}));

		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(jComboBoxSource, 0, 128, Short.MAX_VALUE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(jButtonCreateOrbit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jButtonUnzip, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jButtonDowload, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
												.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
														.addComponent(jCheckBox3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(jCheckBox1, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jCheckBox2, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap())
		);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(jScrollPane1, GroupLayout.Alignment.LEADING)
										.addGroup(GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
												.addContainerGap()
												.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(jButtonDowload)
														.addComponent(jComboBoxSource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButtonUnzip)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButtonCreateOrbit)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(jCheckBox1)
														.addComponent(jCheckBox2))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jCheckBox3)))
								.addContainerGap(268, Short.MAX_VALUE))
		);

		jTabbedPane1.addTab("Астероиды", jPanel1);

		jPanel3.setBorder(BorderFactory.createTitledBorder("Расчет минимального расстояния"));

		jButton4.setText("Расчет");
		jButton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});

		jLabel2.setText("Расстояние  в au = 0");

		GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(
				jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(jPanel3Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jButton4)
								.addGap(18, 18, 18)
								.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(302, Short.MAX_VALUE))
		);
		jPanel3Layout.setVerticalGroup(
				jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(jPanel3Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jButton4)
										.addComponent(jLabel2))
								.addContainerGap(351, Short.MAX_VALUE))
		);

		jTabbedPane1.addTab("Калькулятор", jPanel3);

		setJMenuBar(jMenuBar1);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(jTabbedPane1)
										.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jTabbedPane1, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
		);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jButton4ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		jLabel2.setText("Расстояние в au = 0");
		jLabel1.setText("рассчет расстояния");
		controller.calculateMOID(new onResultParse() {
			@Override
			public void parseResult(String method, boolean success, String result) {
				String message = Controller.getMessage(success, method);
				jLabel2.setText(result);
				jLabel1.setText(message);
				int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
				JOptionPane.showMessageDialog(null, message, method, messType);

			}
		});
	}//GEN-LAST:event_jButton4ActionPerformed

	private void jButtonCreateOrbitActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonCreateOrbitActionPerformed
		jLabel1.setText("Запись файла...");
		jTextArea1.setText("");
		ArrayList<String> orbitalTypes = new ArrayList<>();
		if (jCheckBox1.isSelected()) {
			orbitalTypes.add("Apollo");
		}
		if (jCheckBox2.isSelected()) {
			orbitalTypes.add("Amor");
		}
		if (jCheckBox3.isSelected()) {
			orbitalTypes.add("Aten");
		}
		controller.writeOrbitalParamFile(orbitalTypes, new onResultParse() {
			@Override
			public void parseResult(String method, boolean success, String result) {
				String message = Controller.getMessage(success, method);
				jTextArea1.setText(result);
				jLabel1.setToolTipText(message);
				int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
				JOptionPane.showMessageDialog(null, message, method, messType);

			}
		});
	}//GEN-LAST:event_jButtonCreateOrbitActionPerformed

	private void jButtonUnzipActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonUnzipActionPerformed
		JFileChooser fileopen = new JFileChooser();
		fileopen.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int ret = fileopen.showDialog(null, "Открыть файл");
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fileopen.getSelectedFile();
			jLabel1.setText("File:" + file.getAbsolutePath());
			jTextArea1.setText("Парсинг в процессе...");
			controller.workJsonFile(file, new onResultParse() {
				@Override
				public void parseResult(String method, boolean success, String result) {
					String message = Controller.getMessage(success, method);
					jTextArea1.setText(result);
					jLabel1.setText(message);
					int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
					JOptionPane.showMessageDialog(null, message, method, messType);
				}
			});

		}
	}
	private void jButtonDowloadActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonDowloadActionPerformed
		jLabel1.setText("Загрузка файла...");
		jTextArea1.setText("");
		controller.downloadFile(jComboBoxSource.getSelectedIndex(), new onResultParse() {
			@Override
			public void parseResult(String method, boolean success, String result) {
				String message = Controller.getMessage(success, method);
				jLabel1.setText(result);
				jTextArea1.setText(message);
				int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
				JOptionPane.showMessageDialog(null, message, method, messType);

			}
		});
	}

	public static void main(String args[]) {
	/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
	     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
	 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

	/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainForm().setVisible(true);
			}
		});
	}

}
