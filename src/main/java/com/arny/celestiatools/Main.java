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
//		test();
	}

	static void test() {
		try {
			int floor = (int) MathUtils.round(86400.0000001 / 86400,0);
			System.out.println(floor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
