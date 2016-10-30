package ch.hsr.zebrastreifensafari.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public class JTextPlaceHolder extends JTextField implements FocusListener {

    private final String placeholder;
    private final Color defaultForeground;
    private final Color hintForground;
    private boolean showHint;

    public JTextPlaceHolder(String placeholder) {
        super(placeholder);
        this.placeholder = placeholder;
        showHint = true;
        defaultForeground = getForeground();
        hintForground = new Color(100, 100, 100);
        addFocusListener(this);
        setForeground(hintForground);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (getText().isEmpty()) {
            setText("");
            setForeground(defaultForeground);
            showHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (getText().isEmpty()) {
            setText(placeholder);
            setForeground(hintForground);
            showHint = true;
        }
    }

    @Override
    public String getText() {
        return showHint ? "" : super.getText();
    }
}
