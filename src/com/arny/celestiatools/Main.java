package com.arny.celestiatools;
import com.arny.celestiatools.views.ToolsForm;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {

        JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ToolsForm controller = new ToolsForm();
		controller.showMainFrameWindow();
	}
}
