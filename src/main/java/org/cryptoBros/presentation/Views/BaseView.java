package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class BaseView extends JPanel {

    private JPanel content;

    public BaseView() {
        setLayout(new BorderLayout());
        setBackground(new Color(239, 247, 255));

        content =  new JPanel(new BorderLayout());
        content.setBackground(new Color(239, 247, 255));
        content.setOpaque(true);

        add(content, BorderLayout.CENTER);
        configureView();
    }

    public JPanel getContent () {
        return this.content;
    }

    protected abstract void configureView();

    public abstract void setActions(ActionListener listener);
}

