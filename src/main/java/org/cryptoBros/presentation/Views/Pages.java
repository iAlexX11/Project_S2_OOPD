package org.cryptoBros.presentation.Views;

import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Abstract base for views with a navigation header and balance display.
 */
public abstract class Pages extends BaseView{

	/** Creates a new Pages instance. */
	protected Pages() {}

	/** The balance display label. */
	private JLabel jLBalance;
    /** The estimated profit label. */
    private JLabel jLEstimatedProfit;
	/** The home navigation button. */
	private JButton jBHome;
	/** The settings navigation button. */
	private JButton jBSettings;
	/** The portfolio navigation button. */
	private JButton jBPortfolio;
	/** The manage crypto navigation button. */
	private JButton jBManageCrypto;
	/** Whether the current user is an admin. */
	private boolean isAdmin = false;
	protected static final Color BG_COLOR     = new Color(239, 247, 255);
	protected static final Color CARD_COLOR   = new Color(185, 200, 245);
	protected static final Color BORDER_COLOR = new Color(150, 170, 230);

	/**
	 * Creates a base modal dialog with the specified title.
	 *
	 * @param title the title of the dialog
	 * @return a configured modal JDialog
	 */
	protected JDialog createBaseDialog(String title) {
		JDialog dialog = new JDialog();
		dialog.setTitle(title);
		dialog.setModal(true);
		return dialog;
	}

	/**
	 * Creates GridBagConstraints for form field layout positioning.
	 *
	 * @param column the grid column index
	 * @param row the grid row index
	 * @return configured GridBagConstraints
	 */
	protected GridBagConstraints createFieldConstraints(int column, int row) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = column;
		c.gridy = row;
		c.insets = new Insets(8, 10, 8, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = column == 1 ? 1.0 : 0;
		return c;
	}

	/**
	 * Sets whether the current user is an admin.
	 *
	 * @param isAdmin true if the user is an admin
	 */
	public void setTypeUser(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * Builds and returns the navigation header panel.
	 *
	 * @return the header panel
	 */
	public JPanel setHeader() {
		return buildHeader(isAdmin ? buildAdminButtons() : buildButtons());
	}

	/**
	 * Binds action listeners to header navigation buttons.
	 *
	 * @param listener the action listener to bind
	 */
	public void addHeaderActions(ActionListener listener) {
		bindButton(jBHome, ButtonEnumeration.HOME, listener);
		bindButton(jBSettings, ButtonEnumeration.SETTINGS, listener);
		if (!isAdmin) bindButton(jBPortfolio, ButtonEnumeration.PORTFOLIO, listener);
		if (isAdmin) bindButton(jBManageCrypto, ButtonEnumeration.MANAGE_CRYPTO, listener);
	}

	/** Re-initializes the view configuration. */
	public void init() {
		configureView();
	}

	/**
	 * Builds the full header layout including navigation and balance panel.
	 *
	 * @param navWrapper the navigation button panel
	 * @return the complete header panel
	 */
	private JPanel buildHeader(JPanel navWrapper) {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(83, 136, 252));
		header.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 0));
		header.setOpaque(true);
		header.add(navWrapper, BorderLayout.WEST);
		header.add(buildBalancePanel(), BorderLayout.EAST);
		return header;
	}

	/**
	 * Builds navigation buttons for standard users.
	 *
	 * @return panel containing navigation buttons
	 */
	private JPanel buildButtons() {
		jBHome = createIconButton("images/home.png");
		jBSettings = createIconButton("images/settings.png");
		jBPortfolio = createIconButton("images/portfolio.png");

		return getPanel(jBPortfolio);
	}

	/**
	 * Builds navigation buttons for admin users.
	 *
	 * @return panel containing admin navigation buttons
	 */
	private JPanel buildAdminButtons() {
		jBHome = createIconButton("images/home.png");
		jBSettings = createIconButton("images/settings.png");
		jBManageCrypto = createIconButton("images/manage_crypto.png");

		return getPanel(jBManageCrypto);
	}

	/**
	 * Assembles navigation buttons into a horizontal layout panel.
	 *
	 * @param jBManageCrypto navigation button (admin or portfolio context)
	 * @return wrapped navigation panel
	 */
	private JPanel getPanel(JButton jBManageCrypto) {
		JPanel col1 = new JPanel();
		col1.setLayout(new BoxLayout(col1, BoxLayout.X_AXIS));
		col1.setOpaque(false);
		col1.add(Box.createHorizontalGlue());
		col1.add(jBHome);
		col1.add(Box.createHorizontalStrut(20));
		col1.add(jBSettings);
		col1.add(Box.createHorizontalStrut(20));
		col1.add(jBManageCrypto);
		col1.add(Box.createHorizontalGlue());

		return wrapPanel(col1);
	}

	/**
	 * Wraps a panel inside a transparent container for layout consistency.
	 *
	 * @param panel the panel to wrap
	 * @return wrapped panel
	 */
	private JPanel wrapPanel(JPanel panel) {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(panel, BorderLayout.CENTER);
		return wrapper;
	}

	/**
	 * Builds the right-side panel containing balance and profit labels.
	 *
	 * @return the balance panel
	 */
	private JPanel buildBalancePanel() {
		JPanel col2 = new JPanel(new GridLayout(2, 1, 0, 5));
		col2.setOpaque(false);
		col2.add(setBalance());
        if (!isAdmin) col2.add(setEstimatedProfit());
		col2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
		return col2;
	}

	/**
	 * Creates a navigation icon button with standardized styling.
	 *
	 * @param imagePath path to the icon image
	 * @return configured JButton
	 */
	private JButton createIconButton(String imagePath) {
		JButton jButton = new JButton(new ImageIcon(imagePath));
		jButton.setBackground(Color.WHITE);
		jButton.setOpaque(true);
		jButton.setContentAreaFilled(true);
		jButton.setBorderPainted(false);
		jButton.setFocusPainted(false);
		jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jButton.setPreferredSize(new Dimension(90, 35));
		jButton.setMaximumSize(new Dimension(90, 35));
		return jButton;
	}

	/**
	 * Binds an action command and listener to a navigation button.
	 *
	 * @param button the button to configure
	 * @param command the action command enum
	 * @param listener the action listener
	 */
	private void bindButton(JButton button, ButtonEnumeration command, ActionListener listener) {
		button.setActionCommand(command.name());
		button.addActionListener(listener);
	}

	/**
	 * Creates and returns the balance label.
	 *
	 * @return the balance label
	 */
	public JLabel setBalance() {
		jLBalance = new JLabel();
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		return jLBalance;
	}

	/**
	 * Updates the displayed balance value.
	 *
	 * @param balance the new balance amount
	 */
	public void updateBalance(double balance){
		jLBalance.setText("The balance is: " + String.format("%.2f", balance) + "€");
		jLBalance.setForeground(Color.WHITE);
		jLBalance.setFont(new Font("Apple Casual", Font.PLAIN, 18));
		jLBalance.revalidate();
		jLBalance.repaint();
	}

    /**
     * Creates and returns the estimated profit label.
     *
     * @return the estimated profit label
     */
    public JLabel setEstimatedProfit() {
        jLEstimatedProfit = new JLabel();
        jLEstimatedProfit.setForeground(Color.WHITE);
        jLEstimatedProfit.setFont(new Font("Apple Casual", Font.PLAIN, 18));
        return jLEstimatedProfit;
    }

    /**
     * Updates the displayed estimated profit value.
     *
     * @param profit the new estimated profit amount
     */
    public void updateEstimatedProfit(double profit) {
		if (jLEstimatedProfit == null) return;
        String sign = profit >= 0 ? "+" : "";
        jLEstimatedProfit.setText("Estimated Profit: " + sign + String.format("%,.2f", profit) + "€");
        jLEstimatedProfit.setForeground(Color.WHITE);
        jLEstimatedProfit.setFont(new Font("Apple Casual", Font.PLAIN, 18));
        jLEstimatedProfit.revalidate();
        jLEstimatedProfit.repaint();
    }

	/**
	 * Shows a simple input dialog with a single text field.
	 *
	 * @param title the dialog title
	 * @param label the input field label
	 * @return the entered text or null if cancelled
	 */
	protected String showSingleFieldDialog(String title, String label) {
		JDialog dialog = createBaseDialog(title);
		JPanel form = new JPanel(new GridBagLayout());
		form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JTextField field = new JTextField(15);
		form.add(new JLabel(label), createFieldConstraints(0, 0));
		form.add(field, createFieldConstraints(1, 0));

		final String[] result = {null};
		JButton submit = new JButton("Confirm");
		submit.addActionListener(e -> {
			result[0] = field.getText().trim();
			dialog.dispose();
		});

		GridBagConstraints bc = createFieldConstraints(0, 1);
		buildAndShowDialog(dialog, form, submit, bc);
		return result[0];
	}

	/**
	 * Finalizes and displays a dialog with form content and submit button.
	 *
	 * @param dialog the dialog window
	 * @param form the form panel
	 * @param submit the submit button
	 * @param bc layout constraints for the button
	 */
	static void buildAndShowDialog(JDialog dialog, JPanel form, JButton submit, GridBagConstraints bc) {
		bc.gridwidth = 2;
		bc.fill = GridBagConstraints.NONE;
		bc.anchor = GridBagConstraints.CENTER;
		form.add(submit, bc);

		dialog.add(form);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	/**
	 * Creates a styled wrapper panel for card-based UI sections.
	 *
	 * @param cardColor background color of the inner card
	 * @return wrapper panel containing the styled card
	 */
	protected JPanel buildCardWrapper(Color cardColor) {
		JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		wrapper.setBackground(BG_COLOR);
		wrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(cardColor);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
				BorderFactory.createEmptyBorder(16, 40, 16, 40)
		));

		wrapper.add(card);
		return wrapper;
	}

	/**
	 * Extracts the inner card panel from a wrapper.
	 *
	 * @param wrapper the wrapper panel
	 * @return inner card panel
	 */
	protected JPanel getCard(JPanel wrapper) {
		return (JPanel) wrapper.getComponent(0);
	}

	/**
	 * Creates a primary styled action button.
	 *
	 * @param text the button label
	 * @param actionCommand the action command identifier
	 * @return configured JButton
	 */
	protected JButton createPrimaryButton(String text, String actionCommand) {
		JButton button = new JButton(text);
		button.setActionCommand(actionCommand);
		button.setFont(new Font("SansSerif", Font.BOLD, 15));
		button.setBackground(new Color(99, 125, 217));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return button;
	}

	/**
	 * Creates a styled JButton with consistent UI formatting.
	 *
	 * @param text the button label
	 * @param background the background color of the button
	 * @return the configured JButton
	 */
	protected JButton getButton(String text, Color background) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.PLAIN, 16));
		button.setBackground(background);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setOpaque(true);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setPreferredSize(new Dimension(300, 50));
		button.setMaximumSize(new Dimension(300, 50));
		return button;
	}

}
