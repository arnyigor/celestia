package com.arny.celestiatools;
import com.arny.celestiatools.utils.MathUtils;
import com.arny.celestiatools.views.MainForm;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new MainForm();
	}
}
