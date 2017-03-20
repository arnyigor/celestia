package com.arny.celestiatools.views;
import com.arny.celestiatools.models.CelestiaAsteroid;
import com.arny.celestiatools.models.Controller;
import com.arny.celestiatools.models.onResultCelestiaAsteroids;
import com.arny.celestiatools.models.onResultParse;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
public class ToolsForm extends JFrame {
	private JFrame mainFrame;
	private JPanel panel;
	private JTabbedPane tabbedPane1;
	private JPanel asteroidPanel;
	private JPanel CalcPanel;
	private JButton btnDownload;
	private JButton btnUnpackJson;
	private JButton btnWriteOrbits;
	private JLabel JLabel1;
	private JComboBox jComboBoxSource;
	private JCheckBox checkBox1;
	private JCheckBox checkBox2;
	private JCheckBox checkBox3;
	private JButton btnCalc;
	private JLabel lblCalcRes;
	private JTable tblAsteroidsData;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private Controller controller;
	private ArrayList<CelestiaAsteroid> celestiaAsteroids;
	private TableModel tableModel;

	public ToolsForm() {
		mainFrame = new JFrame("Celestia Tools");
		mainFrame.setResizable(false);
		mainFrame.setContentPane(panel);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initUI();
		mainFrame.pack();
		mainFrame.setVisible(true);
		controller = new Controller();
	}

	private void initUI() {
		setFrameForm(mainFrame);
		initButtons();
		checkBox1.setText("Apollo");
		checkBox2.setText("Amor");
		checkBox3.setText("Aten");
	}

	public void showMainFrameWindow() {
		mainFrame.setVisible(true);
	}

	private void initButtons() {
		btnDownload.setText("Загрузить");
		btnDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLabel1.setText("Загрузка файла...");
				controller.downloadFile(jComboBoxSource.getSelectedIndex(), new onResultParse() {
					@Override
					public void parseResult(String method, boolean success, String result) {
						String message = Controller.getMessage(success, method);
						JLabel1.setText(result);
//						jTextArea1.setText(message);
						int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
						JOptionPane.showMessageDialog(null, message, method, messType);

					}
				});
			}
		});
		btnUnpackJson.setText("Распаковать");
		btnUnpackJson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileopen = new JFileChooser();
				fileopen.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int ret = fileopen.showDialog(null, "Открыть файл");
				if (ret == JFileChooser.APPROVE_OPTION) {
					File file = fileopen.getSelectedFile();
					JLabel1.setText("Парсинг в процессе...File:" + file.getAbsolutePath());
					controller.workJsonFile(file, new onResultParse() {
						@Override
						public void parseResult(String method, boolean success, String result) {
							String message = Controller.getMessage(success, method);
							JLabel1.setText(result);
//							jLabel1.setText(message);
							int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
							JOptionPane.showMessageDialog(null, message, method, messType);
						}
					});

				}
			}
		});
		btnWriteOrbits.setText("Записать орбиты");
		celestiaAsteroids = new ArrayList<>();
		tableModel = new AbstractTableModel() {
			String[] columnNames = {"№","Name","Radius,km"};

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
						return rowIndex+1;
					case 1:
						return celestiaAsteroids.get(rowIndex).getName();
                    case 2:
                        return celestiaAsteroids.get(rowIndex).getRadius();
				}
				return "";
			}
		};
		btnWriteOrbits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLabel1.setText("Запись файла...");
				ArrayList<String> orbitalTypes = new ArrayList<>();
				if (checkBox1.isSelected()) {
					orbitalTypes.add("Apollo");
				}
				if (checkBox2.isSelected()) {
					orbitalTypes.add("Amor");
				}
				if (checkBox3.isSelected()) {
					orbitalTypes.add("Aten");
				}
				controller.writeOrbitalParamFile(orbitalTypes, new onResultParse() {
					@Override
					public void parseResult(String method, boolean success, String result) {
						String message = Controller.getMessage(success, method);
						JLabel1.setText(result);
						int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
						JOptionPane.showMessageDialog(null, message, method, messType);

					}
				}, new onResultCelestiaAsteroids() {
					@Override
					public void dataCallback(ArrayList<CelestiaAsteroid> asteroids) {
                        celestiaAsteroids =  asteroids;
						tblAsteroidsData.setModel(tableModel);
					}
				});
			}
		});
		lblCalcRes.setText("Расстояние в au = 0");
		btnCalc.setText("Расчет");
		btnCalc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.calculateMOID(new onResultParse() {
					@Override
					public void parseResult(String method, boolean success, String result) {
						lblCalcRes.setText(result);
					}
				});
			}
		});
	}

	private void setFrameForm(JFrame frame) {
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int dx = (int) (dimension.getWidth() / 2);
		int dy = (int) (dimension.getHeight() / 2);
		frame.setLocation(dx - (WIDTH/2), dy - (HEIGHT/2));
	}
}
