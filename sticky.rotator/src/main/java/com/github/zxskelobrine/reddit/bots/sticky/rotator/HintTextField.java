package com.github.zxskelobrine.reddit.bots.sticky.rotator;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * Hint Text Field class - <a href=
 * "http://stackoverflow.com/questions/1738966/java-jtextfield-with-input-hint"
 * >http
 * ://stackoverflow.com/questions/1738966/java-jtextfield-with-input-hint</a>
 * 
 * 
 * @author Bart Kiers
 *
 */
public class HintTextField extends JTextField implements FocusListener {

	private static final long serialVersionUID = 1066954197666697481L;
	private final String hint;
	private boolean showingHint;

	public HintTextField(final String hint) {
		super(hint);
		this.hint = hint;
		this.showingHint = true;
		super.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText("");
			showingHint = false;
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText(hint);
			showingHint = true;
		}
	}

	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}
}