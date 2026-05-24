package org.cryptoBros.presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Abstract base class for all application views.
 */
public abstract class BaseView extends JPanel {

    /** The main content panel. */
    private JPanel content;

    /**
     * Initializes the layout and background.
     */
    public BaseView() {
        setLayout(new BorderLayout());
        setBackground(new Color(239, 247, 255));

        content =  new JPanel(new BorderLayout());
        content.setBackground(new Color(239, 247, 255));
        content.setOpaque(true);

        add(content, BorderLayout.CENTER);
        configureView();
    }

    /**
     * Returns the main content panel.
     *
     * @return the content panel
     */
    public JPanel getContent () {
        return this.content;
    }

    /**
     * Configures the view-specific components.
     */
    protected abstract void configureView();

    /**
     * Registers action listeners for the view.
     *
     * @param listener the action listener to register
     */
    public abstract void setActions(ActionListener listener);
}

