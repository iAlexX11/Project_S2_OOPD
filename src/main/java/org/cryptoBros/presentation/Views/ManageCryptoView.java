package org.cryptoBros.presentation.Views;
import org.cryptoBros.presentation.Enum.ButtonEnumeration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays the admin crypto management interface.
 */
public class ManageCryptoView extends Pages {

    /** Creates a new ManageCryptoView. */
    public ManageCryptoView() {}

    /** The crypto management table. */
    private ManageCryptoTable manageCryptoTable;
	/** The add crypto button. */
	private JButton addButton;
	/** The confirmed new crypto name. */
	private String confirmedCryptoName;
	/** The current action listener. */
	private ActionListener actionListener;

	/**
	 * Configures the manage crypto view layout with the table section and the add-crypto section.
	 */
	@Override
	protected void configureView() {
		getContent().add(setCore(), BorderLayout.CENTER);
	}

	/**
	 * Registers action listeners for the header navigation and the add-crypto button.
	 *
	 * @param listener the action listener to attach
	 */
	@Override
	public void setActions(ActionListener listener) {
		this.actionListener = listener;
		getContent().add(setHeader(), BorderLayout.NORTH);
		addHeaderActions(listener);
		addButton.addActionListener(listener);
	}

	/**
	 * Builds the main content area of the view.
	 * Combines the cryptocurrency table section and the add-crypto section.
	 *
	 * @return the configured core panel
	 */
    private JPanel setCore() {
        JPanel core = new JPanel(new BorderLayout());
        core.setBackground(new Color(239, 247, 255));
        core.add(buildTableSection(), BorderLayout.CENTER);
        core.add(buildAddSection(), BorderLayout.SOUTH);
        return core;
    }

	/**
	 * Builds the cryptocurrency management table section.
	 * Displays the title and the table containing the available cryptocurrencies.
	 *
	 * @return the configured table section panel
	 */
    private JPanel buildTableSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(239, 247, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        JLabel jlTitle = new JLabel("Manage Cryptocurrencies", SwingConstants.CENTER);
        jlTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        jlTitle.setForeground(Color.BLACK);

        manageCryptoTable = new ManageCryptoTable();

        panel.add(jlTitle, BorderLayout.NORTH);
        panel.add(manageCryptoTable, BorderLayout.CENTER);

        return panel;
    }

	/**
	 * Builds the section used to add a new cryptocurrency.
	 * Contains a description and a button for selecting a JSON file.
	 *
	 * @return the configured add-crypto section panel
	 */
	private JPanel buildAddSection() {
		JPanel wrapper = buildCardWrapper(CARD_COLOR);
		JPanel card = getCard(wrapper);

		JLabel addTitle = new JLabel("Add New Cryptocurrency", SwingConstants.CENTER);
		addTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
		addTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel addDescription = new JLabel("Select the JSON file containing the currency information.");
		addDescription.setFont(new Font("SansSerif", Font.PLAIN, 13));
		addDescription.setAlignmentX(Component.CENTER_ALIGNMENT);

		addButton = createPrimaryButton("ADD", ButtonEnumeration.ADD_CRYPTO.name());

		card.add(addTitle);
		card.add(Box.createVerticalStrut(6));
		card.add(addDescription);
		card.add(Box.createVerticalStrut(12));
		card.add(addButton);

		return wrapper;
	}

    /**
     * Populates the table with cryptocurrency data.
     *
     * @param data the cryptocurrency data rows
     */
    public void setCryptoData (Object[][] data) {
        manageCryptoTable.clearRows();
        for (Object[] row : data) manageCryptoTable.addRow(row);
    }

    /**
     * Sets the callback for delete button clicks.
     *
     * @param callback the callback to invoke when delete is clicked
     */
    public void setDeleteCallback(AbstractTable.ButtonRowCallback callback) {
        manageCryptoTable.setDeleteCallback(callback);
    }

	/**
	 * Sets the callback for edit button clicks.
	 *
	 * @param callback the callback to invoke when edit is clicked
	 */
	public void setEditCallback(AbstractTable.ButtonRowCallback callback) {
		manageCryptoTable.setEditCallback(row -> {
			askNewCryptoName();
			callback.onButtonClicked(row);
		});
	}

	/**
	 * Prompts the user for a new cryptocurrency name and notifies
	 * the registered action listener if a valid name is provided.
	 */
	private void askNewCryptoName() {
		confirmedCryptoName = showSingleFieldDialog("Change Crypto Name", "New Name:");
		if (confirmedCryptoName != null) {
			actionListener.actionPerformed(new ActionEvent(
					this, ActionEvent.ACTION_PERFORMED,
					ButtonEnumeration.EDIT_CRYPTO.name()));
		}
	}

    /**
     * Returns the cryptocurrency name at the given table row.
     *
     * @param row the table row index
     * @return the cryptocurrency name
     */
    public String getCryptoNameAtRow(int row) {
        return (String) manageCryptoTable.getValueAt(row, 0);
    }

    /**
     * Creates a file chooser filtered for JSON files.
     *
     * @return the configured file chooser
     */
    public JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Cryptocurrency JSON file");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON files", "json"));
        return fileChooser;
    }

	/**
	 * Returns the confirmed new cryptocurrency name.
	 *
	 * @return the new cryptocurrency name
	 */
	public String getCryptoName() {
		return confirmedCryptoName;
	}
}
