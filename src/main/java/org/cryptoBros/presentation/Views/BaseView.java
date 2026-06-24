package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.JImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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

	/**
	 * Builds the main content area combining the data panel and image panel.
	 *
	 * @param dataPanel the panel containing the view data and controls
	 * @param imagePanel the panel containing the application image
	 * @return the assembled core panel
	 */
	protected JPanel buildCore(JPanel dataPanel, JPanel imagePanel) {
		JPanel row = new JPanel(new BorderLayout());
		row.setBackground(new Color(239, 247, 255));
		row.add(dataPanel,  BorderLayout.WEST);
		row.add(imagePanel, BorderLayout.EAST);

		JPanel core = new JPanel(new GridBagLayout());
		core.setBackground(new Color(239, 247, 255));
		core.setOpaque(true);
		core.add(row);

		return core;
	}

	/**
	 * Builds the header panel displaying the specified title.
	 *
	 * @param title the title to display
	 * @return the configured header panel
	 */
	protected JPanel buildHeader(String title) {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(83, 136, 252));
		header.setBorder(BorderFactory.createEmptyBorder(50, 50, 10, 0));
		header.setOpaque(true);

		JLabel label = new JLabel(title);
		label.setFont(new Font("Apple Casual", Font.BOLD, 50));
		label.setForeground(Color.WHITE);

		header.add(label, BorderLayout.SOUTH);
		return header;
	}

	/**
	 * Builds the image panel containing the application logo.
	 *
	 * @return the configured image panel
	 */
	protected JPanel buildImagePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(true);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));
		panel.setMaximumSize(new Dimension(550, 350));
		panel.setPreferredSize(new Dimension(500, 420));
		panel.setBackground(new Color(239, 247, 255));
		panel.add(new JImagePanel("images/logo.png"), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Creates a label with the specified text and default styling.
	 *
	 * @param text the text to display
	 * @return the configured label
	 */
	protected JLabel buildLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Apple Casual", Font.PLAIN, 20));
		return label;
	}

	/**
	 * Creates a text field with placeholder text.
	 *
	 * @param placeholder the placeholder text to display
	 * @return the configured text field
	 */
	protected JTextField buildTextField(String placeholder) {
		JTextField jTextField = new JTextField();
		jTextField.setPreferredSize(new Dimension(500, 25));
		jTextField.setMaximumSize(new Dimension(500, 25));
		jTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
		return createPlaceholderTextField(placeholder, jTextField);
	}

	/**
	 * Configures a text field with placeholder behavior.
	 * The placeholder is displayed when the field is empty and
	 * removed when the field gains focus.
	 *
	 * @param placeholder the placeholder text to display
	 * @param jTextField the text field to configure
	 * @return the configured text field
	 */
	static JTextField createPlaceholderTextField(String placeholder, JTextField jTextField) {
		jTextField.setText(placeholder);
		jTextField.setForeground(Color.GRAY);
		jTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (jTextField.getText().equals(placeholder)) {
					jTextField.setText("");
					jTextField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (jTextField.getText().isEmpty()) {
					jTextField.setForeground(Color.GRAY);
					jTextField.setText(placeholder);
				}
			}
		});

		return jTextField;
	}

	/**
	 * Creates a password field with the default styling.
	 *
	 * @return the configured password field
	 */
	protected JPasswordField buildPasswordField() {
		JPasswordField password = new JPasswordField();
		password.setPreferredSize(new Dimension(500, 25));
		password.setMaximumSize(new Dimension(500, 25));
		password.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
		password.setForeground(Color.GRAY);

		return password;
	}

	/**
	 * Creates a confirmation button with the specified label.
	 *
	 * @param label the text displayed on the button
	 * @return the configured button
	 */
	protected JButton buildConfirmButton(String label) {
		JButton button = new JButton(label);
		button.setBackground(new Color(100, 149, 237));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.PLAIN, 20));
		button.setOpaque(true);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(150, 35));
		button.setMaximumSize(new Dimension(150, 35));
		return button;
	}

	/**
	 * Creates a button styled as a clickable link.
	 *
	 * @param label the text displayed on the button
	 * @return the configured link button
	 */
	protected JButton buildLinkButton(String label) {
		JButton button = new JButton(label);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setForeground(new Color(83, 136, 252));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setFont(new Font("Apple Casual", Font.PLAIN, 14));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		return button;
	}

	/**
	 * Adds a labeled field and spacing components to the specified panel.
	 *
	 * @param panel the panel to which the field is added
	 * @param label the label text associated with the field
	 * @param field the input component to add
	 */
	protected void addField(JPanel panel, String label, JComponent field) {
		panel.add(buildLabel(label));
		panel.add(Box.createVerticalStrut(10));
		panel.add(field);
		panel.add(Box.createVerticalStrut(15));
	}

}

