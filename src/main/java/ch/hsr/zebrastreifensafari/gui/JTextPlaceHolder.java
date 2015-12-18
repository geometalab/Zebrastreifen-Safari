package ch.hsr.zebrastreifensafari.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 13:59
 * @date : 18.12.2015
 */

public class JTextPlaceHolder extends JTextField implements FocusListener {

    private String placeholder;
    private boolean showHint;
    private Color defaultForeground;
    private Color hintForground;

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
