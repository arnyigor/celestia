package com.arny.celestiatools.views;
import com.arny.celestiatools.models.CelestiaAsteroid;
import com.arny.celestiatools.controller.Controller;
import com.arny.celestiatools.models.onProgressUpdate;
import com.arny.celestiatools.models.onResultCelestiaAsteroids;
import com.arny.celestiatools.models.onResultCallback;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
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
	private JLabel labelInfo;
	private JComboBox jComboBoxSource;
	private JCheckBox checkBox1;
	private JCheckBox checkBox2;
	private JCheckBox checkBox3;
	private JButton btnCalc;
	private JTable tblAsteroidsData;
	private JTextPane lblCalcRes;
    private JTextPane pnlAsteroidData;
    private JProgressBar progressBar;
	private JTextField textField1;
    private JButton btnOrbitViewer;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
	private Controller controller;
	private ArrayList<CelestiaAsteroid> celestiaAsteroids;
    private CelestiaAsteroid celestiaAsteroid;
    private TableModel tableModel;
	private TableRowSorter<TableModel> rowSorter;
	public ToolsForm() {
		initUI();
		controller = new Controller();
        initTableAsteroids();
    }


    private void initTableAsteroids() {
        celestiaAsteroids = new ArrayList<>();
        controller.getAsterTableData(new onResultCelestiaAsteroids() {
            @Override
            public void dataCallback(ArrayList<CelestiaAsteroid> asteroids) {
                celestiaAsteroids = asteroids;
                setModelToTable();
                if (celestiaAsteroids.size()>0){
                    celestiaAsteroid = celestiaAsteroids.get(0);
                    pnlAsteroidData.setText(controller.formatAsteroidData(celestiaAsteroid));
                }
                progressBar.setValue(0);
            }
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
        setFrameForm(mainFrame,WIDTH,HEIGHT);
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
		btnDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				labelInfo.setText("Загрузка файла...");
				controller.downloadFile(jComboBoxSource.getSelectedIndex(), new onResultCallback() {
					@Override
					public void result(String method, boolean success, String result) {
						String message = Controller.getMessage(success, method);
						labelInfo.setText(result);
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
					labelInfo.setText("Парсинг в процессе...File:" + file.getAbsolutePath());
					controller.workJsonFile(file, new onResultCallback() {
						@Override
						public void result(String method, boolean success, String result) {
							String message = Controller.getMessage(success, method);
							labelInfo.setText(result);
							int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
							JOptionPane.showMessageDialog(null, message, method, messType);
						}
					});

				}
			}
		});
		btnWriteOrbits.setText("Записать орбиты");
        progressBar.setMinimum(0);
        progressBar.setStringPainted(true);
		btnWriteOrbits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				labelInfo.setText("Запись файла...");
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
                controller.writeOrbitalParamFile(orbitalTypes, new onResultCallback() {
                    @Override
                    public void result(String method, boolean success, String result) {
                        String message = Controller.getMessage(success, method);
                        labelInfo.setText(result);
                        int messType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                        JOptionPane.showMessageDialog(null, message, method, messType);

                    }
                }, new onResultCelestiaAsteroids() {
                    @Override
                    public void dataCallback(ArrayList<CelestiaAsteroid> asteroids) {
                        celestiaAsteroids = asteroids;
                        setModelToTable();
                        if (celestiaAsteroids.size() > 0) {
                            celestiaAsteroid = celestiaAsteroids.get(0);
                            pnlAsteroidData.setText(controller.formatAsteroidData(celestiaAsteroid));
                        }
                        progressBar.setValue(0);
                    }
                }, new onProgressUpdate() {
                    @Override
                    public void update(String method, int total, int progress, String estimate) {
                        progressBar.setMaximum(total);
                        progressBar.setValue(progress);
                        labelInfo.setText("Примерно осталось:" + estimate);
                    }
                });
			}
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
		btnCalc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.calculate(new onResultCallback() {
                    @Override
                    public void result(String method, boolean success, String result) {
                        lblCalcRes.setText("Результат:" + result);
                    }
                });
			}
		});
        btnOrbitViewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.orbitViewerStart(celestiaAsteroid);
            }
        });
	}

    private void setModelToTable() {
        tableModel = new AbstractTableModel() {
            String[] columnNames = {"№","Name","Type","Radius,km"};
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
                        return celestiaAsteroids.get(rowIndex).getOrbitType();
                    case 3:
                        return celestiaAsteroids.get(rowIndex).getRadius();
                }
                return "";
            }
        };
	    rowSorter = new TableRowSorter<TableModel>(tableModel);
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

	public static void setFrameForm(JFrame frame,int w,int h) {
		frame.setPreferredSize(new Dimension(w, h));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int dx = (int) (dimension.getWidth() / 2);
		int dy = (int) (dimension.getHeight() / 2);
		frame.setLocation(dx - (w/2), dy - (h/2));
	}

}
