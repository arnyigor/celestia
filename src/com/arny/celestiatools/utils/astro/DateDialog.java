package com.arny.celestiatools.utils.astro;

import java.awt.*;
import java.util.Date;

/**
 *  Date Setting Dialog
 */
class DateDialog extends Frame {

	protected TextField tfYear;
	protected TextField tfDate;
	protected Choice choiceMonth;

	protected Button buttonOk;
	protected Button buttonCancel;
	protected Button buttonToday;

	protected OrbitViewer orbitViewer;

	public DateDialog(OrbitViewer orbitViewer, ATime atime) {
		this.orbitViewer = orbitViewer;

		// Layout
		setLayout(new GridLayout(2, 3, 4, 4));
		setFont(new Font("Dialog", Font.PLAIN, 14));

		// Controls
		choiceMonth = new Choice();
		for (int i = 0; i < 12; i++) {
			choiceMonth.addItem(ATime.getMonthAbbr(i + 1));
		}
		choiceMonth.select(atime.getMonth() - 1);
		add(choiceMonth);

		Integer iDate = atime.getDay();
		tfDate = new TextField(iDate.toString(), 2);
		add(tfDate);

		Integer iYear = atime.getYear();
		tfYear = new TextField(iYear.toString(), 4);
		add(tfYear);

		buttonToday = new Button("Today");
		add(buttonToday);
		buttonOk = new Button("OK");
		add(buttonOk);
		buttonCancel = new Button("Cancel");
		add(buttonCancel);

		pack();
		setTitle("Date");
		setResizable(false);
		show();
	}

	/**
	 * Event Handler
	 */
	public boolean handleEvent(Event evt) {
		if (evt.id == Event.ACTION_EVENT) {
			ATime atime = null;
			if (evt.target == buttonOk) {
				int nYear = Integer.valueOf(tfYear.getText());
				int nMonth = choiceMonth.getSelectedIndex() + 1;
				int nDate = Integer.valueOf(tfDate.getText());
				if (1600 <= nYear && nYear <= 2199 &&
						1 <= nMonth && nMonth <= 12 &&
						1 <= nDate && nDate <= 31) {
					atime = new ATime(nYear, nMonth, (double) nDate, 0.0);
				}
			} else if (evt.target == buttonToday) {
				Date date = new Date();
				choiceMonth.select(date.getMonth());
				tfDate.setText(Integer.toString(date.getDate()));
				tfYear.setText(Integer.toString(date.getYear() + 1900));
				return false;
			} else if (evt.target != buttonCancel) {
				return false;
			}
			dispose();
			orbitViewer.endDateDialog(atime);
			return true;
		}
		return false;    // super.handleEvent(evt);
	}
}
